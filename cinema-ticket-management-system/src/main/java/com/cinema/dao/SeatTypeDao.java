package com.cinema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.cinema.config.DBConnection;
import com.cinema.entity.SeatType;
import com.cinema.validator.SeatTypeValidator;

/**
 * DAO cho thực thể SeatType
 * Chịu trách nhiệm thao tác dữ liệu với bảng loai_ghe trong MySQL
 * 
 * Bảng ánh xạ:
 * loai_ghe(ma_loai_ghe, ten_loai_ghe, created_at, updated_at)
 * 
 * @author Hải Anh (chính)
 * @author Minh Huy (review/sửa tên method)
 */
public class SeatTypeDao {

	private static final String INSERT_MYSQL = """
			INSERT INTO loai_ghe (
				ten_loai_ghe
			)
			VALUES (?)
			""";

	private static final String UPDATE_MYSQL = """
			UPDATE loai_ghe
			SET ten_loai_ghe = ?
			WHERE ma_loai_ghe = ?
			""";

	private static final String DELETE_MYSQL = """
			DELETE FROM loai_ghe
			WHERE ma_loai_ghe = ?
			""";

	private static final String SELECT_BY_ID_MYSQL = """
			SELECT ma_loai_ghe,
			       ten_loai_ghe,
			       created_at,
			       updated_at
			FROM loai_ghe
			WHERE ma_loai_ghe = ?
			""";

	private static final String SELECT_ALL_MYSQL = """
			SELECT ma_loai_ghe,
			       ten_loai_ghe,
			       created_at,
			       updated_at
			FROM loai_ghe
			ORDER BY ten_loai_ghe ASC, ma_loai_ghe ASC
			""";

	private static final String SEARCH_BY_NAME_MYSQL = """
			SELECT ma_loai_ghe,
			       ten_loai_ghe,
			       created_at,
			       updated_at
			FROM loai_ghe
			WHERE ten_loai_ghe LIKE ?
			ORDER BY ten_loai_ghe ASC, ma_loai_ghe ASC
			""";

	private static final String EXISTS_BY_NAME_MYSQL = """
			SELECT 1
			FROM loai_ghe
			WHERE ten_loai_ghe = ?
			LIMIT 1
			""";

	private static final String EXISTS_BY_NAME_EXCEPT_ID_MYSQL = """
			SELECT 1
			FROM loai_ghe
			WHERE ten_loai_ghe = ?
			  AND ma_loai_ghe <> ?
			LIMIT 1
			""";

	private static final String IS_USED_IN_SEAT_MYSQL = """
			SELECT 1
			FROM ghe
			WHERE ma_loai_ghe = ?
			LIMIT 1
			""";

	/**
	 * Ánh xạ một dòng dữ liệu ResultSet thành đối tượng SeatType.
	 * 
	 * @param rs - ResultSet đang trỏ tới dòng dữ liệu hợp lệ
	 * @return đối tượng SeatType
	 * @throws SQLException nếu có lỗi đọc dữ liệu
	 */
	private SeatType mapResultSetToSeatType(ResultSet rs) throws SQLException {
		Timestamp createdAt = rs.getTimestamp("created_at");
		Timestamp updatedAt = rs.getTimestamp("updated_at");

		return new SeatType(
				rs.getInt("ma_loai_ghe"),
				rs.getString("ten_loai_ghe"),
				createdAt != null ? createdAt.toLocalDateTime() : null,
				updatedAt != null ? updatedAt.toLocalDateTime() : null
		);
	}

