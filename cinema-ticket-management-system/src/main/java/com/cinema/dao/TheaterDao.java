package com.cinema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.cinema.config.DBConnection;
import com.cinema.entity.Theater;
import com.cinema.validator.TheaterValidator;

/**
 * DAO cho thực thể Theater
 * Chịu trách nhiệm thao tác dữ liệu với bảng rap trong MySQL
 * 
 * Bảng ánh xạ:
 * rap(
 * 	ma_rap,
 * 	ten_rap,
 * 	dia_chi,
 * 	created_at,
 * 	updated_at
 * )
 * 
 * @author Hải Anh (chính)
 * @author Minh Huy (review, chỉnh tên method và ràng buộc)
 */
public class TheaterDao {

	private static final String INSERT_MYSQL = """
			INSERT INTO rap (
				ten_rap,
				dia_chi
			)
			VALUES (?, ?)
			""";

	private static final String UPDATE_MYSQL = """
			UPDATE rap
			SET ten_rap = ?,
				dia_chi = ?
			WHERE ma_rap = ?
			""";

	private static final String DELETE_MYSQL = """
			DELETE FROM rap
			WHERE ma_rap = ?
			""";

	private static final String SELECT_BY_ID_MYSQL = """
			SELECT ma_rap,
			       ten_rap,
			       dia_chi,
			       created_at,
			       updated_at
			FROM rap
			WHERE ma_rap = ?
			""";

	private static final String SELECT_ALL_MYSQL = """
			SELECT ma_rap,
			       ten_rap,
			       dia_chi,
			       created_at,
			       updated_at
			FROM rap
			ORDER BY ten_rap ASC, ma_rap ASC
			""";

	private static final String SEARCH_BY_NAME_MYSQL = """
			SELECT ma_rap,
			       ten_rap,
			       dia_chi,
			       created_at,
			       updated_at
			FROM rap
			WHERE ten_rap LIKE ?
			ORDER BY ten_rap ASC, ma_rap ASC
			""";

	private static final String EXISTS_BY_NAME_MYSQL = """
			SELECT 1
			FROM rap
			WHERE ten_rap = ?
			LIMIT 1
			""";

	private static final String EXISTS_BY_NAME_EXCEPT_ID_MYSQL = """
			SELECT 1
			FROM rap
			WHERE ten_rap = ?
			  AND ma_rap <> ?
			LIMIT 1
			""";

	private static final String IS_USED_IN_SCREEN_MYSQL = """
			SELECT 1
			FROM phong_chieu
			WHERE ma_rap = ?
			LIMIT 1
			""";

	/**
	 * Kiểm tra rạp đã tồn tại theo tên hay chưa
	 * 
	 * @param theaterName - Tên rạp
	 * @return true nếu đã tồn tại
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean existsByName(String theaterName) throws SQLException {
		if (theaterName == null || theaterName.trim().isEmpty()) {
			return false;
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(EXISTS_BY_NAME_MYSQL)) {

			ps.setString(1, theaterName.trim());

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	/**
	 * Kiểm tra rạp đã tồn tại ở bản ghi khác hay chưa
	 * 
	 * @param theaterName - Tên rạp
	 * @param theaterId - Mã rạp
	 * @return true nếu đã tồn tại ở bản ghi khác
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean existsByNameExceptId(String theaterName, int theaterId) throws SQLException {
		if (theaterId <= 0 || theaterName == null || theaterName.trim().isEmpty()) {
			throw new IllegalArgumentException("theaterId phải lớn hơn 0 và theaterName không được để trống!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(EXISTS_BY_NAME_EXCEPT_ID_MYSQL)) {

			ps.setString(1, theaterName.trim());
			ps.setInt(2, theaterId);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	/**
	 * Kiểm tra rạp có đang được sử dụng ở bảng Screen hay không
	 * 
	 * @param theaterId - Mã rạp
	 * @return true nếu đang được sử dụng
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean isUsedInScreen(int theaterId) throws SQLException {
		if (theaterId <= 0) {
			throw new IllegalArgumentException("theaterId phải lớn hơn 0!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(IS_USED_IN_SCREEN_MYSQL)) {

			ps.setInt(1, theaterId);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	/**
	 * Kiểm tra rạp có đang được sử dụng ở các bảng liên quan hay không
	 * 
	 * @param theaterId - Mã rạp
	 * @return true nếu đang được sử dụng
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean isTheaterUsed(int theaterId) throws SQLException {
		if (theaterId <= 0) {
			throw new IllegalArgumentException("theaterId phải lớn hơn 0!");
		}

		return isUsedInScreen(theaterId);
	}

	/**
	 * Ánh xạ một dòng dữ liệu ResultSet thành đối tượng Theater
	 * 
	 * @param rs - ResultSet đang trỏ tới dòng dữ liệu hợp lệ
	 * @return đối tượng Theater
	 * @throws SQLException nếu có lỗi đọc dữ liệu
	 */
	private Theater mapResultSetToTheater(ResultSet rs) throws SQLException {
		Timestamp createdAt = rs.getTimestamp("created_at");
		Timestamp updatedAt = rs.getTimestamp("updated_at");

		Theater aTheater = new Theater(
				rs.getInt("ma_rap"),
				rs.getString("ten_rap"),
				rs.getString("dia_chi"),
				createdAt != null ? createdAt.toLocalDateTime() : null,
				updatedAt != null ? updatedAt.toLocalDateTime() : null
		);

		return aTheater;
	}

