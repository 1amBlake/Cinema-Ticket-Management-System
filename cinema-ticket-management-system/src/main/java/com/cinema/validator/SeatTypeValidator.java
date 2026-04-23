package com.cinema.validator;

import main.java.com.cinema.entity.SeatType;


public final class SeatTypeValidator {

    private SeatTypeValidator() {
    }


    public static void validateForCreate(SeatType seatType) {
        validateCommon(seatType);
    }

 
    public static void validateForUpdate(SeatType seatType) {
        validateCommon(seatType);

        if (seatType.getSeatTypeId() <= 0) {
            throw new IllegalArgumentException("seatTypeId phải lớn hơn 0!");
        }
    }


    private static void validateCommon(SeatType seatType) {
        if (seatType == null) {
            throw new IllegalArgumentException("seatType không được null!");
        }

        if (seatType.getSeatTypeName() == null 
                || seatType.getSeatTypeName().trim().isEmpty()) {
            throw new IllegalArgumentException("seatTypeName không được để trống!");
        }

        if (seatType.getSeatTypeName().trim().length() > 255) {
            throw new IllegalArgumentException("seatTypeName không được vượt quá 255 ký tự!");
        }

        validateBusinessRule(seatType);
    }


    private static void validateBusinessRule(SeatType seatType) {
    }
}