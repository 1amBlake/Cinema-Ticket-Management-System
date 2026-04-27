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
import com.cinema.validator.EmployeeAccountValidator;

/**
 * DAO cho thực thể EmployeeAccount
 * Chịu trách nhiệm thao tác dữ liệu với bảng tai_khoan trong MySQL.
 *
 * Bảng ánh xạ:
 * tai_khoan(
 *     ma_tai_khoan,
 *     ma_nhan_vien,
 *     ten_tai_khoan,
 *     mat_khau,
 *     trang_thai,
 *     created_at,
 *     updated_at
 * )
 *
 * Ghi chú nghiệp vụ đề xuất:
 * - Mỗi nhân viên chỉ có tối đa 1 tài khoản.
 * - Tên tài khoản là duy nhất trong hệ thống.
 * - Không nên đổi nhân viên sở hữu tài khoản bằng update thông thường.
 *
 * @author Quốc Anh (chính)
 * @author Minh Huy (chỉnh sửa vài chỗ)
 */
public class EmployeeAccountDao {

    private static final String INSERT_MYSQL = """
            INSERT INTO tai_khoan (
                ma_nhan_vien,
                ten_tai_khoan,
                mat_khau,
                trang_thai
            )
            VALUES (?, ?, ?, ?)
            """;

    private static final String UPDATE_MYSQL = """
            UPDATE tai_khoan
            SET ten_tai_khoan = ?,
                mat_khau = ?,
                trang_thai = ?
            WHERE ma_tai_khoan = ?
            """;

    private static final String DELETE_MYSQL = """
            DELETE FROM tai_khoan
            WHERE ma_tai_khoan = ?
            """;

    private static final String SELECT_BY_ID_MYSQL = """
            SELECT ma_tai_khoan,
                   ma_nhan_vien,
                   ten_tai_khoan,
                   mat_khau,
                   trang_thai,
                   created_at,
                   updated_at
            FROM tai_khoan
            WHERE ma_tai_khoan = ?
            """;

    private static final String SELECT_ALL_MYSQL = """
            SELECT ma_tai_khoan,
                   ma_nhan_vien,
                   ten_tai_khoan,
                   mat_khau,
                   trang_thai,
                   created_at,
                   updated_at
            FROM tai_khoan
            ORDER BY ten_tai_khoan ASC, ma_tai_khoan ASC
            """;

    private static final String SEARCH_BY_ACCOUNT_NAME_MYSQL = """
            SELECT ma_tai_khoan,
                   ma_nhan_vien,
                   ten_tai_khoan,
                   mat_khau,
                   trang_thai,
                   created_at,
                   updated_at
            FROM tai_khoan
            WHERE ten_tai_khoan LIKE ?
            ORDER BY ten_tai_khoan ASC, ma_tai_khoan ASC
            """;

    private static final String SELECT_BY_EMPLOYEE_ID_MYSQL = """
            SELECT ma_tai_khoan,
                   ma_nhan_vien,
                   ten_tai_khoan,
                   mat_khau,
                   trang_thai,
                   created_at,
                   updated_at
            FROM tai_khoan
            WHERE ma_nhan_vien = ?
            LIMIT 1
            """;

    private static final String EXISTS_BY_ACCOUNT_NAME_MYSQL = """
            SELECT 1
            FROM tai_khoan
            WHERE ten_tai_khoan = ?
            LIMIT 1
            """;

    private static final String EXISTS_BY_ACCOUNT_NAME_EXCEPT_ID_MYSQL = """
            SELECT 1
            FROM tai_khoan
            WHERE ten_tai_khoan = ?
              AND ma_tai_khoan <> ?
            LIMIT 1
            """;

    private static final String EXISTS_BY_EMPLOYEE_ID_MYSQL = """
            SELECT 1
            FROM tai_khoan
            WHERE ma_nhan_vien = ?
            LIMIT 1
            """;

    private static final String EXISTS_EMPLOYEE_BY_ID_MYSQL = """
            SELECT 1
            FROM nhan_vien
            WHERE ma_nhan_vien = ?
            LIMIT 1
            """;

    private static final String LOGIN_MYSQL = """
            SELECT ma_tai_khoan,
                   ma_nhan_vien,
                   ten_tai_khoan,
                   mat_khau,
                   trang_thai,
                   created_at,
                   updated_at
            FROM tai_khoan
            WHERE ten_tai_khoan = ?
              AND mat_khau = ?
              AND trang_thai = 1
            LIMIT 1
            """;

