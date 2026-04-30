package com.cinema.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cinema.dao.MovieDao;
import com.cinema.entity.Movie;
import com.cinema.enums.MovieStatus;
import com.cinema.validator.MovieValidator;

/**
 * Service xử lý nghiệp vụ cho thực thể Movie.
 * 
 * Lớp này là tầng trung gian giữa Controller và MovieDao.
 * Chịu trách nhiệm kiểm tra dữ liệu đầu vào, xử lý nghiệp vụ cơ bản
 * và gọi DAO để thao tác với cơ sở dữ liệu.
 * 
 * Các ràng buộc chính:
 * - Tên phim không được để trống và không vượt quá 255 ký tự.
 * - Thời lượng phim phải lớn hơn 0 và không vượt quá 600 phút.
 * - Trạng thái phim và giới hạn tuổi không được null.
 * - Ngôn ngữ phim không được vượt quá 100 ký tự.
 * - URL ảnh phim không được vượt quá 255 ký tự.
 * - Không cho phép thêm/cập nhật phim trùng tên và ngày phát hành.
 * - Không cho phép xóa phim đang được sử dụng ở suất chiếu,
 *   bảng phim - đạo diễn hoặc bảng phim - thể loại.
 * 
 * @author Quốc Anh
 */
public class MovieService {

    private static final int MAX_MOVIE_NAME_LENGTH = 255;

    private final MovieDao movieDao;

    /**
     * Constructor mặc định.
     * 
     * Khởi tạo MovieService với MovieDao mặc định.
     */
    public MovieService() {
        this(new MovieDao());
    }

    /**
     * Constructor cho phép truyền MovieDao từ bên ngoài.
     * 
     * Thường dùng khi cần test hoặc muốn thay thế MovieDao bằng đối tượng khác.
     *
     * @param movieDao - Đối tượng DAO dùng để thao tác dữ liệu Movie
     * @throws IllegalArgumentException nếu movieDao là null
     */
    public MovieService(MovieDao movieDao) {
        if (movieDao == null) {
            throw new IllegalArgumentException("movieDao không được null!");
        }

        this.movieDao = movieDao;
    }

    /**
     * Lấy danh sách tất cả phim trong hệ thống.
     *
     * @return danh sách tất cả phim
     * @throws SQLException nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public List<Movie> getAllMovies() throws SQLException {
        return movieDao.getAllMovies();
    }

    /**
     * Tìm phim theo mã phim.
     *
     * @param movieId - Mã phim cần tìm
     * @return đối tượng Movie nếu tìm thấy, ngược lại trả về null
     * @throws IllegalArgumentException nếu movieId không hợp lệ
     * @throws SQLException nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public Movie findMovieById(int movieId) throws SQLException {
        if (movieId <= 0) {
            throw new IllegalArgumentException("movieId phải lớn hơn 0!");
        }

        return movieDao.findById(movieId);
    }

    /**
     * Tìm kiếm phim theo tên gần đúng.
     * 
     * Nếu keyword là null hoặc chuỗi rỗng thì DAO sẽ xử lý như tìm tất cả
     * các phim có tên phù hợp với chuỗi rỗng.
     *
     * @param keyword - Từ khóa tên phim cần tìm
     * @return danh sách phim có tên phù hợp với từ khóa
     * @throws IllegalArgumentException nếu keyword vượt quá 255 ký tự
     * @throws SQLException nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public List<Movie> searchMoviesByName(String keyword) throws SQLException {
        if (keyword != null && keyword.trim().length() > MAX_MOVIE_NAME_LENGTH) {
            throw new IllegalArgumentException("keyword không được vượt quá 255 ký tự!");
        }

        return movieDao.searchByName(keyword);
    }

    /**
     * Thêm một phim mới vào hệ thống.
     * 
     * Dữ liệu phim sẽ được kiểm tra trước khi thêm vào cơ sở dữ liệu.
     * Phim không được trùng tên và ngày phát hành với phim đã tồn tại.
     *
     * @param movie - Đối tượng Movie cần thêm
     * @return true nếu thêm thành công, false nếu thêm thất bại
     * @throws IllegalArgumentException nếu dữ liệu movie không hợp lệ hoặc phim đã tồn tại
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu
     */
    public boolean addMovie(Movie movie) throws SQLException {
        MovieValidator.validateForCreate(movie);
        return movieDao.addMovie(movie);
    }

