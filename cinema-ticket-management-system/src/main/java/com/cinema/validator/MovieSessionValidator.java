package com.cinema.validator;

import java.time.LocalDateTime;

import com.cinema.entity.MovieSession;
import com.cinema.enums.MovieSessionStatus;
import com.cinema.enums.MovieStatus;
import com.cinema.enums.ScreenStatus;

/**
 * Validator cho thực thể MovieSession.
 * Chịu trách nhiệm kiểm tra dữ liệu của đối tượng MovieSession
 * trước khi xử lý ở các lớp khác trong chương trình.
 * 
 * @author Minh Huy (chính)
 */
public final class MovieSessionValidator {

    private MovieSessionValidator() {
    }

    /**
     * Kiểm tra dữ liệu của MovieSession trong trường hợp thêm mới.
     *
     * @param movieSession - Đối tượng MovieSession cần kiểm tra
     * @throws IllegalArgumentException nếu dữ liệu không hợp lệ
     */
    public static void validateForCreate(MovieSession movieSession) {
        validateCommon(movieSession);

        if (movieSession.getMovieSessionStatus() == MovieSessionStatus.CANCELLED) {
            throw new IllegalArgumentException("Không thể tạo mới suất chiếu với trạng thái đã hủy!");
        }

        if (movieSession.getMovieSessionStatus() == MovieSessionStatus.FINISHED) {
            throw new IllegalArgumentException("Không thể tạo mới suất chiếu với trạng thái đã kết thúc!");
        }
    }

    /**
     * Kiểm tra dữ liệu của MovieSession trong trường hợp cập nhật.
     *
     * @param movieSession - Đối tượng MovieSession cần kiểm tra
     * @throws IllegalArgumentException nếu dữ liệu không hợp lệ
     */
    public static void validateForUpdate(MovieSession movieSession) {
        validateCommon(movieSession);

        if (movieSession.getMovieSessionId() <= 0) {
            throw new IllegalArgumentException("movieSessionId phải lớn hơn 0!");
        }

        if (movieSession.getMovieSessionStatus() == MovieSessionStatus.FINISHED) {
            throw new IllegalArgumentException("Suất chiếu đã kết thúc, không thể cập nhật!");
        }
    }

    /**
     * Kiểm tra các ràng buộc chung của thực thể MovieSession.
     *
     * @param movieSession - Đối tượng MovieSession cần kiểm tra
     * @throws IllegalArgumentException nếu dữ liệu không hợp lệ
     */

    private static void validateCommon(MovieSession movieSession) {
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

        if (movieSession.getMovieFormat() == null) {
            throw new IllegalArgumentException("movieFormat không được null!");
        }

        if (movieSession.getStartsAt() == null) {
            throw new IllegalArgumentException("startsAt không được null!");
        }

        if (movieSession.getEndsAt() == null) {
            throw new IllegalArgumentException("endsAt không được null!");
        }

        if (!movieSession.getEndsAt().isAfter(movieSession.getStartsAt())) {
            throw new IllegalArgumentException("endsAt phải sau startsAt!");
        }

        if (movieSession.getMovieSessionStatus() == null) {
            throw new IllegalArgumentException("movieSessionStatus không được null!");
        }

        validateBusinessRule(movieSession);
    }

    /**
     * Kiểm tra các ràng buộc nghiệp vụ nâng cao của MovieSession.
     * 
     * Quy tắc hiện tại:
     * - Không cho tạo/cập nhật suất chiếu cho phim đã dừng chiếu.
     * - Không cho tạo/cập nhật suất chiếu ở phòng bảo trì hoặc ngưng hoạt động.
     * - Thời gian kết thúc phải bằng thời gian bắt đầu cộng với thời lượng phim.
     *
     * @param movieSession - Đối tượng MovieSession cần kiểm tra
     */
    private static void validateBusinessRule(MovieSession movieSession) {
        if (movieSession.getMovie().getMovieStatus() == null) {
            throw new IllegalArgumentException("movieStatus không được null!");
        }

        if (movieSession.getMovie().getMovieStatus() == MovieStatus.DISCONTINUED) {
            throw new IllegalArgumentException("Phim đã dừng chiếu, không thể tạo hoặc cập nhật suất chiếu!");
        }

        if (movieSession.getScreen().getScreenStatus() == null) {
            throw new IllegalArgumentException("screenStatus không được null!");
        }

        if (movieSession.getScreen().getScreenStatus() != ScreenStatus.AVAILABLE) {
            throw new IllegalArgumentException(
                    "Phòng chiếu đang bảo trì hoặc ngưng hoạt động, không thể tạo hoặc cập nhật suất chiếu!"
            );
        }

        if (movieSession.getMovie().getMovieDuration() <= 0) {
            throw new IllegalArgumentException("movieDuration phải lớn hơn 0!");
        }

        LocalDateTime expectedEndsAt = movieSession.getStartsAt()
                .plusMinutes(movieSession.getMovie().getMovieDuration());

        if (!expectedEndsAt.equals(movieSession.getEndsAt())) {
            throw new IllegalArgumentException("endsAt phải bằng startsAt cộng với thời lượng phim!");
        }
    }
}