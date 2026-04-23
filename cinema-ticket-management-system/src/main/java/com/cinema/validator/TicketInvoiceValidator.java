package com.cinema.validator;

import com.cinema.entity.TicketInvoice;

/**
 * Validator cho thực thể TicketInvoice.
 * Chịu trách nhiệm kiểm tra dữ liệu của đối tượng TicketInvoice
 * trước khi xử lý ở các lớp khác trong chương trình.
 * 
 * @author Minh Huy (chính)
 */
public final class TicketInvoiceValidator {

    /**
     * Constructor private để ngăn tạo đối tượng từ lớp TicketInvoiceValidator.
     */
    private TicketInvoiceValidator() {
    }

    /**
     * Kiểm tra dữ liệu của TicketInvoice trong trường hợp thêm mới.
     *
     * @param ticketInvoice - Đối tượng TicketInvoice cần kiểm tra
     */
    public static void validateForCreate(TicketInvoice ticketInvoice) {
        validateCommon(ticketInvoice);
    }

    /**
     * Kiểm tra dữ liệu của TicketInvoice trong trường hợp cập nhật.
     *
     * @param ticketInvoice - Đối tượng TicketInvoice cần kiểm tra
     */
    public static void validateForUpdate(TicketInvoice ticketInvoice) {
        validateCommon(ticketInvoice);
    }

    /**
     * Kiểm tra các ràng buộc chung của thực thể TicketInvoice.
     *
     * @param ticketInvoice - Đối tượng TicketInvoice cần kiểm tra
     */
    private static void validateCommon(TicketInvoice ticketInvoice) {
        if (ticketInvoice == null) {
            throw new IllegalArgumentException("ticketInvoice không được null!");
        }

        if (ticketInvoice.getInvoice() == null) {
            throw new IllegalArgumentException("invoice không được null!");
        }

        if (ticketInvoice.getInvoice().getInvoiceId() <= 0) {
            throw new IllegalArgumentException("invoiceId phải lớn hơn 0!");
        }

        if (ticketInvoice.getTicket() == null) {
            throw new IllegalArgumentException("ticket không được null!");
        }

        if (ticketInvoice.getTicket().getTicketId() <= 0) {
            throw new IllegalArgumentException("ticketId phải lớn hơn 0!");
        }

        if (ticketInvoice.getPrice() <= 0) {
            throw new IllegalArgumentException("price phải lớn hơn 0!");
        }

        validateBusinessRule(ticketInvoice);
    }

    /**
     * Kiểm tra các ràng buộc nghiệp vụ nâng cao của TicketInvoice.
     *
     * @param ticketInvoice - Đối tượng TicketInvoice cần kiểm tra
     */
    private static void validateBusinessRule(TicketInvoice ticketInvoice) {
    }
}