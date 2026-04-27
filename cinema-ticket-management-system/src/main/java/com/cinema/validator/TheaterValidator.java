package com.cinema.validator;

import com.cinema.entity.Theater;

/**
 * Validator cho thực thể Theater.
 * Chịu trách nhiệm kiểm tra dữ liệu của đối tượng Theater
 * trước khi xử lý ở các lớp khác trong chương trình.
 * 
 * @author Trọng (chính)
 * @author Minh Huy (sửa)
 */
public final class TheaterValidator {

    /**
     * Constructor private để ngăn tạo đối tượng từ lớp TheaterValidator.
     */
    private TheaterValidator() {
    }

    /**
     * Kiểm tra dữ liệu của Theater trong trường hợp thêm mới.
     *
     * @param theater - Đối tượng Theater cần kiểm tra
     */
    public static void validateForCreate(Theater theater) {
        validateCommon(theater);
    }

    /**
     * Kiểm tra dữ liệu của Theater trong trường hợp cập nhật.
     *
     * @param theater - Đối tượng Theater cần kiểm tra
     */
    public static void validateForUpdate(Theater theater) {
        validateCommon(theater);

        if (theater.getTheaterId() <= 0) {
            throw new IllegalArgumentException("theaterId phải lớn hơn 0!");
        }
    }

    /**
     * Kiểm tra các ràng buộc chung của thực thể Theater.
     * Đây là các kiểm tra dữ liệu cơ bản để đảm bảo object hợp lệ
     * trước khi đi vào DAO hoặc Service.
     *
     * @param theater - Đối tượng Theater cần kiểm tra
     */
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

    /**
     * Kiểm tra các ràng buộc nghiệp vụ nâng cao của Theater.
     *
     * @param theater - Đối tượng Theater cần kiểm tra
     */
    private static void validateBusinessRule(Theater theater) {
    }
}