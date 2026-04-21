package com.cinema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cinema.config.DBConnection;
import com.cinema.entity.Invoice;
import com.cinema.entity.Ticket;
import com.cinema.entity.TicketInvoice;
import com.cinema.enums.InvoiceStatus;

/**
 * DAO cho thực thể TicketInvoice
 * Chịu trách nhiệm thao tác dữ liệu với bảng hóa đơn vé trong MySQL
 * 
 * Bảng ánh xạ:
 * hoa_don_ve(
 * 	ma_hoa_don,
 * 	ma_ve,
 * 	don_gia
 * )
 * 
 * Ghi chú nghiệp vụ:
 * - Một hóa đơn có thể chứa nhiều vé.
 * - Một vé trong thực tế chỉ nên thuộc về một hóa đơn bán vé.
 * - Chỉ cho phép thêm / sửa / xóa chi tiết vé khi hóa đơn còn ở trạng thái PENDING.
 * - Khi hóa đơn đã SUCCESS, FAILED hoặc CANCELLED thì nên khóa chi tiết để tránh lệch dữ liệu.
 * 
 * @author Minh Huy (đề xuất)
 */
public class TicketInvoiceDao {

	private static final String INSERT_MYSQL = """
			INSERT INTO hoa_don_ve (
				ma_hoa_don,
				ma_ve,
				don_gia
			)
			VALUES (?, ?, ?)
			""";

	private static final String UPDATE_PRICE_MYSQL = """
			UPDATE hoa_don_ve
			SET don_gia = ?
			WHERE ma_hoa_don = ?
			  AND ma_ve = ?
			""";

	private static final String DELETE_MYSQL = """
			DELETE FROM hoa_don_ve
			WHERE ma_hoa_don = ?
			  AND ma_ve = ?
			""";

	private static final String SELECT_BY_IDS_MYSQL = """
			SELECT ma_hoa_don,
			       ma_ve,
			       don_gia
			FROM hoa_don_ve
			WHERE ma_hoa_don = ?
			  AND ma_ve = ?
			""";

	private static final String SELECT_ALL_MYSQL = """
			SELECT ma_hoa_don,
			       ma_ve,
			       don_gia
			FROM hoa_don_ve
			ORDER BY ma_hoa_don DESC, ma_ve ASC
			""";

	private static final String SEARCH_BY_INVOICE_ID_MYSQL = """
			SELECT ma_hoa_don,
			       ma_ve,
			       don_gia
			FROM hoa_don_ve
			WHERE ma_hoa_don = ?
			ORDER BY ma_ve ASC
			""";

	private static final String SEARCH_BY_TICKET_ID_MYSQL = """
			SELECT ma_hoa_don,
			       ma_ve,
			       don_gia
			FROM hoa_don_ve
			WHERE ma_ve = ?
			ORDER BY ma_hoa_don DESC
			""";

	private static final String EXISTS_BY_IDS_MYSQL = """
			SELECT 1
			FROM hoa_don_ve
			WHERE ma_hoa_don = ?
			  AND ma_ve = ?
			LIMIT 1
			""";

	private static final String EXISTS_BY_TICKET_ID_MYSQL = """
			SELECT 1
			FROM hoa_don_ve
			WHERE ma_ve = ?
			LIMIT 1
			""";

	private static final String EXISTS_INVOICE_BY_ID_MYSQL = """
			SELECT 1
			FROM hoa_don
			WHERE ma_hoa_don = ?
			LIMIT 1
			""";

	private static final String EXISTS_TICKET_BY_ID_MYSQL = """
			SELECT 1
			FROM ve
			WHERE ma_ve = ?
			LIMIT 1
			""";

	private static final String SELECT_INVOICE_STATUS_BY_ID_MYSQL = """
			SELECT trang_thai
			FROM hoa_don
			WHERE ma_hoa_don = ?
			LIMIT 1
			""";

	/**
	 * Kiểm tra dữ liệu đầu vào của TicketInvoice
	 * 
	 * @param ticketInvoice - Đối tượng TicketInvoice để kiểm tra
	 */
	private void validateTicketInvoice(TicketInvoice ticketInvoice) { // TODO: làm validate internal và package

	}

