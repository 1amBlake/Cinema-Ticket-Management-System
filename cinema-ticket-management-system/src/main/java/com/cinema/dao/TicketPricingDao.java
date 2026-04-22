package com.cinema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.cinema.config.DBConnection;
import com.cinema.entity.ScreenType;
import com.cinema.entity.SeatType;
import com.cinema.entity.TicketPricing;

/**
 * DAO cho thực thể TicketPricing
 * Chịu trách nhiệm thao tác dữ liệu với bảng bang_gia_ve trong MySQL
 * 
 * Bảng ánh xạ:
 * bang_gia_ve(
 * 	ma_bang_gia,
 * 	ma_loai_ghe,
 * 	ma_loai_phong,
 * 	gia,
 * 	created_at,
 * 	updated_at
 * )
 * 
 * @author Minh Huy (sửa theo mẫu)
 */
public class TicketPricingDao {

	private static final String INSERT_MYSQL = """
			INSERT INTO bang_gia_ve (
				ma_loai_ghe,
				ma_loai_phong,
				gia
			)
			VALUES (?, ?, ?)
			""";

	private static final String UPDATE_MYSQL = """
			UPDATE bang_gia_ve
			SET ma_loai_ghe = ?,
				ma_loai_phong = ?,
				gia = ?
			WHERE ma_bang_gia = ?
			""";

	private static final String DELETE_MYSQL = """
			DELETE FROM bang_gia_ve
			WHERE ma_bang_gia = ?
			""";

	private static final String SELECT_BY_ID_MYSQL = """
			SELECT ma_bang_gia,
				   ma_loai_ghe,
				   ma_loai_phong,
				   gia,
				   created_at,
				   updated_at
			FROM bang_gia_ve
			WHERE ma_bang_gia = ?
			""";

	private static final String SELECT_ALL_MYSQL = """
			SELECT ma_bang_gia,
				   ma_loai_ghe,
				   ma_loai_phong,
				   gia,
				   created_at,
				   updated_at
			FROM bang_gia_ve
			ORDER BY ma_bang_gia ASC
			""";

	private static final String SEARCH_BY_SEAT_TYPE_ID_MYSQL = """
			SELECT ma_bang_gia,
				   ma_loai_ghe,
				   ma_loai_phong,
				   gia,
				   created_at,
				   updated_at
			FROM bang_gia_ve
			WHERE ma_loai_ghe = ?
			ORDER BY ma_bang_gia ASC
			""";

	private static final String SEARCH_BY_SCREEN_TYPE_ID_MYSQL = """
			SELECT ma_bang_gia,
				   ma_loai_ghe,
				   ma_loai_phong,
				   gia,
				   created_at,
				   updated_at
			FROM bang_gia_ve
			WHERE ma_loai_phong = ?
			ORDER BY ma_bang_gia ASC
			""";

	private static final String EXISTS_BY_SEAT_TYPE_AND_SCREEN_TYPE_MYSQL = """
			SELECT 1
			FROM bang_gia_ve
			WHERE ma_loai_ghe = ?
			  AND ma_loai_phong = ?
			LIMIT 1
			""";

	private static final String EXISTS_BY_SEAT_TYPE_AND_SCREEN_TYPE_EXCEPT_ID_MYSQL = """
			SELECT 1
			FROM bang_gia_ve
			WHERE ma_loai_ghe = ?
			  AND ma_loai_phong = ?
			  AND ma_bang_gia <> ?
			LIMIT 1
			""";

	private static final String IS_USED_IN_TICKET_MYSQL = """
			SELECT 1
			FROM ve
			WHERE ma_bang_gia = ?
			LIMIT 1
			""";

	/**
	 * Kiểm tra dữ liệu đầu vào của TicketPricing
	 * 
	 * @param ticketPricing - Đối tượng TicketPricing để kiểm tra
	 */
	private void validateTicketPricing(TicketPricing ticketPricing) { // TODO: làm validate internal và package

	}

