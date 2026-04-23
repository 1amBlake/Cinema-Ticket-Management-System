package com.cinema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.cinema.config.DBConnection;
import com.cinema.entity.JobTitle;

/**
 * DAO cho thực thể JobTitle
 * Chịu trách nhiệm thao tác dữ liệu với bảng chuc_vu trong MySQL.
 * 
 * Bảng ánh xạ:
 * chuc_vu(ma_chuc_vu, ten_chuc_vu, created_at, updated_at)
 * 
 * @author Quốc Anh (chính)
 * @author Minh Huy (sửa một số điểm)
 */
public class JobTitleDao {

	private static final String INSERT_MYSQL = """
			INSERT INTO chuc_vu (ten_chuc_vu)
			VALUES (?)
			""";

	private static final String UPDATE_MYSQL = """
			UPDATE chuc_vu
			SET ten_chuc_vu = ?
			WHERE ma_chuc_vu = ?
			""";

	private static final String DELETE_MYSQL = """
			DELETE FROM chuc_vu
			WHERE ma_chuc_vu = ?
			""";

	private static final String SELECT_BY_ID_MYSQL = """
			SELECT ma_chuc_vu,
				   ten_chuc_vu,
				   created_at,
				   updated_at
			FROM chuc_vu
			WHERE ma_chuc_vu = ?
			""";

	private static final String SELECT_ALL_MYSQL = """
			SELECT ma_chuc_vu,
				   ten_chuc_vu,
				   created_at,
				   updated_at
			FROM chuc_vu
			ORDER BY ten_chuc_vu ASC, ma_chuc_vu ASC
			""";

	private static final String SEARCH_BY_NAME_MYSQL = """
			SELECT ma_chuc_vu,
				   ten_chuc_vu,
				   created_at,
				   updated_at
			FROM chuc_vu
			WHERE ten_chuc_vu LIKE ?
			ORDER BY ten_chuc_vu ASC, ma_chuc_vu ASC
			""";

	private static final String EXISTS_BY_NAME_MYSQL = """
			SELECT 1
			FROM chuc_vu
			WHERE ten_chuc_vu = ?
			LIMIT 1
			""";

	private static final String EXISTS_BY_NAME_EXCEPT_ID_MYSQL = """
			SELECT 1
			FROM chuc_vu
			WHERE ten_chuc_vu = ?
			  AND ma_chuc_vu <> ?
			LIMIT 1
			""";

	private static final String IS_USED_IN_EMPLOYEE_MYSQL = """
			SELECT 1
			FROM nhan_vien
			WHERE ma_chuc_vu = ?
			LIMIT 1
			""";

	/**
	 * Kiểm tra tên chức vụ đã tồn tại hay chưa.
	 * 
	 * @param jobTitleName - Tên chức vụ
	 * @return true nếu đã tồn tại
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean existsByName(String jobTitleName) throws SQLException {
		if (jobTitleName == null || jobTitleName.trim().isEmpty()) {
			return false;
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(EXISTS_BY_NAME_MYSQL)) {

			ps.setString(1, jobTitleName.trim());

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	/**
	 * Kiểm tra tên chức vụ đã tồn tại ở bản ghi khác hay chưa.
	 * 
	 * @param jobTitleName - Tên chức vụ
	 * @param jobTitleId - Mã chức vụ
	 * @return true nếu đã tồn tại ở bản ghi khác
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean existsByNameExceptId(String jobTitleName, int jobTitleId) throws SQLException {
		if (jobTitleId <= 0 || jobTitleName == null || jobTitleName.trim().isEmpty()) {
			throw new IllegalArgumentException("jobTitleId phải lớn hơn 0 và jobTitleName không được để trống!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(EXISTS_BY_NAME_EXCEPT_ID_MYSQL)) {

			ps.setString(1, jobTitleName.trim());
			ps.setInt(2, jobTitleId);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	/**
	 * Kiểm tra chức vụ có đang được sử dụng trong bảng nhân viên hay không.
	 * 
	 * @param jobTitleId - Mã chức vụ
	 * @return true nếu đang được sử dụng
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean isUsedInEmployee(int jobTitleId) throws SQLException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(IS_USED_IN_EMPLOYEE_MYSQL)) {

			ps.setInt(1, jobTitleId);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	/**
	 * Kiểm tra chức vụ có đang được sử dụng ở các bảng liên quan hay không.
	 * 
	 * @param jobTitleId - Mã chức vụ
	 * @return true nếu đang được sử dụng
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean isJobTitleUsed(int jobTitleId) throws SQLException {
		if (jobTitleId <= 0) {
			throw new IllegalArgumentException("jobTitleId phải lớn hơn 0!");
		}

		return isUsedInEmployee(jobTitleId);
	}

	/**
	 * Ánh xạ một dòng dữ liệu ResultSet thành đối tượng JobTitle.
	 * 
	 * @param rs - ResultSet đang trỏ tới dòng dữ liệu hợp lệ
	 * @return đối tượng JobTitle
	 * @throws SQLException nếu có lỗi SQL
	 */
	private JobTitle mapResultSetToJobTitle(ResultSet rs) throws SQLException {
		Timestamp createdAt = rs.getTimestamp("created_at");
		Timestamp updatedAt = rs.getTimestamp("updated_at");

		return new JobTitle(
				rs.getInt("ma_chuc_vu"),
				rs.getString("ten_chuc_vu"),
				createdAt != null ? createdAt.toLocalDateTime() : null,
				updatedAt != null ? updatedAt.toLocalDateTime() : null
		);
	}

