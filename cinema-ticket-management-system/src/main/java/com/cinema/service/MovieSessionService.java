package com.cinema.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.cinema.dao.MovieDao;
import com.cinema.dao.MovieSessionDao;
import com.cinema.dao.ScreenDao;
import com.cinema.entity.Movie;
import com.cinema.entity.MovieSession;
import com.cinema.entity.Screen;
import com.cinema.enums.MovieSessionStatus;
import com.cinema.validator.MovieSessionValidator;

/**
 * Service xử lý nghiệp vụ cho thực thể MovieSession.
 * 
 * Lớp này là tầng trung gian giữa Controller và MovieSessionDao.
 * Chịu trách nhiệm kiểm tra dữ liệu đầu vào, xử lý nghiệp vụ suất chiếu
 * và gọi DAO để thao tác với cơ sở dữ liệu.
 * 
 * Các ràng buộc nghiệp vụ chính:
 * - Không cho phép tạo/cập nhật suất chiếu nếu phim không tồn tại.
 * - Không cho phép tạo/cập nhật suất chiếu nếu phim đã dừng chiếu.
 * - Không cho phép tạo/cập nhật suất chiếu nếu phòng chiếu không tồn tại.
 * - Không cho phép tạo/cập nhật suất chiếu nếu phòng chiếu đang bảo trì
 *   hoặc ngưng hoạt động.
 * - Thời gian kết thúc suất chiếu được tự động tính theo:
 *   startsAt + movieDuration.
 * - Không cho phép trùng lịch chiếu trong cùng một phòng.
 * - Không cho phép cập nhật hoặc xóa suất chiếu đã phát sinh vé.
 * - Trạng thái suất chiếu được tự động tính theo thời gian hiện tại
 *   khi trả dữ liệu về cho Controller.
 * 
 * Lưu ý:
 * - Việc kiểm tra trùng lịch phòng chiếu được xử lý ở MovieSessionDao.
 * - Việc kiểm tra suất chiếu đã phát sinh vé được xử lý ở MovieSessionDao.
 * - Trạng thái tự động trong Service chỉ dùng cho dữ liệu trả về,
 *   không tự cập nhật xuống database.
 * 
 * @author Quốc Anh (chính)
 * @author Minh Huy (thêm tính năng, sửa)
 */
public class MovieSessionService {

    private static final int NOW_SHOWING_BEFORE_START_MINUTES = 5;

    private final MovieSessionDao movieSessionDao;
    private final MovieDao movieDao;
    private final ScreenDao screenDao;

    /**
     * Constructor mặc định.
     * 
     * Khởi tạo MovieSessionService với các DAO mặc định.
     */
    public MovieSessionService() {
        this(new MovieSessionDao(), new MovieDao(), new ScreenDao());
    }

    /**
     * Constructor cho phép truyền DAO từ bên ngoài.
     * 
     * Thường dùng khi cần test hoặc muốn thay thế DAO bằng đối tượng khác.
     *
     * @param movieSessionDao - DAO thao tác dữ liệu MovieSession
     * @param movieDao - DAO thao tác dữ liệu Movie
     * @param screenDao - DAO thao tác dữ liệu Screen
     * @throws IllegalArgumentException nếu một trong các DAO là null
     */
    public MovieSessionService(MovieSessionDao movieSessionDao, MovieDao movieDao, ScreenDao screenDao) {
        if (movieSessionDao == null) {
            throw new IllegalArgumentException("movieSessionDao không được null!");
        }

        if (movieDao == null) {
            throw new IllegalArgumentException("movieDao không được null!");
        }

        if (screenDao == null) {
            throw new IllegalArgumentException("screenDao không được null!");
        }

        this.movieSessionDao = movieSessionDao;
        this.movieDao = movieDao;
        this.screenDao = screenDao;
    }

