package com.cinema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.cinema.config.DBConnection;
import com.cinema.entity.MovieSession;
import com.cinema.entity.Seat;
import com.cinema.entity.Ticket;
import com.cinema.entity.TicketPricing;
import com.cinema.enums.TicketStatus;

/**
 * DAO cho thực thể Ticket
 * Chịu trách nhiệm thao tác dữ liệu với bảng vé trong MySQL
 * 
 * Bảng ánh xạ:
 * ve(
 * 	ma_ve,
 * 	ma_suat_chieu,
 * 	ma_ghe,
 * 	ma_bang_gia,
 * 	trang_thai,
 * 	created_at,
 * 	updated_at
 * )
 * 
 * @author Minh Huy (chính)
 */
public class TicketDao {

	private static final String INSERT_MYSQL = """
			INSERT INTO ve (
				ma_suat_chieu,
				ma_ghe,
				ma_bang_gia,
				trang_thai
			)
			VALUES (?, ?, ?, ?)
			""";

	private static final String UPDATE_MYSQL = """
			UPDATE ve
			SET ma_suat_chieu = ?,
				ma_ghe = ?,
				ma_bang_gia = ?,
				trang_thai = ?
			WHERE ma_ve = ?
			""";

	private static final String DELETE_MYSQL = """
			DELETE FROM ve
			WHERE ma_ve = ?
			""";

	private static final String SELECT_BY_ID_MYSQL = """
			SELECT ma_ve,
				   ma_suat_chieu,
				   ma_ghe,
				   ma_bang_gia,
				   trang_thai,
				   created_at,
				   updated_at
			FROM ve
			WHERE ma_ve = ?
			""";

	private static final String SELECT_ALL_MYSQL = """
			SELECT ma_ve,
				   ma_suat_chieu,
				   ma_ghe,
				   ma_bang_gia,
				   trang_thai,
				   created_at,
				   updated_at
			FROM ve
			ORDER BY ma_ve ASC
			""";

	private static final String SEARCH_BY_MOVIE_SESSION_ID_MYSQL = """
			SELECT ma_ve,
				   ma_suat_chieu,
				   ma_ghe,
				   ma_bang_gia,
				   trang_thai,
				   created_at,
				   updated_at
			FROM ve
			WHERE ma_suat_chieu = ?
			ORDER BY ma_ve ASC
			""";

	private static final String SEARCH_BY_SEAT_ID_MYSQL = """
			SELECT ma_ve,
				   ma_suat_chieu,
				   ma_ghe,
				   ma_bang_gia,
				   trang_thai,
				   created_at,
				   updated_at
			FROM ve
			WHERE ma_ghe = ?
			ORDER BY ma_ve ASC
			""";

	private static final String SEARCH_BY_STATUS_MYSQL = """
			SELECT ma_ve,
				   ma_suat_chieu,
				   ma_ghe,
				   ma_bang_gia,
				   trang_thai,
				   created_at,
				   updated_at
			FROM ve
			WHERE trang_thai = ?
			ORDER BY ma_ve ASC
			""";

	private static final String EXISTS_BY_MOVIE_SESSION_AND_SEAT_MYSQL = """
			SELECT 1
			FROM ve
			WHERE ma_suat_chieu = ?
			  AND ma_ghe = ?
			LIMIT 1
			""";

	private static final String EXISTS_BY_MOVIE_SESSION_AND_SEAT_EXCEPT_ID_MYSQL = """
			SELECT 1
			FROM ve
			WHERE ma_suat_chieu = ?
			  AND ma_ghe = ?
			  AND ma_ve <> ?
			LIMIT 1
			""";

	private static final String IS_USED_IN_TICKET_INVOICE_MYSQL = """
			SELECT 1
			FROM hoa_don_ve
			WHERE ma_ve = ?
			LIMIT 1
			""";

	private static final String EXISTS_SEAT_IN_MOVIE_SESSION_SCREEN_MYSQL = """
			SELECT 1
			FROM suat_chieu sc
			JOIN ghe g ON g.ma_phong = sc.ma_phong
			WHERE sc.ma_suat_chieu = ?
			  AND g.ma_ghe = ?
			LIMIT 1
			""";

