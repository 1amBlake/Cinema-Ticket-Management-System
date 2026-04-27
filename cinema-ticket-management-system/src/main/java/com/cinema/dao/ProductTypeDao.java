package com.cinema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.cinema.config.DBConnection;
import com.cinema.entity.ProductType;
import com.cinema.validator.ProductTypeValidator;

/**
 * DAO cho thực thể ProductType
 * Chịu trách nhiệm thao tác dữ liệu với bảng loai_san_pham trong MySQL.
 * 
 * Bảng ánh xạ:
 * loai_san_pham(
 *     ma_loai_san_pham,
 *     ten_loai_san_pham,
 *     created_at,
 *     updated_at
 * )
 * 
 * @author Quốc Anh (chính)
 * @author Minh Huy (sửa vài chi tiết)
 */
public class ProductTypeDao {

    private static final String INSERT_MYSQL = """
            INSERT INTO loai_san_pham (ten_loai_san_pham)
            VALUES (?)
            """;

    private static final String UPDATE_MYSQL = """
            UPDATE loai_san_pham
            SET ten_loai_san_pham = ?
            WHERE ma_loai_san_pham = ?
            """;

    private static final String DELETE_MYSQL = """
            DELETE FROM loai_san_pham
            WHERE ma_loai_san_pham = ?
            """;

    private static final String SELECT_BY_ID_MYSQL = """
            SELECT ma_loai_san_pham,
                   ten_loai_san_pham,
                   created_at,
                   updated_at
            FROM loai_san_pham
            WHERE ma_loai_san_pham = ?
            """;

    private static final String SELECT_ALL_MYSQL = """
            SELECT ma_loai_san_pham,
                   ten_loai_san_pham,
                   created_at,
                   updated_at
            FROM loai_san_pham
            ORDER BY ten_loai_san_pham ASC, ma_loai_san_pham ASC
            """;

    private static final String SEARCH_BY_NAME_MYSQL = """
            SELECT ma_loai_san_pham,
                   ten_loai_san_pham,
                   created_at,
                   updated_at
            FROM loai_san_pham
            WHERE ten_loai_san_pham LIKE ?
            ORDER BY ten_loai_san_pham ASC, ma_loai_san_pham ASC
            """;

    private static final String EXISTS_BY_NAME_MYSQL = """
            SELECT 1
            FROM loai_san_pham
            WHERE ten_loai_san_pham = ?
            LIMIT 1
            """;

    private static final String EXISTS_BY_NAME_EXCEPT_ID_MYSQL = """
            SELECT 1
            FROM loai_san_pham
            WHERE ten_loai_san_pham = ?
              AND ma_loai_san_pham <> ?
            LIMIT 1
            """;

    private static final String IS_USED_IN_PRODUCT_MYSQL = """
            SELECT 1
            FROM san_pham
            WHERE ma_loai_san_pham = ?
            LIMIT 1
            """;

