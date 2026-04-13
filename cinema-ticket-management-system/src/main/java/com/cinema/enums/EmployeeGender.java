package com.cinema.enums;

/**
 * Phân loại giới tính của nhân viên.
 * 0 - FEMALE: Nữ
 * 1 - MALE: Nam
 * 
 * @author Minh Huy (chính)
 */
public enum EmployeeGender {
	FEMALE(0, "Nữ"),
	MALE(1, "Nam");

	private final int genderId;
	private final String displayName;

	/**
	 * Khởi tạo giới tính nhân viên.
	 * 
	 * @param genderId - Mã giới tính
	 * @param displayName - Tên hiển thị giới tính
	 */
	EmployeeGender(int genderId, String displayName) {
		this.genderId = genderId;
		this.displayName = displayName;
	}

	/**
	 * Lấy mã giới tính.
	 * 
	 * @return mã giới tính
	 */
	public int getGenderId() {
		return genderId;
	}

	/**
	 * Lấy tên hiển thị của giới tính.
	 * 
	 * @return tên hiển thị giới tính
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Chuyển mã giới tính thành enum EmployeeGender.
	 * 0 - FEMALE: Nữ
	 * 1 - MALE: Nam
	 * 
	 * @param id - Mã giới tính
	 * @return giới tính tương ứng
	 * @throws IllegalArgumentException nếu id không hợp lệ
	 */
	public static EmployeeGender fromId(int id) {
		for (EmployeeGender gender : values()) {
			if (gender.genderId == id) {
				return gender;
			}
		}
		throw new IllegalArgumentException("Mã giới tính không hợp lệ: " + id);
	}
}