package com.cinema.enums;

/**
 * Phân loại trạng thái của tài khoản.
 * 0 - LOCKED: Tài khoản bị khóa
 * 1 - ACTIVE: Tài khoản hoạt động
 * 
 * @author Minh Huy (chính)
 */
public enum AccountStatus {
	LOCKED(0, "Bị khóa"),
	ACTIVE(1, "Hoạt động");

	private final int accountStatusId;
	private final String displayName;

	/**
	 * Khởi tạo trạng thái tài khoản.
	 * 
	 * @param accountStatusId - Mã trạng thái tài khoản
	 * @param displayName - Tên hiển thị trạng thái tài khoản
	 */
	AccountStatus(int accountStatusId, String displayName) {
		this.accountStatusId = accountStatusId;
		this.displayName = displayName;
	}

	/**
	 * Lấy mã trạng thái tài khoản.
	 * 
	 * @return mã trạng thái tài khoản
	 */
	public int getAccountStatusId() {
		return accountStatusId;
	}

	/**
	 * Lấy tên hiển thị của trạng thái tài khoản.
	 * 
	 * @return tên hiển thị trạng thái tài khoản
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Chuyển mã trạng thái thành enum AccountStatus.
	 * 0 - LOCKED: Tài khoản bị khóa
	 * 1 - ACTIVE: Tài khoản hoạt động
	 * 
	 * @param id - Mã trạng thái tài khoản
	 * @return trạng thái tài khoản tương ứng
	 * @throws IllegalArgumentException nếu id không hợp lệ
	 */
	public static AccountStatus fromId(int id) {
		for (AccountStatus status : values()) {
			if (status.accountStatusId == id) {
				return status;
			}
		}
		throw new IllegalArgumentException("Mã trạng thái tài khoản không hợp lệ: " + id);
	}
}