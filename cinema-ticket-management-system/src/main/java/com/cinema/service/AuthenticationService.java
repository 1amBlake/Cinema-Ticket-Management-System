package com.cinema.service;

import java.sql.SQLException;

import com.cinema.dao.EmployeeAccountDao;
import com.cinema.dao.EmployeeDao;
import com.cinema.entity.Employee;
import com.cinema.entity.EmployeeAccount;

/**
 * Service cho xác thực tài khoản.
 * Chứa các chức vụ liên quan đến xác thực tài khoản.
 * 
 * @author Hải Anh (chính)
 */
public class AuthenticationService {

    private final EmployeeDao employeeDao;
    private final EmployeeAccountDao employeeAccountDao;
    private Employee currentEmployee;
    private EmployeeAccount currentEmployeeAccount;

    public AuthenticationService() {
        this.employeeAccountDao = new EmployeeAccountDao();
        this.employeeDao = new EmployeeDao();
    }

	/**
	 * Đăng nhập vào hệ thống bằng tài khoản
	 * 
	 * @param username - Tên tài khoản
	 * @param password - Mật khẩu
	 * @return true nếu đăng nhập thành công
	 * @throws SQLException nếu có lỗi SQL
	 */
    public boolean login(String username, String password) throws SQLException {
        EmployeeAccount employeeAccount = employeeAccountDao.login(username, password);
        if(employeeAccount == null) return false;

        this.currentEmployeeAccount = employeeAccount;
        // load employee đầy đủ
        int employeeId = employeeAccount.getEmployee().getEmployeeId();
        this.currentEmployee = employeeDao.findById(employeeId);

        return true;
    }

	/**
	 * Đăng xuất khỏi hệ thống
	 * 
	 * @return true nếu đăng xuất thành công
	 */
    public boolean logout() {
        this.currentEmployee = null;
        this.currentEmployeeAccount = null;
        return true;
    }

	/**
	 * kiểm tra đã đăng nhập tài khoản chưa
	 * 
	 * @return true nếu đã đăng nhập
	 */
    public boolean isLoggedIn() {
        return currentEmployeeAccount != null;
    }

	/**
	 * Lấy tài khoản đăng nhập
	 * 
	 * @return đối tượng EmployeeAccount
	 */
    public EmployeeAccount getCurrentAccount() {
        return currentEmployeeAccount;
    }

	/**
	 * Lấy nhân viên đăng nhập
	 * 
	 * @return đối tượng Employee
	 */
    public Employee getCurrentEmployee() {
        return currentEmployee;
    }

	/**
	 * kiểm tra chức vụ của nhân viên
	 * 
	 * @return true nếu nhân viên có chức vụ
	 */
    public boolean hasRole(int jobTitleId) {
        if(currentEmployee == null) return false;
        if(currentEmployee.getJobTitle() == null) return false;

        return currentEmployee.getJobTitle().getJobTitleId() == jobTitleId;
    }

    /**
     * không biết cụ thể chức vụ gì nên chỉ làm cơ bản
     * 1 = ADMIN
     * 2 = MANAGER
     * 3 = STAFF
     */
    public boolean isAdmin() {
        return hasRole(1);
    }

    public boolean isManager() {
        return hasRole(2);
    }

    public boolean isStaff() {
        return hasRole(3);
    }

    /**
     * các quyền cơ bản tuỳ vào chức vụ
     */
    public boolean canManageEmployee() {
        return isAdmin() || isManager();
    }

    public boolean canDeleteEmployee() {
        return isAdmin();
    }

    public boolean canManageEmployeeAccount() {
        return isAdmin() || isManager();
    }

    public boolean canDeleteEmployeeAccount() {
        return isAdmin();
    }
    
    public boolean canSellTicket() {
        return isLoggedIn();
    }
}