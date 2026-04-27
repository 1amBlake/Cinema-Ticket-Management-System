package com.cinema.validator;

import com.cinema.entity.Product;

/**
 * Validator cho thực thể Product.
 * Chịu trách nhiệm kiểm tra dữ liệu của đối tượng Product
 * trước khi xử lý ở các lớp khác trong chương trình.
 * 
 * @author Quốc Anh (chính)
 * @author Minh Huy (sửa)
 */
public final class ProductValidator {

    /**
     * Constructor private để ngăn tạo đối tượng từ lớp ProductValidator.
     */
    private ProductValidator() {
    }

    /**
     * Kiểm tra dữ liệu của Product trong trường hợp thêm mới.
     *
     * @param product - Đối tượng Product cần kiểm tra
     */
    public static void validateForCreate(Product product) {
        validateCommon(product);
    }

    /**
     * Kiểm tra dữ liệu của Product trong trường hợp cập nhật.
     *
     * @param product - Đối tượng Product cần kiểm tra
     */
    public static void validateForUpdate(Product product) {
        validateCommon(product);

        if (product.getProductId() <= 0) {
            throw new IllegalArgumentException("productId phải lớn hơn 0!");
        }
    }

    /**
     * Kiểm tra các ràng buộc chung của thực thể Product.
     * Đây là các kiểm tra dữ liệu cơ bản để đảm bảo object hợp lệ
     * trước khi đi vào DAO hoặc Service.
     *
     * @param product - Đối tượng Product cần kiểm tra
     */
    private static void validateCommon(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("product không được null!");
        }

        if (product.getProductName() == null || product.getProductName().trim().isEmpty()) {
            throw new IllegalArgumentException("productName không được để trống!");
        }

        if (product.getProductName().trim().length() > 255) {
            throw new IllegalArgumentException("productName không được vượt quá 255 ký tự!");
        }

        if (product.getProductType() == null) {
            throw new IllegalArgumentException("productType không được null!");
        }

        if (product.getProductType().getProductTypeId() <= 0) {
            throw new IllegalArgumentException("productTypeId phải lớn hơn 0!");
        }

        if (Double.isNaN(product.getPrice()) || Double.isInfinite(product.getPrice())) {
            throw new IllegalArgumentException("price không hợp lệ!");
        }

        if (product.getPrice() <= 0) {
            throw new IllegalArgumentException("price phải lớn hơn 0!");
        }

        if (product.getStockQuantity() < 0) {
            throw new IllegalArgumentException("stockQuantity phải lớn hơn hoặc bằng 0!");
        }

        if (product.getProductStatus() == null) {
            throw new IllegalArgumentException("productStatus không được null!");
        }

        if (product.getPictureUrl() != null && product.getPictureUrl().trim().length() > 255) {
            throw new IllegalArgumentException("pictureUrl không được vượt quá 255 ký tự!");
        }

        validateBusinessRule(product);
    }

    /**
     * Kiểm tra các ràng buộc nghiệp vụ nâng cao của Product.
     *
     * @param product - Đối tượng Product cần kiểm tra
     */
    private static void validateBusinessRule(Product product) {
    }
}