package com.cinema.validator;

import com.cinema.entity.MovieDirector;

/**
 * Validator cho thực thể MovieDirector.
 * Chịu trách nhiệm kiểm tra dữ liệu của đối tượng MovieDirector
 * trước khi xử lý ở các lớp khác trong chương trình.
 * 
 * @author Quốc Anh (chính)
 * @author Minh Huy (sửa)
 */
public final class MovieDirectorValidator {

    /**
     * Constructor private để ngăn tạo đối tượng từ lớp MovieDirectorValidator.
     */
    private MovieDirectorValidator() {
    }

    /**
     * Kiểm tra dữ liệu của MovieDirector trong trường hợp thêm mới.
     *
     * @param movieDirector - Đối tượng MovieDirector cần kiểm tra
     */
    public static void validateForCreate(MovieDirector movieDirector) {
        validateCommon(movieDirector);
    }

    /**
     * Kiểm tra dữ liệu của MovieDirector trong trường hợp cập nhật.
     *
     * @param movieDirector - Đối tượng MovieDirector cần kiểm tra
     */
    public static void validateForUpdate(MovieDirector movieDirector) {
        validateCommon(movieDirector);
    }

    /**
     * Kiểm tra các ràng buộc chung của thực thể MovieDirector.
     * Đây là các kiểm tra dữ liệu cơ bản để đảm bảo object hợp lệ
     * trước khi đi vào DAO hoặc Service.
     *
     * @param movieDirector - Đối tượng MovieDirector cần kiểm tra
     */
    private static void validateCommon(MovieDirector movieDirector) {
        if (movieDirector == null) {
            throw new IllegalArgumentException("movieDirector không được null!");
        }

        if (movieDirector.getMovie() == null) {
            throw new IllegalArgumentException("movie không được null!");
        }

        if (movieDirector.getMovie().getMovieId() <= 0) {
            throw new IllegalArgumentException("movieId phải lớn hơn 0!");
        }

        if (movieDirector.getDirector() == null) {
            throw new IllegalArgumentException("director không được null!");
        }

        if (movieDirector.getDirector().getDirectorId() <= 0) {
            throw new IllegalArgumentException("directorId phải lớn hơn 0!");
        }

        validateBusinessRule(movieDirector);
    }

    /**
     * Kiểm tra các ràng buộc nghiệp vụ nâng cao của MovieDirector.
     *
     * @param movieDirector - Đối tượng MovieDirector cần kiểm tra
     */
    private static void validateBusinessRule(MovieDirector movieDirector) {
    }
}