package com.cinema.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.cinema.enums.EmployeeGender;
import com.cinema.enums.EmployeeStatus;

/**
 * Đại diện cho nhân viên trong hệ thống.
 * Chứa các thuộc tính nhân viên.
 * 
 * @author Thanh Trọng (chính)
 * @author Minh Huy (sửa)
 */
public class Employee {

	private int employeeId; //Do database tự sinh
	private String employeeName; //not null
	private EmployeeGender employeeGender;
	private String employeePhoneNumber;
	private LocalDate birthDate;
	private String employeeEmail;
	private LocalDate hireDate;
	private JobTitle jobTitle; //not null
	private EmployeeStatus employeeStatus; //not null
	private LocalDateTime createdAt; //Do database tự sinh
	private LocalDateTime updatedAt; //Do database tự sinh

	/**
	 * Constructor mặc định, không có dữ liệu để truyền
	 */
	public Employee() {
		super();
	}

	/**
	 * Constructor để khởi tạo thực thể theo mã, phục vụ truy vấn và ánh xạ quan hệ
	 * 
	 * @param employeeId - Mã nhân viên
	 */
	public Employee(int employeeId) {
		super();
		this.employeeId = employeeId;
	}

	/**
	 * Constructor để thêm dữ liệu cho CSDL (các dữ liệu not null)
	 * 
	 * @param employeeName - Tên nhân viên
	 * @param jobTitle - Chức vụ
	 * @param employeeStatus - Trạng thái nhân viên
	 */
	public Employee(String employeeName, JobTitle jobTitle, EmployeeStatus employeeStatus) {
		super();
		setEmployeeName(employeeName);
		setJobTitle(jobTitle);
		setEmployeeStatus(employeeStatus);
	}

	/**
	 * Constructor để thêm dữ liệu cho CSDL (các dữ liệu nhập tay)
	 * 
	 * @param employeeName - Tên nhân viên
	 * @param employeeGender - Giới tính nhân viên
	 * @param employeePhoneNumber - Số điện thoại nhân viên
	 * @param birthDate - Ngày sinh nhân viên
	 * @param employeeEmail - Email nhân viên
	 * @param hireDate - Ngày vào làm
	 * @param jobTitle - Chức vụ
	 * @param employeeStatus - Trạng thái nhân viên
	 */
	public Employee(String employeeName, EmployeeGender employeeGender, String employeePhoneNumber,
			LocalDate birthDate, String employeeEmail, LocalDate hireDate, JobTitle jobTitle,
			EmployeeStatus employeeStatus) {
		super();
		setEmployeeName(employeeName);
		setEmployeeGender(employeeGender);
		setEmployeePhoneNumber(employeePhoneNumber);
		setBirthDate(birthDate);
		setEmployeeEmail(employeeEmail);
		setHireDate(hireDate);
		setJobTitle(jobTitle);
		setEmployeeStatus(employeeStatus);
	}

	/**
	 * Constructor truyền dữ liệu với các thông tin "not null"
	 * 
	 * @param employeeId - Mã nhân viên
	 * @param employeeName - Tên nhân viên
	 * @param jobTitle - Chức vụ
	 * @param employeeStatus - Trạng thái nhân viên
	 */
	public Employee(int employeeId, String employeeName, JobTitle jobTitle, EmployeeStatus employeeStatus) {
		super();
		this.employeeId = employeeId;
		setEmployeeName(employeeName);
		setJobTitle(jobTitle);
		setEmployeeStatus(employeeStatus);
	}

