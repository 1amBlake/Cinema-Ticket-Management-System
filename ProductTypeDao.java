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

/**
 * DAO cho thực thể ProductType
 * Chịu trách nhiệm thao tác dữ liệu với bảng loai_san_pham trong MySQL.
 * 
 * Bảng ánh xạ:
 * loai_san_pham(ma_loai_san_pham, ten_loai_san_pham, created_at, updated_at)
 * 
 * @author Quốc Anh
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
			SELECT ma_loai_san_pham, ten_loai_san_pham, created_at, updated_at
			FROM loai_san_pham
			WHERE ma_loai_san_pham = ?
			""";

	private static final String SELECT_ALL_MYSQL = """
			SELECT ma_loai_san_pham, ten_loai_san_pham, created_at, updated_at
			FROM loai_san_pham
			ORDER BY ten_loai_san_pham ASC
			""";

	private static final String SEARCH_BY_NAME_MYSQL = """
			SELECT ma_loai_san_pham, ten_loai_san_pham, created_at, updated_at
			FROM loai_san_pham
			WHERE ten_loai_san_pham LIKE ?
			ORDER BY ten_loai_san_pham ASC
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
			WHERE ten_loai_san_pham = ? AND ma_loai_san_pham <> ?
			LIMIT 1
			""";

	/**
	 * Thêm loại sản phẩm mới
	 * 
	 * @param productType - đối tượng cần thêm
	 * @return true nếu thêm thành công
	 * @throws SQLException nếu lỗi SQL
	 */
	public boolean addProductType(ProductType productType) throws SQLException {
		validateProductType(productType);

		if (existsByName(productType.getProductTypeName())) {
			throw new IllegalArgumentException("Tên loại sản phẩm đã tồn tại!");
		}

		try (
			Connection con = DBConnection.getConnection();
			PreparedStatement ps = con.prepareStatement(INSERT_MYSQL)
		) {
			ps.setString(1, productType.getProductTypeName());
			return ps.executeUpdate() > 0;
		}
	}

	/**
	 * Cập nhật loại sản phẩm
	 * 
	 * @param productType - dữ liệu cập nhật
	 * @return true nếu cập nhật thành công
	 * @throws SQLException nếu lỗi SQL
	 */
	public boolean updateProductType(ProductType productType) throws SQLException {
		validateProductType(productType);

		if (productType.getProductTypeId() <= 0) {
			throw new IllegalArgumentException("productTypeId phải lớn hơn 0!");
		}

		if (existsByNameExceptId(
				productType.getProductTypeName(),
				productType.getProductTypeId())) {
			throw new IllegalArgumentException("Tên loại sản phẩm đã tồn tại!");
		}

		try (
			Connection con = DBConnection.getConnection();
			PreparedStatement ps = con.prepareStatement(UPDATE_MYSQL)
		) {
			ps.setString(1, productType.getProductTypeName());
			ps.setInt(2, productType.getProductTypeId());

			return ps.executeUpdate() > 0;
		}
	}

	/**
	 * Xóa theo mã
	 * 
	 * @param productTypeId - mã loại sản phẩm
	 * @return true nếu xóa thành công
	 * @throws SQLException nếu lỗi SQL
	 */
	public boolean deleteProductTypeById(int productTypeId) throws SQLException {
		if (productTypeId <= 0) {
			throw new IllegalArgumentException("productTypeId phải lớn hơn 0!");
		}

		try (
			Connection con = DBConnection.getConnection();
			PreparedStatement ps = con.prepareStatement(DELETE_MYSQL)
		) {
			ps.setInt(1, productTypeId);
			return ps.executeUpdate() > 0;
		}
	}

	/**
	 * Tìm theo mã
	 * 
	 * @param productTypeId - mã loại sản phẩm
	 * @return ProductType hoặc null
	 * @throws SQLException nếu lỗi SQL
	 */
	public ProductType findById(int productTypeId) throws SQLException {
		if (productTypeId <= 0) {
			throw new IllegalArgumentException("productTypeId phải lớn hơn 0!");
		}

		try (
			Connection con = DBConnection.getConnection();
			PreparedStatement ps = con.prepareStatement(SELECT_BY_ID_MYSQL)
		) {
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
	 * @throws SQLException nếu lỗi SQL
	 */
	public List<ProductType> getAllProductTypes() throws SQLException {
		List<ProductType> list = new ArrayList<>();

		try (
			Connection con = DBConnection.getConnection();
			PreparedStatement ps = con.prepareStatement(SELECT_ALL_MYSQL);
			ResultSet rs = ps.executeQuery()
		) {
			while (rs.next()) {
				list.add(mapResultSetToProductType(rs));
			}
		}

		return list;
	}

	/**
	 * Tìm kiếm gần đúng theo tên
	 * 
	 * @param keyword từ khóa
	 * @return danh sách phù hợp
	 * @throws SQLException nếu lỗi SQL
	 */
	public List<ProductType> searchByName(String keyword) throws SQLException {
		List<ProductType> list = new ArrayList<>();

		if (keyword == null) {
			keyword = "";
		}

		try (
			Connection con = DBConnection.getConnection();
			PreparedStatement ps = con.prepareStatement(SEARCH_BY_NAME_MYSQL)
		) {
			ps.setString(1, "%" + keyword.trim() + "%");

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					list.add(mapResultSetToProductType(rs));
				}
			}
		}

		return list;
	}

	/**
	 * Kiểm tra tên đã tồn tại
	 */
	public boolean existsByName(String productTypeName) throws SQLException {
		if (productTypeName == null || productTypeName.trim().isEmpty()) {
			return false;
		}

		try (
			Connection con = DBConnection.getConnection();
			PreparedStatement ps = con.prepareStatement(EXISTS_BY_NAME_MYSQL)
		) {
			ps.setString(1, productTypeName.trim());

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	/**
	 * Kiểm tra tên đã tồn tại ở record khác
	 */
	public boolean existsByNameExceptId(String productTypeName, int productTypeId)
			throws SQLException {

		try (
			Connection con = DBConnection.getConnection();
			PreparedStatement ps = con.prepareStatement(EXISTS_BY_NAME_EXCEPT_ID_MYSQL)
		) {
			ps.setString(1, productTypeName.trim());
			ps.setInt(2, productTypeId);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	/**
	 * Ánh xạ ResultSet -> ProductType
	 */
	private ProductType mapResultSetToProductType(ResultSet rs) throws SQLException {
		Timestamp createdAt = rs.getTimestamp("created_at");
		Timestamp updatedAt = rs.getTimestamp("updated_at");

		return new ProductType(
			rs.getInt("ma_loai_san_pham"),
			rs.getString("ten_loai_san_pham"),
			createdAt != null ? createdAt.toLocalDateTime() : null,
			updatedAt != null ? updatedAt.toLocalDateTime() : null
		);
	}

	/**
	 * Validate dữ liệu đầu vào
	 */
	private void validateProductType(ProductType productType) {
		if (productType == null) {
			throw new IllegalArgumentException("ProductType không được null!");
		}

		if (productType.getProductTypeName() == null
				|| productType.getProductTypeName().trim().isEmpty()) {
			throw new IllegalArgumentException("Tên loại sản phẩm không được để trống!");
		}
	}
}