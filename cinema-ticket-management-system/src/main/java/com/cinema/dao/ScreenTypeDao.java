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

/**
 * DAO cho thực thể ScreenType
 * Chịu trách nhiệm thao tác dữ liệu với bảng loai_phong trong MySQL
 * 
 * Bảng ánh xạ:
 * loai_phong(ma_loai_phong, ten_loai_phong, created_at, updated_at)
 * 
 * @author Hải Anh (chính)
 * @author Minh Huy (review/sửa tên method)
 */
public class ScreenTypeDao {

	private static final String INSERT_MYSQL = """
			INSERT INTO loai_phong (
				ten_loai_phong
			)
			VALUES (?)
			""";

	private static final String UPDATE_MYSQL = """
			UPDATE loai_phong
			SET ten_loai_phong = ?
			WHERE ma_loai_phong = ?
			""";

	private static final String DELETE_MYSQL = """
			DELETE FROM loai_phong
			WHERE ma_loai_phong = ?
			""";

	private static final String SELECT_BY_ID_MYSQL = """
			SELECT ma_loai_phong,
			       ten_loai_phong,
			       created_at,
			       updated_at
			FROM loai_phong
			WHERE ma_loai_phong = ?
			""";

	private static final String SELECT_ALL_MYSQL = """
			SELECT ma_loai_phong,
			       ten_loai_phong,
			       created_at,
			       updated_at
			FROM loai_phong
			ORDER BY ten_loai_phong ASC
			""";

	private static final String SEARCH_BY_NAME_MYSQL = """
			SELECT ma_loai_phong,
			       ten_loai_phong,
			       created_at,
			       updated_at
			FROM loai_phong
			WHERE ten_loai_phong LIKE ?
			ORDER BY ten_loai_phong ASC
			""";

	private static final String EXISTS_BY_NAME_MYSQL = """
			SELECT 1
			FROM loai_phong
			WHERE ten_loai_phong = ?
			LIMIT 1
			""";

	private static final String EXISTS_BY_NAME_EXCEPT_ID_MYSQL = """
			SELECT 1
			FROM loai_phong
			WHERE ten_loai_phong = ?
			  AND ma_loai_phong <> ?
			LIMIT 1
			""";

	private static final String IS_USED_IN_SCREEN_MYSQL = """
			SELECT 1
			FROM phong_chieu
			WHERE ma_loai_phong = ?
			LIMIT 1
			""";

	/**
	 * Kiểm tra dữ liệu đầu vào của ScreenType.
	 * Một phần ràng buộc đã được kiểm tra ở entity.
	 * 
	 * @param screenType - Đối tượng ScreenType cần kiểm tra
	 */
	private void validateScreenType(ScreenType screenType) { // TODO: làm validate package
		// ScreenTypeValidator -> package validator
	}

	/**
	 * Ánh xạ một dòng dữ liệu ResultSet thành đối tượng ScreenType.
	 * 
	 * @param rs - ResultSet đang trỏ tới dòng dữ liệu hợp lệ
	 * @return đối tượng ScreenType
	 * @throws SQLException nếu có lỗi đọc dữ liệu
	 */
	private ScreenType mapResultSetToScreenType(ResultSet rs) throws SQLException {
		Timestamp createdAt = rs.getTimestamp("created_at");
		Timestamp updatedAt = rs.getTimestamp("updated_at");

		return new ScreenType(
				rs.getInt("ma_loai_phong"),
				rs.getString("ten_loai_phong"),
				createdAt != null ? createdAt.toLocalDateTime() : null,
				updatedAt != null ? updatedAt.toLocalDateTime() : null
		);
	}

