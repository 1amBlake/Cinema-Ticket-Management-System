package com.cinema.entity;

import java.time.LocalDateTime;

import com.cinema.enums.AccountStatus;

/**
 * Đại diện cho tài khoản nhân viên trong hệ thống.
 * Chứa các thuộc tính tài khoản nhân viên.
 * 
 * @author Thanh Trọng (chính)
 * @author Minh Huy (sửa)
 */
public class EmployeeAccount {

	private int accountId; // Do database tự sinh
	private Employee employee; // not null
	private String accountName; // not null
	private String accountPassword; // not null
	private AccountStatus accountStatus; // not null
	private LocalDateTime createdAt; // Do database tự sinh
	private LocalDateTime updatedAt; // Do database tự sinh

	/**
	 * Constructor mặc định, không có dữ liệu để truyền
	 */
	public EmployeeAccount() {
		super();
	}

	/**
	 * Constructor để khởi tạo thực thể theo mã, phục vụ truy vấn và ánh xạ quan hệ
	 * 
	 * @param accountId - Mã tài khoản
	 */
	public EmployeeAccount(int accountId) {
		super();
		this.accountId = accountId;
	}

	/**
	 * Constructor để thêm dữ liệu cho CSDL (các dữ liệu not null)
	 * 
	 * @param employee - Nhân viên
	 * @param accountName - Tên tài khoản
	 * @param accountPassword - Mật khẩu tài khoản
	 * @param accountStatus - Trạng thái tài khoản
	 */
	public EmployeeAccount(Employee employee, String accountName, String accountPassword, AccountStatus accountStatus) {
		super();
		setEmployee(employee);
		setAccountName(accountName);
		setAccountPassword(accountPassword);
		setAccountStatus(accountStatus);
	}

	/**
	 * Constructor truyền dữ liệu với các thông tin "not null"
	 * 
	 * @param accountId - Mã tài khoản
	 * @param employee - Nhân viên
	 * @param accountName - Tên tài khoản
	 * @param accountPassword - Mật khẩu tài khoản
	 * @param accountStatus - Trạng thái tài khoản
	 */
	public EmployeeAccount(int accountId, Employee employee, String accountName, String accountPassword,
			AccountStatus accountStatus) {
		super();
		this.accountId = accountId;
		setEmployee(employee);
		setAccountName(accountName);
		setAccountPassword(accountPassword);
		setAccountStatus(accountStatus);
	}

	/**
	 * Constructor đầy đủ thông tin
	 * 
	 * @param accountId - Mã tài khoản
	 * @param employee - Nhân viên
	 * @param accountName - Tên tài khoản
	 * @param accountPassword - Mật khẩu tài khoản
	 * @param accountStatus - Trạng thái tài khoản
	 * @param createdAt - Thời điểm khởi tạo dữ liệu
	 * @param updatedAt - Thời điểm dữ liệu được cập nhật
	 */
	public EmployeeAccount(int accountId, Employee employee, String accountName, String accountPassword,
			AccountStatus accountStatus, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.accountId = accountId;
		setEmployee(employee);
		setAccountName(accountName);
		setAccountPassword(accountPassword);
		setAccountStatus(accountStatus);
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public int getAccountId() {
		return accountId;
	}

	public Employee getEmployee() {
		return employee;
	}

	public String getAccountName() {
		return accountName;
	}

	public String getAccountPassword() {
		return accountPassword;
	}

	public AccountStatus getAccountStatus() {
		return accountStatus;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setEmployee(Employee employee) {
		if (employee == null)
			throw new IllegalArgumentException("employee không được null");
		this.employee = employee;
	}

	public void setAccountName(String accountName) {
		if (accountName == null || accountName.trim().isEmpty())
			throw new IllegalArgumentException("accountName không được để trống");
		if (accountName.trim().length() > 255)
			throw new IllegalArgumentException("accountName không được vượt quá 255 ký tự");
		this.accountName = accountName.trim();
	}

	public void setAccountPassword(String accountPassword) {
		if (accountPassword == null || accountPassword.trim().isEmpty())
			throw new IllegalArgumentException("accountPassword không được để trống");
		if (accountPassword.trim().length() > 255)
			throw new IllegalArgumentException("accountPassword không được vượt quá 255 ký tự");
		this.accountPassword = accountPassword.trim();
	}

	public void setAccountStatus(AccountStatus accountStatus) {
		if (accountStatus == null)
			throw new IllegalArgumentException("accountStatus không được null");
		this.accountStatus = accountStatus;
	}

	@Override
	public int hashCode() {
		return (accountId > 0) ? Integer.hashCode(accountId) : System.identityHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		EmployeeAccount other = (EmployeeAccount) obj;
		if (this.accountId <= 0 || other.accountId <= 0)
			return false;
		return this.accountId == other.accountId;
	}

	@Override
	public String toString() {
		return "EmployeeAccount [accountId=" + accountId
				+ ", employeeId=" + (employee != null ? employee.getEmployeeId() : null)
				+ ", accountName=" + accountName
				+ ", accountStatus=" + accountStatus
				+ ", createdAt=" + createdAt
				+ ", updatedAt=" + updatedAt + "]";
	}
}