    /**
     * Lấy danh sách tất cả suất chiếu trong hệ thống.
     *
     * @return danh sách tất cả suất chiếu
     * @throws SQLException nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public List<MovieSession> getAllMovieSessions() throws SQLException {
        return applyAutoStatus(movieSessionDao.getAllMovieSessions());
    }

    /**
     * Tìm suất chiếu theo mã suất chiếu.
     *
     * @param movieSessionId - Mã suất chiếu cần tìm
     * @return đối tượng MovieSession nếu tìm thấy, ngược lại trả về null
     * @throws IllegalArgumentException nếu movieSessionId không hợp lệ
     * @throws SQLException nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public MovieSession findMovieSessionById(int movieSessionId) throws SQLException {
        validateRequiredId(movieSessionId, "movieSessionId");

        MovieSession movieSession = movieSessionDao.findById(movieSessionId);
        applyAutoStatus(movieSession);

        return movieSession;
    }

    /**
     * Tìm/lọc suất chiếu theo phim, phòng chiếu và ngày chiếu.
     * 
     * Các tham số có thể null. Nếu tham số null thì không lọc theo điều kiện đó.
     *
     * @param movieId - Mã phim cần lọc, có thể null
     * @param screenId - Mã phòng chiếu cần lọc, có thể null
     * @param showDate - Ngày chiếu cần lọc, có thể null
     * @return danh sách suất chiếu phù hợp với điều kiện lọc
     * @throws IllegalArgumentException nếu movieId hoặc screenId không hợp lệ
     * @throws SQLException nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public List<MovieSession> searchMovieSessions(Integer movieId, Integer screenId, LocalDate showDate)
            throws SQLException {
        validateOptionalId(movieId, "movieId");
        validateOptionalId(screenId, "screenId");

        List<MovieSession> baseSessions = getBaseSessions(movieId, screenId, showDate);
        List<MovieSession> filteredSessions = new ArrayList<>();

        for (MovieSession movieSession : baseSessions) {
            if (matches(movieSession, movieId, screenId, showDate)) {
                filteredSessions.add(movieSession);
            }
        }

        return applyAutoStatus(filteredSessions);
    }

    /**
     * Lọc suất chiếu theo phim, phòng chiếu và ngày chiếu.
     * 
     * Method này gọi lại searchMovieSessions() để hỗ trợ cách gọi dễ hiểu
     * từ Controller.
     *
     * @param movieId - Mã phim cần lọc, có thể null
     * @param screenId - Mã phòng chiếu cần lọc, có thể null
     * @param showDate - Ngày chiếu cần lọc, có thể null
     * @return danh sách suất chiếu phù hợp
     * @throws SQLException nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public List<MovieSession> filterMovieSessions(Integer movieId, Integer screenId, LocalDate showDate)
            throws SQLException {
        return searchMovieSessions(movieId, screenId, showDate);
    }

    /**
     * Lấy danh sách suất chiếu theo mã phim.
     *
     * @param movieId - Mã phim cần tìm suất chiếu
     * @return danh sách suất chiếu của phim
     * @throws IllegalArgumentException nếu movieId không hợp lệ
     * @throws SQLException nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public List<MovieSession> getMovieSessionsByMovie(int movieId) throws SQLException {
        validateRequiredId(movieId, "movieId");

        return applyAutoStatus(movieSessionDao.searchByMovieId(movieId));
    }

    /**
     * Lấy danh sách suất chiếu theo mã phòng chiếu.
     *
     * @param screenId - Mã phòng chiếu cần tìm suất chiếu
     * @return danh sách suất chiếu của phòng
     * @throws IllegalArgumentException nếu screenId không hợp lệ
     * @throws SQLException nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public List<MovieSession> getMovieSessionsByScreen(int screenId) throws SQLException {
        validateRequiredId(screenId, "screenId");

        return applyAutoStatus(movieSessionDao.searchByScreenId(screenId));
    }

    /**
     * Lấy danh sách suất chiếu theo ngày chiếu.
     *
     * @param showDate - Ngày chiếu cần tìm
     * @return danh sách suất chiếu trong ngày
     * @throws IllegalArgumentException nếu showDate là null
     * @throws SQLException nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public List<MovieSession> getMovieSessionsByDate(LocalDate showDate) throws SQLException {
        if (showDate == null) {
            throw new IllegalArgumentException("showDate không được null!");
        }

        return applyAutoStatus(movieSessionDao.searchByDate(showDate));
    }

    /**
     * Lấy danh sách suất chiếu trong ngày hiện tại.
     *
     * @return danh sách suất chiếu hôm nay
     * @throws SQLException nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public List<MovieSession> getTodayMovieSessions() throws SQLException {
        return getMovieSessionsByDate(LocalDate.now());
    }

    /**
     * Lấy danh sách suất chiếu sắp chiếu.
     * 
     * Trạng thái được tính tự động dựa trên thời gian hiện tại.
     *
     * @return danh sách suất chiếu có trạng thái UPCOMING
     * @throws SQLException nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public List<MovieSession> getUpcomingMovieSessions() throws SQLException {
        return getMovieSessionsByStatus(MovieSessionStatus.UPCOMING);
    }

    /**
     * Lấy danh sách suất chiếu đang chiếu.
     * 
     * Một suất chiếu được xem là NOW_SHOWING khi thời gian hiện tại
     * nằm trong khoảng từ 5 phút trước giờ bắt đầu đến trước hoặc bằng giờ kết thúc.
     *
     * @return danh sách suất chiếu có trạng thái NOW_SHOWING
     * @throws SQLException nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public List<MovieSession> getNowShowingMovieSessions() throws SQLException {
        return getMovieSessionsByStatus(MovieSessionStatus.NOW_SHOWING);
    }

    /**
     * Lấy danh sách suất chiếu đã kết thúc.
     * 
     * Trạng thái được tính tự động dựa trên thời gian hiện tại.
     *
     * @return danh sách suất chiếu có trạng thái FINISHED
     * @throws SQLException nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public List<MovieSession> getFinishedMovieSessions() throws SQLException {
        return getMovieSessionsByStatus(MovieSessionStatus.FINISHED);
    }

    /**
     * Lấy danh sách suất chiếu theo trạng thái.
     * 
     * Trạng thái được tính tự động trước khi lọc, trừ trạng thái CANCELLED
     * sẽ được giữ nguyên nếu dữ liệu trong database là CANCELLED.
     *
     * @param status - Trạng thái suất chiếu cần lọc
     * @return danh sách suất chiếu có trạng thái phù hợp
     * @throws IllegalArgumentException nếu status là null
     * @throws SQLException nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public List<MovieSession> getMovieSessionsByStatus(MovieSessionStatus status) throws SQLException {
        if (status == null) {
            throw new IllegalArgumentException("status không được null!");
        }

        List<MovieSession> sessions = getAllMovieSessions();
        List<MovieSession> result = new ArrayList<>();

        for (MovieSession movieSession : sessions) {
            if (movieSession != null && movieSession.getMovieSessionStatus() == status) {
                result.add(movieSession);
            }
        }

        return result;
    }

    /**
     * Thêm một suất chiếu mới.
     * 
     * Service sẽ tự động:
     * - Kiểm tra phim tồn tại và không bị dừng chiếu.
     * - Kiểm tra phòng chiếu tồn tại và đang hoạt động.
     * - Tính tg_ket_thuc = tg_bat_dau + thời lượng phim.
     * - Kiểm tra dữ liệu bằng MovieSessionValidator.
     * - Gọi DAO để kiểm tra trùng lịch và thêm dữ liệu.
     *
     * @param movieSession - Đối tượng suất chiếu cần thêm
     * @return true nếu thêm thành công, false nếu thêm thất bại
     * @throws IllegalArgumentException nếu dữ liệu không hợp lệ
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu
     */
    public boolean addMovieSession(MovieSession movieSession) throws SQLException {
        prepareMovieSessionForSave(movieSession);
        MovieSessionValidator.validateForCreate(movieSession);

        return movieSessionDao.addMovieSession(movieSession);
    }

