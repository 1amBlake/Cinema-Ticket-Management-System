package com.cinema.validator;

import java.time.LocalDateTime;

import com.cinema.entity.Invoice;
import com.cinema.enums.InvoiceStatus;

/**
 * Validator cho thực thể Invoice.
 * Chịu trách nhiệm kiểm tra dữ liệu của đối tượng Invoice
 * trước khi xử lý ở các lớp khác trong chương trình.
 * 
 * @author Minh Huy (chính)
 */
public final class InvoiceValidator {

    /**
     * Constructor private để ngăn tạo đối tượng từ lớp InvoiceValidator.
     */
    private InvoiceValidator() {
    }

    /**
     * Kiểm tra dữ liệu của Invoice trong trường hợp thêm mới.
     *
     * @param invoice - Đối tượng Invoice cần kiểm tra
     */
    public static void validateForCreate(Invoice invoice) {
        validateCommon(invoice);
    }

    /**
     * Kiểm tra dữ liệu của Invoice trong trường hợp cập nhật.
     *
     * @param invoice - Đối tượng Invoice cần kiểm tra
     */
    public static void validateForUpdate(Invoice invoice) {
        validateCommon(invoice);

        if (invoice.getInvoiceId() <= 0) {
            throw new IllegalArgumentException("invoiceId phải lớn hơn 0!");
        }
    }

    /**
     * Kiểm tra các ràng buộc chung của thực thể Invoice.
     *
     * @param invoice - Đối tượng Invoice cần kiểm tra
     */
    private static void validateCommon(Invoice invoice) {
        if (invoice == null) {
            throw new IllegalArgumentException("invoice không được null!");
        }

        if (invoice.getEmployee() == null) {
            throw new IllegalArgumentException("employee không được null!");
        }

        if (invoice.getEmployee().getEmployeeId() <= 0) {
            throw new IllegalArgumentException("employeeId phải lớn hơn 0!");
        }

        if (invoice.getPaymentMethod() == null) {
            throw new IllegalArgumentException("paymentMethod không được null!");
        }

        if (invoice.getInvoiceStatus() == null) {
            throw new IllegalArgumentException("invoiceStatus không được null!");
        }

        if (invoice.getPaymentTime() != null
                && invoice.getPaymentTime().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("paymentTime không được ở tương lai!");
        }

        validateBusinessRule(invoice);
    }

    /**
     * Kiểm tra các ràng buộc nghiệp vụ nâng cao của Invoice.
     *
     * @param invoice - Đối tượng Invoice cần kiểm tra
     */
    private static void validateBusinessRule(Invoice invoice) {
        if (invoice.getInvoiceStatus() == InvoiceStatus.PENDING
                && invoice.getPaymentTime() != null) {
            throw new IllegalArgumentException("Hóa đơn PENDING thì paymentTime phải là null!");
        }

        if (invoice.getInvoiceStatus() == InvoiceStatus.SUCCESS
                && invoice.getPaymentTime() == null) {
            throw new IllegalArgumentException("Hóa đơn SUCCESS thì paymentTime không được null!");
        }
    }
}