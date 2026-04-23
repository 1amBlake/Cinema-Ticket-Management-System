package com.cinema.validator;

import com.cinema.entity.ProductInvoice;

/**
 * Validator cho thực thể ProductInvoice.
 * Chịu trách nhiệm kiểm tra dữ liệu của đối tượng ProductInvoice
 * trước khi xử lý ở các lớp khác trong chương trình.
 * 
 * @author Minh Huy (chính)
 */
public final class ProductInvoiceValidator {

    /**
     * Constructor private để ngăn tạo đối tượng từ lớp ProductInvoiceValidator.
     */
    private ProductInvoiceValidator() {
    }

    /**
     * Kiểm tra dữ liệu của ProductInvoice trong trường hợp thêm mới.
     *
     * @param productInvoice - Đối tượng ProductInvoice cần kiểm tra
     */
    public static void validateForCreate(ProductInvoice productInvoice) {
        validateCommon(productInvoice);
    }

    /**
     * Kiểm tra dữ liệu của ProductInvoice trong trường hợp cập nhật.
     *
     * @param productInvoice - Đối tượng ProductInvoice cần kiểm tra
     */
    public static void validateForUpdate(ProductInvoice productInvoice) {
        validateCommon(productInvoice);
    }

    /**
     * Kiểm tra các ràng buộc chung của thực thể ProductInvoice.
     *
     * @param productInvoice - Đối tượng ProductInvoice cần kiểm tra
     */
    private static void validateCommon(ProductInvoice productInvoice) {
        if (productInvoice == null) {
            throw new IllegalArgumentException("productInvoice không được null!");
        }

        if (productInvoice.getInvoice() == null) {
            throw new IllegalArgumentException("invoice không được null!");
        }

        if (productInvoice.getInvoice().getInvoiceId() <= 0) {
            throw new IllegalArgumentException("invoiceId phải lớn hơn 0!");
        }

        if (productInvoice.getProduct() == null) {
            throw new IllegalArgumentException("product không được null!");
        }

        if (productInvoice.getProduct().getProductId() <= 0) {
            throw new IllegalArgumentException("productId phải lớn hơn 0!");
        }

        if (productInvoice.getQuantity() <= 0) {
            throw new IllegalArgumentException("quantity phải lớn hơn 0!");
        }

        if (productInvoice.getUnitPrice() <= 0) {
            throw new IllegalArgumentException("unitPrice phải lớn hơn 0!");
        }

        validateBusinessRule(productInvoice);
    }

    /**
     * Kiểm tra các ràng buộc nghiệp vụ nâng cao của ProductInvoice.
     *
     * @param productInvoice - Đối tượng ProductInvoice cần kiểm tra
     */
    private static void validateBusinessRule(ProductInvoice productInvoice) {
    }
}