    /**
     * Cập nhật thông tin suất chiếu.
     * 
     * Service sẽ tự động:
     * - Kiểm tra phim tồn tại và không bị dừng chiếu.
     * - Kiểm tra phòng chiếu tồn tại và đang hoạt động.
     * - Tính lại tg_ket_thuc = tg_bat_dau + thời lượng phim.
     * - Kiểm tra dữ liệu bằng MovieSessionValidator.
     * - Gọi DAO để kiểm tra trùng lịch và cập nhật dữ liệu.
     *
     * @param movieSession - Đối tượng suất chiếu cần cập nhật
     * @return true nếu cập nhật thành công, false nếu không tìm thấy bản ghi
     * @throws IllegalArgumentException nếu dữ liệu không hợp lệ hoặc suất chiếu đã phát sinh vé
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu
     */
    public boolean updateMovieSession(MovieSession movieSession) throws SQLException {
        if (movieSession == null) {
            throw new IllegalArgumentException("movieSession không được null!");
        }

        validateRequiredId(movieSession.getMovieSessionId(), "movieSessionId");

        MovieSession existingSession = movieSessionDao.findById(movieSession.getMovieSessionId());

        if (existingSession == null) {
            throw new IllegalArgumentException("Suất chiếu không tồn tại!");
        }

        applyAutoStatus(existingSession);

        if (existingSession.getMovieSessionStatus() == MovieSessionStatus.FINISHED) {
            throw new IllegalArgumentException("Suất chiếu đã kết thúc, không thể cập nhật!");
        }

        prepareMovieSessionForSave(movieSession);
        MovieSessionValidator.validateForUpdate(movieSession);

        return movieSessionDao.updateMovieSession(movieSession);
    }