	/**
	 * Kiểm tra loại phòng chiếu đã tồn tại theo tên hay chưa.
	 * 
	 * @param screenTypeName - Tên loại phòng chiếu
	 * @return true nếu đã tồn tại
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean existsByName(String screenTypeName) throws SQLException {
		if (screenTypeName == null || screenTypeName.trim().isEmpty()) {
			return false;
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(EXISTS_BY_NAME_MYSQL)) {

			ps.setString(1, screenTypeName.trim());

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	/**
	 * Kiểm tra loại phòng chiếu đã tồn tại ở bản ghi khác hay chưa.
	 * 
	 * @param screenTypeName - Tên loại phòng chiếu
	 * @param screenTypeId - Mã loại phòng chiếu
	 * @return true nếu đã tồn tại ở bản ghi khác
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean existsByNameExceptId(String screenTypeName, int screenTypeId) throws SQLException {
		if (screenTypeId <= 0 || screenTypeName == null || screenTypeName.trim().isEmpty()) {
			throw new IllegalArgumentException("screenTypeId phải lớn hơn 0 và screenTypeName không được để trống!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(EXISTS_BY_NAME_EXCEPT_ID_MYSQL)) {

			ps.setString(1, screenTypeName.trim());
			ps.setInt(2, screenTypeId);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	/**
	 * Kiểm tra loại phòng chiếu có đang được sử dụng ở bảng Screen hay không.
	 * 
	 * @param screenTypeId - Mã loại phòng chiếu
	 * @return true nếu đang được sử dụng
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean isUsedInScreen(int screenTypeId) throws SQLException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(IS_USED_IN_SCREEN_MYSQL)) {

			ps.setInt(1, screenTypeId);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	/**
	 * Kiểm tra loại phòng chiếu có đang được sử dụng ở các bảng liên quan hay không.
	 * 
	 * @param screenTypeId - Mã loại phòng chiếu
	 * @return true nếu đang được sử dụng
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean isScreenTypeUsed(int screenTypeId) throws SQLException {
		if (screenTypeId <= 0) {
			throw new IllegalArgumentException("screenTypeId phải lớn hơn 0!");
		}

		return isUsedInScreen(screenTypeId);
	}

	/**
	 * Thêm loại phòng chiếu mới.
	 * 
	 * @param screenType - Loại phòng chiếu cần thêm
	 * @return true nếu thêm thành công
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean addScreenType(ScreenType screenType) throws SQLException {
		validateScreenType(screenType);

		if (screenType == null) {
			throw new IllegalArgumentException("screenType không được null!");
		}

		if (existsByName(screenType.getScreenTypeName())) {
			throw new IllegalArgumentException("Loại phòng chiếu đã tồn tại!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(INSERT_MYSQL)) {

			ps.setString(1, screenType.getScreenTypeName().trim());

			return ps.executeUpdate() > 0;
		}
	}

	/**
	 * Cập nhật thông tin loại phòng chiếu.
	 * 
	 * @param screenType - Loại phòng chiếu cần cập nhật
	 * @return true nếu cập nhật thành công, false nếu không tìm thấy bản ghi
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean updateScreenType(ScreenType screenType) throws SQLException {
		validateScreenType(screenType);

		if (screenType == null) {
			throw new IllegalArgumentException("screenType không được null!");
		}

		if (screenType.getScreenTypeId() <= 0) {
			throw new IllegalArgumentException("screenTypeId phải lớn hơn 0!");
		}

		if (existsByNameExceptId(screenType.getScreenTypeName(), screenType.getScreenTypeId())) {
			throw new IllegalArgumentException("Tên loại phòng chiếu đã tồn tại ở bản ghi khác!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(UPDATE_MYSQL)) {

			ps.setString(1, screenType.getScreenTypeName().trim());
			ps.setInt(2, screenType.getScreenTypeId());

			return ps.executeUpdate() > 0;
		}
	}

	/**
	 * Xóa loại phòng chiếu theo mã.
	 * Không cho xóa nếu loại phòng chiếu đang được sử dụng trong bảng Screen.
	 * 
	 * @param screenTypeId - Mã loại phòng chiếu
	 * @return true nếu xóa thành công
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean deleteScreenTypeById(int screenTypeId) throws SQLException {
		if (screenTypeId <= 0) {
			throw new IllegalArgumentException("screenTypeId phải lớn hơn 0!");
		}

		if (isScreenTypeUsed(screenTypeId)) {
			throw new IllegalArgumentException("Loại phòng chiếu đang được sử dụng, không thể xóa!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(DELETE_MYSQL)) {

			ps.setInt(1, screenTypeId);

			return ps.executeUpdate() > 0;
		}
	}

	/**
	 * Tìm loại phòng chiếu theo mã.
	 * 
	 * @param screenTypeId - Mã loại phòng chiếu
	 * @return đối tượng ScreenType nếu tìm thấy, ngược lại trả về null
	 * @throws SQLException nếu có lỗi SQL
	 */
	public ScreenType findById(int screenTypeId) throws SQLException {
		if (screenTypeId <= 0) {
			throw new IllegalArgumentException("screenTypeId phải lớn hơn 0!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID_MYSQL)) {

			ps.setInt(1, screenTypeId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mapResultSetToScreenType(rs);
				}
			}
		}

		return null;
	}

	/**
	 * Tìm loại phòng chiếu theo tên gần đúng.
	 * 
	 * @param keyword - Từ khóa tên loại phòng chiếu
	 * @return danh sách loại phòng chiếu phù hợp
	 * @throws SQLException nếu có lỗi SQL
	 */
	public List<ScreenType> searchByName(String keyword) throws SQLException {
		List<ScreenType> screenTypes = new ArrayList<>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SEARCH_BY_NAME_MYSQL)) {

			keyword = (keyword == null) ? "" : keyword.trim();
			ps.setString(1, "%" + keyword + "%");

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					screenTypes.add(mapResultSetToScreenType(rs));
				}
			}
		}

		return screenTypes;
	}

	/**
	 * Lấy tất cả loại phòng chiếu.
	 * 
	 * @return danh sách loại phòng chiếu
	 * @throws SQLException nếu có lỗi SQL
	 */
	public List<ScreenType> getAllScreenTypes() throws SQLException {
		List<ScreenType> screenTypes = new ArrayList<>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_ALL_MYSQL);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				screenTypes.add(mapResultSetToScreenType(rs));
			}
		}

		return screenTypes;
	}
}