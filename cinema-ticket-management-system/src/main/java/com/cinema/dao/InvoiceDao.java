package com.cinema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.cinema.config.DBConnection;
import com.cinema.entity.Employee;
import com.cinema.entity.Invoice;
import com.cinema.enums.InvoiceStatus;
import com.cinema.enums.PaymentMethod;

/**
 * DAO cho thực thể Invoice
 * Chịu trách nhiệm thao tác dữ liệu với bảng hóa đơn trong MySQL
 * 
 * Bảng ánh xạ:
 * hoa_don(
 * 	ma_hoa_don,
 * 	ma_nhan_vien,
 * 	phuong_thuc_thanh_toan,
 * 	trang_thai,
 * 	thoi_gian_thanh_toan,
 * 	created_at,
 * 	updated_at
 * )
 * 
 * Lưu ý:
 * DAO này đang bám theo entity Invoice hiện tại.
 * Không xử lý cột tong_tien vì entity hiện tại không còn field đó.
 * 
 * @author Minh Huy (chính)
 */
public class InvoiceDao {
	private static final String INSERT_MYSQL = """
			INSERT INTO hoa_don (
				ma_nhan_vien,
				phuong_thuc_thanh_toan,
				trang_thai,
				thoi_gian_thanh_toan
			)
			VALUES (?, ?, ?, ?)
			""";

	private static final String UPDATE_MYSQL = """
			UPDATE hoa_don
			SET ma_nhan_vien = ?,
				phuong_thuc_thanh_toan = ?,
				trang_thai = ?,
				thoi_gian_thanh_toan = ?
			WHERE ma_hoa_don = ?
			""";

	private static final String DELETE_MYSQL = """
			DELETE FROM hoa_don
			WHERE ma_hoa_don = ?
			""";

	private static final String SELECT_BY_ID_MYSQL = """
			SELECT ma_hoa_don,
				ma_nhan_vien,
				phuong_thuc_thanh_toan,
				trang_thai,
				thoi_gian_thanh_toan,
				created_at,
				updated_at
			FROM hoa_don
			WHERE ma_hoa_don = ?
			""";

	private static final String SELECT_ALL_MYSQL = """
			SELECT ma_hoa_don,
				ma_nhan_vien,
				phuong_thuc_thanh_toan,
				trang_thai,
				thoi_gian_thanh_toan,
				created_at,
				updated_at
			FROM hoa_don
			ORDER BY ma_hoa_don DESC
			""";

	private static final String SEARCH_BY_EMPLOYEE_ID_MYSQL = """
			SELECT ma_hoa_don,
				ma_nhan_vien,
				phuong_thuc_thanh_toan,
				trang_thai,
				thoi_gian_thanh_toan,
				created_at,
				updated_at
			FROM hoa_don
			WHERE ma_nhan_vien = ?
			ORDER BY ma_hoa_don DESC
			""";

	private static final String SEARCH_BY_STATUS_MYSQL = """
			SELECT ma_hoa_don,
				ma_nhan_vien,
				phuong_thuc_thanh_toan,
				trang_thai,
				thoi_gian_thanh_toan,
				created_at,
				updated_at
			FROM hoa_don
			WHERE trang_thai = ?
			ORDER BY ma_hoa_don DESC
			""";

	private static final String IS_USED_IN_TICKET_INVOICE_MYSQL = """
			SELECT 1
			FROM hoa_don_ve
			WHERE ma_hoa_don = ?
			LIMIT 1
			""";

	private static final String IS_USED_IN_PRODUCT_INVOICE_MYSQL = """
			SELECT 1
			FROM hoa_don_san_pham
			WHERE ma_hoa_don = ?
			LIMIT 1
			""";

	/**
	 * Kiểm tra dữ liệu đầu vào của Invoice
	 * 
	 * @param invoice - Đối tượng Invoice để kiểm tra
	 */
	private void validateInvoice(Invoice invoice) { // TODO: làm validate internal và package

	}