    /**
     * Xóa suất chiếu theo mã suất chiếu.
     * 
     * Không cho phép xóa nếu mã suất chiếu không hợp lệ.
     * Việc kiểm tra suất chiếu đã phát sinh vé sẽ được xử lý ở MovieSessionDao.
     *
     * @param movieSessionId - Mã suất chiếu cần xóa
     * @return true nếu xóa thành công, false nếu không tìm thấy bản ghi
     * @throws IllegalArgumentException nếu movieSessionId không hợp lệ hoặc suất chiếu đã phát sinh vé
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu
     */
    public boolean deleteMovieSessionById(int movieSessionId) throws SQLException {
        validateRequiredId(movieSessionId, "movieSessionId");

        return movieSessionDao.deleteMovieSessionById(movieSessionId);
    }

    /**
     * Chuẩn bị dữ liệu suất chiếu trước khi thêm hoặc cập nhật.
     * 
     * Method này xử lý các nghiệp vụ cần truy vấn database:
     * - Lấy thông tin đầy đủ của phim.
     * - Lấy thông tin đầy đủ của phòng chiếu.
     * - Kiểm tra trạng thái phim.
     * - Kiểm tra trạng thái phòng.
     * - Tự động tính thời gian kết thúc theo thời lượng phim.
     *
     * @param movieSession - Suất chiếu cần chuẩn bị dữ liệu
     * @throws SQLException nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    private void prepareMovieSessionForSave(MovieSession movieSession) throws SQLException {
        if (movieSession == null) {
            throw new IllegalArgumentException("movieSession không được null!");
        }

        if (movieSession.getMovie() == null) {
            throw new IllegalArgumentException("movie không được null!");
        }

        if (movieSession.getMovie().getMovieId() <= 0) {
            throw new IllegalArgumentException("movieId phải lớn hơn 0!");
        }

        if (movieSession.getScreen() == null) {
            throw new IllegalArgumentException("screen không được null!");
        }

        if (movieSession.getScreen().getScreenId() <= 0) {
            throw new IllegalArgumentException("screenId phải lớn hơn 0!");
        }

        if (movieSession.getStartsAt() == null) {
            throw new IllegalArgumentException("startsAt không được null!");
        }

        Movie movie = movieDao.findById(movieSession.getMovie().getMovieId());
        if (movie == null) {
            throw new IllegalArgumentException("Phim không tồn tại!");
        }

        Screen screen = screenDao.findById(movieSession.getScreen().getScreenId());
        if (screen == null) {
            throw new IllegalArgumentException("Phòng chiếu không tồn tại!");
        }

        movieSession.setMovie(movie);
        movieSession.setScreen(screen);
        movieSession.setEndsAt(movieSession.getStartsAt().plusMinutes(movie.getMovieDuration()));
        movieSession.setMovieSessionStatus(calculateAutoStatus(movieSession));
    }

    /**
     * Kiểm tra ID bắt buộc.
     *
     * @param id - Giá trị ID cần kiểm tra
     * @param fieldName - Tên trường dùng trong thông báo lỗi
     */
    private void validateRequiredId(int id, String fieldName) {
        if (id <= 0) {
            throw new IllegalArgumentException(fieldName + " phải lớn hơn 0!");
        }
    }

    /**
     * Kiểm tra ID tùy chọn.
     * 
     * Nếu ID là null thì bỏ qua kiểm tra.
     *
     * @param id - Giá trị ID cần kiểm tra
     * @param fieldName - Tên trường dùng trong thông báo lỗi
     */
    private void validateOptionalId(Integer id, String fieldName) {
        if (id != null && id <= 0) {
            throw new IllegalArgumentException(fieldName + " phải lớn hơn 0!");
        }
    }

