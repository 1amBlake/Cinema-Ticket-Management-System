package com.cinema.validator;

import com.cinema.entity.TicketInvoice;

/**
 * Validator cho thực thể TicketInvoice.
 * Chịu trách nhiệm kiểm tra dữ liệu của đối tượng TicketInvoice
 * trước khi xử lý ở các lớp khác trong chương trình.
 * 
 * <p>
 * Lưu ý:
 * Một phần ràng buộc đã được kiểm tra ở entity thông qua setter,
 * tuy nhiên validator vẫn kiểm tra lại để bảo vệ đa lớp, tránh dữ liệu
 * không hợp lệ đi sâu vào DAO / Service.
 * </p>
 * 
 * <p>
 * Các ràng buộc nghiệp vụ liên quan đến truy vấn dữ liệu như:
 * vé đã tồn tại trong cùng hóa đơn hay chưa,
 * vé đã được gán sang hóa đơn khác chưa,
 * hóa đơn đã thanh toán / hủy rồi có được sửa chi tiết hay không,
 * hoặc giá vé trong chi tiết hóa đơn có cần đối chiếu với bảng giá hiện tại hay không
 * nên tiếp tục đặt ở DAO / Service.
 * </p>
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
     * Kiểm tra một số ràng buộc nghiệp vụ cơ bản của TicketInvoice
     * không cần truy vấn database.
     *
     * @param ticketInvoice - Đối tượng TicketInvoice cần kiểm tra
     */
    private static void validateBusinessRule(TicketInvoice ticketInvoice) {
        if (ticketInvoice.getInvoice() == null
                || ticketInvoice.getTicket() == null) {
            return;
        }

        if (ticketInvoice.getInvoice().getInvoiceId() <= 0
                || ticketInvoice.getTicket().getTicketId() <= 0) {
            return;
        }

        if (ticketInvoice.getPrice() <= 0) {
            throw new IllegalArgumentException("price phải lớn hơn 0!");
        }
    }
}