    /**
     * Cập nhật thông tin của một phim.
     * 
     * Dữ liệu phim sẽ được kiểm tra trước khi cập nhật.
     * Phim sau khi cập nhật không được trùng tên và ngày phát hành
     * với phim khác trong hệ thống.
     *
     * @param movie - Đối tượng Movie chứa thông tin cần cập nhật
     * @return true nếu cập nhật thành công, false nếu không tìm thấy bản ghi cần cập nhật
     * @throws IllegalArgumentException nếu dữ liệu movie không hợp lệ hoặc phim đã tồn tại
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu
     */
    public boolean updateMovie(Movie movie) throws SQLException {
        MovieValidator.validateForUpdate(movie);
        return movieDao.updateMovie(movie);
    }

    /**
     * Xóa phim theo mã phim.
     * 
     * Không cho phép xóa nếu mã phim không hợp lệ.
     * Việc kiểm tra phim có đang được sử dụng ở suất chiếu,
     * bảng phim - đạo diễn hoặc bảng phim - thể loại sẽ được xử lý ở MovieDao.
     *
     * @param movieId - Mã phim cần xóa
     * @return true nếu xóa thành công, false nếu không tìm thấy bản ghi cần xóa
     * @throws IllegalArgumentException nếu movieId không hợp lệ hoặc phim đang được sử dụng
     * @throws SQLException nếu có lỗi khi thao tác với cơ sở dữ liệu
     */
    public boolean deleteMovieById(int movieId) throws SQLException {
        if (movieId <= 0) {
            throw new IllegalArgumentException("movieId phải lớn hơn 0!");
        }

        return movieDao.deleteMovieById(movieId);
    }

    /**
     * Lấy danh sách các phim đang chiếu.
     *
     * @return danh sách phim có trạng thái SHOWING
     * @throws SQLException nếu có lỗi khi truy vấn cơ sở dữ liệu
     */
    public List<Movie> getShowingMovies() throws SQLException {
        List<Movie> showingMovies = new ArrayList<>();

        for (Movie movie : movieDao.getAllMovies()) {
            if (movie != null && movie.getMovieStatus() == MovieStatus.SHOWING) {
                showingMovies.add(movie);
            }
        }

        return showingMovies;
    }
}

/*
 * Các điểm đã bổ sung so với service mẫu ban đầu:
 * - Bổ sung Javadoc cho class, constructor và các method để mô tả rõ chức năng.
 * - Đổi tên method theo hướng rõ nghĩa hơn, ví dụ getAllMovies(), findMovieById(),
 *   searchMoviesByName(), addMovie(), updateMovie(), deleteMovieById().
 * - Bổ sung import MovieValidator để Service kiểm tra dữ liệu trước khi gọi DAO.
 * - Gọi MovieValidator ở tầng Service trước khi thêm hoặc cập nhật dữ liệu.
 * - Bổ sung hằng số MAX_MOVIE_NAME_LENGTH để kiểm tra độ dài keyword khi tìm kiếm.
 * - Kiểm tra movieId hợp lệ trước khi tìm phim theo mã hoặc xóa phim.
 * - Kiểm tra độ dài keyword trước khi tìm kiếm phim theo tên.
 * - Giữ lại getShowingMovies() để lấy danh sách phim có trạng thái SHOWING.
 * 
 * Các ràng buộc nghiệp vụ/database liên quan:
 * - Tên phim không được để trống và không được vượt quá 255 ký tự.
 * - Thời lượng phim phải lớn hơn 0 và không vượt quá 600 phút.
 * - Trạng thái phim và giới hạn tuổi không được null.
 * - Ngôn ngữ phim không được vượt quá 100 ký tự.
 * - URL ảnh phim không được vượt quá 255 ký tự.
 * - Không cho phép thêm hoặc cập nhật phim trùng tên và ngày phát hành.
 * - Không cho phép xóa phim nếu phim đang được sử dụng ở suất chiếu,
 *   bảng phim - đạo diễn hoặc bảng phim - thể loại.
 * 
 * Lưu ý:
 * - Việc kiểm tra dữ liệu cơ bản được gọi ở MovieService thông qua MovieValidator.
 * - Việc kiểm tra trùng tên phim theo ngày phát hành được xử lý ở MovieDao.
 * - Việc kiểm tra phim có đang được sử dụng ở các bảng liên quan hay không
 *   được xử lý ở MovieDao khi xóa.
 * - DAO vẫn chịu trách nhiệm thao tác trực tiếp với cơ sở dữ liệu.
 * Minh Huy
 */