	/**
	 * Thêm rạp mới
	 * 
	 * @param theater - Đối tượng rạp
	 * @return true nếu thêm thành công
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean addTheater(Theater theater) throws SQLException {
		TheaterValidator.validateForCreate(theater);

		if (existsByName(theater.getTheaterName())) {
			throw new IllegalArgumentException("Rạp đã tồn tại!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(INSERT_MYSQL)) {

			ps.setString(1, theater.getTheaterName().trim());
			ps.setString(2, theater.getTheaterAddress() != null ? theater.getTheaterAddress().trim() : null);

			return ps.executeUpdate() > 0;
		}
	}

	/**
	 * Cập nhật thông tin rạp
	 * 
	 * @param theater - Rạp cần cập nhật
	 * @return true nếu cập nhật thành công
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean updateTheater(Theater theater) throws SQLException {
		TheaterValidator.validateForUpdate(theater);

		if (theater.getTheaterId() <= 0) {
			throw new IllegalArgumentException("theaterId phải lớn hơn 0!");
		}

		if (existsByNameExceptId(theater.getTheaterName(), theater.getTheaterId())) {
			throw new IllegalArgumentException("Tên rạp đã tồn tại ở bản ghi khác!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(UPDATE_MYSQL)) {

			ps.setString(1, theater.getTheaterName().trim());
			ps.setString(2, theater.getTheaterAddress() != null ? theater.getTheaterAddress().trim() : null);
			ps.setInt(3, theater.getTheaterId());

			return ps.executeUpdate() > 0;
		}
	}

	/**
	 * Xóa rạp theo mã
	 * Không cho xóa nếu rạp đang được sử dụng ở bảng liên quan
	 * 
	 * @param theaterId - Mã rạp
	 * @return true nếu xóa thành công
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean deleteTheaterById(int theaterId) throws SQLException {
		if (theaterId <= 0) {
			throw new IllegalArgumentException("theaterId phải lớn hơn 0!");
		}

		if (isTheaterUsed(theaterId)) {
			throw new IllegalArgumentException("Rạp đang được sử dụng, không thể xóa!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(DELETE_MYSQL)) {

			ps.setInt(1, theaterId);

			return ps.executeUpdate() > 0;
		}
	}

	/**
	 * Tìm rạp theo mã
	 * 
	 * @param theaterId - Mã rạp
	 * @return đối tượng Theater nếu tìm thấy, ngược lại trả về null
	 * @throws SQLException nếu có lỗi SQL
	 */
	public Theater findById(int theaterId) throws SQLException {
		if (theaterId <= 0) {
			throw new IllegalArgumentException("theaterId phải lớn hơn 0!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID_MYSQL)) {

			ps.setInt(1, theaterId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mapResultSetToTheater(rs);
				}
			}
		}

		return null;
	}

	/**
	 * Tìm rạp theo tên gần đúng
	 * 
	 * @param keyword - Từ khóa tên rạp
	 * @return danh sách rạp phù hợp
	 * @throws SQLException nếu có lỗi SQL
	 */
	public List<Theater> searchByName(String keyword) throws SQLException {
		List<Theater> theaters = new ArrayList<Theater>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SEARCH_BY_NAME_MYSQL)) {

			keyword = (keyword == null) ? "" : keyword.trim();
			ps.setString(1, "%" + keyword + "%");

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					theaters.add(mapResultSetToTheater(rs));
				}
			}
		}

		return theaters;
	}

	/**
	 * Lấy tất cả rạp
	 * 
	 * @return danh sách rạp
	 * @throws SQLException nếu có lỗi SQL
	 */
	public List<Theater> getAllTheaters() throws SQLException {
		List<Theater> theaters = new ArrayList<Theater>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_ALL_MYSQL);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				theaters.add(mapResultSetToTheater(rs));
			}
		}

		return theaters;
	}
}