	/**
	 * Kiểm tra cặp invoiceId - ticketId đã tồn tại hay chưa
	 * 
	 * @param invoiceId - Mã hóa đơn
	 * @param ticketId - Mã vé
	 * @return true nếu đã tồn tại
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean existsByIds(int invoiceId, int ticketId) throws SQLException {
		if (invoiceId <= 0 || ticketId <= 0) {
			throw new IllegalArgumentException("invoiceId và ticketId phải lớn hơn 0!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(EXISTS_BY_IDS_MYSQL)) {

			ps.setInt(1, invoiceId);
			ps.setInt(2, ticketId);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	/**
	 * Kiểm tra vé đã thuộc hóa đơn nào khác chưa.
	 * Theo nghiệp vụ bán vé, một vé không nên xuất hiện ở nhiều hóa đơn khác nhau.
	 * 
	 * @param ticketId - Mã vé
	 * @return true nếu vé đã được gán cho hóa đơn
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean existsByTicketId(int ticketId) throws SQLException {
		if (ticketId <= 0) {
			throw new IllegalArgumentException("ticketId phải lớn hơn 0!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(EXISTS_BY_TICKET_ID_MYSQL)) {

			ps.setInt(1, ticketId);

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
	 * Kiểm tra vé có tồn tại hay không
	 * 
	 * @param ticketId - Mã vé
	 * @return true nếu tồn tại
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean existsTicketById(int ticketId) throws SQLException {
		if (ticketId <= 0) {
			throw new IllegalArgumentException("ticketId phải lớn hơn 0!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(EXISTS_TICKET_BY_ID_MYSQL)) {

			ps.setInt(1, ticketId);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	/**
	 * Lấy trạng thái hóa đơn theo mã hóa đơn
	 * 
	 * @param invoiceId - Mã hóa đơn
	 * @return InvoiceStatus nếu tìm thấy, null nếu không tìm thấy
	 * @throws SQLException nếu có lỗi SQL
	 */
	private InvoiceStatus findInvoiceStatusById(int invoiceId) throws SQLException {
		if (invoiceId <= 0) {
			throw new IllegalArgumentException("invoiceId phải lớn hơn 0!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_INVOICE_STATUS_BY_ID_MYSQL)) {

			ps.setInt(1, invoiceId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return InvoiceStatus.fromId(rs.getInt("trang_thai"));
				}
			}
		}

		return null;
	}

	/**
	 * Kiểm tra hóa đơn có còn cho phép chỉnh sửa chi tiết vé hay không.
	 * 
	 * Quy ước nghiệp vụ đề xuất:
	 * - PENDING: được phép thêm / sửa / xóa chi tiết
	 * - SUCCESS, FAILED, CANCELLED: không được phép chỉnh sửa
	 * 
	 * @param invoiceId - Mã hóa đơn
	 * @return true nếu hóa đơn còn được phép chỉnh sửa
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean isInvoiceEditable(int invoiceId) throws SQLException {
		InvoiceStatus invoiceStatus = findInvoiceStatusById(invoiceId);

		if (invoiceStatus == null) {
			return false;
		}

		return invoiceStatus == InvoiceStatus.PENDING;
	}

	/**
	 * Ánh xạ một dòng dữ liệu ResultSet thành đối tượng TicketInvoice
	 * 
	 * @param rs - ResultSet đang trỏ tới dòng dữ liệu hợp lệ
	 * @return đối tượng TicketInvoice
	 * @throws SQLException nếu có lỗi SQL
	 */
	private TicketInvoice mapResultSetToTicketInvoice(ResultSet rs) throws SQLException {
		Invoice invoice = new Invoice(rs.getInt("ma_hoa_don"));
		Ticket ticket = new Ticket(rs.getInt("ma_ve"));

		TicketInvoice aTicketInvoice = new TicketInvoice(
				invoice,
				ticket,
				rs.getDouble("don_gia"));

		return aTicketInvoice;
	}

