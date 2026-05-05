package com.cinema.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.cinema.config.DBConnection;
import com.cinema.entity.Employee;
import com.cinema.entity.JobTitle;
import com.cinema.enums.EmployeeGender;
import com.cinema.enums.EmployeeStatus;
import com.cinema.validator.EmployeeValidator;

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
		    SELECT nv.ma_nhan_vien,
		           nv.ten_nhan_vien,
		           nv.gioi_tinh,
		           nv.so_dien_thoai,
		           nv.ngay_sinh,
		           nv.email,
		           nv.ngay_vao_lam,
		           nv.ma_chuc_vu,
		           cv.ten_chuc_vu,   -- 🔥 thêm dòng này
		           nv.trang_thai,
		           nv.created_at,
		           nv.updated_at
		    FROM nhan_vien nv
		    LEFT JOIN chuc_vu cv ON nv.ma_chuc_vu = cv.ma_chuc_vu
		    WHERE nv.ma_nhan_vien = ?
		    """;
	
	private static final String SELECT_ALL_MYSQL = """
		    SELECT nv.ma_nhan_vien,
		           nv.ten_nhan_vien,
		           nv.gioi_tinh,
		           nv.so_dien_thoai,
		           nv.ngay_sinh,
		           nv.email,
		           nv.ngay_vao_lam,
		           nv.ma_chuc_vu,
		           cv.ten_chuc_vu,
		           nv.trang_thai,
		           nv.created_at,
		           nv.updated_at
		    FROM nhan_vien nv
		    LEFT JOIN chuc_vu cv ON nv.ma_chuc_vu = cv.ma_chuc_vu
		    ORDER BY nv.ten_nhan_vien ASC, nv.ma_nhan_vien ASC
		    """;
	
	private static final String SEARCH_BY_NAME_MYSQL = """
		    SELECT nv.ma_nhan_vien,
		           nv.ten_nhan_vien,
		           nv.gioi_tinh,
		           nv.so_dien_thoai,
		           nv.ngay_sinh,
		           nv.email,
		           nv.ngay_vao_lam,
		           nv.ma_chuc_vu,
		           cv.ten_chuc_vu,
		           nv.trang_thai,
		           nv.created_at,
		           nv.updated_at
		    FROM nhan_vien nv
		    LEFT JOIN chuc_vu cv ON nv.ma_chuc_vu = cv.ma_chuc_vu
		    WHERE nv.ten_nhan_vien LIKE ?
		    ORDER BY nv.ten_nhan_vien ASC, nv.ma_nhan_vien ASC
		    """;
	
	private static final String  EXISTS_BY_PHONE_MYSQL = """
			SELECT 1
			FROM nhan_vien
			WHERE so_dien_thoai = ?
			LIMIT 1
			""";
	
	private static final String EXISTS_BY_PHONE_EXCEPT_ID_MYSQL = """
			SELECT 1
			FROM nhan_vien
			WHERE so_dien_thoai = ?
				AND ma_nhan_vien <> ?
			LIMIT 1
			""";
	
	private static final String EXISTS_BY_EMAIL_MYSQL = """
			SELECT 1
			FROM nhan_vien
			WHERE email = ?
			LIMIT 1
			""";
	
	private static final String EXISTS_BY_EMAIL_EXCEPT_ID_MYSQL = """
			SELECT 1
			FROM nhan_vien
			WHERE email = ?
				AND ma_nhan_vien <> ?
			LIMIT 1
			""";
	
	private static final String IS_USED_IN_INVOICE_MYSQL = """
			SELECT 1
			FROM hoa_don
			WHERE ma_nhan_vien = ?
			LIMIT 1
			""";
	
	private static final String IS_USED_IN_EMPLOYEE_ACCOUNT_MYSQL = """
			SELECT 1
			FROM tai_khoan
			WHERE ma_nhan_vien = ?
			LIMIT 1
			""";

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
				PreparedStatement ps = connection.prepareStatement(EXISTS_BY_EMAIL_MYSQL)){
			
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
		if (email == null || email.trim().isEmpty()) {
			return false;
		}
		
		if (employeeId <= 0) {
			throw new IllegalArgumentException("employeeId phải lớn hơn 0!");
		}
		
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(EXISTS_BY_EMAIL_EXCEPT_ID_MYSQL)){
			
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
	private boolean existsByPhone (String phone) throws SQLException { //TODO: nhớ regex cho sđt
		if (phone == null || phone.trim().isEmpty()) {
			return false;
		}
		
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(EXISTS_BY_PHONE_MYSQL)){
			
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
	private boolean existsByPhoneExceptId (String phone, int employeeId) throws SQLException { //TODO: nhớ regex cho sđt
		if (phone == null || phone.trim().isEmpty()) {
			return false;
		}
		
		if (employeeId <= 0) {
			throw new IllegalArgumentException("employeeId phải lớn hơn 0!");
		}
		
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(EXISTS_BY_PHONE_EXCEPT_ID_MYSQL)){
			
			ps.setString(1, phone.trim());
			ps.setInt(2, employeeId);
			
			try (ResultSet rs = ps.executeQuery()){
				return rs.next();
			}
		}
	}
	
	/**
	 * Kiểm tra liệu nhân viên có đang được sử dụng ở bảng Invoice hay không
	 * 
	 * @param employeeId - Mã nhân viên
	 * @return true nếu đang được sử dụng
	 * @throws SQLException nếu có lỗi SQl
	 */
	private boolean isUsedInInvoice (int employeeId) throws SQLException{
		
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(IS_USED_IN_INVOICE_MYSQL)){
			
			ps.setInt(1, employeeId);
			
			try (ResultSet rs = ps.executeQuery()){
				return rs.next();
			}
		}
	}
	
	/**
	 * Kiểm tra liệu nhân viên có đang được sử dụng ở bảng EmployeeAccount hay không
	 * 
	 * @param employeeId - Mã nhân viên
	 * @return true nếu đã tồn tại
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean isUsedInEmployeeAccount (int employeeId) throws SQLException{
		
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(IS_USED_IN_EMPLOYEE_ACCOUNT_MYSQL)){
			
			ps.setInt(1, employeeId);
			
			try (ResultSet rs = ps.executeQuery()){
				return rs.next();
			}
		}
	}
	
	/**
	 * Kiểm tra liệu nhân viên có đang được sử dụng ở bảng liên quan hay không
	 * 
	 * @param employeeId - Mã nhân viên
	 * @return true nếu đã tồn tại
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean isEmployeeUsed (int employeeId) throws SQLException{
		if (employeeId <= 0) {
			throw new IllegalArgumentException("employeeId phải lớn hơn 0!");
		}
		
		return isUsedInInvoice(employeeId) || isUsedInEmployeeAccount(employeeId);
	}
	
	/**
	 * Ánh xạ một dòng dữ liệu ReseultSet thành đối tượng Employee
	 * 
	 * @param rs ResultSet đang trỏ tới dòng dữ liệu hợp lệ
	 * @return đối tượng Employee
	 * @throws SQLException nếu có lỗi SQL
	 */
	private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
	    Timestamp createdAt = rs.getTimestamp("created_at");
	    Timestamp updatedAt = rs.getTimestamp("updated_at");

	    String phoneNumber = rs.getString("so_dien_thoai");
	    Date birthDate = rs.getDate("ngay_sinh");
	    String email = rs.getString("email");
	    Date hireDate = rs.getDate("ngay_vao_lam");

	    int genderValue = rs.getInt("gioi_tinh");
	    EmployeeGender gender = rs.wasNull() ? null : EmployeeGender.fromId(genderValue);

	    // 🔥 LẤY jobTitleName TRƯỚC
	    String jobTitleName = rs.getString("ten_chuc_vu");

	    JobTitle jobTitle = new JobTitle(
	        rs.getInt("ma_chuc_vu"),
	        jobTitleName
	    );

	    Employee employee = new Employee(
	        rs.getInt("ma_nhan_vien"),
	        rs.getString("ten_nhan_vien"),
	        gender,
	        phoneNumber,
	        birthDate != null ? birthDate.toLocalDate() : null,
	        email,
	        hireDate != null ? hireDate.toLocalDate() : null,
	        jobTitle,
	        EmployeeStatus.fromId(rs.getInt("trang_thai")),
	        createdAt != null ? createdAt.toLocalDateTime() : null,
	        updatedAt != null ? updatedAt.toLocalDateTime() : null
	    );

	    return employee;
	}
	
	/**
	 * Thêm nhân viên mới
	 * @param employee - Đối tượng nhân viên
	 * @return true nếu thêm thành công
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean addEmployee (Employee employee) throws SQLException{
		EmployeeValidator.validateForCreate(employee);
		
		if (existsByEmail(employee.getEmployeeEmail())) {
		    throw new IllegalArgumentException("Email thuộc về nhân viên khác!");
		}

		if (existsByPhone(employee.getEmployeePhoneNumber())) {
		    throw new IllegalArgumentException("Số điện thoại thuộc về nhân viên khác!");
		}
		
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(INSERT_MYSQL)){
			
			ps.setString(1, employee.getEmployeeName().trim());

			if (employee.getEmployeeGender() != null) {
			    ps.setInt(2, employee.getEmployeeGender().getGenderId());
			} else {
			    ps.setNull(2, java.sql.Types.BIT);
			}

			ps.setString(3, employee.getEmployeePhoneNumber() != null ? employee.getEmployeePhoneNumber().trim() : null);

			if (employee.getBirthDate() != null) {
			    ps.setDate(4, Date.valueOf(employee.getBirthDate()));
			} else {
			    ps.setDate(4, null);
			}

			ps.setString(5, employee.getEmployeeEmail() != null ? employee.getEmployeeEmail().trim() : null);

			if (employee.getHireDate() != null) {
			    ps.setDate(6, Date.valueOf(employee.getHireDate()));
			} else {
			    ps.setDate(6, null);
			}

			ps.setInt(7, employee.getJobTitle().getJobTitleId());
			ps.setInt(8, employee.getEmployeeStatus().getEmployeeStatusId());
			
			return ps.executeUpdate() > 0;
		}
	}
	
	/**
	 * Cập nhật thông tin nhân viên
	 * 
	 * @param employee - Nhân viên cần cập nhật
	 * @return true nếu cập nhật thành công
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean updateEmployee (Employee employee) throws SQLException{
		EmployeeValidator.validateForUpdate(employee);
		
		if (employee.getEmployeeId() <= 0) {
			throw new IllegalArgumentException("employeeId phải lớn hơn 0!");
		}
		
		if (existsByEmailExceptId(employee.getEmployeeEmail(), employee.getEmployeeId())) {
			throw new IllegalArgumentException("Nhân viên đã tồn tại email này!");
		}
		
		if (existsByPhoneExceptId(employee.getEmployeePhoneNumber(), employee.getEmployeeId())) {
			throw new IllegalArgumentException("Nhân viên đã tồn tại số điện thoại này!");
		}
		
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(UPDATE_MYSQL)){
			
			ps.setString(1, employee.getEmployeeName().trim());

			if (employee.getEmployeeGender() != null) {
			    ps.setInt(2, employee.getEmployeeGender().getGenderId());
			} else {
			    ps.setNull(2, java.sql.Types.BIT);
			}

			ps.setString(3, employee.getEmployeePhoneNumber() != null ? employee.getEmployeePhoneNumber().trim() : null);

			if (employee.getBirthDate() != null) {
			    ps.setDate(4, Date.valueOf(employee.getBirthDate()));
			} else {
			    ps.setDate(4, null);
			}

			ps.setString(5, employee.getEmployeeEmail() != null ? employee.getEmployeeEmail().trim() : null);

			if (employee.getHireDate() != null) {
			    ps.setDate(6, Date.valueOf(employee.getHireDate()));
			} else {
			    ps.setDate(6, null);
			}

			ps.setInt(7, employee.getJobTitle().getJobTitleId());
			ps.setInt(8, employee.getEmployeeStatus().getEmployeeStatusId());
			ps.setInt(9, employee.getEmployeeId());
			
			return ps.executeUpdate() > 0;
		}
	}
	
	/**
	 * Xóa nhân viên theo mã
	 * 
	 * @param employeeId - Mã nhân viên
	 * @return true nếu xóa thành công
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean deleteEmployeeById (int employeeId) throws SQLException{
		if (employeeId <= 0) {
			throw new IllegalArgumentException("employeeId phải lớn hơn 0!");
		}
		
		if (isEmployeeUsed(employeeId)) {
			throw new IllegalArgumentException("Nhân viên đang được sử dụng không thể xóa!");
		}
		
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(DELETE_MYSQL)){
			
			ps.setInt(1, employeeId);
			
			return ps.executeUpdate() > 0;
		}
	}
	
	/**
	 * Tìm nhân viên theo mã
	 * 
	 * @param employeeId - Mã nhân viên
	 * @return đối tượng Employee được tìm thấy, ngược lại trả về null
	 * @throws SQLException nếu có lỗi SQLs
	 */
	public Employee findById (int employeeId) throws SQLException{
		if (employeeId <= 0) {
			throw new IllegalArgumentException("employeeId phải lớn hơn 0!");
		}
		
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID_MYSQL)){
			
			ps.setInt(1, employeeId);
			
			try (ResultSet rs = ps.executeQuery()){
				if (rs.next()) {
					return mapResultSetToEmployee(rs);
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Tìm nhân viên theo tên gần đúng
	 * 
	 * @param keyword - Từ khóa nhân viên
	 * @return danh sách nhân viên phù hợp
	 * @throws SQLException nếu có lỗi SQL
	 */
	public List<Employee> searchByName(String keyword) throws SQLException{
    	List<Employee> employees = new ArrayList<Employee>();
    	
    	try (Connection connection = DBConnection.getConnection();
    			PreparedStatement ps = connection.prepareStatement(SEARCH_BY_NAME_MYSQL)){
    		
    		keyword = (keyword == null) ? "" : keyword.trim();
    		ps.setString(1, "%" + keyword + "%");
    		
    		try(ResultSet rs = ps.executeQuery()){
    			while (rs.next()) {
    				employees.add(mapResultSetToEmployee(rs));
    			}
    		}
    	}
    	
    	return employees;
	}
	
	/**
	 * Lấy tất cả nhân viên
	 * 
	 * @return danh sách nhân viên
	 * @throws SQLException nếu có lỗi SQL
	 */
	public List<Employee> getAllEmployees() throws SQLException{
		List<Employee> employees = new ArrayList<>();
		
    	try(Connection connection = DBConnection.getConnection();
    			PreparedStatement ps = connection.prepareStatement(SELECT_ALL_MYSQL);
    			ResultSet rs = ps.executeQuery()){
    		while (rs.next()) {
    			employees.add(mapResultSetToEmployee(rs));
    		}
    	}
    	
    	return employees;
	}
} 
