package com.cinema.validator;

import com.cinema.entity.MovieSession;

/**
 * Validator cho thực thể MovieSession.
 * Chịu trách nhiệm kiểm tra dữ liệu của đối tượng MovieSession
 * trước khi xử lý ở các lớp khác trong chương trình.
 * 
 * @author Minh Huy (chính)
 */
public final class MovieSessionValidator {

    /**
     * Constructor private để ngăn tạo đối tượng từ lớp MovieSessionValidator.
     */
    private MovieSessionValidator() {
    }

    /**
     * Kiểm tra dữ liệu của MovieSession trong trường hợp thêm mới.
     *
     * @param movieSession - Đối tượng MovieSession cần kiểm tra
     */
    public static void validateForCreate(MovieSession movieSession) {
        validateCommon(movieSession);
    }

    /**
     * Kiểm tra dữ liệu của MovieSession trong trường hợp cập nhật.
     *
     * @param movieSession - Đối tượng MovieSession cần kiểm tra
     */
    public static void validateForUpdate(MovieSession movieSession) {
        validateCommon(movieSession);

        if (movieSession.getMovieSessionId() <= 0) {
            throw new IllegalArgumentException("movieSessionId phải lớn hơn 0!");
        }
    }

    /**
     * Kiểm tra các ràng buộc chung của thực thể MovieSession.
     * Đây là các kiểm tra dữ liệu cơ bản để đảm bảo object hợp lệ
     * trước khi đi vào DAO hoặc Service.
     *
     * @param movieSession - Đối tượng MovieSession cần kiểm tra
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
     * @param movieSession - Đối tượng MovieSession cần kiểm tra
     */
    private static void validateBusinessRule(MovieSession movieSession) {
    }
}