	/**
	 * Kiểm tra loại ghế đã tồn tại theo tên hay chưa.
	 * 
	 * @param seatTypeName - Tên loại ghế
	 * @return true nếu đã tồn tại
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean existsByName(String seatTypeName) throws SQLException {
		if (seatTypeName == null || seatTypeName.trim().isEmpty()) {
			return false;
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(EXISTS_BY_NAME_MYSQL)) {

			ps.setString(1, seatTypeName.trim());

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	/**
	 * Kiểm tra loại ghế đã tồn tại ở bản ghi khác hay chưa.
	 * 
	 * @param seatTypeName - Tên loại ghế
	 * @param seatTypeId - Mã loại ghế
	 * @return true nếu đã tồn tại ở bản ghi khác
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean existsByNameExceptId(String seatTypeName, int seatTypeId) throws SQLException {
		if (seatTypeId <= 0 || seatTypeName == null || seatTypeName.trim().isEmpty()) {
			throw new IllegalArgumentException("seatTypeId phải lớn hơn 0 và seatTypeName không được để trống!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(EXISTS_BY_NAME_EXCEPT_ID_MYSQL)) {

			ps.setString(1, seatTypeName.trim());
			ps.setInt(2, seatTypeId);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	/**
	 * Kiểm tra loại ghế có đang được sử dụng ở bảng Seat hay không.
	 * 
	 * @param seatTypeId - Mã loại ghế
	 * @return true nếu đang được sử dụng
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean isUsedInSeat(int seatTypeId) throws SQLException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(IS_USED_IN_SEAT_MYSQL)) {

			ps.setInt(1, seatTypeId);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	/**
	 * Kiểm tra loại ghế có đang được sử dụng ở các bảng liên quan hay không.
	 * 
	 * @param seatTypeId - Mã loại ghế
	 * @return true nếu đang được sử dụng
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean isSeatTypeUsed(int seatTypeId) throws SQLException {
		if (seatTypeId <= 0) {
			throw new IllegalArgumentException("seatTypeId phải lớn hơn 0!");
		}

		return isUsedInSeat(seatTypeId);
	}

	/**
	 * Thêm loại ghế mới.
	 * 
	 * @param seatType - Loại ghế cần thêm
	 * @return true nếu thêm thành công
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean addSeatType(SeatType seatType) throws SQLException {
		SeatTypeValidator.validateForCreate(seatType);

		if (seatType == null) {
			throw new IllegalArgumentException("seatType không được null!");
		}

		if (existsByName(seatType.getSeatTypeName())) {
			throw new IllegalArgumentException("Loại ghế đã tồn tại!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(INSERT_MYSQL)) {

			ps.setString(1, seatType.getSeatTypeName().trim());

			return ps.executeUpdate() > 0;
		}
	}

	/**
	 * Cập nhật thông tin loại ghế.
	 * 
	 * @param seatType - Loại ghế cần cập nhật
	 * @return true nếu cập nhật thành công, false nếu không tìm thấy bản ghi
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean updateSeatType(SeatType seatType) throws SQLException {
		SeatTypeValidator.validateForUpdate(seatType);

		if (seatType == null) {
			throw new IllegalArgumentException("seatType không được null!");
		}

		if (seatType.getSeatTypeId() <= 0) {
			throw new IllegalArgumentException("seatTypeId phải lớn hơn 0!");
		}

		if (existsByNameExceptId(seatType.getSeatTypeName(), seatType.getSeatTypeId())) {
			throw new IllegalArgumentException("Tên loại ghế đã tồn tại ở bản ghi khác!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(UPDATE_MYSQL)) {

			ps.setString(1, seatType.getSeatTypeName().trim());
			ps.setInt(2, seatType.getSeatTypeId());

			return ps.executeUpdate() > 0;
		}
	}

	/**
	 * Xóa loại ghế theo mã.
	 * Không cho xóa nếu loại ghế đang được sử dụng trong bảng Seat.
	 * 
	 * @param seatTypeId - Mã loại ghế
	 * @return true nếu xóa thành công
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean deleteSeatTypeById(int seatTypeId) throws SQLException {
		if (seatTypeId <= 0) {
			throw new IllegalArgumentException("seatTypeId phải lớn hơn 0!");
		}

		if (isSeatTypeUsed(seatTypeId)) {
			throw new IllegalArgumentException("Loại ghế đang được sử dụng, không thể xóa!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(DELETE_MYSQL)) {

			ps.setInt(1, seatTypeId);

			return ps.executeUpdate() > 0;
		}
	}

	/**
	 * Tìm loại ghế theo mã.
	 * 
	 * @param seatTypeId - Mã loại ghế
	 * @return đối tượng SeatType nếu tìm thấy, ngược lại trả về null
	 * @throws SQLException nếu có lỗi SQL
	 */
	public SeatType findById(int seatTypeId) throws SQLException {
		if (seatTypeId <= 0) {
			throw new IllegalArgumentException("seatTypeId phải lớn hơn 0!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID_MYSQL)) {

			ps.setInt(1, seatTypeId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mapResultSetToSeatType(rs);
				}
			}
		}

		return null;
	}

	/**
	 * Tìm loại ghế theo tên gần đúng.
	 * 
	 * @param keyword - Từ khóa tên loại ghế
	 * @return danh sách loại ghế phù hợp
	 * @throws SQLException nếu có lỗi SQL
	 */
	public List<SeatType> searchByName(String keyword) throws SQLException {
		List<SeatType> seatTypes = new ArrayList<>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SEARCH_BY_NAME_MYSQL)) {

			keyword = (keyword == null) ? "" : keyword.trim();
			ps.setString(1, "%" + keyword + "%");

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					seatTypes.add(mapResultSetToSeatType(rs));
				}
			}
		}

		return seatTypes;
	}

	/**
	 * Lấy tất cả loại ghế.
	 * 
	 * @return danh sách loại ghế
	 * @throws SQLException nếu có lỗi SQL
	 */
	public List<SeatType> getAllSeatTypes() throws SQLException {
		List<SeatType> seatTypes = new ArrayList<>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_ALL_MYSQL);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				seatTypes.add(mapResultSetToSeatType(rs));
			}
		}

		return seatTypes;
	}
}