    /**
     * Kiểm tra nhân viên có tồn tại hay không.
     *
     * @param employeeId - Mã nhân viên
     * @return true nếu tồn tại
     * @throws SQLException nếu có lỗi SQL
     */
    private boolean existsEmployeeById(int employeeId) throws SQLException {
        if (employeeId <= 0) {
            throw new IllegalArgumentException("employeeId phải lớn hơn 0!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(EXISTS_EMPLOYEE_BY_ID_MYSQL)) {

            ps.setInt(1, employeeId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Kiểm tra tên tài khoản đã tồn tại hay chưa.
     *
     * @param accountName - Tên tài khoản
     * @return true nếu đã tồn tại
     * @throws SQLException nếu có lỗi SQL
     */
    private boolean existsByAccountName(String accountName) throws SQLException {
        if (accountName == null || accountName.trim().isEmpty()) {
            return false;
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(EXISTS_BY_ACCOUNT_NAME_MYSQL)) {

            ps.setString(1, accountName.trim());

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Kiểm tra tên tài khoản đã tồn tại ở bản ghi khác hay chưa.
     *
     * @param accountName - Tên tài khoản
     * @param accountId - Mã tài khoản
     * @return true nếu đã tồn tại ở bản ghi khác
     * @throws SQLException nếu có lỗi SQL
     */
    private boolean existsByAccountNameExceptId(String accountName, int accountId) throws SQLException {
        if (accountId <= 0 || accountName == null || accountName.trim().isEmpty()) {
            throw new IllegalArgumentException("accountId phải lớn hơn 0 và accountName không được để trống!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(EXISTS_BY_ACCOUNT_NAME_EXCEPT_ID_MYSQL)) {

            ps.setString(1, accountName.trim());
            ps.setInt(2, accountId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Kiểm tra nhân viên đã có tài khoản hay chưa.
     *
     * @param employeeId - Mã nhân viên
     * @return true nếu đã có tài khoản
     * @throws SQLException nếu có lỗi SQL
     */
    private boolean existsByEmployeeId(int employeeId) throws SQLException {
        if (employeeId <= 0) {
            throw new IllegalArgumentException("employeeId phải lớn hơn 0!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(EXISTS_BY_EMPLOYEE_ID_MYSQL)) {

            ps.setInt(1, employeeId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Ánh xạ một dòng dữ liệu ResultSet thành đối tượng EmployeeAccount.
     *
     * @param rs - ResultSet đang trỏ tới dòng dữ liệu hợp lệ
     * @return đối tượng EmployeeAccount
     * @throws SQLException nếu có lỗi SQL
     */
    private EmployeeAccount mapResultSetToEmployeeAccount(ResultSet rs) throws SQLException {
        Timestamp createdAt = rs.getTimestamp("created_at");
        Timestamp updatedAt = rs.getTimestamp("updated_at");

        EmployeeAccount anEmployeeAccount = new EmployeeAccount(
                rs.getInt("ma_tai_khoan"),
                new Employee(rs.getInt("ma_nhan_vien")),
                rs.getString("ten_tai_khoan"),
                rs.getString("mat_khau"),
                AccountStatus.fromId(rs.getInt("trang_thai")),
                createdAt != null ? createdAt.toLocalDateTime() : null,
                updatedAt != null ? updatedAt.toLocalDateTime() : null
        );

        return anEmployeeAccount;
    }

    /**
     * Thêm tài khoản nhân viên mới.
     *
     * @param employeeAccount - Đối tượng tài khoản nhân viên
     * @return true nếu thêm thành công
     * @throws SQLException nếu có lỗi SQL
     */
    public boolean addEmployeeAccount(EmployeeAccount employeeAccount) throws SQLException {
        EmployeeAccountValidator.validateForCreate(employeeAccount);

        int employeeId = employeeAccount.getEmployee().getEmployeeId();

        if (!existsEmployeeById(employeeId)) {
            throw new IllegalArgumentException("Nhân viên không tồn tại!");
        }

        if (existsByAccountName(employeeAccount.getAccountName())) {
            throw new IllegalArgumentException("Tên tài khoản đã tồn tại!");
        }

        if (existsByEmployeeId(employeeId)) {
            throw new IllegalArgumentException("Nhân viên này đã có tài khoản!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_MYSQL)) {

            ps.setInt(1, employeeId);
            ps.setString(2, employeeAccount.getAccountName().trim());
            ps.setString(3, employeeAccount.getAccountPassword());
            ps.setInt(4, employeeAccount.getAccountStatus().getAccountStatusId());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Cập nhật thông tin tài khoản nhân viên.
     *
     * Ghi chú:
     * - Không đổi ma_nhan_vien ở đây để tránh đổi chủ sở hữu tài khoản.
     *
     * @param employeeAccount - Tài khoản cần cập nhật
     * @return true nếu cập nhật thành công
     * @throws SQLException nếu có lỗi SQL
     */
    public boolean updateEmployeeAccount(EmployeeAccount employeeAccount) throws SQLException {
    	EmployeeAccountValidator.validateForUpdate(employeeAccount);

        if (employeeAccount.getAccountId() <= 0) {
            throw new IllegalArgumentException("accountId phải lớn hơn 0!");
        }

        if (!existsEmployeeById(employeeAccount.getEmployee().getEmployeeId())) {
            throw new IllegalArgumentException("Nhân viên không tồn tại!");
        }

        if (existsByAccountNameExceptId(employeeAccount.getAccountName(), employeeAccount.getAccountId())) {
            throw new IllegalArgumentException("Tên tài khoản đã tồn tại ở bản ghi khác!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_MYSQL)) {

            ps.setString(1, employeeAccount.getAccountName().trim());
            ps.setString(2, employeeAccount.getAccountPassword());
            ps.setInt(3, employeeAccount.getAccountStatus().getAccountStatusId());
            ps.setInt(4, employeeAccount.getAccountId());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Xóa tài khoản theo mã.
     *
     * @param accountId - Mã tài khoản
     * @return true nếu xóa thành công
     * @throws SQLException nếu có lỗi SQL
     */
    public boolean deleteEmployeeAccountById(int accountId) throws SQLException {
        if (accountId <= 0) {
            throw new IllegalArgumentException("accountId phải lớn hơn 0!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_MYSQL)) {

            ps.setInt(1, accountId);

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Tìm tài khoản theo mã.
     *
     * @param accountId - Mã tài khoản
     * @return đối tượng EmployeeAccount nếu tìm thấy, ngược lại trả về null
     * @throws SQLException nếu có lỗi SQL
     */
    public EmployeeAccount findById(int accountId) throws SQLException {
        if (accountId <= 0) {
            throw new IllegalArgumentException("accountId phải lớn hơn 0!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID_MYSQL)) {

            ps.setInt(1, accountId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEmployeeAccount(rs);
                }
            }
        }

        return null;
    }

    /**
     * Tìm tài khoản theo mã nhân viên.
     *
     * @param employeeId - Mã nhân viên
     * @return đối tượng EmployeeAccount nếu tìm thấy, ngược lại trả về null
     * @throws SQLException nếu có lỗi SQL
     */
    public EmployeeAccount findByEmployeeId(int employeeId) throws SQLException {
        if (employeeId <= 0) {
            throw new IllegalArgumentException("employeeId phải lớn hơn 0!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_BY_EMPLOYEE_ID_MYSQL)) {

            ps.setInt(1, employeeId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEmployeeAccount(rs);
                }
            }
        }

        return null;
    }

    /**
     * Lấy tất cả tài khoản nhân viên.
     *
     * @return danh sách tài khoản nhân viên
     * @throws SQLException nếu có lỗi SQL
     */
    public List<EmployeeAccount> getAllEmployeeAccounts() throws SQLException {
        List<EmployeeAccount> employeeAccounts = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_ALL_MYSQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                employeeAccounts.add(mapResultSetToEmployeeAccount(rs));
            }
        }

        return employeeAccounts;
    }

    /**
     * Tìm kiếm tài khoản theo tên gần đúng.
     *
     * @param keyword - Từ khóa tên tài khoản
     * @return danh sách tài khoản phù hợp
     * @throws SQLException nếu có lỗi SQL
     */
    public List<EmployeeAccount> searchByAccountName(String keyword) throws SQLException {
        List<EmployeeAccount> employeeAccounts = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SEARCH_BY_ACCOUNT_NAME_MYSQL)) {

            keyword = (keyword == null) ? "" : keyword.trim();
            ps.setString(1, "%" + keyword + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    employeeAccounts.add(mapResultSetToEmployeeAccount(rs));
                }
            }
        }

        return employeeAccounts;
    }

    /**
     * Đăng nhập tài khoản.
     *
     * @param accountName - Tên tài khoản
     * @param accountPassword - Mật khẩu tài khoản
     * @return tài khoản nếu đăng nhập thành công, ngược lại trả về null
     * @throws SQLException nếu có lỗi SQL
     */
    public EmployeeAccount login(String accountName, String accountPassword) throws SQLException {
        if (accountName == null || accountName.trim().isEmpty()) {
            throw new IllegalArgumentException("accountName không được để trống!");
        }

        if (accountPassword == null || accountPassword.isEmpty()) {
            throw new IllegalArgumentException("accountPassword không được để trống!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(LOGIN_MYSQL)) {

            ps.setString(1, accountName.trim());
            ps.setString(2, accountPassword);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEmployeeAccount(rs);
                }
            }
        }

        return null;
    }
}