	private static final String EXISTS_MATCHING_TICKET_PRICING_MYSQL = """
			SELECT 1
			FROM suat_chieu sc
			JOIN phong_chieu pc ON pc.ma_phong = sc.ma_phong
			JOIN ghe g ON g.ma_ghe = ?
			JOIN bang_gia_ve bgv 
				ON bgv.ma_bang_gia = ?
			   AND bgv.ma_loai_ghe = g.ma_loai_ghe
			   AND bgv.ma_loai_phong = pc.ma_loai_phong
			WHERE sc.ma_suat_chieu = ?
			LIMIT 1
			""";

	/**
	 * Kiểm tra dữ liệu đầu vào của Ticket
	 * 
	 * @param ticket - Đối tượng Ticket để kiểm tra
	 */
	private void validateTicket(Ticket ticket) { // TODO: làm validate internal và package

	}

	/**
	 * Kiểm tra cặp movieSession - seat đã tồn tại hay chưa
	 * 
	 * @param movieSessionId - Mã suất chiếu
	 * @param seatId - Mã ghế
	 * @return true nếu đã tồn tại
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean existsByMovieSessionIdAndSeatId(int movieSessionId, int seatId) throws SQLException {
		if (movieSessionId <= 0 || seatId <= 0) {
			throw new IllegalArgumentException("movieSessionId và seatId phải lớn hơn 0!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(EXISTS_BY_MOVIE_SESSION_AND_SEAT_MYSQL)) {

			ps.setInt(1, movieSessionId);
			ps.setInt(2, seatId);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	/**
	 * Kiểm tra cặp movieSession - seat đã tồn tại ở bản ghi khác hay chưa
	 * 
	 * @param movieSessionId - Mã suất chiếu
	 * @param seatId - Mã ghế
	 * @param ticketId - Mã vé
	 * @return true nếu đã tồn tại ở bản ghi khác
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean existsByMovieSessionIdAndSeatIdExceptId(int movieSessionId, int seatId, int ticketId)
			throws SQLException {
		if (movieSessionId <= 0 || seatId <= 0 || ticketId <= 0) {
			throw new IllegalArgumentException("movieSessionId, seatId và ticketId phải lớn hơn 0!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(EXISTS_BY_MOVIE_SESSION_AND_SEAT_EXCEPT_ID_MYSQL)) {

			ps.setInt(1, movieSessionId);
			ps.setInt(2, seatId);
			ps.setInt(3, ticketId);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	/**
	 * Kiểm tra ghế có thuộc đúng phòng chiếu của suất chiếu hay không
	 * 
	 * @param movieSessionId - Mã suất chiếu
	 * @param seatId - Mã ghế
	 * @return true nếu ghế thuộc phòng của suất chiếu
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean existsSeatInMovieSessionScreen(int movieSessionId, int seatId) throws SQLException {
		if (movieSessionId <= 0 || seatId <= 0) {
			throw new IllegalArgumentException("movieSessionId và seatId phải lớn hơn 0!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(EXISTS_SEAT_IN_MOVIE_SESSION_SCREEN_MYSQL)) {

			ps.setInt(1, movieSessionId);
			ps.setInt(2, seatId);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	/**
	 * Kiểm tra bảng giá vé có phù hợp với loại ghế và loại phòng của suất chiếu hay không
	 * 
	 * @param movieSessionId - Mã suất chiếu
	 * @param seatId - Mã ghế
	 * @param ticketPricingId - Mã bảng giá vé
	 * @return true nếu phù hợp
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean existsMatchingTicketPricing(int movieSessionId, int seatId, int ticketPricingId) throws SQLException {
		if (movieSessionId <= 0 || seatId <= 0 || ticketPricingId <= 0) {
			throw new IllegalArgumentException("movieSessionId, seatId và ticketPricingId phải lớn hơn 0!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(EXISTS_MATCHING_TICKET_PRICING_MYSQL)) {

			ps.setInt(1, seatId);
			ps.setInt(2, ticketPricingId);
			ps.setInt(3, movieSessionId);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	/**
	 * Kiểm tra vé có đang được sử dụng ở bảng TicketInvoice hay không
	 * 
	 * @param ticketId - Mã vé
	 * @return true nếu đang được sử dụng
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean isUsedInTicketInvoice(int ticketId) throws SQLException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(IS_USED_IN_TICKET_INVOICE_MYSQL)) {

			ps.setInt(1, ticketId);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	/**
	 * Kiểm tra vé có đang được sử dụng ở các bảng liên quan hay không
	 * 
	 * @param ticketId - Mã vé
	 * @return true nếu đang được sử dụng
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean isTicketUsed(int ticketId) throws SQLException {
		if (ticketId <= 0) {
			throw new IllegalArgumentException("ticketId phải lớn hơn 0!");
		}

		return isUsedInTicketInvoice(ticketId);
	}

	/**
	 * Ánh xạ một dòng dữ liệu ResultSet thành đối tượng Ticket
	 * 
	 * @param rs - ResultSet đang trỏ tới dòng dữ liệu hợp lệ
	 * @return đối tượng Ticket
	 * @throws SQLException nếu có lỗi SQL
	 */
	private Ticket mapResultSetToTicket(ResultSet rs) throws SQLException {
		Timestamp createdAt = rs.getTimestamp("created_at");
		Timestamp updatedAt = rs.getTimestamp("updated_at");

		MovieSession movieSession = new MovieSession(rs.getInt("ma_suat_chieu"));
		Seat seat = new Seat(rs.getInt("ma_ghe"));
		TicketPricing ticketPricing = new TicketPricing(rs.getInt("ma_bang_gia"));

		Ticket aTicket = new Ticket(
				rs.getInt("ma_ve"),
				movieSession,
				seat,
				ticketPricing,
				TicketStatus.fromId(rs.getInt("trang_thai")),
				createdAt != null ? createdAt.toLocalDateTime() : null,
				updatedAt != null ? updatedAt.toLocalDateTime() : null);

		return aTicket;
	}

