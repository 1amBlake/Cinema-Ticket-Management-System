package com.cinema.validator;

import com.cinema.entity.TicketPricing;

/**
 * Validator cho thực thể TicketPricing.
 * 
 * @author Hải Anh (chính)
 */
public final class TicketPricingValidator {

    /**
     * Constructor private để ngăn tạo đối tượng từ lớp TicketPricingValidator.
     */
    private TicketPricingValidator() {
    }

    /**
     * Kiểm tra dữ liệu của TicketPricing trong trường hợp thêm mới.
     *
     * @param ticketPricing - Đối tượng TicketPricing cần kiểm tra
     */
    public static void validateForCreate(TicketPricing ticketPricing) {
        validateCommon(ticketPricing);
    }

    /**
     * Kiểm tra dữ liệu của TicketPricing trong trường hợp cập nhật.
     *
     * @param ticketPricing - Đối tượng TicketPricing cần kiểm tra
     */
    public static void validateForUpdate(TicketPricing ticketPricing) {
        validateCommon(ticketPricing);

        if (ticketPricing.getTicketPricingId() <= 0) {
            throw new IllegalArgumentException("Mã bảng gái vé phải lớn hơn 0!");
        }
    }

    /**
     * Kiểm tra các ràng buộc chung của thực thể TicketPricing.
     *
     * @param ticketPricing - Đối tượng TicketPricing cần kiểm tra
     */
    private static void validateCommon(TicketPricing ticketPricing) {
        if (ticketPricing == null) {
            throw new IllegalArgumentException("Bảng giá vé không được null!");
        }

        if (ticketPricing.getSeatType() == null) {
            throw new IllegalArgumentException("Loại ghế không được null!");
        }

        if (ticketPricing.getScreenType() == null) {
            throw new IllegalArgumentException("Loại phòng chiếu không được null!");
        }

        if (ticketPricing.getPrice() <= 0) {
            throw new IllegalArgumentException("Giá vé phải > 0!");
        }

        validateBusinessRule(ticketPricing);
    }

    /**
     * Kiểm tra các ràng buộc nghiệp vụ nâng cao của TicketPricing.
     *
     * @param ticketPricing - Đối tượng TicketPricing cần kiểm tra
     */
    private static void validateBusinessRule(TicketPricing ticketPricing) {
    }
}