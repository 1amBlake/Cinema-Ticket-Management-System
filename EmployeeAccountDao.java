package com.cinema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.cinema.config.DBConnection;
import com.cinema.entity.Employee;
import com.cinema.entity.EmployeeAccount;
import com.cinema.enums.AccountStatus;

/**
 * DAO cho thực thể EmployeeAccount
 * Chịu trách nhiệm thao tác dữ liệu với bảng tai_khoan trong MySQL.
 *
 * Bảng ánh xạ:
 * tai_khoan(
 *   ma_tai_khoan,
 *   ma_nhan_vien,
 *   ten_tai_khoan,
 *   mat_khau,
 *   trang_thai,
 *   created_at,
 *   updated_at
 * )
 *
 * @author Quốc Anh
 */
public class EmployeeAccountDao {

    private static final String INSERT_MYSQL = """
            INSERT INTO tai_khoan
            (ma_nhan_vien, ten_tai_khoan, mat_khau, trang_thai)
            VALUES (?, ?, ?, ?)
            """;

    private static final String UPDATE_MYSQL = """
            UPDATE tai_khoan
            SET ma_nhan_vien = ?,
                ten_tai_khoan = ?,
                mat_khau = ?,
                trang_thai = ?
            WHERE ma_tai_khoan = ?
            """;

    private static final String DELETE_MYSQL = """
            DELETE FROM tai_khoan
            WHERE ma_tai_khoan = ?
            """;

    private static final String SELECT_BY_ID_MYSQL = """
            SELECT ma_tai_khoan, ma_nhan_vien, ten_tai_khoan,
                   mat_khau, trang_thai, created_at, updated_at
            FROM tai_khoan
            WHERE ma_tai_khoan = ?
            """;

    private static final String SELECT_ALL_MYSQL = """
            SELECT ma_tai_khoan, ma_nhan_vien, ten_tai_khoan,
                   mat_khau, trang_thai, created_at, updated_at
            FROM tai_khoan
            ORDER BY ten_tai_khoan ASC
            """;

    private static final String SEARCH_BY_USERNAME_MYSQL = """
            SELECT ma_tai_khoan, ma_nhan_vien, ten_tai_khoan,
                   mat_khau, trang_thai, created_at, updated_at
            FROM tai_khoan
            WHERE ten_tai_khoan LIKE ?
            ORDER BY ten_tai_khoan ASC
            """;

    private static final String EXISTS_USERNAME_MYSQL = """
            SELECT 1
            FROM tai_khoan
            WHERE ten_tai_khoan = ?
            LIMIT 1
            """;

    private static final String EXISTS_USERNAME_EXCEPT_ID_MYSQL = """
            SELECT 1
            FROM tai_khoan
            WHERE ten_tai_khoan = ?
              AND ma_tai_khoan <> ?
            LIMIT 1
            """;

    private static final String EXISTS_EMPLOYEE_ID_MYSQL = """
            SELECT 1
            FROM tai_khoan
            WHERE ma_nhan_vien = ?
            LIMIT 1
            """;

    private static final String EXISTS_EMPLOYEE_ID_EXCEPT_ID_MYSQL = """
            SELECT 1
            FROM tai_khoan
            WHERE ma_nhan_vien = ?
              AND ma_tai_khoan <> ?
            LIMIT 1
            """;

    private static final String LOGIN_MYSQL = """
            SELECT ma_tai_khoan, ma_nhan_vien, ten_tai_khoan,
                   mat_khau, trang_thai, created_at, updated_at
            FROM tai_khoan
            WHERE ten_tai_khoan = ?
              AND mat_khau = ?
              AND trang_thai = 1
            LIMIT 1
            """;

    /**
     * Thêm tài khoản mới
     */
    public boolean addAccount(EmployeeAccount account) throws SQLException {
        validateAccount(account);

        if (existsByUsername(account.getAccountName())) {
            throw new IllegalArgumentException("Tên tài khoản đã tồn tại!");
        }

        if (existsByEmployeeId(account.getEmployee().getEmployeeId())) {
            throw new IllegalArgumentException("Nhân viên này đã có tài khoản!");
        }

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(INSERT_MYSQL)
        ) {
            ps.setInt(1, account.getEmployee().getEmployeeId());
            ps.setString(2, account.getAccountName());
            ps.setString(3, account.getAccountPassword());
            ps.setInt(4, account.getAccountStatus().getAccountStatusId());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Cập nhật tài khoản
     */
    public boolean updateAccount(EmployeeAccount account) throws SQLException {
        validateAccount(account);

        if (account.getAccountId() <= 0) {
            throw new IllegalArgumentException("Mã tài khoản không hợp lệ!");
        }

        if (existsByUsernameExceptId(
                account.getAccountName(),
                account.getAccountId())) {
            throw new IllegalArgumentException("Tên tài khoản đã tồn tại!");
        }

        if (existsByEmployeeIdExceptId(
                account.getEmployee().getEmployeeId(),
                account.getAccountId())) {
            throw new IllegalArgumentException("Nhân viên này đã có tài khoản!");
        }

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(UPDATE_MYSQL)
        ) {
            ps.setInt(1, account.getEmployee().getEmployeeId());
            ps.setString(2, account.getAccountName());
            ps.setString(3, account.getAccountPassword());
            ps.setInt(4, account.getAccountStatus().getAccountStatusId());
            ps.setInt(5, account.getAccountId());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Xóa tài khoản theo mã
     */
    public boolean deleteAccountById(int accountId) throws SQLException {
        if (accountId <= 0) {
            throw new IllegalArgumentException("Mã tài khoản không hợp lệ!");
        }

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DELETE_MYSQL)
        ) {
            ps.setInt(1, accountId);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Tìm tài khoản theo mã
     */
    public EmployeeAccount findById(int accountId) throws SQLException {
        if (accountId <= 0) {
            throw new IllegalArgumentException("Mã tài khoản không hợp lệ!");
        }

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(SELECT_BY_ID_MYSQL)
        ) {
            ps.setInt(1, accountId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAccount(rs);
                }
            }
        }

        return null;
    }

