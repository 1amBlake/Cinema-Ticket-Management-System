package com.cinema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cinema.config.DBConnection;
import com.cinema.entity.Employee;

/**
 * DAO cho thực thể Employee
 * Chịu trách nhiệm thao tác dữ liệu với bảng nhân viên trong MySQL
 * 
 * Bảng ánh xạ:
 * nhan_vien(
 * 	ma_nhan_vien,
 * 	ten_nhan_vien,
 * 	gioi_tinh,
 * 	so_dien_thoai,
 * 	ngay_sinh,
 * 	email,
 * 	ngay_vao_lam,
 * 	ma_chuc_vu,
 * 	trang_thai,
 * 	created_at,
 * 	updated_at
 * )
 * 
 * @author Minh Huy (chính)
 */
public class EmployeeDao {
	private static final String INSERT_MYSQL = """
			INSERT INTO nhan_vien (
				ten_nhan_vien,
				gioi_tinh,
				so_dien_thoai,
				ngay_sinh,
				email,
				ngay_vao_lam,
				ma_chuc_vu,
				trang_thai
			)
			VALUES (?, ?, ?, ?, ?, ?, ?, ?)
			""";
	private static final String UPDATE_MYSQL = """
			UPDATE nhan_vien
			SET ten_nhan_vien = ?,
				gioi_tinh = ?,
				so_dien_thoai = ?,
				ngay_sinh = ?,
				email = ?,
				ngay_vao_lam = ?,
				ma_chuc_vu = ?,
				trang_thai = ?
			WHERE ma_nhan_vien = ?
			""";
	
	private static final String DELETE_MYSQL = """
			DELETE FROM nhan_vien
			WHERE ma_nhan_vien = ?
			""";
	
	private static final String SELECT_BY_ID_MYSQL = """
			SELECT ma_nhan_vien,
				ten_nhan_vien,
				gioi_tinh,
				so_dien_thoai,
				ngay_sinh,
				email,
				ngay_vao_lam,
				ma_chuc_vu,
				trang_thai,
				created_at,
				updated_at
			FROM nhan_vien
			WHERE ma_nhan_vien = ?
			""";
	
	private static final String SELECT_ALL_MYSQL = """
			SELECT ma_nhan_vien,
				ten_nhan_vien,
				gioi_tinh,
				so_dien_thoai,
				ngay_sinh,
				email,
				ngay_vao_lam,
				ma_chuc_vu,
				trang_thai,
				created_at,
				updated_at
			FROM nhan_vien
			ORDER BY ten_nhan_vien ASC
			""";
	
	private static final String SEARCH_BY_NAME_MYSQL = """
			SELECT ma_nhan_vien,
				ten_nhan_vien,
				gioi_tinh,
				so_dien_thoai,
				ngay_sinh,
				email,
				ngay_vao_lam,
				ma_chuc_vu,
				trang_thai,
				created_at,
				updated_at
			FROM nhan_vien
			WHERE ten_nhan_vien LIKE ?
			ORDER BY ten_nhan_vien ASC
			""";
	
	private static final String  EXISTS_BY_PHONE = """
			SELECT 1
			FROM nhan_vien
			WHERE so_dien_thoai = ?
			LIMIT 1
			""";
	
	private static final String EXISTS_BY_PHONE_EXCEPT_ID = """
			SELECT 1
			FROM nhan_vien
			WHERE so_dien_thoai = ?
				AND ma_nhan_vien <> ?
			LIMIT 1
			""";
	
	private static final String EXISTS_BY_EMAIL = """
			SELECT 1
			FROM nhan_vien
			WHERE email = ?
			LIMIT 1
			""";
	
	private static final String EXISTS_BY_EMAIL_EXCEPT_ID = """
			SELECT 1
			FROM nhan_vien
			WHERE email = ?
				AND ma_nhan_vien <> ?
			LIMIT 1
			""";
	
	private static final String IN_USED_INVOICE_MYSQL = """
			SELECT 1
			FROM hoa_don
			WHERE ma_nhan_vien = ?
			LIMIT 1
			""";
	
	private static final String IN_USED_EMP_ACCOUNT_MYSQL = """
			SELECT 1
			FROM tai_khoan
			WHERE ma_nhan_vien = ?
			""";
	
	/**
	 * Kiểm tra dữ liệu đầu vào của Employee
	 * 
	 * @param empl - Đối tượng Employee để kiểm tra
	 */
	private void validateEmployee (Employee empl) { //TODO: làm validate internal và package

	}
	
	/**
	 * Kiểm tra email của nhân viên đã tồn tại trong hệ thống chưa
	 * 
	 * @param email - Email
	 * @return true nếu đã tồn tại
	 * @throws SQLException
	 */
	private boolean existsByEmail (String email) throws SQLException {
		if (email == null || email.trim().isEmpty()) {
			return false;
		}
		
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(EXISTS_BY_EMAIL)){
			
			ps.setString(1, email.trim());
			
			try (ResultSet rs = ps.executeQuery()){
				return rs.next();
			}
		}
	}
	
	/**
	 * Kiểm tra email của nhân viên đã tổn tại ngoại trừ id này hay không
	 * 
	 * @param email - Email
	 * @param employeeId - Mã nhân viên
	 * @return true nếu đã tồn tại
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean existsByEmailExceptId (String email, int employeeId) throws SQLException {
		if (email == null || email.trim().isEmpty() || employeeId <= 0) {
			return false;
		}
		
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(EXISTS_BY_EMAIL_EXCEPT_ID)){
			
			ps.setString(1, email.trim());
			ps.setInt(2, employeeId);
			
			try (ResultSet rs = ps.executeQuery()){
				return rs.next();
			}
		}
	}
	
	/**
	 * Kiểm tra số điện thoại của nhân viên đã tổn tại trong hệ thống hay chưa
	 * 
	 * @param phone - Số điện thoại
	 * @return true nếu đã tồn tại
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean existsByPhone (String phone) throws SQLException {
		if (phone == null || phone.trim().isEmpty()) {
			return false;
		}
		
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(EXISTS_BY_PHONE)){
			
			ps.setString(1, phone.trim());
			
			try (ResultSet rs = ps.executeQuery()){
				return rs.next();
			}
		}
	}
	
	/**
	 * Kiểm tra số điện thoại của nhân viên đã tổn tại ngoại trừ id này hay không
	 * 
	 * @param phone - Số điện thoại
	 * @param employeeId - Mã nhân viên
	 * @return true nếu đã tồn tại
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean existsByPhoneExceptId (String phone, int employeeId) throws SQLException {
		if (phone == null || phone.trim().isEmpty() || employeeId <= 0) {
			return false;
		}
		
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(EXISTS_BY_PHONE_EXCEPT_ID)){
			
			ps.setString(1, phone.trim());
			ps.setInt(2, employeeId);
			
			try (ResultSet rs = ps.executeQuery()){
				return rs.next();
			}
		}
	}
	
	private boolean isUsedInInvoice (int employeeId) {
		return false;
	}
}
