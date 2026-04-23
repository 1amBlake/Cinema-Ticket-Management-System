package com.cinema.validator;

import java.time.LocalDate;

import com.cinema.entity.Movie;

/**
 * Validator cho thực thể Movie.
 * Chịu trách nhiệm kiểm tra dữ liệu của đối tượng Movie
 * trước khi xử lý ở các lớp khác trong chương trình.
 * 
 * @author Minh Huy (chính)
 */
public final class MovieValidator {

    /**
     * Constructor private để ngăn tạo đối tượng từ lớp MovieValidator.
     */
    private MovieValidator() {
    }

    /**
     * Kiểm tra dữ liệu của Movie trong trường hợp thêm mới.
     *
     * @param movie - Đối tượng Movie cần kiểm tra
     */
    public static void validateForCreate(Movie movie) {
        validateCommon(movie);
    }

    /**
     * Kiểm tra dữ liệu của Movie trong trường hợp cập nhật.
     *
     * @param movie - Đối tượng Movie cần kiểm tra
     */
    public static void validateForUpdate(Movie movie) {
        validateCommon(movie);

        if (movie.getMovieId() <= 0) {
            throw new IllegalArgumentException("movieId phải lớn hơn 0!");
        }
    }

    /**
     * Kiểm tra các ràng buộc chung của thực thể Movie.
     *
     * @param movie - Đối tượng Movie cần kiểm tra
     */
    private static void validateCommon(Movie movie) {
        if (movie == null) {
            throw new IllegalArgumentException("movie không được null!");
        }

        if (movie.getMovieName() == null || movie.getMovieName().trim().isEmpty()) {
            throw new IllegalArgumentException("movieName không được để trống!");
        }

        if (movie.getMovieName().trim().length() > 255) {
            throw new IllegalArgumentException("movieName không được vượt quá 255 ký tự!");
        }

        if (movie.getMovieDuration() <= 0 || movie.getMovieDuration() > 600) {
            throw new IllegalArgumentException("movieDuration phải > 0 và <= 600 phút!");
        }

        if (movie.getMovieReleaseDate() != null
                && (movie.getMovieReleaseDate().getYear() < 1800
                || movie.getMovieReleaseDate().getYear() > LocalDate.now().getYear() + 2)) {
            throw new IllegalArgumentException("movieReleaseDate không hợp lệ!");
        }

        if (movie.getMovieStatus() == null) {
            throw new IllegalArgumentException("movieStatus không được null!");
        }

        if (movie.getMovieAgeRating() == null) {
            throw new IllegalArgumentException("movieAgeRating không được null!");
        }

        if (movie.getMovieLanguage() != null && movie.getMovieLanguage().trim().length() > 100) {
            throw new IllegalArgumentException("movieLanguage không được vượt quá 100 ký tự!");
        }

        if (movie.getPictureUrl() != null && movie.getPictureUrl().trim().length() > 255) {
            throw new IllegalArgumentException("pictureUrl không được vượt quá 255 ký tự!");
        }

        validateBusinessRule(movie);
    }

    /**
     * Kiểm tra các ràng buộc nghiệp vụ nâng cao của Movie.
     *
     * @param movie - Đối tượng Movie cần kiểm tra
     */
    private static void validateBusinessRule(Movie movie) {
    }
}