package com.cinema.validator;

import com.cinema.entity.MovieDirector;

public final class MovieDirectorValidator {

    private MovieDirectorValidator() {
    }

    public static void validateForCreate(MovieDirector movieDirector) {
        validateCommon(movieDirector);
    }

    public static void validateForUpdate(MovieDirector movieDirector) {
        validateCommon(movieDirector);
    }

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

    private static void validateBusinessRule(MovieDirector movieDirector) {
    }
}