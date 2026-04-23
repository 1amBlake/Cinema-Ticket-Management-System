package com.cinema.validator;

import main.java.com.cinema.entity.Genre;


public final class GenreValidator {

    /**
     * Constructor private để ngăn tạo đối tượng
     */
    private GenreValidator() {
    }


    public static void validateForCreate(Genre genre) {
        validateCommon(genre);
    }

    public static void validateForUpdate(Genre genre) {
        validateCommon(genre);

        if (genre.getGenreId() <= 0) {
            throw new IllegalArgumentException("genreId phải lớn hơn 0!");
        }
    }


    private static void validateCommon(Genre genre) {
        if (genre == null) {
            throw new IllegalArgumentException("genre không được null!");
        }

        if (genre.getGenreName() == null || genre.getGenreName().trim().isEmpty()) {
            throw new IllegalArgumentException("genreName không được để trống!");
        }

        if (genre.getGenreName().trim().length() > 100) {
            throw new IllegalArgumentException("genreName không được vượt quá 100 ký tự!");
        }

        validateBusinessRule(genre);
    }

    /**
     * Business rule (để trống giống Movie)
     */
    private static void validateBusinessRule(Genre genre) {
    }
}