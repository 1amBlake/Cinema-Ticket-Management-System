package com.cinema.validator;

import main.java.com.cinema.entity.Theater;

public final class TheaterValidator {


    private TheaterValidator() {
    }

    public static void validateForCreate(Theater theater) {
        validateCommon(theater);
    }


    public static void validateForUpdate(Theater theater) {
        validateCommon(theater);

        if (theater.getTheaterId() <= 0) {
            throw new IllegalArgumentException("theaterId phải lớn hơn 0!");
        }
    }

    private static void validateCommon(Theater theater) {
        if (theater == null) {
            throw new IllegalArgumentException("theater không được null!");
        }

        if (theater.getTheaterName() == null || theater.getTheaterName().trim().isEmpty()) {
            throw new IllegalArgumentException("theaterName không được để trống!");
        }

        if (theater.getTheaterName().trim().length() > 255) {
            throw new IllegalArgumentException("theaterName không được vượt quá 255 ký tự!");
        }

        if (theater.getTheaterAddress() != null 
                && theater.getTheaterAddress().trim().length() > 255) {
            throw new IllegalArgumentException("theaterAddress không được vượt quá 255 ký tự!");
        }

        validateBusinessRule(theater);
    }


    private static void validateBusinessRule(Theater theater) {
    }
}