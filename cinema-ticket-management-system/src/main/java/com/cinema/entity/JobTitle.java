package com.cinema.entity;

import java.time.LocalDateTime;

/**
 * Đại diện cho chức vụ trong hệ thống.
 * Chứa các thuộc tính chức vụ.
 * 
 * @author Thanh Trọng (chính)
 * @author Minh Huy (sửa)
 */
public class JobTitle {

	private int jobTitleId; //Do database tự sinh
	private String jobTitleName; //not null
	private LocalDateTime createdAt; //Do database tự sinh
	private LocalDateTime updatedAt; //Do database tự sinh

	/**
	 * Constructor mặc định, không có dữ liệu để truyền
	 */
	public JobTitle() {
		super();
	}

	/**
	 * Constructor để khởi tạo thực thể theo mã, phục vụ truy vấn và ánh xạ quan hệ
	 * 
	 * @param jobTitleId - Mã chức vụ
	 */
	public JobTitle(int jobTitleId) {
		super();
		this.jobTitleId = jobTitleId;
	}

	/**
	 * Constructor để thêm dữ liệu cho CSDL (các dữ liệu not null)
	 * 
	 * @param jobTitleName - Tên chức vụ
	 */
	public JobTitle(String jobTitleName) {
		super();
		setJobTitleName(jobTitleName);
	}

	/**
	 * Constructor truyền dữ liệu với các thông tin "not null"
	 * 
	 * @param jobTitleId - Mã chức vụ
	 * @param jobTitleName - Tên chức vụ
	 */
	public JobTitle(int jobTitleId, String jobTitleName) {
		super();
		this.jobTitleId = jobTitleId;
		setJobTitleName(jobTitleName);
	}

	/**
	 * Constructor đầy đủ thông tin
	 * 
	 * @param jobTitleId - Mã chức vụ
	 * @param jobTitleName - Tên chức vụ
	 * @param createdAt - Thời điểm khởi tạo dữ liệu
	 * @param updatedAt - Thời điểm dữ liệu được cập nhật
	 */
	public JobTitle(int jobTitleId, String jobTitleName, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.jobTitleId = jobTitleId;
		setJobTitleName(jobTitleName);
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public int getJobTitleId() {
		return jobTitleId;
	}

	public String getJobTitleName() {
		return jobTitleName;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setJobTitleName(String jobTitleName) {
		if (jobTitleName == null || jobTitleName.trim().isEmpty())
			throw new IllegalArgumentException("jobTitleName không được để trống");
		if (jobTitleName.trim().length() > 100)
			throw new IllegalArgumentException("jobTitleName không được vượt quá 100 ký tự");
		this.jobTitleName = jobTitleName.trim();
	}

	/**
	 * Nếu đã có id hợp lệ thì hash theo id. Nếu chưa có id hợp lệ thì hash theo chính object
	 */
	@Override
	public int hashCode() {
		return (jobTitleId > 0) ? Integer.hashCode(jobTitleId) : System.identityHashCode(this);
	}

	/**
	 * Hai Object JobTitle được xem là bằng nhau khi có cùng jobTitleId hợp lệ
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		JobTitle other = (JobTitle) obj;
		if (this.jobTitleId <= 0 || other.jobTitleId <= 0)
			return false;
		return this.jobTitleId == other.jobTitleId;
	}

	@Override
	public String toString() {
		return "JobTitle [jobTitleId=" + jobTitleId + ", jobTitleName=" + jobTitleName + ", createdAt=" + createdAt
				+ ", updatedAt=" + updatedAt + "]";
	}
}