package com.cinema.validator;

import com.cinema.entity.SeatType;

/**
 * Validator cho thực thể SeatType.
 * Chịu trách nhiệm kiểm tra dữ liệu của đối tượng SeatType
 * trước khi xử lý ở các lớp khác trong chương trình.
 * 
 * @author Trọng (chính)
 * @author Minh Huy (sửa)
 */
public final class SeatTypeValidator {

    /**
     * Constructor private để ngăn tạo đối tượng từ lớp SeatTypeValidator.
     */
    private SeatTypeValidator() {
    }

    /**
     * Kiểm tra dữ liệu của SeatType trong trường hợp thêm mới.
     *
     * @param seatType - Đối tượng SeatType cần kiểm tra
     */
    public static void validateForCreate(SeatType seatType) {
        validateCommon(seatType);
    }

    /**
     * Kiểm tra dữ liệu của SeatType trong trường hợp cập nhật.
     *
     * @param seatType - Đối tượng SeatType cần kiểm tra
     */
    public static void validateForUpdate(SeatType seatType) {
        validateCommon(seatType);

        if (seatType.getSeatTypeId() <= 0) {
            throw new IllegalArgumentException("seatTypeId phải lớn hơn 0!");
        }
    }

    /**
     * Kiểm tra các ràng buộc chung của thực thể SeatType.
     * Đây là các kiểm tra dữ liệu cơ bản để đảm bảo object hợp lệ
     * trước khi đi vào DAO hoặc Service.
     *
     * @param seatType - Đối tượng SeatType cần kiểm tra
     */
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

    /**
     * Kiểm tra các ràng buộc nghiệp vụ nâng cao của SeatType.
     *
     * @param seatType - Đối tượng SeatType cần kiểm tra
     */
    private static void validateBusinessRule(SeatType seatType) {
    }
}