	/**
	 * Thêm vé mới
	 * 
	 * @param ticket - Đối tượng vé
	 * @return true nếu thêm thành công
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean addTicket(Ticket ticket) throws SQLException {
		validateTicket(ticket);

		if (existsByMovieSessionIdAndSeatId(
				ticket.getMovieSession().getMovieSessionId(),
				ticket.getSeat().getSeatId())) {
			throw new IllegalArgumentException("Ghế này đã có vé trong suất chiếu này!");
		}

		if (!existsSeatInMovieSessionScreen(
				ticket.getMovieSession().getMovieSessionId(),
				ticket.getSeat().getSeatId())) {
			throw new IllegalArgumentException("Ghế không thuộc phòng chiếu của suất chiếu này!");
		}

		if (!existsMatchingTicketPricing(
				ticket.getMovieSession().getMovieSessionId(),
				ticket.getSeat().getSeatId(),
				ticket.getTicketPricing().getTicketPricingId())) {
			throw new IllegalArgumentException("Bảng giá vé không phù hợp với loại ghế hoặc loại phòng!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(INSERT_MYSQL)) {

			ps.setInt(1, ticket.getMovieSession().getMovieSessionId());
			ps.setInt(2, ticket.getSeat().getSeatId());
			ps.setInt(3, ticket.getTicketPricing().getTicketPricingId());
			ps.setInt(4, ticket.getTicketStatus().getTicketStatusId());

			return ps.executeUpdate() > 0;
		}
	}

	/**
	 * Cập nhật thông tin vé
	 * 
	 * @param ticket - Đối tượng vé cần cập nhật
	 * @return true nếu cập nhật thành công
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean updateTicket(Ticket ticket) throws SQLException {
		validateTicket(ticket);

		if (ticket.getTicketId() <= 0) {
			throw new IllegalArgumentException("ticketId phải lớn hơn 0!");
		}

		if (isTicketUsed(ticket.getTicketId())) {
			throw new IllegalArgumentException("Vé đã phát sinh trong hóa đơn, không thể cập nhật!");
		}

		if (existsByMovieSessionIdAndSeatIdExceptId(
				ticket.getMovieSession().getMovieSessionId(),
				ticket.getSeat().getSeatId(),
				ticket.getTicketId())) {
			throw new IllegalArgumentException("Ghế này đã có vé trong suất chiếu này ở bản ghi khác!");
		}

		if (!existsSeatInMovieSessionScreen(
				ticket.getMovieSession().getMovieSessionId(),
				ticket.getSeat().getSeatId())) {
			throw new IllegalArgumentException("Ghế không thuộc phòng chiếu của suất chiếu này!");
		}

		if (!existsMatchingTicketPricing(
				ticket.getMovieSession().getMovieSessionId(),
				ticket.getSeat().getSeatId(),
				ticket.getTicketPricing().getTicketPricingId())) {
			throw new IllegalArgumentException("Bảng giá vé không phù hợp với loại ghế hoặc loại phòng!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(UPDATE_MYSQL)) {

			ps.setInt(1, ticket.getMovieSession().getMovieSessionId());
			ps.setInt(2, ticket.getSeat().getSeatId());
			ps.setInt(3, ticket.getTicketPricing().getTicketPricingId());
			ps.setInt(4, ticket.getTicketStatus().getTicketStatusId());
			ps.setInt(5, ticket.getTicketId());

			return ps.executeUpdate() > 0;
		}
	}

	/**
	 * Xóa vé theo mã.
	 * Không cho xóa nếu vé đang được sử dụng trong hóa đơn vé hoặc bảng liên quan
	 * 
	 * @param ticketId - Mã vé
	 * @return true nếu xóa thành công
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean deleteTicketById(int ticketId) throws SQLException {
		if (ticketId <= 0) {
			throw new IllegalArgumentException("ticketId phải lớn hơn 0!");
		}

		if (isTicketUsed(ticketId)) {
			throw new IllegalArgumentException("Vé đang được sử dụng, không thể xóa!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(DELETE_MYSQL)) {

			ps.setInt(1, ticketId);

			return ps.executeUpdate() > 0;
		}
	}

	/**
	 * Tìm vé theo mã
	 * 
	 * @param ticketId - Mã vé
	 * @return đối tượng Ticket nếu tìm thấy, null nếu không
	 * @throws SQLException nếu có lỗi SQL
	 */
	public Ticket findById(int ticketId) throws SQLException {
		if (ticketId <= 0) {
			throw new IllegalArgumentException("ticketId phải lớn hơn 0!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID_MYSQL)) {

			ps.setInt(1, ticketId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mapResultSetToTicket(rs);
				}
			}
		}

		return null;
	}

