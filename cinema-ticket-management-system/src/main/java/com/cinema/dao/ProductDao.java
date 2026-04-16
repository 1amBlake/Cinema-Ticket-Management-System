package com.cinema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.cinema.config.DBConnection;
import com.cinema.entity.Product;
import com.cinema.entity.ProductType;
import com.cinema.enums.ProductStatus;

/**
 * DAO cho thực thể Product
 * Chịu trách nhiệm thao tác dữ liệu với bảng sản phẩm trong MySQL
 * 
 * Bảng ánh xạ:
 * san_pham(
 * 	ma_san_pham,
 * 	ten_san_pham,
 * 	ma_loai_san_pham,
 * 	gia_co_ban,
 * 	so_luong_ton,
 * 	trang_thai,
 * 	url_anh,
 * 	created_at,
 * 	updated_at
 * )
 * 
 * @author Minh Huy (chính)
 */
public class ProductDao {
	private static final String INSERT_MYSQL = """
	        INSERT INTO san_pham (
	                ten_san_pham,
	                ma_loai_san_pham,
	                gia_co_ban,
	                so_luong_ton,
	                trang_thai,
	                url_anh
	        )
	        VALUES (?, ?, ?, ?, ?, ?)
	        """;

	private static final String UPDATE_MYSQL = """
	        UPDATE san_pham
	        SET ten_san_pham = ?,
	            ma_loai_san_pham = ?,
	            gia_co_ban = ?,
	            so_luong_ton = ?,
	            trang_thai = ?,
	            url_anh = ?
	        WHERE ma_san_pham = ?
	        """;

	private static final String DELETE_MYSQL = """
	        DELETE FROM san_pham
	        WHERE ma_san_pham = ?
	        """;

	private static final String SELECT_BY_ID_MYSQL = """
	        SELECT ma_san_pham,
	               ten_san_pham,
	               ma_loai_san_pham,
	               gia_co_ban,
	               so_luong_ton,
	               trang_thai,
	               url_anh,
	               created_at,
	               updated_at
	        FROM san_pham
	        WHERE ma_san_pham = ?
	        """;

	private static final String SELECT_ALL_MYSQL = """
	        SELECT ma_san_pham,
	               ten_san_pham,
	               ma_loai_san_pham,
	               gia_co_ban,
	               so_luong_ton,
	               trang_thai,
	               url_anh,
	               created_at,
	               updated_at
	        FROM san_pham
	        ORDER BY ten_san_pham ASC
	        """;

	private static final String SEARCH_BY_NAME_MYSQL = """
	        SELECT ma_san_pham,
	               ten_san_pham,
	               ma_loai_san_pham,
	               gia_co_ban,
	               so_luong_ton,
	               trang_thai,
	               url_anh,
	               created_at,
	               updated_at
	        FROM san_pham
	        WHERE ten_san_pham LIKE ?
	        ORDER BY ten_san_pham ASC
	        """;

	private static final String EXISTS_BY_NAME_MYSQL = """
	        SELECT 1
	        FROM san_pham
	        WHERE ten_san_pham = ?
	        LIMIT 1
	        """;

	private static final String EXISTS_BY_NAME_EXCEPT_ID_MYSQL = """
	        SELECT 1
	        FROM san_pham
	        WHERE ten_san_pham = ?
	          AND ma_san_pham <> ?
	        LIMIT 1
	        """;

	private static final String IS_USED_MYSQL = """
	        SELECT 1
	        FROM hoa_don_san_pham
	        WHERE ma_san_pham = ?
	        LIMIT 1
			""";
	
