package com.cinema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cinema.config.DBConnection;
import com.cinema.entity.Invoice;
import com.cinema.entity.Product;
import com.cinema.entity.ProductInvoice;
import com.cinema.enums.InvoiceStatus;
import com.cinema.enums.ProductStatus;

/**
 * DAO cho thực thể ProductInvoice
 * Chịu trách nhiệm thao tác dữ liệu với bảng hóa đơn sản phẩm trong MySQL
 * 
 * Bảng ánh xạ:
 * hoa_don_san_pham(
 * 	ma_hoa_don,
 * 	ma_san_pham,
 * 	so_luong_tong,
 * 	don_gia,
 * 	thanh_tien
 * )
 * 
 * Lưu ý:
 * - Bảng này dùng khóa chính kép: (ma_hoa_don, ma_san_pham)
 * - totalPrice không lưu trong entity, được suy ra từ quantity * unitPrice
 * - DAO này chỉ thao tác chi tiết hóa đơn sản phẩm
 * - Việc trừ tồn kho / cộng lại tồn kho nếu làm chặt nghiệp vụ nên đặt ở Service và transaction
 * 
 * @author Minh Huy (chính)
 */
public class ProductInvoiceDao {

	private static final String INSERT_MYSQL = """
			INSERT INTO hoa_don_san_pham (
				ma_hoa_don,
				ma_san_pham,
				so_luong_tong,
				don_gia,
				thanh_tien
			)
			VALUES (?, ?, ?, ?, ?)
			""";

	private static final String UPDATE_MYSQL = """
			UPDATE hoa_don_san_pham
			SET so_luong_tong = ?,
				don_gia = ?,
				thanh_tien = ?
			WHERE ma_hoa_don = ?
			  AND ma_san_pham = ?
			""";

	private static final String DELETE_MYSQL = """
			DELETE FROM hoa_don_san_pham
			WHERE ma_hoa_don = ?
			  AND ma_san_pham = ?
			""";

	private static final String SELECT_BY_ID_MYSQL = """
			SELECT ma_hoa_don,
			       ma_san_pham,
			       so_luong_tong,
			       don_gia,
			       thanh_tien
			FROM hoa_don_san_pham
			WHERE ma_hoa_don = ?
			  AND ma_san_pham = ?
			""";

	private static final String SELECT_ALL_MYSQL = """
			SELECT ma_hoa_don,
			       ma_san_pham,
			       so_luong_tong,
			       don_gia,
			       thanh_tien
			FROM hoa_don_san_pham
			ORDER BY ma_hoa_don DESC, ma_san_pham ASC
			""";

	private static final String SEARCH_BY_INVOICE_ID_MYSQL = """
			SELECT ma_hoa_don,
			       ma_san_pham,
			       so_luong_tong,
			       don_gia,
			       thanh_tien
			FROM hoa_don_san_pham
			WHERE ma_hoa_don = ?
			ORDER BY ma_san_pham ASC
			""";

	private static final String SEARCH_BY_PRODUCT_ID_MYSQL = """
			SELECT ma_hoa_don,
			       ma_san_pham,
			       so_luong_tong,
			       don_gia,
			       thanh_tien
			FROM hoa_don_san_pham
			WHERE ma_san_pham = ?
			ORDER BY ma_hoa_don DESC
			""";

	private static final String EXISTS_BY_ID_MYSQL = """
			SELECT 1
			FROM hoa_don_san_pham
			WHERE ma_hoa_don = ?
			  AND ma_san_pham = ?
			LIMIT 1
			""";

	private static final String EXISTS_INVOICE_BY_ID_MYSQL = """
			SELECT 1
			FROM hoa_don
			WHERE ma_hoa_don = ?
			LIMIT 1
			""";

	private static final String EXISTS_PRODUCT_BY_ID_MYSQL = """
			SELECT 1
			FROM san_pham
			WHERE ma_san_pham = ?
			LIMIT 1
			""";

	private static final String SELECT_INVOICE_STATUS_BY_ID_MYSQL = """
			SELECT trang_thai
			FROM hoa_don
			WHERE ma_hoa_don = ?
			""";

	private static final String SELECT_PRODUCT_STATUS_AND_STOCK_MYSQL = """
			SELECT trang_thai, so_luong_ton
			FROM san_pham
			WHERE ma_san_pham = ?
			""";

	/**
	 * Kiểm tra dữ liệu đầu vào của ProductInvoice
	 * 
	 * @param productInvoice - Đối tượng ProductInvoice để kiểm tra
	 */
	private void validateProductInvoice(ProductInvoice productInvoice) { // TODO: làm validate internal và package

	}

