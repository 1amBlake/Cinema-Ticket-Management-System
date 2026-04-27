package com.cinema.validator;

import com.cinema.entity.TicketPricing;

/**
 * Validator cho thực thể TicketPricing.
 * Chịu trách nhiệm kiểm tra dữ liệu của đối tượng TicketPricing
 * trước khi xử lý ở các lớp khác trong chương trình.
 * 
 * @author Hải Anh (chính)
 * @author Minh Huy (sửa)
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
            throw new IllegalArgumentException("ticketPricingId phải lớn hơn 0!");
        }
    }

    /**
     * Kiểm tra các ràng buộc chung của thực thể TicketPricing.
     * Đây là các kiểm tra dữ liệu cơ bản để đảm bảo object hợp lệ
     * trước khi đi vào DAO hoặc Service.
     *
     * @param ticketPricing - Đối tượng TicketPricing cần kiểm tra
     */
    private static void validateCommon(TicketPricing ticketPricing) {
        if (ticketPricing == null) {
            throw new IllegalArgumentException("ticketPricing không được null!");
        }

        if (ticketPricing.getSeatType() == null) {
            throw new IllegalArgumentException("seatType không được null!");
        }

        if (ticketPricing.getSeatType().getSeatTypeId() <= 0) {
            throw new IllegalArgumentException("seatTypeId phải lớn hơn 0!");
        }

        if (ticketPricing.getScreenType() == null) {
            throw new IllegalArgumentException("screenType không được null!");
        }

        if (ticketPricing.getScreenType().getScreenTypeId() <= 0) {
            throw new IllegalArgumentException("screenTypeId phải lớn hơn 0!");
        }

        if (Double.isNaN(ticketPricing.getPrice()) || Double.isInfinite(ticketPricing.getPrice())) {
            throw new IllegalArgumentException("price không hợp lệ!");
        }

        if (ticketPricing.getPrice() <= 0) {
            throw new IllegalArgumentException("price phải lớn hơn 0!");
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