	/**
	 * Kiểm tra dữ liệu đầu vào của Product
	 * 
	 * @param product - Đối tượng Product để kiểm tra
	 */
	public void validateProduct(Product product) {
		if (product == null) {
			throw new IllegalArgumentException("Product không được null!");
		}
		
		if (product.getProductName() == null || product.getProductName().trim().isEmpty()) {
			throw new IllegalArgumentException("Tên sản phẩm không được trống!");
		}
		
		if (product.getProductType() == null) {
			throw new IllegalArgumentException("Loại sản phẩm không được rỗng");
		}
		
		if (product.getProductType().getProductTypeId() <= 0) {
			throw new IllegalArgumentException("Mã loại sản phẩm phải lớn hơn 0");
		}
		
		if (product.getPrice() <= 0) {
			throw new IllegalArgumentException("Giá cơ bản phải lớn hơn 0!");
		}
		
		if (product.getStockQuantity() < 0) {
			throw new IllegalArgumentException("Số lượng sản phần tồn phải lớn hơn bằng 0!");
		}
		
		if (product.getProductStatus() == null) {
			throw new IllegalArgumentException("Tình trạng của sản phầm không được phép trống!");
		}
	}
	
	/**
	 * Kiểm tra xem sản phẩm đã tồn tại theo tên hay chưa
	 * 
	 * @param productName - Tên sản phẩm
	 * @return true nếu đã tồn tại
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean existsByName (String productName) throws SQLException{
		if (productName == null || productName.trim().isEmpty()) {
			return false;
		}
		
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(EXISTS_BY_NAME_MYSQL)){
			
			ps.setString(1, productName.trim());
			
			try (ResultSet rs = ps.executeQuery()){
				return rs.next();
			}
		}
	}
	
	/**
	 * Kiểm tra xem sản phẩm đã tồn tại ở bản ghi khác hay chưa
	 * 
	 * @param productName - Tên sản phầm
	 * @param productId - Mã sản phẩm
	 * @return true nếu đã tồn tại
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean existsByNameExceptId(String productName, int productId) throws SQLException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(EXISTS_BY_NAME_EXCEPT_ID_MYSQL)){
			
			ps.setString(1, productName.trim());
			
			ps.setInt(2, productId);
			
			try (ResultSet rs = ps.executeQuery()){
				return rs.next();
			}
		}
	}
	
	/**
	 * Kiểm tra sản phẩm có đang được sử dụng ở bảng ProductInvoice (hoa_don_san_pham) hay không 
	 * 
	 * @param productId - Mã sản phẩm
	 * @return true nếu đang được sử dụng
	 * @throws SQLException nếu có lõi SQL
	 */
	private boolean isUsedInProductInvoice (int productId) throws SQLException{
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(IS_USED_MYSQL)){
			
			ps.setInt(1, productId);
			
			try (ResultSet rs = ps.executeQuery()){
				return rs.next();
			}
		}
	}
	
	/**
	 * Kiểm tra sản phẩm có đang được sử dụng ở các bảng liên quan hay không
	 * 
	 * @param productId - Mã sản phẩm
	 * @return true nếu đang đang được dụng
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean isProductUsed (int productId) throws SQLException{
		return isUsedInProductInvoice(productId);
	}
	
	/**
	 * Ánh xạ một dòng dữ liệu ResultSet thành đối tượng Product
	 * 
	 * @param rs - ResultSet đang trỏ tới dòng dữ liêu hợp lệ
	 * @return đối tượng Product
	 * @throws SQLException nếu có lỗi SQL
	 */
	private Product mapResultSetToProduct (ResultSet rs) throws SQLException{
		Timestamp createdAt = rs.getTimestamp("created_at");
		Timestamp updatedAt = rs.getTimestamp("updated_at");
		ProductType productType = new ProductType(rs.getInt("ma_loai_san_pham"));
		
		Product aProduct = new Product(
				rs.getInt("ma_san_pham"),
				rs.getString("ten_san_pham"),
				productType,
				rs.getDouble("gia_co_ban"),
				rs.getInt("so_luong_ton"),
				ProductStatus.fromId(rs.getInt("trang_thai")),
				rs.getString("url_anh"),
				createdAt != null ? createdAt.toLocalDateTime() : null,
		        updatedAt != null ? updatedAt.toLocalDateTime() : null
				);
		return aProduct;
	}
	
	/**
	 * Thêm sản phẩm mới
	 * 
	 * @param product - Đối tượng sản phẩm
	 * @return true nếu thêm thành công
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean addProduct(Product product) throws SQLException{
		validateProduct(product);
		
		if (existsByName(product.getProductName())) {
			throw new IllegalArgumentException("Sản phẩm đã tồn tại!");
		}
		
		try(Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(INSERT_MYSQL)){
			
			ps.setString(1, product.getProductName());
			ps.setInt(2, product.getProductType().getProductTypeId());
			ps.setDouble(3, product.getPrice());
			ps.setInt(4, product.getStockQuantity());
			ps.setInt(5, product.getProductStatus().getProductStatusId());
			ps.setString(6, product.getPictureUrl());
			
			return ps.executeUpdate() > 0;
		}
	}
	
	/**
	 * Cập nhật thông tin phim
	 * 
	 * @param product - Đối tượng Product
	 * @return true nếu thêm thành công
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean updateProduct (Product product) throws SQLException{
		validateProduct(product);
		
		if (product.getProductId() <= 0) {
			throw new IllegalArgumentException("productId phải lớn hơn 0!");
		}
		
		if (existsByNameExceptId(product.getProductName(), product.getProductId())) {
			throw new IllegalArgumentException("Sản phẩm đã tổn tại ở bảng ghi khác!");
		}
		
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(UPDATE_MYSQL)){
			
			ps.setString(1, product.getProductName());
			ps.setInt(2, product.getProductType().getProductTypeId());
			ps.setDouble(3, product.getPrice());
			ps.setInt(4, product.getStockQuantity());
			ps.setInt(5, product.getProductStatus().getProductStatusId());
			ps.setString(6, product.getPictureUrl());
			ps.setInt(7, product.getProductId());
			
			return ps.executeUpdate() > 0;
		}
	}
	
	/**
	 * Xóa sản phẩm theo mã.
	 * Không cho xóa nếu sản phẩm được dùng trong hóa đơn sản phẩm hoặc bảng liên quan
	 * 
	 * @param productId - Mã sản phẩm
	 * @return true nếu xóa thành công
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean deleteProductById (int productId) throws SQLException{
		if (productId <= 0) {
			throw new IllegalArgumentException("productId phải lớn hơn 0!");
		}
		
		if (isProductUsed(productId)) {
			throw new IllegalArgumentException("Sản phẩm đang được sử dụng, không thể xóa!");
		}
		
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(DELETE_MYSQL)){
			ps.setInt(1, productId);
			
			return ps.executeUpdate() > 0;
		}
	}
	
	/**
	 * Tìm sản phẩm theo mã
	 * 
	 * @param productId - Mã sản phẩm
	 * @return đối tượng Product nếu tìm thấy, ngược lại trả về null
	 * @throws SQLException nếu có lỗi SQl
	 */
	public Product findById (int productId) throws SQLException{
		if (productId <= 0) {
			throw new IllegalArgumentException("productId phải lớn hơn 0!");
		}
		
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID_MYSQL)){
			
			ps.setInt(1, productId);
			
			try (ResultSet rs = ps.executeQuery()){
				if (rs.next()) {
					return mapResultSetToProduct(rs);
				}
			}
		}
		return null;
	}
	
	/**
	 * Tìm sản phẩm theo tên gần đúng
	 * 
	 * @param keyword - Từ khóa sản phẩm
	 * @return danh sách sản phẩm phù hợp
	 * @throws SQLException nếu có lỗi SQL
	 */
	public List<Product> searchByName(String keyword) throws SQLException{
		List<Product> products = new ArrayList<Product>();
		
		if (keyword == null) {
			keyword = "";
		}
		
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SEARCH_BY_NAME_MYSQL)){
			
			ps.setString(1, "%" + keyword.trim() + "%");
			
			try (ResultSet rs = ps.executeQuery()){
				while (rs.next()) {
					products.add(mapResultSetToProduct(rs));
				}
			}
		}
		
		return products;
	}
	
	/**
	 * Lấy tất cả sản phẩm
	 * 
	 * @return danh sách sản phẩm
	 * @throws SQLException nếu có lỗi SQL
	 */
	public List<Product> getAllProducts() throws SQLException{
		List<Product> products = new ArrayList<Product>();
		
		try(Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_ALL_MYSQL);
				ResultSet rs = ps.executeQuery()){
			
			while (rs.next()) {
				products.add(mapResultSetToProduct(rs));
			}
		}
		
		return products;
	}
}