	/**
	 * Kiểm tra nghiệp vụ trạng thái hóa đơn và thời gian thanh toán.
	 * 
	 * Quy ước:
	 * PENDING - paymentTime phải null,
	 * SUCCESS - paymentTime bắt buộc khác null,
	 * FAILED, CANCELLED - paymentTime được phép null,
	 * 
	 * @param invoice - Hóa đơn cần kiểm tra
	 */
	private void validateInvoiceBusinessRule(Invoice invoice) {
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

		if (invoice.getInvoiceStatus() == InvoiceStatus.PENDING && invoice.getPaymentTime() != null) {
			throw new IllegalArgumentException("Hóa đơn PENDING thì paymentTime phải là null!");
		}

		if (invoice.getInvoiceStatus() == InvoiceStatus.SUCCESS && invoice.getPaymentTime() == null) {
			throw new IllegalArgumentException("Hóa đơn SUCCESS thì paymentTime không được null!");
		}
	}

	/**
	 * Kiểm tra hóa đơn có đang được sử dụng trong bảng hoa_don_ve hay không
	 * 
	 * @param invoiceId - Mã hóa đơn
	 * @return true nếu đang được sử dụng
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean isUsedInTicketInvoice(int invoiceId) throws SQLException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(IS_USED_IN_TICKET_INVOICE_MYSQL)) {

			ps.setInt(1, invoiceId);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	/**
	 * Kiểm tra hóa đơn có đang được sử dụng trong bảng hoa_don_san_pham hay không
	 * 
	 * @param invoiceId - Mã hóa đơn
	 * @return true nếu đang được sử dụng
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean isUsedInProductInvoice(int invoiceId) throws SQLException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(IS_USED_IN_PRODUCT_INVOICE_MYSQL)) {

			ps.setInt(1, invoiceId);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	/**
	 * Kiểm tra hóa đơn có đang được sử dụng ở các bảng liên quan hay không
	 * 
	 * @param invoiceId - Mã hóa đơn
	 * @return true nếu đang được sử dụng
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean isInvoiceUsed(int invoiceId) throws SQLException {
		if (invoiceId <= 0) {
			throw new IllegalArgumentException("invoiceId phải lớn hơn 0!");
		}

		return isUsedInTicketInvoice(invoiceId) || isUsedInProductInvoice(invoiceId);
	}

	/**
	 * Ánh xạ một dòng dữ liệu ResultSet thành đối tượng Invoice
	 * 
	 * @param rs - ResultSet đang trỏ tới dòng dữ liệu hợp lệ
	 * @return đối tượng Invoice
	 * @throws SQLException nếu có lỗi SQL
	 */
	private Invoice mapResultSetToInvoice(ResultSet rs) throws SQLException {
		Timestamp paymentTime = rs.getTimestamp("thoi_gian_thanh_toan");
		Timestamp createdAt = rs.getTimestamp("created_at");
		Timestamp updatedAt = rs.getTimestamp("updated_at");

		Employee employee = new Employee(rs.getInt("ma_nhan_vien"));

		Invoice anInvoice = new Invoice(
				rs.getInt("ma_hoa_don"),
				employee,
				PaymentMethod.fromId(rs.getInt("phuong_thuc_thanh_toan")),
				InvoiceStatus.fromId(rs.getInt("trang_thai")),
				paymentTime != null ? paymentTime.toLocalDateTime() : null,
				createdAt != null ? createdAt.toLocalDateTime() : null,
				updatedAt != null ? updatedAt.toLocalDateTime() : null
		);

		return anInvoice;
	}

