package com.cinema.validator;

import com.cinema.entity.ProductType;

/**
 * Validator cho thực thể ProductType.
 * Chịu trách nhiệm kiểm tra dữ liệu của đối tượng ProductType
 * trước khi xử lý ở các lớp khác trong chương trình.
 * 
 * @author Trọng (chính)
 * @author Minh Huy (sửa)
 */
public final class ProductTypeValidator {

    /**
     * Constructor private để ngăn tạo đối tượng từ lớp ProductTypeValidator.
     */
    private ProductTypeValidator() {
    }

    /**
     * Kiểm tra dữ liệu của ProductType trong trường hợp thêm mới.
     *
     * @param productType - Đối tượng ProductType cần kiểm tra
     */
    public static void validateForCreate(ProductType productType) {
        validateCommon(productType);
    }

    /**
     * Kiểm tra dữ liệu của ProductType trong trường hợp cập nhật.
     *
     * @param productType - Đối tượng ProductType cần kiểm tra
     */
    public static void validateForUpdate(ProductType productType) {
        validateCommon(productType);

        if (productType.getProductTypeId() <= 0) {
            throw new IllegalArgumentException("productTypeId phải lớn hơn 0!");
        }
    }

    /**
     * Kiểm tra các ràng buộc chung của thực thể ProductType.
     * Đây là các kiểm tra dữ liệu cơ bản để đảm bảo object hợp lệ
     * trước khi đi vào DAO hoặc Service.
     *
     * @param productType - Đối tượng ProductType cần kiểm tra
     */
    private static void validateCommon(ProductType productType) {
        if (productType == null) {
            throw new IllegalArgumentException("productType không được null!");
        }

        if (productType.getProductTypeName() == null
                || productType.getProductTypeName().trim().isEmpty()) {
            throw new IllegalArgumentException("productTypeName không được để trống!");
        }

        if (productType.getProductTypeName().trim().length() > 255) {
            throw new IllegalArgumentException("productTypeName không được vượt quá 255 ký tự!");
        }

        validateBusinessRule(productType);
    }

    /**
     * Kiểm tra các ràng buộc nghiệp vụ nâng cao của ProductType.
     *
     * @param productType - Đối tượng ProductType cần kiểm tra
     */
    private static void validateBusinessRule(ProductType productType) {
    }
}