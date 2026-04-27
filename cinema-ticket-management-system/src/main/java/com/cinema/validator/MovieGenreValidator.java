package com.cinema.validator;

import com.cinema.entity.MovieGenre;

/**
 * Validator cho thực thể MovieGenre.
 * Chịu trách nhiệm kiểm tra dữ liệu của đối tượng MovieGenre
 * trước khi xử lý ở các lớp khác trong chương trình.
 * 
 * @author Hải Anh (chính)
 * @author Minh Huy (sửa)
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
     * Đây là các kiểm tra dữ liệu cơ bản để đảm bảo object hợp lệ
     * trước khi đi vào DAO hoặc Service.
     *
     * @param movieGenre - Đối tượng MovieGenre cần kiểm tra
     */
    private static void validateCommon(MovieGenre movieGenre) {
        if (movieGenre == null) {
            throw new IllegalArgumentException("movieGenre không được null!");
        }

        if (movieGenre.getMovie() == null) {
            throw new IllegalArgumentException("movie không được null!");
        }

        if (movieGenre.getMovie().getMovieId() <= 0) {
            throw new IllegalArgumentException("movieId phải lớn hơn 0!");
        }

        if (movieGenre.getGenre() == null) {
            throw new IllegalArgumentException("genre không được null!");
        }

        if (movieGenre.getGenre().getGenreId() <= 0) {
            throw new IllegalArgumentException("genreId phải lớn hơn 0!");
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