	/**
	 * Kiểm tra cặp loại ghế - loại phòng đã tồn tại hay chưa
	 * 
	 * @param seatTypeId - Mã loại ghế
	 * @param screenTypeId - Mã loại phòng
	 * @return true nếu đã tồn tại
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean existsBySeatTypeIdAndScreenTypeId(int seatTypeId, int screenTypeId) throws SQLException {
		if (seatTypeId <= 0 || screenTypeId <= 0) {
			throw new IllegalArgumentException("seatTypeId và screenTypeId phải lớn hơn 0!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(EXISTS_BY_SEAT_TYPE_AND_SCREEN_TYPE_MYSQL)) {

			ps.setInt(1, seatTypeId);
			ps.setInt(2, screenTypeId);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	/**
	 * Kiểm tra cặp loại ghế - loại phòng đã tồn tại ở bản ghi khác hay chưa
	 * 
	 * @param seatTypeId - Mã loại ghế
	 * @param screenTypeId - Mã loại phòng
	 * @param ticketPricingId - Mã bảng giá vé
	 * @return true nếu đã tồn tại ở bản ghi khác
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean existsBySeatTypeIdAndScreenTypeIdExceptId(int seatTypeId, int screenTypeId, int ticketPricingId)
			throws SQLException {
		if (seatTypeId <= 0 || screenTypeId <= 0 || ticketPricingId <= 0) {
			throw new IllegalArgumentException("seatTypeId, screenTypeId và ticketPricingId phải lớn hơn 0!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(EXISTS_BY_SEAT_TYPE_AND_SCREEN_TYPE_EXCEPT_ID_MYSQL)) {

			ps.setInt(1, seatTypeId);
			ps.setInt(2, screenTypeId);
			ps.setInt(3, ticketPricingId);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	/**
	 * Kiểm tra bảng giá vé có đang được sử dụng ở bảng Ticket hay không
	 * 
	 * @param ticketPricingId - Mã bảng giá vé
	 * @return true nếu đang được sử dụng
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean isUsedInTicket(int ticketPricingId) throws SQLException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(IS_USED_IN_TICKET_MYSQL)) {

			ps.setInt(1, ticketPricingId);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	/**
	 * Kiểm tra bảng giá vé có đang được sử dụng ở các bảng liên quan hay không
	 * 
	 * @param ticketPricingId - Mã bảng giá vé
	 * @return true nếu đang được sử dụng
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean isTicketPricingUsed(int ticketPricingId) throws SQLException {
		if (ticketPricingId <= 0) {
			throw new IllegalArgumentException("ticketPricingId phải lớn hơn 0!");
		}

		return isUsedInTicket(ticketPricingId);
	}

	/**
	 * Ánh xạ một dòng dữ liệu ResultSet thành đối tượng TicketPricing
	 * 
	 * @param rs - ResultSet đang trỏ tới dòng dữ liệu hợp lệ
	 * @return đối tượng TicketPricing
	 * @throws SQLException nếu có lỗi SQL
	 */
	private TicketPricing mapResultSetToTicketPricing(ResultSet rs) throws SQLException {
		Timestamp createdAt = rs.getTimestamp("created_at");
		Timestamp updatedAt = rs.getTimestamp("updated_at");

		SeatType seatType = new SeatType(rs.getInt("ma_loai_ghe"));
		ScreenType screenType = new ScreenType(rs.getInt("ma_loai_phong"));

		TicketPricing aTicketPricing = new TicketPricing(
				rs.getInt("ma_bang_gia"),
				seatType,
				screenType,
				rs.getDouble("gia"),
				createdAt != null ? createdAt.toLocalDateTime() : null,
				updatedAt != null ? updatedAt.toLocalDateTime() : null
		);

		return aTicketPricing;
	}

