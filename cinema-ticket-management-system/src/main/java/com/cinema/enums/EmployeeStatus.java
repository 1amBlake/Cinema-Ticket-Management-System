package com.cinema.enums;

/**
 * Phân loại trạng thái của nhân viên.
 * 0 - RESIGNED: Nhân viên đã nghỉ
 * 1 - WORKING: Nhân viên đang làm
 * 2 - ON_LEAVE: Nhân viên tạm nghỉ
 * 
 * @author Minh Huy (chính)
 */
public enum EmployeeStatus {
	RESIGNED(0, "Đã nghỉ"),
	WORKING(1, "Đang làm"),
	ON_LEAVE(2, "Tạm nghỉ");

	private final int employeeStatusId;
	private final String displayName;

	/**
	 * Khởi tạo trạng thái nhân viên.
	 * 
	 * @param employeeStatusId - Mã trạng thái nhân viên
	 * @param displayName - Tên hiển thị trạng thái nhân viên
	 */
	EmployeeStatus(int employeeStatusId, String displayName) {
		this.employeeStatusId = employeeStatusId;
		this.displayName = displayName;
	}

	/**
	 * Lấy mã trạng thái nhân viên.
	 * 
	 * @return mã trạng thái nhân viên
	 */
	public int getEmployeeStatusId() {
		return employeeStatusId;
	}

	/**
	 * Lấy tên hiển thị của trạng thái nhân viên.
	 * 
	 * @return tên hiển thị trạng thái nhân viên
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Chuyển mã trạng thái thành enum EmployeeStatus.
	 * 0 - RESIGNED: Nhân viên đã nghỉ
	 * 1 - WORKING: Nhân viên đang làm
	 * 2 - ON_LEAVE: Nhân viên tạm nghỉ
	 * 
	 * @param id - Mã trạng thái nhân viên
	 * @return trạng thái nhân viên tương ứng
	 * @throws IllegalArgumentException nếu id không hợp lệ
	 */
	public static EmployeeStatus fromId(int id) {
		for (EmployeeStatus status : values()) {
			if (status.employeeStatusId == id) {
				return status;
			}
		}
		throw new IllegalArgumentException("Mã trạng thái nhân viên không hợp lệ: " + id);
	}
}