	/**
	 * Thêm chức vụ mới.
	 * 
	 * @param jobTitle - Đối tượng chức vụ
	 * @return true nếu thêm thành công
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean addJobTitle(JobTitle jobTitle) throws SQLException {
		// JobTitleValidator.validateForCreate(jobTitle);

		if (jobTitle == null) {
			throw new IllegalArgumentException("jobTitle không được null!");
		}

		if (existsByName(jobTitle.getJobTitleName())) {
			throw new IllegalArgumentException("Chức vụ đã tồn tại!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(INSERT_MYSQL)) {

			ps.setString(1, jobTitle.getJobTitleName().trim());

			return ps.executeUpdate() > 0;
		}
	}

	/**
	 * Cập nhật thông tin chức vụ.
	 * 
	 * @param jobTitle - Đối tượng chức vụ cần cập nhật
	 * @return true nếu cập nhật thành công
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean updateJobTitle(JobTitle jobTitle) throws SQLException {
		// JobTitleValidator.validateForUpdate(jobTitle);

		if (jobTitle == null) {
			throw new IllegalArgumentException("jobTitle không được null!");
		}

		if (jobTitle.getJobTitleId() <= 0) {
			throw new IllegalArgumentException("jobTitleId phải lớn hơn 0!");
		}

		if (existsByNameExceptId(jobTitle.getJobTitleName(), jobTitle.getJobTitleId())) {
			throw new IllegalArgumentException("Chức vụ đã tồn tại ở bản ghi khác!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(UPDATE_MYSQL)) {

			ps.setString(1, jobTitle.getJobTitleName().trim());
			ps.setInt(2, jobTitle.getJobTitleId());

			return ps.executeUpdate() > 0;
		}
	}

	/**
	 * Xóa chức vụ theo mã.
	 * Không cho xóa nếu chức vụ đang được sử dụng ở bảng nhan_vien.
	 * 
	 * @param jobTitleId - Mã chức vụ
	 * @return true nếu xóa thành công
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean deleteJobTitleById(int jobTitleId) throws SQLException {
		if (jobTitleId <= 0) {
			throw new IllegalArgumentException("jobTitleId phải lớn hơn 0!");
		}

		if (isJobTitleUsed(jobTitleId)) {
			throw new IllegalArgumentException("Chức vụ đang được gán cho nhân viên, không thể xóa!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(DELETE_MYSQL)) {

			ps.setInt(1, jobTitleId);

			return ps.executeUpdate() > 0;
		}
	}

	/**
	 * Tìm chức vụ theo mã.
	 * 
	 * @param jobTitleId - Mã chức vụ
	 * @return đối tượng JobTitle nếu tìm thấy, ngược lại trả về null
	 * @throws SQLException nếu có lỗi SQL
	 */
	public JobTitle findById(int jobTitleId) throws SQLException {
		if (jobTitleId <= 0) {
			throw new IllegalArgumentException("jobTitleId phải lớn hơn 0!");
		}

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID_MYSQL)) {

			ps.setInt(1, jobTitleId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mapResultSetToJobTitle(rs);
				}
			}
		}

		return null;
	}

	/**
	 * Lấy tất cả chức vụ.
	 * 
	 * @return danh sách chức vụ
	 * @throws SQLException nếu có lỗi SQL
	 */
	public List<JobTitle> getAllJobTitles() throws SQLException {
		List<JobTitle> jobTitles = new ArrayList<>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_ALL_MYSQL);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				jobTitles.add(mapResultSetToJobTitle(rs));
			}
		}

		return jobTitles;
	}

	/**
	 * Tìm kiếm chức vụ theo tên gần đúng.
	 * 
	 * @param keyword - Từ khóa tên chức vụ
	 * @return danh sách chức vụ phù hợp
	 * @throws SQLException nếu có lỗi SQL
	 */
	public List<JobTitle> searchByName(String keyword) throws SQLException {
		List<JobTitle> jobTitles = new ArrayList<>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SEARCH_BY_NAME_MYSQL)) {

			keyword = (keyword == null) ? "" : keyword.trim();
			ps.setString(1, "%" + keyword + "%");

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					jobTitles.add(mapResultSetToJobTitle(rs));
				}
			}
		}

		return jobTitles;
	}
}