	/**
	 * Lấy tất cả vé
	 * 
	 * @return danh sách vé
	 * @throws SQLException nếu có lỗi SQL
	 */
	public List<Ticket> getAllTickets() throws SQLException {
		List<Ticket> tickets = new ArrayList<Ticket>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_ALL_MYSQL);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				tickets.add(mapResultSetToTicket(rs));
			}
		}

		return tickets;
	}

	/**
	 * Tìm vé theo mã suất chiếu
	 * 
	 * @param movieSessionId - Mã suất chiếu
	 * @return danh sách vé phù hợp
	 * @throws SQLException nếu có lỗi SQL
	 */
	public List<Ticket> searchByMovieSessionId(int movieSessionId) throws SQLException {
		if (movieSessionId <= 0) {
			throw new IllegalArgumentException("movieSessionId phải lớn hơn 0!");
		}

		List<Ticket> tickets = new ArrayList<Ticket>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SEARCH_BY_MOVIE_SESSION_ID_MYSQL)) {

			ps.setInt(1, movieSessionId);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					tickets.add(mapResultSetToTicket(rs));
				}
			}
		}

		return tickets;
	}

	/**
	 * Tìm vé theo mã ghế
	 * 
	 * @param seatId - Mã ghế
	 * @return danh sách vé phù hợp
	 * @throws SQLException nếu có lỗi SQL
	 */
	public List<Ticket> searchBySeatId(int seatId) throws SQLException {
		if (seatId <= 0) {
			throw new IllegalArgumentException("seatId phải lớn hơn 0!");
		}

		List<Ticket> tickets = new ArrayList<Ticket>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SEARCH_BY_SEAT_ID_MYSQL)) {

			ps.setInt(1, seatId);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					tickets.add(mapResultSetToTicket(rs));
				}
			}
		}

		return tickets;
	}

	/**
	 * Tìm vé theo trạng thái
	 * 
	 * @param ticketStatus - Trạng thái vé
	 * @return danh sách vé phù hợp
	 * @throws SQLException nếu có lỗi SQL
	 */
	public List<Ticket> searchByStatus(TicketStatus ticketStatus) throws SQLException {
		if (ticketStatus == null) {
			throw new IllegalArgumentException("ticketStatus không được null!");
		}

		List<Ticket> tickets = new ArrayList<Ticket>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SEARCH_BY_STATUS_MYSQL)) {

			ps.setInt(1, ticketStatus.getTicketStatusId());

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					tickets.add(mapResultSetToTicket(rs));
				}
			}
		}

		return tickets;
	}
}