package com.cinema.validator;

import main.java.com.cinema.entity.ScreenType;


public final class ScreenTypeValidator {


    private ScreenTypeValidator() {
    }

    public static void validateForCreate(ScreenType screenType) {
        validateCommon(screenType);
    }


    public static void validateForUpdate(ScreenType screenType) {
        validateCommon(screenType);

        if (screenType.getScreenTypeId() <= 0) {
            throw new IllegalArgumentException("screenTypeId phải lớn hơn 0!");
        }
    }


    private static void validateCommon(ScreenType screenType) {
        if (screenType == null) {
            throw new IllegalArgumentException("screenType không được null!");
        }

        
        if (screenType.getScreenTypeName() == null 
                || screenType.getScreenTypeName().trim().isEmpty()) {
            throw new IllegalArgumentException("screenTypeName không được để trống!");
        }

        if (screenType.getScreenTypeName().trim().length() > 100) {
            throw new IllegalArgumentException("screenTypeName không được vượt quá 100 ký tự!");
        }

        validateBusinessRule(screenType);
    }


    private static void validateBusinessRule(ScreenType screenType) {
    }
}