    /**
     * Kiểm tra tên loại sản phẩm đã tồn tại hay chưa
     * 
     * @param productTypeName - Tên loại sản phẩm
     * @return true nếu đã tồn tại
     * @throws SQLException nếu có lỗi SQL
     */
    private boolean existsByName(String productTypeName) throws SQLException {
        if (productTypeName == null || productTypeName.trim().isEmpty()) {
            return false;
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(EXISTS_BY_NAME_MYSQL)) {

            ps.setString(1, productTypeName.trim());

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Kiểm tra tên loại sản phẩm đã tồn tại ở bản ghi khác hay chưa
     * 
     * @param productTypeName - Tên loại sản phẩm
     * @param productTypeId - Mã loại sản phẩm
     * @return true nếu đã tồn tại ở bản ghi khác
     * @throws SQLException nếu có lỗi SQL
     */
    private boolean existsByNameExceptId(String productTypeName, int productTypeId) throws SQLException {
        if (productTypeId <= 0 || productTypeName == null || productTypeName.trim().isEmpty()) {
            throw new IllegalArgumentException("productTypeId phải lớn hơn 0 và productTypeName không được để trống!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(EXISTS_BY_NAME_EXCEPT_ID_MYSQL)) {

            ps.setString(1, productTypeName.trim());
            ps.setInt(2, productTypeId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Kiểm tra loại sản phẩm có đang được sử dụng ở bảng Product hay không
     * 
     * @param productTypeId - Mã loại sản phẩm
     * @return true nếu đang được sử dụng
     * @throws SQLException nếu có lỗi SQL
     */
    private boolean isUsedInProduct(int productTypeId) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(IS_USED_IN_PRODUCT_MYSQL)) {

            ps.setInt(1, productTypeId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Kiểm tra loại sản phẩm có đang được sử dụng ở các bảng liên quan hay không
     * 
     * @param productTypeId - Mã loại sản phẩm
     * @return true nếu đang được sử dụng
     * @throws SQLException nếu có lỗi SQL
     */
    private boolean isProductTypeUsed(int productTypeId) throws SQLException {
        if (productTypeId <= 0) {
            throw new IllegalArgumentException("productTypeId phải lớn hơn 0!");
        }

        return isUsedInProduct(productTypeId);
    }

    /**
     * Ánh xạ một dòng dữ liệu ResultSet thành đối tượng ProductType
     * 
     * @param rs - ResultSet đang trỏ tới dòng dữ liệu hợp lệ
     * @return đối tượng ProductType
     * @throws SQLException nếu có lỗi SQL
     */
    private ProductType mapResultSetToProductType(ResultSet rs) throws SQLException {
        Timestamp createdAt = rs.getTimestamp("created_at");
        Timestamp updatedAt = rs.getTimestamp("updated_at");

        ProductType aProductType = new ProductType(
                rs.getInt("ma_loai_san_pham"),
                rs.getString("ten_loai_san_pham"),
                createdAt != null ? createdAt.toLocalDateTime() : null,
                updatedAt != null ? updatedAt.toLocalDateTime() : null
        );

        return aProductType;
    }

    /**
     * Thêm loại sản phẩm mới
     * 
     * @param productType - Đối tượng loại sản phẩm
     * @return true nếu thêm thành công
     * @throws SQLException nếu có lỗi SQL
     */
    public boolean addProductType(ProductType productType) throws SQLException {
        ProductTypeValidator.validateForCreate(productType);

        if (productType == null) {
            throw new IllegalArgumentException("productType không được null!");
        }

        if (existsByName(productType.getProductTypeName())) {
            throw new IllegalArgumentException("Loại sản phẩm đã tồn tại!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_MYSQL)) {

            ps.setString(1, productType.getProductTypeName().trim());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Cập nhật thông tin loại sản phẩm
     * 
     * @param productType - Đối tượng loại sản phẩm
     * @return true nếu cập nhật thành công
     * @throws SQLException nếu có lỗi SQL
     */
    public boolean updateProductType(ProductType productType) throws SQLException {
    	ProductTypeValidator.validateForUpdate(productType);

        if (productType == null) {
            throw new IllegalArgumentException("productType không được null!");
        }

        if (productType.getProductTypeId() <= 0) {
            throw new IllegalArgumentException("productTypeId phải lớn hơn 0!");
        }

        if (existsByNameExceptId(productType.getProductTypeName(), productType.getProductTypeId())) {
            throw new IllegalArgumentException("Loại sản phẩm đã tồn tại ở bản ghi khác!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_MYSQL)) {

            ps.setString(1, productType.getProductTypeName().trim());
            ps.setInt(2, productType.getProductTypeId());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Xóa loại sản phẩm theo mã.
     * Không cho xóa nếu loại sản phẩm đang được dùng trong bảng sản phẩm.
     * 
     * @param productTypeId - Mã loại sản phẩm
     * @return true nếu xóa thành công
     * @throws SQLException nếu có lỗi SQL
     */
    public boolean deleteProductTypeById(int productTypeId) throws SQLException {
        if (productTypeId <= 0) {
            throw new IllegalArgumentException("productTypeId phải lớn hơn 0!");
        }

        if (isProductTypeUsed(productTypeId)) {
            throw new IllegalArgumentException("Loại sản phẩm đang được sử dụng, không thể xóa!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_MYSQL)) {

            ps.setInt(1, productTypeId);

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Tìm loại sản phẩm theo mã
     * 
     * @param productTypeId - Mã loại sản phẩm
     * @return đối tượng ProductType nếu tìm thấy, ngược lại trả về null
     * @throws SQLException nếu có lỗi SQL
     */
    public ProductType findById(int productTypeId) throws SQLException {
        if (productTypeId <= 0) {
            throw new IllegalArgumentException("productTypeId phải lớn hơn 0!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID_MYSQL)) {

            ps.setInt(1, productTypeId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProductType(rs);
                }
            }
        }

        return null;
    }

    /**
     * Lấy tất cả loại sản phẩm
     * 
     * @return danh sách loại sản phẩm
     * @throws SQLException nếu có lỗi SQL
     */
    public List<ProductType> getAllProductTypes() throws SQLException {
        List<ProductType> productTypes = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_ALL_MYSQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                productTypes.add(mapResultSetToProductType(rs));
            }
        }

        return productTypes;
    }

    /**
     * Tìm loại sản phẩm theo tên gần đúng
     * 
     * @param keyword - Từ khóa tên loại sản phẩm
     * @return danh sách loại sản phẩm phù hợp
     * @throws SQLException nếu có lỗi SQL
     */
    public List<ProductType> searchByName(String keyword) throws SQLException {
        List<ProductType> productTypes = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SEARCH_BY_NAME_MYSQL)) {

            keyword = (keyword == null) ? "" : keyword.trim();
            ps.setString(1, "%" + keyword + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    productTypes.add(mapResultSetToProductType(rs));
                }
            }
        }

        return productTypes;
    }
}