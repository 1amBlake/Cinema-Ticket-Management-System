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
 * @author Quốc Anh
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
			SELECT ma_chuc_vu, ten_chuc_vu, created_at, updated_at
			FROM chuc_vu
			WHERE ma_chuc_vu = ?
			""";

	private static final String SELECT_ALL_MYSQL = """
			SELECT ma_chuc_vu, ten_chuc_vu, created_at, updated_at
			FROM chuc_vu
			ORDER BY ten_chuc_vu ASC
			""";

	private static final String SEARCH_BY_NAME_MYSQL = """
			SELECT ma_chuc_vu, ten_chuc_vu, created_at, updated_at
			FROM chuc_vu
			WHERE ten_chuc_vu LIKE ?
			ORDER BY ten_chuc_vu ASC
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
			WHERE ten_chuc_vu = ? AND ma_chuc_vu <> ?
			LIMIT 1
			""";

	/**
	 * Thêm chức vụ mới
	 * 
	 * @param jobTitle đối tượng cần thêm
	 * @return true nếu thêm thành công
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean addJobTitle(JobTitle jobTitle) throws SQLException {
		validateJobTitle(jobTitle);

		if (existsByName(jobTitle.getJobTitleName())) {
			throw new IllegalArgumentException("Tên chức vụ đã tồn tại!");
		}

		try (
			Connection con = DBConnection.getConnection();
			PreparedStatement ps = con.prepareStatement(INSERT_MYSQL)
		) {
			ps.setString(1, jobTitle.getJobTitleName());
			return ps.executeUpdate() > 0;
		}
	}

	/**
	 * Cập nhật chức vụ
	 * 
	 * @param jobTitle dữ liệu cập nhật
	 * @return true nếu cập nhật thành công
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean updateJobTitle(JobTitle jobTitle) throws SQLException {
		validateJobTitle(jobTitle);

		if (jobTitle.getJobTitleId() <= 0) {
			throw new IllegalArgumentException("jobTitleId phải lớn hơn 0!");
		}

		if (existsByNameExceptId(
				jobTitle.getJobTitleName(),
				jobTitle.getJobTitleId())) {
			throw new IllegalArgumentException("Tên chức vụ đã tồn tại!");
		}

		try (
			Connection con = DBConnection.getConnection();
			PreparedStatement ps = con.prepareStatement(UPDATE_MYSQL)
		) {
			ps.setString(1, jobTitle.getJobTitleName());
			ps.setInt(2, jobTitle.getJobTitleId());

			return ps.executeUpdate() > 0;
		}
	}

	/**
	 * Xóa chức vụ theo mã
	 * 
	 * @param jobTitleId mã chức vụ
	 * @return true nếu xóa thành công
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean deleteJobTitleById(int jobTitleId) throws SQLException {
		if (jobTitleId <= 0) {
			throw new IllegalArgumentException("jobTitleId phải lớn hơn 0!");
		}

		try (
			Connection con = DBConnection.getConnection();
			PreparedStatement ps = con.prepareStatement(DELETE_MYSQL)
		) {
			ps.setInt(1, jobTitleId);
			return ps.executeUpdate() > 0;
		}
	}

	/**
	 * Tìm chức vụ theo mã
	 * 
	 * @param jobTitleId mã chức vụ
	 * @return JobTitle hoặc null
	 * @throws SQLException nếu có lỗi SQL
	 */
	public JobTitle findById(int jobTitleId) throws SQLException {
		if (jobTitleId <= 0) {
			throw new IllegalArgumentException("jobTitleId phải lớn hơn 0!");
		}

		try (
			Connection con = DBConnection.getConnection();
			PreparedStatement ps = con.prepareStatement(SELECT_BY_ID_MYSQL)
		) {
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
	 * Lấy tất cả chức vụ
	 * 
	 * @return danh sách chức vụ
	 * @throws SQLException nếu có lỗi SQL
	 */
	public List<JobTitle> getAllJobTitles() throws SQLException {
		List<JobTitle> list = new ArrayList<>();

		try (
			Connection con = DBConnection.getConnection();
			PreparedStatement ps = con.prepareStatement(SELECT_ALL_MYSQL);
			ResultSet rs = ps.executeQuery()
		) {
			while (rs.next()) {
				list.add(mapResultSetToJobTitle(rs));
			}
		}

		return list;
	}

	/**
	 * Tìm kiếm theo tên gần đúng
	 * 
	 * @param keyword từ khóa
	 * @return danh sách phù hợp
	 * @throws SQLException nếu có lỗi SQL
	 */
	public List<JobTitle> searchByName(String keyword) throws SQLException {
		List<JobTitle> list = new ArrayList<>();

		if (keyword == null) {
			keyword = "";
		}

		try (
			Connection con = DBConnection.getConnection();
			PreparedStatement ps = con.prepareStatement(SEARCH_BY_NAME_MYSQL)
		) {
			ps.setString(1, "%" + keyword.trim() + "%");

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					list.add(mapResultSetToJobTitle(rs));
				}
			}
		}

		return list;
	}

	/**
	 * Kiểm tra tên chức vụ đã tồn tại
	 */
	public boolean existsByName(String jobTitleName) throws SQLException {
		if (jobTitleName == null || jobTitleName.trim().isEmpty()) {
			return false;
		}

		try (
			Connection con = DBConnection.getConnection();
			PreparedStatement ps = con.prepareStatement(EXISTS_BY_NAME_MYSQL)
		) {
			ps.setString(1, jobTitleName.trim());

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	/**
	 * Kiểm tra tên đã tồn tại ở record khác
	 */
	public boolean existsByNameExceptId(String jobTitleName, int jobTitleId)
			throws SQLException {

		try (
			Connection con = DBConnection.getConnection();
			PreparedStatement ps = con.prepareStatement(EXISTS_BY_NAME_EXCEPT_ID_MYSQL)
		) {
			ps.setString(1, jobTitleName.trim());
			ps.setInt(2, jobTitleId);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	/**
	 * Ánh xạ ResultSet -> JobTitle
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
	 * Kiểm tra dữ liệu đầu vào
	 */
	private void validateJobTitle(JobTitle jobTitle) {
		if (jobTitle == null) {
			throw new IllegalArgumentException("JobTitle không được null!");
		}

		if (jobTitle.getJobTitleName() == null
				|| jobTitle.getJobTitleName().trim().isEmpty()) {
			throw new IllegalArgumentException("Tên chức vụ không được để trống!");
		}
	}
}