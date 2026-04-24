package com.cinema.validator;

import com.cinema.entity.Seat;

/**
 * Validator cho thực thể Seat.
 * 
 * @author Hải Anh (chính)
 */
public final class SeatValidator {

    /**
     * Constructor private để ngăn tạo đối tượng từ lớp SeatValidator.
     */
    private SeatValidator() {
    }

    /**
     * Kiểm tra dữ liệu của Seat trong trường hợp thêm mới.
     *
     * @param seat - Đối tượng Seat cần kiểm tra
     */
    public static void validateForCreate(Seat seat) {
        validateCommon(seat);
    }

    /**
     * Kiểm tra dữ liệu của Seat trong trường hợp cập nhật.
     *
     * @param seat - Đối tượng Seat cần kiểm tra
     */
    public static void validateForUpdate(Seat seat) {
        validateCommon(seat);

        if (seat.getSeatId() <= 0) {
            throw new IllegalArgumentException("Mã ghế phải lớn hơn 0!");
        }
    }

    /**
     * Kiểm tra các ràng buộc chung của thực thể Seat.
     *
     * @param seat - Đối tượng Seat cần kiểm tra
     */
    private static void validateCommon(Seat seat) {
        if (seat == null) {
            throw new IllegalArgumentException("Ghế không được null!");
        }

        if (seat.getScreen() == null) {
            throw new IllegalArgumentException("Phòng chiếu không được null!");
        }

        if (seat.getSeatType() == null) {
            throw new IllegalArgumentException("Loại ghế không được null!");
        }

        if (seat.getSeatRow() == null || seat.getSeatRow().trim().isEmpty()) {
            throw new IllegalArgumentException("Hàng ghế không được để trống!");
        }

        if (seat.getSeatRow().trim().length() > 5) {
            throw new IllegalArgumentException("Hàng ghế không được vượt quá 5 ký tự!");
        }
        
        if (seat.getSeatCol() == null || seat.getSeatCol().trim().isEmpty()) {
            throw new IllegalArgumentException("Cột ghế không được để trống!");
        }

        if (seat.getSeatCol().trim().length() > 5) {
            throw new IllegalArgumentException("Cột ghế không được vượt quá 5 ký tự!");
        }
        
        if (seat.getSeatStatus() == null) {
            throw new IllegalArgumentException("Trạng thái ghế không được null!");
        }

        validateBusinessRule(seat);
    }

    /**
     * Kiểm tra các ràng buộc nghiệp vụ nâng cao của Seat.
     *
     * @param seat - Đối tượng Seat cần kiểm tra
     */
    private static void validateBusinessRule(Seat seat) {
    }
}