	/**
	 * Kiểm tra nghiệp vụ cơ bản của ProductInvoice
	 * 
	 * @param productInvoice - Chi tiết hóa đơn sản phẩm cần kiểm tra
	 */
	private void validateProductInvoiceBusinessRule(ProductInvoice productInvoice) {
		if (productInvoice == null) {
			throw new IllegalArgumentException("productInvoice không được null!");
		}

		if (productInvoice.getInvoice() == null) {
			throw new IllegalArgumentException("invoice không được null!");
		}

		if (productInvoice.getProduct() == null) {
			throw new IllegalArgumentException("product không được null!");
		}

		if (productInvoice.getInvoice().getInvoiceId() <= 0) {
			throw new IllegalArgumentException("invoiceId phải lớn hơn 0!");
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
	}

	/**
	 * Kiểm tra chi tiết hóa đơn sản phẩm đã tồn tại hay chưa
	 * 
	 * @param invoiceId - Mã hóa đơn
	 * @param productId - Mã sản phẩm
	 * @return true nếu đã tồn tại
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean existsById(int invoiceId, int productId) throws SQLException {
		if (invoiceId <= 0 || productId <= 0) {
			throw new IllegalArgumentException("invoiceId và productId phải lớn hơn 0!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(EXISTS_BY_ID_MYSQL)) {

			ps.setInt(1, invoiceId);
			ps.setInt(2, productId);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	/**
	 * Kiểm tra hóa đơn có tồn tại hay không
	 * 
	 * @param invoiceId - Mã hóa đơn
	 * @return true nếu tồn tại
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean existsInvoiceById(int invoiceId) throws SQLException {
		if (invoiceId <= 0) {
			throw new IllegalArgumentException("invoiceId phải lớn hơn 0!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(EXISTS_INVOICE_BY_ID_MYSQL)) {

			ps.setInt(1, invoiceId);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	/**
	 * Kiểm tra sản phẩm có tồn tại hay không
	 * 
	 * @param productId - Mã sản phẩm
	 * @return true nếu tồn tại
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean existsProductById(int productId) throws SQLException {
		if (productId <= 0) {
			throw new IllegalArgumentException("productId phải lớn hơn 0!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(EXISTS_PRODUCT_BY_ID_MYSQL)) {

			ps.setInt(1, productId);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	/**
	 * Kiểm tra hóa đơn có còn cho phép chỉnh sửa chi tiết hay không
	 * 
	 * Nghiệp vụ đề xuất:
	 * - Chỉ cho phép thêm / sửa / xóa chi tiết khi hóa đơn đang PENDING
	 * - SUCCESS / FAILED / CANCELLED thì khóa chi tiết
	 * 
	 * @param invoiceId - Mã hóa đơn
	 * @return true nếu còn cho chỉnh sửa
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean isInvoiceEditable(int invoiceId) throws SQLException {
		if (invoiceId <= 0) {
			throw new IllegalArgumentException("invoiceId phải lớn hơn 0!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_INVOICE_STATUS_BY_ID_MYSQL)) {

			ps.setInt(1, invoiceId);

			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next()) {
					return false;
				}

				InvoiceStatus invoiceStatus = InvoiceStatus.fromId(rs.getInt("trang_thai"));
				return invoiceStatus == InvoiceStatus.PENDING;
			}
		}
	}

	/**
	 * Kiểm tra sản phẩm có còn bán và đủ tồn kho cho số lượng yêu cầu hay không
	 * 
	 * @param productId - Mã sản phẩm
	 * @param quantity - Số lượng cần bán
	 * @return true nếu hợp lệ
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean isProductAvailableForSale(int productId, int quantity) throws SQLException {
		if (productId <= 0) {
			throw new IllegalArgumentException("productId phải lớn hơn 0!");
		}

		if (quantity <= 0) {
			throw new IllegalArgumentException("quantity phải lớn hơn 0!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_PRODUCT_STATUS_AND_STOCK_MYSQL)) {

			ps.setInt(1, productId);

			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next()) {
					return false;
				}

				ProductStatus productStatus = ProductStatus.fromId(rs.getInt("trang_thai"));
				int stockQuantity = rs.getInt("so_luong_ton");

				return productStatus == ProductStatus.SELLING && stockQuantity >= quantity;
			}
		}
	}

	/**
	 * Ánh xạ một dòng dữ liệu ResultSet thành đối tượng ProductInvoice
	 * 
	 * @param rs - ResultSet đang trỏ tới dòng dữ liệu hợp lệ
	 * @return đối tượng ProductInvoice
	 * @throws SQLException nếu có lỗi SQL
	 */
	private ProductInvoice mapResultSetToProductInvoice(ResultSet rs) throws SQLException {
		Invoice invoice = new Invoice(rs.getInt("ma_hoa_don"));
		Product product = new Product(rs.getInt("ma_san_pham"));

		return new ProductInvoice(
				invoice,
				product,
				rs.getInt("so_luong_tong"),
				rs.getDouble("don_gia"));
	}

	/**
	 * Thêm chi tiết hóa đơn sản phẩm mới
	 * 
	 * Nghiệp vụ đề xuất:
	 * - Hóa đơn phải tồn tại
	 * - Sản phẩm phải tồn tại
	 * - Không cho thêm trùng (invoiceId, productId)
	 * - Chỉ cho thêm khi hóa đơn còn PENDING
	 * - Sản phẩm phải đang SELLING và đủ tồn kho
	 * 
	 * @param productInvoice - Đối tượng ProductInvoice
	 * @return true nếu thêm thành công
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean addProductInvoice(ProductInvoice productInvoice) throws SQLException {
		validateProductInvoice(productInvoice);
		validateProductInvoiceBusinessRule(productInvoice);

		int invoiceId = productInvoice.getInvoice().getInvoiceId();
		int productId = productInvoice.getProduct().getProductId();

		if (!existsInvoiceById(invoiceId)) {
			throw new IllegalArgumentException("Hóa đơn không tồn tại!");
		}

		if (!existsProductById(productId)) {
			throw new IllegalArgumentException("Sản phẩm không tồn tại!");
		}

		if (existsById(invoiceId, productId)) {
			throw new IllegalArgumentException("Sản phẩm này đã tồn tại trong hóa đơn!");
		}

		if (!isInvoiceEditable(invoiceId)) {
			throw new IllegalArgumentException("Hóa đơn không còn cho phép chỉnh sửa chi tiết sản phẩm!");
		}

		if (!isProductAvailableForSale(productId, productInvoice.getQuantity())) {
			throw new IllegalArgumentException("Sản phẩm không còn bán hoặc không đủ số lượng tồn!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(INSERT_MYSQL)) {

			ps.setInt(1, invoiceId);
			ps.setInt(2, productId);
			ps.setInt(3, productInvoice.getQuantity());
			ps.setDouble(4, productInvoice.getUnitPrice());
			ps.setDouble(5, productInvoice.getTotalPrice());

			return ps.executeUpdate() > 0;
		}
	}

	/**
	 * Cập nhật chi tiết hóa đơn sản phẩm
	 * 
	 * Lưu ý:
	 * - Vì entity hiện tại không có "old key", method này chỉ cập nhật
	 *   quantity và unitPrice theo đúng cặp khóa hiện có (invoiceId, productId)
	 * - Không hỗ trợ đổi invoiceId hoặc productId của bản ghi
	 * 
	 * @param productInvoice - Chi tiết hóa đơn sản phẩm cần cập nhật
	 * @return true nếu cập nhật thành công
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean updateProductInvoice(ProductInvoice productInvoice) throws SQLException {
		validateProductInvoice(productInvoice);
		validateProductInvoiceBusinessRule(productInvoice);

		int invoiceId = productInvoice.getInvoice().getInvoiceId();
		int productId = productInvoice.getProduct().getProductId();

		if (!existsById(invoiceId, productId)) {
			throw new IllegalArgumentException("Chi tiết hóa đơn sản phẩm không tồn tại!");
		}

		if (!isInvoiceEditable(invoiceId)) {
			throw new IllegalArgumentException("Hóa đơn không còn cho phép chỉnh sửa chi tiết sản phẩm!");
		}

		if (!isProductAvailableForSale(productId, productInvoice.getQuantity())) {
			throw new IllegalArgumentException("Sản phẩm không còn bán hoặc không đủ số lượng tồn!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(UPDATE_MYSQL)) {

			ps.setInt(1, productInvoice.getQuantity());
			ps.setDouble(2, productInvoice.getUnitPrice());
			ps.setDouble(3, productInvoice.getTotalPrice());
			ps.setInt(4, invoiceId);
			ps.setInt(5, productId);

			return ps.executeUpdate() > 0;
		}
	}

	/**
	 * Xóa chi tiết hóa đơn sản phẩm theo khóa kép
	 * 
	 * @param invoiceId - Mã hóa đơn
	 * @param productId - Mã sản phẩm
	 * @return true nếu xóa thành công
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean deleteProductInvoiceById(int invoiceId, int productId) throws SQLException {
		if (invoiceId <= 0 || productId <= 0) {
			throw new IllegalArgumentException("invoiceId và productId phải lớn hơn 0!");
		}

		if (!existsById(invoiceId, productId)) {
			return false;
		}

		if (!isInvoiceEditable(invoiceId)) {
			throw new IllegalArgumentException("Hóa đơn không còn cho phép xóa chi tiết sản phẩm!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(DELETE_MYSQL)) {

			ps.setInt(1, invoiceId);
			ps.setInt(2, productId);

			return ps.executeUpdate() > 0;
		}
	}

	/**
	 * Tìm chi tiết hóa đơn sản phẩm theo khóa kép
	 * 
	 * @param invoiceId - Mã hóa đơn
	 * @param productId - Mã sản phẩm
	 * @return đối tượng ProductInvoice nếu tìm thấy, ngược lại trả về null
	 * @throws SQLException nếu có lỗi SQL
	 */
	public ProductInvoice findById(int invoiceId, int productId) throws SQLException {
		if (invoiceId <= 0 || productId <= 0) {
			throw new IllegalArgumentException("invoiceId và productId phải lớn hơn 0!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID_MYSQL)) {

			ps.setInt(1, invoiceId);
			ps.setInt(2, productId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mapResultSetToProductInvoice(rs);
				}
			}
		}

		return null;
	}

	/**
	 * Lấy tất cả chi tiết hóa đơn sản phẩm
	 * 
	 * @return danh sách ProductInvoice
	 * @throws SQLException nếu có lỗi SQL
	 */
	public List<ProductInvoice> getAllProductInvoices() throws SQLException {
		List<ProductInvoice> productInvoices = new ArrayList<ProductInvoice>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_ALL_MYSQL);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				productInvoices.add(mapResultSetToProductInvoice(rs));
			}
		}

		return productInvoices;
	}

	/**
	 * Tìm danh sách chi tiết sản phẩm theo mã hóa đơn
	 * 
	 * @param invoiceId - Mã hóa đơn
	 * @return danh sách chi tiết hóa đơn sản phẩm
	 * @throws SQLException nếu có lỗi SQL
	 */
	public List<ProductInvoice> searchByInvoiceId(int invoiceId) throws SQLException {
		if (invoiceId <= 0) {
			throw new IllegalArgumentException("invoiceId phải lớn hơn 0!");
		}

		List<ProductInvoice> productInvoices = new ArrayList<ProductInvoice>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SEARCH_BY_INVOICE_ID_MYSQL)) {

			ps.setInt(1, invoiceId);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					productInvoices.add(mapResultSetToProductInvoice(rs));
				}
			}
		}

		return productInvoices;
	}

	/**
	 * Tìm danh sách chi tiết hóa đơn theo mã sản phẩm
	 * 
	 * @param productId - Mã sản phẩm
	 * @return danh sách chi tiết hóa đơn sản phẩm
	 * @throws SQLException nếu có lỗi SQL
	 */
	public List<ProductInvoice> searchByProductId(int productId) throws SQLException {
		if (productId <= 0) {
			throw new IllegalArgumentException("productId phải lớn hơn 0!");
		}

		List<ProductInvoice> productInvoices = new ArrayList<ProductInvoice>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SEARCH_BY_PRODUCT_ID_MYSQL)) {

			ps.setInt(1, productId);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					productInvoices.add(mapResultSetToProductInvoice(rs));
				}
			}
		}

		return productInvoices;
	}

	/**
	 * Tính tổng tiền sản phẩm theo mã hóa đơn
	 * 
	 * @param invoiceId - Mã hóa đơn
	 * @return tổng tiền sản phẩm của hóa đơn
	 * @throws SQLException nếu có lỗi SQL
	 */
	public double getTotalAmountByInvoiceId(int invoiceId) throws SQLException {
		if (invoiceId <= 0) {
			throw new IllegalArgumentException("invoiceId phải lớn hơn 0!");
		}

		double totalAmount = 0;

		for (ProductInvoice productInvoice : searchByInvoiceId(invoiceId)) {
			totalAmount += productInvoice.getTotalPrice();
		}

		return totalAmount;
	}
}