	/**
	 * Thêm hóa đơn mới
	 * 
	 * @param invoice - Đối tượng hóa đơn
	 * @return true nếu thêm thành công
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean addInvoice(Invoice invoice) throws SQLException {
		validateInvoice(invoice);
		validateInvoiceBusinessRule(invoice);

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(INSERT_MYSQL)) {

			ps.setInt(1, invoice.getEmployee().getEmployeeId());
			ps.setInt(2, invoice.getPaymentMethod().getPaymentMethodId());
			ps.setInt(3, invoice.getInvoiceStatus().getInvoiceStatusId());

			if (invoice.getPaymentTime() != null) {
				ps.setTimestamp(4, Timestamp.valueOf(invoice.getPaymentTime()));
			} else {
				ps.setTimestamp(4, null);
			}

			return ps.executeUpdate() > 0;
		}
	}

	/**
	 * Cập nhật thông tin hóa đơn
	 * 
	 * Nghiệp vụ đề xuất:
	 * - Không cho update khi hóa đơn đã có chi tiết vé hoặc chi tiết sản phẩm
	 *   để tránh lệch dữ liệu bán hàng.
	 * 
	 * @param invoice - Hóa đơn cần cập nhật
	 * @return true nếu cập nhật thành công
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean updateInvoice(Invoice invoice) throws SQLException {
		validateInvoice(invoice);
		validateInvoiceBusinessRule(invoice);

		if (invoice.getInvoiceId() <= 0) {
			throw new IllegalArgumentException("invoiceId phải lớn hơn 0!");
		}

		if (isInvoiceUsed(invoice.getInvoiceId())) {
			throw new IllegalArgumentException("Hóa đơn đã phát sinh chi tiết, không thể cập nhật!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(UPDATE_MYSQL)) {

			ps.setInt(1, invoice.getEmployee().getEmployeeId());
			ps.setInt(2, invoice.getPaymentMethod().getPaymentMethodId());
			ps.setInt(3, invoice.getInvoiceStatus().getInvoiceStatusId());

			if (invoice.getPaymentTime() != null) {
				ps.setTimestamp(4, Timestamp.valueOf(invoice.getPaymentTime()));
			} else {
				ps.setTimestamp(4, null);
			}

			ps.setInt(5, invoice.getInvoiceId());

			return ps.executeUpdate() > 0;
		}
	}

	/**
	 * Xóa hóa đơn theo mã
	 * 
	 * Không cho xóa nếu hóa đơn đang được sử dụng ở bảng chi tiết hóa đơn vé
	 * hoặc bảng chi tiết hóa đơn sản phẩm.
	 * 
	 * @param invoiceId - Mã hóa đơn
	 * @return true nếu xóa thành công
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean deleteInvoiceById(int invoiceId) throws SQLException {
		if (invoiceId <= 0) {
			throw new IllegalArgumentException("invoiceId phải lớn hơn 0!");
		}

		if (isInvoiceUsed(invoiceId)) {
			throw new IllegalArgumentException("Hóa đơn đang được sử dụng, không thể xóa!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(DELETE_MYSQL)) {

			ps.setInt(1, invoiceId);

			return ps.executeUpdate() > 0;
		}
	}

	/**
	 * Tìm hóa đơn theo mã
	 * 
	 * @param invoiceId - Mã hóa đơn
	 * @return đối tượng Invoice nếu tìm thấy, ngược lại trả về null
	 * @throws SQLException nếu có lỗi SQL
	 */
	public Invoice findById(int invoiceId) throws SQLException {
		if (invoiceId <= 0) {
			throw new IllegalArgumentException("invoiceId phải lớn hơn 0!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID_MYSQL)) {

			ps.setInt(1, invoiceId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mapResultSetToInvoice(rs);
				}
			}
		}

		return null;
	}

	/**
	 * Lấy tất cả hóa đơn
	 * 
	 * @return danh sách hóa đơn
	 * @throws SQLException nếu có lỗi SQL
	 */
	public List<Invoice> getAllInvoices() throws SQLException {
		List<Invoice> invoices = new ArrayList<Invoice>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_ALL_MYSQL);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				invoices.add(mapResultSetToInvoice(rs));
			}
		}

		return invoices;
	}

	/**
	 * Tìm hóa đơn theo mã nhân viên
	 * 
	 * @param employeeId - Mã nhân viên
	 * @return danh sách hóa đơn phù hợp
	 * @throws SQLException nếu có lỗi SQL
	 */
	public List<Invoice> searchByEmployeeId(int employeeId) throws SQLException {
		if (employeeId <= 0) {
			throw new IllegalArgumentException("employeeId phải lớn hơn 0!");
		}

		List<Invoice> invoices = new ArrayList<Invoice>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SEARCH_BY_EMPLOYEE_ID_MYSQL)) {

			ps.setInt(1, employeeId);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					invoices.add(mapResultSetToInvoice(rs));
				}
			}
		}

		return invoices;
	}

	/**
	 * Tìm hóa đơn theo trạng thái
	 * 
	 * @param invoiceStatus - Trạng thái hóa đơn
	 * @return danh sách hóa đơn phù hợp
	 * @throws SQLException nếu có lỗi SQL
	 */
	public List<Invoice> searchByStatus(InvoiceStatus invoiceStatus) throws SQLException {
		if (invoiceStatus == null) {
			throw new IllegalArgumentException("invoiceStatus không được null!");
		}

		List<Invoice> invoices = new ArrayList<Invoice>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SEARCH_BY_STATUS_MYSQL)) {

			ps.setInt(1, invoiceStatus.getInvoiceStatusId());

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					invoices.add(mapResultSetToInvoice(rs));
				}
			}
		}

		return invoices;
	}
}