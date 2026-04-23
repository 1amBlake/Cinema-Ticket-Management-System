package com.cinema.validator;

import com.cinema.entity.Ticket;

/**
 * Validator cho thực thể Ticket.
 * Chịu trách nhiệm kiểm tra dữ liệu của đối tượng Ticket
 * trước khi xử lý ở các lớp khác trong chương trình.
 * 
 * @author Minh Huy (chính)
 */
public final class TicketValidator {

    /**
     * Constructor private để ngăn tạo đối tượng từ lớp TicketValidator.
     */
    private TicketValidator() {
    }

    /**
     * Kiểm tra dữ liệu của Ticket trong trường hợp thêm mới.
     *
     * @param ticket - Đối tượng Ticket cần kiểm tra
     */
    public static void validateForCreate(Ticket ticket) {
        validateCommon(ticket);
    }

    /**
     * Kiểm tra dữ liệu của Ticket trong trường hợp cập nhật.
     *
     * @param ticket - Đối tượng Ticket cần kiểm tra
     */
    public static void validateForUpdate(Ticket ticket) {
        validateCommon(ticket);

        if (ticket.getTicketId() <= 0) {
            throw new IllegalArgumentException("ticketId phải lớn hơn 0!");
        }
    }

    /**
     * Kiểm tra các ràng buộc chung của thực thể Ticket.
     *
     * @param ticket - Đối tượng Ticket cần kiểm tra
     */
    private static void validateCommon(Ticket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("ticket không được null!");
        }

        if (ticket.getMovieSession() == null) {
            throw new IllegalArgumentException("movieSession không được null!");
        }

        if (ticket.getMovieSession().getMovieSessionId() <= 0) {
            throw new IllegalArgumentException("movieSessionId phải lớn hơn 0!");
        }

        if (ticket.getSeat() == null) {
            throw new IllegalArgumentException("seat không được null!");
        }

        if (ticket.getSeat().getSeatId() <= 0) {
            throw new IllegalArgumentException("seatId phải lớn hơn 0!");
        }

        if (ticket.getTicketPricing() == null) {
            throw new IllegalArgumentException("ticketPricing không được null!");
        }

        if (ticket.getTicketPricing().getTicketPricingId() <= 0) {
            throw new IllegalArgumentException("ticketPricingId phải lớn hơn 0!");
        }

        if (ticket.getTicketStatus() == null) {
            throw new IllegalArgumentException("ticketStatus không được null!");
        }

        validateBusinessRule(ticket);
    }

    /**
     * Kiểm tra các ràng buộc nghiệp vụ nâng cao của Ticket.
     *
     * @param ticket - Đối tượng Ticket cần kiểm tra
     */
    private static void validateBusinessRule(Ticket ticket) {
    }
}