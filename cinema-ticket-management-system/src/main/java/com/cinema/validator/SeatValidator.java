package com.cinema.validator;

import com.cinema.entity.Seat;

/**
 * Validator cho thực thể Seat.
 * Chịu trách nhiệm kiểm tra dữ liệu của đối tượng Seat
 * trước khi xử lý ở các lớp khác trong chương trình.
 * 
 * @author Hải Anh (chính)
 * @author Minh Huy (sửa)
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
            throw new IllegalArgumentException("seatId phải lớn hơn 0!");
        }
    }

    /**
     * Kiểm tra các ràng buộc chung của thực thể Seat.
     * Đây là các kiểm tra dữ liệu cơ bản để đảm bảo object hợp lệ
     * trước khi đi vào DAO hoặc Service.
     *
     * @param seat - Đối tượng Seat cần kiểm tra
     */
    private static void validateCommon(Seat seat) {
        if (seat == null) {
            throw new IllegalArgumentException("seat không được null!");
        }

        if (seat.getScreen() == null) {
            throw new IllegalArgumentException("screen không được null!");
        }

        if (seat.getScreen().getScreenId() <= 0) {
            throw new IllegalArgumentException("screenId phải lớn hơn 0!");
        }

        if (seat.getSeatType() == null) {
            throw new IllegalArgumentException("seatType không được null!");
        }

        if (seat.getSeatType().getSeatTypeId() <= 0) {
            throw new IllegalArgumentException("seatTypeId phải lớn hơn 0!");
        }

        if (seat.getSeatRow() == null || seat.getSeatRow().trim().isEmpty()) {
            throw new IllegalArgumentException("seatRow không được để trống!");
        }

        if (seat.getSeatRow().trim().length() > 10) {
            throw new IllegalArgumentException("seatRow không được vượt quá 10 ký tự!");
        }

        if (seat.getSeatCol() == null || seat.getSeatCol().trim().isEmpty()) {
            throw new IllegalArgumentException("seatCol không được để trống!");
        }

        if (seat.getSeatCol().trim().length() > 10) {
            throw new IllegalArgumentException("seatCol không được vượt quá 10 ký tự!");
        }

        if (seat.getSeatStatus() == null) {
            throw new IllegalArgumentException("seatStatus không được null!");
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