    /**
     * Lấy danh sách suất chiếu nền để lọc tiếp trong Service.
     *
     * @param movieId - Mã phim, có thể null
     * @param screenId - Mã phòng, có thể null
     * @param showDate - Ngày chiếu, có thể null
     * @return danh sách suất chiếu nền
     * @throws SQLException nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    private List<MovieSession> getBaseSessions(Integer movieId, Integer screenId, LocalDate showDate)
            throws SQLException {
        if (movieId != null) {
            return movieSessionDao.searchByMovieId(movieId);
        }

        if (screenId != null) {
            return movieSessionDao.searchByScreenId(screenId);
        }

        if (showDate != null) {
            return movieSessionDao.searchByDate(showDate);
        }

        return movieSessionDao.getAllMovieSessions();
    }

    /**
     * Kiểm tra một suất chiếu có khớp với điều kiện lọc hay không.
     *
     * @param movieSession - Suất chiếu cần kiểm tra
     * @param movieId - Mã phim, có thể null
     * @param screenId - Mã phòng chiếu, có thể null
     * @param showDate - Ngày chiếu, có thể null
     * @return true nếu suất chiếu phù hợp, ngược lại false
     */
    private boolean matches(MovieSession movieSession, Integer movieId, Integer screenId, LocalDate showDate) {
        if (movieSession == null) {
            return false;
        }

        if (movieId != null) {
            if (movieSession.getMovie() == null || movieSession.getMovie().getMovieId() != movieId) {
                return false;
            }
        }

        if (screenId != null) {
            if (movieSession.getScreen() == null || movieSession.getScreen().getScreenId() != screenId) {
                return false;
            }
        }

        if (showDate != null) {
            if (movieSession.getStartsAt() == null || !showDate.equals(movieSession.getStartsAt().toLocalDate())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Áp dụng trạng thái suất chiếu tự động cho một danh sách suất chiếu.
     *
     * @param movieSessions - Danh sách suất chiếu cần cập nhật trạng thái
     * @return danh sách suất chiếu sau khi cập nhật trạng thái
     */
    private List<MovieSession> applyAutoStatus(List<MovieSession> movieSessions) {
        if (movieSessions == null) {
            return new ArrayList<>();
        }

        for (MovieSession movieSession : movieSessions) {
            applyAutoStatus(movieSession);
        }

        return movieSessions;
    }

    /**
     * Áp dụng trạng thái suất chiếu tự động cho một suất chiếu.
     * 
     * Quy tắc:
     * - Nếu trạng thái hiện tại là CANCELLED thì giữ nguyên.
     * - Nếu hiện tại trước thời điểm bắt đầu hơn 5 phút thì UPCOMING.
     * - Nếu hiện tại nằm trong khoảng từ 5 phút trước giờ bắt đầu
     *   đến trước hoặc bằng giờ kết thúc thì NOW_SHOWING.
     * - Nếu hiện tại sau giờ kết thúc thì FINISHED.
     *
     * @param movieSession - Suất chiếu cần cập nhật trạng thái
     */
    private void applyAutoStatus(MovieSession movieSession) {
        if (movieSession == null) {
            return;
        }

        if (movieSession.getMovieSessionStatus() == MovieSessionStatus.CANCELLED) {
            return;
        }

        if (movieSession.getStartsAt() == null || movieSession.getEndsAt() == null) {
            return;
        }

        movieSession.setMovieSessionStatus(calculateAutoStatus(movieSession));
    }
    
    private MovieSessionStatus calculateAutoStatus(MovieSession movieSession) {
        if (movieSession == null
                || movieSession.getStartsAt() == null
                || movieSession.getEndsAt() == null) {
            return MovieSessionStatus.UPCOMING;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowShowingTime = movieSession.getStartsAt()
                .minusMinutes(NOW_SHOWING_BEFORE_START_MINUTES);

        if (now.isBefore(nowShowingTime)) {
            return MovieSessionStatus.UPCOMING;
        }

        if (!now.isAfter(movieSession.getEndsAt())) {
            return MovieSessionStatus.NOW_SHOWING;
        }

        return MovieSessionStatus.FINISHED;
    }
}

/*
 * Các điểm đã bổ sung so với service mẫu ban đầu:
 * - Bổ sung Javadoc cho class, constructor và các method để mô tả rõ chức năng.
 * - Đổi tên method theo hướng rõ nghĩa hơn, ví dụ getAllMovieSessions(),
 *   findMovieSessionById(), searchMovieSessions(), addMovieSession(),
 *   updateMovieSession(), deleteMovieSessionById().
 * - Bổ sung MovieDao và ScreenDao để Service có thể lấy thông tin đầy đủ
 *   của phim và phòng chiếu trước khi thêm/cập nhật suất chiếu.
 * - Bổ sung MovieSessionValidator để Service kiểm tra dữ liệu và nghiệp vụ
 *   trước khi gọi DAO.
 * - Bổ sung validateRequiredId() để kiểm tra các ID bắt buộc như
 *   movieSessionId, movieId, screenId.
 * - Sửa validateOptionalId() để thông báo lỗi tiếng Việt có dấu:
 *   "phải lớn hơn 0".
 * - Bổ sung getMovieSessionsByScreen() để lấy suất chiếu theo phòng chiếu.
 * - Bổ sung getMovieSessionsByDate() để lấy suất chiếu theo ngày chiếu.
 * - Đổi getTodaySessions() thành getTodayMovieSessions() cho rõ nghĩa hơn.
 * - Bổ sung getUpcomingMovieSessions(), getNowShowingMovieSessions(),
 *   getFinishedMovieSessions() và getMovieSessionsByStatus() để lọc suất chiếu
 *   theo trạng thái.
 * - Bổ sung prepareMovieSessionForSave() để chuẩn bị dữ liệu trước khi lưu:
 *   lấy Movie đầy đủ, lấy Screen đầy đủ, kiểm tra dữ liệu nền và tự tính
 *   tg_ket_thuc = tg_bat_dau + thời lượng phim.
 * - Bổ sung logic tự động tính trạng thái suất chiếu theo thời gian hiện tại.
 *   Nếu còn hơn 5 phút trước giờ chiếu thì UPCOMING, từ 5 phút trước giờ chiếu
 *   đến hết giờ chiếu thì NOW_SHOWING, sau giờ kết thúc thì FINISHED.
 * - Bổ sung kiểm tra trong updateMovieSession(): không cho cập nhật nếu suất chiếu
 *   hiện tại trong database đã kết thúc.
 * 
 * Các ràng buộc nghiệp vụ/database liên quan:
 * - Không cho phép tạo hoặc cập nhật suất chiếu nếu phim không tồn tại.
 * - Không cho phép tạo hoặc cập nhật suất chiếu nếu phòng chiếu không tồn tại.
 * - Không cho phép tạo hoặc cập nhật suất chiếu nếu phim đã dừng chiếu.
 * - Không cho phép tạo hoặc cập nhật suất chiếu nếu phòng chiếu đang bảo trì
 *   hoặc ngưng hoạt động.
 * - Không cho phép tạo mới suất chiếu với trạng thái CANCELLED hoặc FINISHED.
 * - Không cho phép cập nhật suất chiếu nếu trạng thái đã là FINISHED.
 * - Thời gian kết thúc suất chiếu phải bằng thời gian bắt đầu cộng với
 *   thời lượng phim.
 * - Không cho phép trùng lịch chiếu trong cùng một phòng.
 * - Không cho phép cập nhật hoặc xóa suất chiếu nếu suất chiếu đã phát sinh vé.
 * 
 * Lưu ý:
 * - Việc kiểm tra phim dừng chiếu, phòng không hoạt động và thời gian kết thúc
 *   hợp lệ được xử lý trong MovieSessionValidator.
 * - Việc lấy Movie/Screen đầy đủ và tự tính thời gian kết thúc được xử lý
 *   trong MovieSessionService.
 * - Việc kiểm tra trùng lịch phòng chiếu được xử lý ở MovieSessionDao.
 * - Việc kiểm tra suất chiếu đã phát sinh vé được xử lý ở MovieSessionDao.
 * - Trạng thái tự động trong Service hiện chỉ thay đổi trên object trả về
 *   cho Controller, chưa tự cập nhật xuống database.
 * Minh Huy
 */