    /**
     * Lấy toàn bộ tài khoản
     */
    public List<EmployeeAccount> getAllAccounts() throws SQLException {
        List<EmployeeAccount> list = new ArrayList<>();

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(SELECT_ALL_MYSQL);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                list.add(mapResultSetToAccount(rs));
            }
        }

        return list;
    }

    /**
     * Tìm kiếm theo tên tài khoản
     */
    public List<EmployeeAccount> searchByUsername(String keyword) throws SQLException {
        List<EmployeeAccount> list = new ArrayList<>();

        if (keyword == null) {
            keyword = "";
        }

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(SEARCH_BY_USERNAME_MYSQL)
        ) {
            ps.setString(1, "%" + keyword.trim() + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToAccount(rs));
                }
            }
        }

        return list;
    }

    /**
     * Đăng nhập
     */
    public EmployeeAccount login(String username, String password) throws SQLException {
        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(LOGIN_MYSQL)
        ) {
            ps.setString(1, username.trim());
            ps.setString(2, password.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAccount(rs);
                }
            }
        }

        return null;
    }

    /**
     * Kiểm tra username đã tồn tại chưa
     */
    public boolean existsByUsername(String username) throws SQLException {
        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(EXISTS_USERNAME_MYSQL)
        ) {
            ps.setString(1, username.trim());

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Kiểm tra username tồn tại ở bản ghi khác
     */
    public boolean existsByUsernameExceptId(String username, int accountId)
            throws SQLException {

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(EXISTS_USERNAME_EXCEPT_ID_MYSQL)
        ) {
            ps.setString(1, username.trim());
            ps.setInt(2, accountId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Kiểm tra nhân viên đã có tài khoản chưa
     */
    public boolean existsByEmployeeId(int employeeId) throws SQLException {
        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(EXISTS_EMPLOYEE_ID_MYSQL)
        ) {
            ps.setInt(1, employeeId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Kiểm tra nhân viên đã có tài khoản ở bản ghi khác
     */
    public boolean existsByEmployeeIdExceptId(int employeeId, int accountId)
            throws SQLException {

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(EXISTS_EMPLOYEE_ID_EXCEPT_ID_MYSQL)
        ) {
            ps.setInt(1, employeeId);
            ps.setInt(2, accountId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Chuyển ResultSet -> EmployeeAccount
     */
    private EmployeeAccount mapResultSetToAccount(ResultSet rs) throws SQLException {
        Timestamp createdAt = rs.getTimestamp("created_at");
        Timestamp updatedAt = rs.getTimestamp("updated_at");

        return new EmployeeAccount(
            rs.getInt("ma_tai_khoan"),
            new Employee(rs.getInt("ma_nhan_vien")),
            rs.getString("ten_tai_khoan"),
            rs.getString("mat_khau"),
            AccountStatus.fromId(rs.getInt("trang_thai")),
            createdAt != null ? createdAt.toLocalDateTime() : null,
            updatedAt != null ? updatedAt.toLocalDateTime() : null
        );
    }

    /**
     * Validate dữ liệu đầu vào
     */
    private void validateAccount(EmployeeAccount account) {
        if (account == null) {
            throw new IllegalArgumentException("Tài khoản không được null!");
        }

        if (account.getEmployee() == null
                || account.getEmployee().getEmployeeId() <= 0) {
            throw new IllegalArgumentException("Nhân viên không hợp lệ!");
        }

        if (account.getAccountName() == null
                || account.getAccountName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên tài khoản không được rỗng!");
        }

        if (account.getAccountPassword() == null
                || account.getAccountPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu không được rỗng!");
        }

        if (account.getAccountStatus() == null) {
            throw new IllegalArgumentException("Trạng thái tài khoản không hợp lệ!");
        }
    }
}