	/**
	 * Thêm chi tiết hóa đơn vé mới
	 * 
	 * @param ticketInvoice - Đối tượng TicketInvoice
	 * @return true nếu thêm thành công
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean addTicketInvoice(TicketInvoice ticketInvoice) throws SQLException {
		validateTicketInvoice(ticketInvoice);

		if (ticketInvoice == null) {
			throw new IllegalArgumentException("ticketInvoice không được null!");
		}

		if (ticketInvoice.getInvoice() == null || ticketInvoice.getInvoice().getInvoiceId() <= 0) {
			throw new IllegalArgumentException("invoiceId phải lớn hơn 0!");
		}

		if (ticketInvoice.getTicket() == null || ticketInvoice.getTicket().getTicketId() <= 0) {
			throw new IllegalArgumentException("ticketId phải lớn hơn 0!");
		}

		if (ticketInvoice.getPrice() <= 0) {
			throw new IllegalArgumentException("price phải lớn hơn 0!");
		}

		int invoiceId = ticketInvoice.getInvoice().getInvoiceId();
		int ticketId = ticketInvoice.getTicket().getTicketId();

		if (!existsInvoiceById(invoiceId)) {
			throw new IllegalArgumentException("Hóa đơn không tồn tại!");
		}

		if (!existsTicketById(ticketId)) {
			throw new IllegalArgumentException("Vé không tồn tại!");
		}

		if (!isInvoiceEditable(invoiceId)) {
			throw new IllegalArgumentException("Hóa đơn không ở trạng thái cho phép thêm chi tiết vé!");
		}

		if (existsByIds(invoiceId, ticketId)) {
			throw new IllegalArgumentException("Chi tiết hóa đơn vé đã tồn tại!");
		}

		if (existsByTicketId(ticketId)) {
			throw new IllegalArgumentException("Vé này đã thuộc một hóa đơn khác!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(INSERT_MYSQL)) {

			ps.setInt(1, invoiceId);
			ps.setInt(2, ticketId);
			ps.setDouble(3, ticketInvoice.getPrice());

			return ps.executeUpdate() > 0;
		}
	}

	/**
	 * Cập nhật đơn giá của chi tiết hóa đơn vé.
	 * 
	 * Lưu ý:
	 * - Do bảng này dùng khóa chính kép (ma_hoa_don, ma_ve),
	 *   nên chỉ nên update don_gia, không nên đổi invoice hoặc ticket.
	 * - Nếu muốn đổi vé hoặc đổi hóa đơn, nên xóa bản ghi cũ rồi thêm bản ghi mới.
	 * 
	 * @param ticketInvoice - Đối tượng TicketInvoice cần cập nhật
	 * @return true nếu cập nhật thành công
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean updateTicketInvoice(TicketInvoice ticketInvoice) throws SQLException {
		validateTicketInvoice(ticketInvoice);

		if (ticketInvoice == null) {
			throw new IllegalArgumentException("ticketInvoice không được null!");
		}

		if (ticketInvoice.getInvoice() == null || ticketInvoice.getInvoice().getInvoiceId() <= 0) {
			throw new IllegalArgumentException("invoiceId phải lớn hơn 0!");
		}

		if (ticketInvoice.getTicket() == null || ticketInvoice.getTicket().getTicketId() <= 0) {
			throw new IllegalArgumentException("ticketId phải lớn hơn 0!");
		}

		if (ticketInvoice.getPrice() <= 0) {
			throw new IllegalArgumentException("price phải lớn hơn 0!");
		}

		int invoiceId = ticketInvoice.getInvoice().getInvoiceId();
		int ticketId = ticketInvoice.getTicket().getTicketId();

		if (!existsByIds(invoiceId, ticketId)) {
			throw new IllegalArgumentException("Chi tiết hóa đơn vé không tồn tại!");
		}

		if (!isInvoiceEditable(invoiceId)) {
			throw new IllegalArgumentException("Hóa đơn không ở trạng thái cho phép cập nhật chi tiết vé!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(UPDATE_PRICE_MYSQL)) {

			ps.setDouble(1, ticketInvoice.getPrice());
			ps.setInt(2, invoiceId);
			ps.setInt(3, ticketId);

			return ps.executeUpdate() > 0;
		}
	}

	/**
	 * Xóa chi tiết hóa đơn vé theo khóa kép
	 * 
	 * @param invoiceId - Mã hóa đơn
	 * @param ticketId - Mã vé
	 * @return true nếu xóa thành công
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean deleteTicketInvoice(int invoiceId, int ticketId) throws SQLException {
		if (invoiceId <= 0 || ticketId <= 0) {
			throw new IllegalArgumentException("invoiceId và ticketId phải lớn hơn 0!");
		}

		if (!existsByIds(invoiceId, ticketId)) {
			return false;
		}

		if (!isInvoiceEditable(invoiceId)) {
			throw new IllegalArgumentException("Hóa đơn không ở trạng thái cho phép xóa chi tiết vé!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(DELETE_MYSQL)) {

			ps.setInt(1, invoiceId);
			ps.setInt(2, ticketId);

			return ps.executeUpdate() > 0;
		}
	}

	/**
	 * Tìm chi tiết hóa đơn vé theo khóa kép
	 * 
	 * @param invoiceId - Mã hóa đơn
	 * @param ticketId - Mã vé
	 * @return đối tượng TicketInvoice nếu tìm thấy, ngược lại trả về null
	 * @throws SQLException nếu có lỗi SQL
	 */
	public TicketInvoice findById(int invoiceId, int ticketId) throws SQLException {
		if (invoiceId <= 0 || ticketId <= 0) {
			throw new IllegalArgumentException("invoiceId và ticketId phải lớn hơn 0!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_BY_IDS_MYSQL)) {

			ps.setInt(1, invoiceId);
			ps.setInt(2, ticketId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mapResultSetToTicketInvoice(rs);
				}
			}
		}

		return null;
	}