	/**
	 * Constructor đầy đủ thông tin
	 * 
	 * @param employeeId - Mã nhân viên
	 * @param employeeName - Tên nhân viên
	 * @param employeeGender - Giới tính nhân viên
	 * @param employeePhoneNumber - Số điện thoại nhân viên
	 * @param birthDate - Ngày sinh nhân viên
	 * @param employeeEmail - Email nhân viên
	 * @param hireDate - Ngày vào làm
	 * @param jobTitle - Chức vụ
	 * @param employeeStatus - Trạng thái nhân viên
	 * @param createdAt - Thời điểm khởi tạo dữ liệu
	 * @param updatedAt - Thời điểm dữ liệu được cập nhật
	 */
	public Employee(int employeeId, String employeeName, EmployeeGender employeeGender, String employeePhoneNumber,
			LocalDate birthDate, String employeeEmail, LocalDate hireDate, JobTitle jobTitle,
			EmployeeStatus employeeStatus, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.employeeId = employeeId;
		setEmployeeName(employeeName);
		setEmployeeGender(employeeGender);
		setEmployeePhoneNumber(employeePhoneNumber);
		setBirthDate(birthDate);
		setEmployeeEmail(employeeEmail);
		setHireDate(hireDate);
		setJobTitle(jobTitle);
		setEmployeeStatus(employeeStatus);
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public EmployeeGender getEmployeeGender() {
		return employeeGender;
	}

	public String getEmployeePhoneNumber() {
		return employeePhoneNumber;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public String getEmployeeEmail() {
		return employeeEmail;
	}

	public LocalDate getHireDate() {
		return hireDate;
	}

	public JobTitle getJobTitle() {
		return jobTitle;
	}

	public EmployeeStatus getEmployeeStatus() {
		return employeeStatus;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setEmployeeName(String employeeName) {
		if (employeeName == null || employeeName.trim().isEmpty())
			throw new IllegalArgumentException("employeeName không được để trống");
		if (employeeName.trim().length() > 255)
			throw new IllegalArgumentException("employeeName không được vượt quá 255 ký tự");
		this.employeeName = employeeName.trim();
	}

	public void setEmployeeGender(EmployeeGender employeeGender) {
		this.employeeGender = employeeGender;
	}

	public void setEmployeePhoneNumber(String employeePhoneNumber) {
		if (employeePhoneNumber == null || employeePhoneNumber.trim().isEmpty()) {
			this.employeePhoneNumber = null;
			return;
		}
		if (employeePhoneNumber.trim().length() > 20)
			throw new IllegalArgumentException("employeePhoneNumber không được vượt quá 20 ký tự");
		this.employeePhoneNumber = employeePhoneNumber.trim();
	}

	public void setBirthDate(LocalDate birthDate) {
		if (birthDate != null && birthDate.isAfter(LocalDate.now()))
			throw new IllegalArgumentException("birthDate không được ở tương lai");
		this.birthDate = birthDate;
	}

	public void setEmployeeEmail(String employeeEmail) {
		if (employeeEmail == null || employeeEmail.trim().isEmpty()) {
			this.employeeEmail = null;
			return;
		}
		if (employeeEmail.trim().length() > 255)
			throw new IllegalArgumentException("employeeEmail không được vượt quá 255 ký tự");
		this.employeeEmail = employeeEmail.trim();
	}

	public void setHireDate(LocalDate hireDate) {
		if (hireDate != null && hireDate.isAfter(LocalDate.now().plusYears(1)))
			throw new IllegalArgumentException("hireDate không hợp lệ");
		this.hireDate = hireDate;
	}

	public void setJobTitle(JobTitle jobTitle) {
		if (jobTitle == null)
			throw new IllegalArgumentException("jobTitle không được null");
		this.jobTitle = jobTitle;
	}

	public void setEmployeeStatus(EmployeeStatus employeeStatus) {
		if (employeeStatus == null)
			throw new IllegalArgumentException("employeeStatus không được null");
		this.employeeStatus = employeeStatus;
	}

	/**
	 * Nếu đã có id hợp lệ thì hash theo id. Nếu chưa có id hợp lệ thì hash theo chính object
	 */
	@Override
	public int hashCode() {
		return (employeeId > 0) ? Integer.hashCode(employeeId) : System.identityHashCode(this);
	}

	/**
	 * Hai Object Employee được xem là bằng nhau khi có cùng employeeId hợp lệ
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		if (this.employeeId <= 0 || other.employeeId <= 0)
			return false;
		return this.employeeId == other.employeeId;
	}

	@Override
	public String toString() {
		return "Employee [employeeId=" + employeeId + ", employeeName=" + employeeName + ", employeeGender="
				+ employeeGender + ", employeePhoneNumber=" + employeePhoneNumber + ", birthDate=" + birthDate
				+ ", employeeEmail=" + employeeEmail + ", hireDate=" + hireDate + ", jobTitle=" + jobTitle
				+ ", employeeStatus=" + employeeStatus + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt
				+ "]";
	}
}