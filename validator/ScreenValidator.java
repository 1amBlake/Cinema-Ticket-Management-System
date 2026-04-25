package com.cinema.validator;

import com.cinema.entity.Screen;

public final class ScreenValidator {

    private ScreenValidator() {
    }

    public static void validateForCreate(Screen screen) {
        validateCommon(screen);
    }

    public static void validateForUpdate(Screen screen) {
        validateCommon(screen);

        if (screen.getScreenId() <= 0) {
            throw new IllegalArgumentException("screenId phải lớn hơn 0!");
        }
    }

    private static void validateCommon(Screen screen) {
        if (screen == null) {
            throw new IllegalArgumentException("screen không được null!");
        }

        if (screen.getScreenName() == null || screen.getScreenName().trim().isEmpty()) {
            throw new IllegalArgumentException("screenName không được để trống!");
        }

        if (screen.getScreenName().trim().length() > 255) {
            throw new IllegalArgumentException("screenName không được vượt quá 255 ký tự!");
        }

        if (screen.getTheater() == null) {
            throw new IllegalArgumentException("theater không được null!");
        }

        if (screen.getTheater().getTheaterId() <= 0) {
            throw new IllegalArgumentException("theaterId phải lớn hơn 0!");
        }

        if (screen.getScreenType() == null) {
            throw new IllegalArgumentException("screenType không được null!");
        }

        if (screen.getScreenType().getScreenTypeId() <= 0) {
            throw new IllegalArgumentException("screenTypeId phải lớn hơn 0!");
        }

        if (screen.getScreenStatus() == null) {
            throw new IllegalArgumentException("screenStatus không được null!");
        }

        validateBusinessRule(screen);
    }

    private static void validateBusinessRule(Screen screen) {
    }
}