	/**
	 * Lấy tất cả chi tiết hóa đơn vé
	 * 
	 * @return danh sách chi tiết hóa đơn vé
	 * @throws SQLException nếu có lỗi SQL
	 */
	public List<TicketInvoice> getAllTicketInvoices() throws SQLException {
		List<TicketInvoice> ticketInvoices = new ArrayList<TicketInvoice>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_ALL_MYSQL);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				ticketInvoices.add(mapResultSetToTicketInvoice(rs));
			}
		}

		return ticketInvoices;
	}

	/**
	 * Tìm chi tiết hóa đơn vé theo mã hóa đơn
	 * 
	 * @param invoiceId - Mã hóa đơn
	 * @return danh sách chi tiết hóa đơn vé phù hợp
	 * @throws SQLException nếu có lỗi SQL
	 */
	public List<TicketInvoice> searchByInvoiceId(int invoiceId) throws SQLException {
		if (invoiceId <= 0) {
			throw new IllegalArgumentException("invoiceId phải lớn hơn 0!");
		}

		List<TicketInvoice> ticketInvoices = new ArrayList<TicketInvoice>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SEARCH_BY_INVOICE_ID_MYSQL)) {

			ps.setInt(1, invoiceId);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ticketInvoices.add(mapResultSetToTicketInvoice(rs));
				}
			}
		}

		return ticketInvoices;
	}

	/**
	 * Tìm chi tiết hóa đơn vé theo mã vé
	 * 
	 * @param ticketId - Mã vé
	 * @return danh sách chi tiết hóa đơn vé phù hợp
	 * @throws SQLException nếu có lỗi SQL
	 */
	public List<TicketInvoice> searchByTicketId(int ticketId) throws SQLException {
		if (ticketId <= 0) {
			throw new IllegalArgumentException("ticketId phải lớn hơn 0!");
		}

		List<TicketInvoice> ticketInvoices = new ArrayList<TicketInvoice>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SEARCH_BY_TICKET_ID_MYSQL)) {

			ps.setInt(1, ticketId);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ticketInvoices.add(mapResultSetToTicketInvoice(rs));
				}
			}
		}

		return ticketInvoices;
	}
}