	/**
	 * Thêm bảng giá vé mới
	 * 
	 * @param ticketPricing - Đối tượng TicketPricing
	 * @return true nếu thêm thành công
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean addTicketPricing(TicketPricing ticketPricing) throws SQLException {
		validateTicketPricing(ticketPricing);

		if (existsBySeatTypeIdAndScreenTypeId(
				ticketPricing.getSeatType().getSeatTypeId(),
				ticketPricing.getScreenType().getScreenTypeId())) {
			throw new IllegalArgumentException("Cặp loại ghế và loại phòng đã có bảng giá!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(INSERT_MYSQL)) {

			ps.setInt(1, ticketPricing.getSeatType().getSeatTypeId());
			ps.setInt(2, ticketPricing.getScreenType().getScreenTypeId());
			ps.setDouble(3, ticketPricing.getPrice());

			return ps.executeUpdate() > 0;
		}
	}

	/**
	 * Cập nhật thông tin bảng giá vé
	 * 
	 * @param ticketPricing - Đối tượng TicketPricing cần cập nhật
	 * @return true nếu cập nhật thành công
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean updateTicketPricing(TicketPricing ticketPricing) throws SQLException {
		validateTicketPricing(ticketPricing);

		if (ticketPricing.getTicketPricingId() <= 0) {
			throw new IllegalArgumentException("ticketPricingId phải lớn hơn 0!");
		}

		if (isTicketPricingUsed(ticketPricing.getTicketPricingId())) {
			throw new IllegalArgumentException("Bảng giá vé đã phát sinh vé, không thể cập nhật!");
		}

		if (existsBySeatTypeIdAndScreenTypeIdExceptId(
				ticketPricing.getSeatType().getSeatTypeId(),
				ticketPricing.getScreenType().getScreenTypeId(),
				ticketPricing.getTicketPricingId())) {
			throw new IllegalArgumentException("Cặp loại ghế và loại phòng đã tồn tại ở bản ghi khác!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(UPDATE_MYSQL)) {

			ps.setInt(1, ticketPricing.getSeatType().getSeatTypeId());
			ps.setInt(2, ticketPricing.getScreenType().getScreenTypeId());
			ps.setDouble(3, ticketPricing.getPrice());
			ps.setInt(4, ticketPricing.getTicketPricingId());

			return ps.executeUpdate() > 0;
		}
	}

	/**
	 * Xóa bảng giá vé theo mã
	 * 
	 * @param ticketPricingId - Mã bảng giá vé
	 * @return true nếu xóa thành công
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean deleteTicketPricingById(int ticketPricingId) throws SQLException {
		if (ticketPricingId <= 0) {
			throw new IllegalArgumentException("ticketPricingId phải lớn hơn 0!");
		}

		if (isTicketPricingUsed(ticketPricingId)) {
			throw new IllegalArgumentException("Bảng giá vé đang được sử dụng, không thể xóa!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(DELETE_MYSQL)) {

			ps.setInt(1, ticketPricingId);
			return ps.executeUpdate() > 0;
		}
	}

	/**
	 * Tìm bảng giá vé theo mã
	 * 
	 * @param ticketPricingId - Mã bảng giá vé
	 * @return đối tượng TicketPricing nếu tìm thấy, ngược lại trả về null
	 * @throws SQLException nếu có lỗi SQL
	 */
	public TicketPricing findById(int ticketPricingId) throws SQLException {
		if (ticketPricingId <= 0) {
			throw new IllegalArgumentException("ticketPricingId phải lớn hơn 0!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID_MYSQL)) {

			ps.setInt(1, ticketPricingId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mapResultSetToTicketPricing(rs);
				}
			}
		}

		return null;
	}

	/**
	 * Lấy tất cả bảng giá vé
	 * 
	 * @return danh sách bảng giá vé
	 * @throws SQLException nếu có lỗi SQL
	 */
	public List<TicketPricing> getAllTicketPricings() throws SQLException {
		List<TicketPricing> ticketPricings = new ArrayList<TicketPricing>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_ALL_MYSQL);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				ticketPricings.add(mapResultSetToTicketPricing(rs));
			}
		}

		return ticketPricings;
	}

	/**
	 * Tìm bảng giá vé theo loại ghế
	 * 
	 * @param seatTypeId - Mã loại ghế
	 * @return danh sách bảng giá vé phù hợp
	 * @throws SQLException nếu có lỗi SQL
	 */
	public List<TicketPricing> searchBySeatTypeId(int seatTypeId) throws SQLException {
		if (seatTypeId <= 0) {
			throw new IllegalArgumentException("seatTypeId phải lớn hơn 0!");
		}

		List<TicketPricing> ticketPricings = new ArrayList<TicketPricing>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SEARCH_BY_SEAT_TYPE_ID_MYSQL)) {

			ps.setInt(1, seatTypeId);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ticketPricings.add(mapResultSetToTicketPricing(rs));
				}
			}
		}

		return ticketPricings;
	}

	/**
	 * Tìm bảng giá vé theo loại phòng chiếu
	 * 
	 * @param screenTypeId - Mã loại phòng chiếu
	 * @return danh sách bảng giá vé phù hợp
	 * @throws SQLException nếu có lỗi SQL
	 */
	public List<TicketPricing> searchByScreenTypeId(int screenTypeId) throws SQLException {
		if (screenTypeId <= 0) {
			throw new IllegalArgumentException("screenTypeId phải lớn hơn 0!");
		}

		List<TicketPricing> ticketPricings = new ArrayList<TicketPricing>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SEARCH_BY_SCREEN_TYPE_ID_MYSQL)) {

			ps.setInt(1, screenTypeId);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ticketPricings.add(mapResultSetToTicketPricing(rs));
				}
			}
		}

		return ticketPricings;
	}
}