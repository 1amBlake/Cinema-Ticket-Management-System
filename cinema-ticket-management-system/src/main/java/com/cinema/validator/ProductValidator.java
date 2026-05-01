package com.cinema.validator;

import com.cinema.entity.Product;
import com.cinema.enums.ProductStatus;

/**
 * Validator cho thực thể Product.
 * Chịu trách nhiệm kiểm tra dữ liệu của đối tượng Product
 * trước khi xử lý ở các lớp khác trong chương trình.
 * 
 * @author Quốc Anh (chính)
 * @author Minh Huy (sửa)
 */
public final class ProductValidator {

    private static final int MAX_PRODUCT_NAME_LENGTH = 255;
    private static final int MAX_PICTURE_URL_LENGTH = 255;

    /*
     * Cho phép:
     * - Chữ tiếng Việt / tiếng Anh
     * - Số
     * - Khoảng trắng
     * - Các ký tự thường dùng trong tên sản phẩm: _ - . ( ) + / &
     * 
     * Ví dụ hợp lệ:
     * - Bắp L
     * - Coca-Cola M
     * - Combo 1 + 1
     * - Pepsi / 7Up
     * - Michael Figure
     */
    private static final String PRODUCT_NAME_REGEX = "[\\p{L}\\p{N}\\s_\\-().+/&]+";

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

        if (product.getProductName().trim().length() > MAX_PRODUCT_NAME_LENGTH) {
            throw new IllegalArgumentException(
                    "productName không được vượt quá " + MAX_PRODUCT_NAME_LENGTH + " ký tự!"
            );
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

        if (product.getPictureUrl() != null
                && product.getPictureUrl().trim().length() > MAX_PICTURE_URL_LENGTH) {
            throw new IllegalArgumentException(
                    "pictureUrl không được vượt quá " + MAX_PICTURE_URL_LENGTH + " ký tự!"
            );
        }

        validateBusinessRule(product);
    }

    /**
     * Kiểm tra các ràng buộc nghiệp vụ nâng cao của Product.
     *
     * Quy ước nghiệp vụ:
     * - Tên sản phẩm chỉ được chứa các ký tự phù hợp với dữ liệu bán hàng.
     * - Sản phẩm đang bán (SELLING) phải còn tồn kho.
     * - Sản phẩm hết hàng (SOLD_OUT) thì số lượng tồn phải bằng 0.
     * - Sản phẩm ngừng bán (STOPPED) có thể còn hoặc hết tồn kho,
     *   vì đây là trạng thái do rạp chủ động ngừng kinh doanh.
     *
     * @param product - Đối tượng Product cần kiểm tra
     */
    private static void validateBusinessRule(Product product) {
        String productName = product.getProductName().trim();

        if (!productName.matches(PRODUCT_NAME_REGEX)) {
            throw new IllegalArgumentException(
                    "productName chỉ được chứa chữ cái, chữ số, khoảng trắng "
                    + "và các ký tự _ - . ( ) + / &!"
            );
        }

        if (product.getProductStatus() == ProductStatus.SELLING
                && product.getStockQuantity() == 0) {
            throw new IllegalArgumentException(
                    "Sản phẩm đang bán phải có số lượng tồn lớn hơn 0!"
            );
        }

        if (product.getProductStatus() == ProductStatus.SOLD_OUT
                && product.getStockQuantity() > 0) {
            throw new IllegalArgumentException(
                    "Sản phẩm hết hàng thì số lượng tồn phải bằng 0!"
            );
        }
    }
}
