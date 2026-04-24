package com.cinema.validator;

import com.cinema.entity.MovieGenre;

/**
 * Validator cho thực thể MovieGenre.
 * 
 * @author Hải Anh (chính)
 */
public final class MovieGenreValidator {

    /**
     * Constructor private để ngăn tạo đối tượng từ lớp MovieGenreValidator.
     */
    private MovieGenreValidator() {
    }

    /**
     * Kiểm tra dữ liệu của MovieGenre trong trường hợp thêm mới.
     *
     * @param movieGenre - Đối tượng MovieGenre cần kiểm tra
     */
    public static void validateForCreate(MovieGenre movieGenre) {
        validateCommon(movieGenre);
    }

    /**
     * Kiểm tra các ràng buộc chung của thực thể MovieGenre.
     *
     * @param movieGenre - Đối tượng MovieGenre cần kiểm tra
     */
    private static void validateCommon(MovieGenre movieGenre) {
        if (movieGenre == null) {
            throw new IllegalArgumentException("Thể loại phim không được null!");
        }

        if (movieGenre.getMovie() == null) {
            throw new IllegalArgumentException("Phim không được null!");
        }

        if (movieGenre.getGenre() == null) {
            throw new IllegalArgumentException("Thể loại không được null!");
        }

        validateBusinessRule(movieGenre);
    }

    /**
     * Kiểm tra các ràng buộc nghiệp vụ nâng cao của MovieGenre.
     *
     * @param movieGenre - Đối tượng MovieGenre cần kiểm tra
     */
    private static void validateBusinessRule(MovieGenre movieGenre) {
    }
}