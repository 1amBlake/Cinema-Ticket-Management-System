package com.cinema.enums;

/**
 * Phân loại trạng thái của ghế trong phòng chiếu.
 * 0 - AVAILABLE: Ghế hoạt động
 * 1 - MAINTENANCE: Ghế bảo trì
 * 
 * @author Minh Huy (chính)
 */
public enum SeatStatus {
	AVAILABLE(0, "Hoạt động"),
	MAINTENANCE(1, "Bảo trì");

	private final int seatStatusId;
	private final String displayName;

	/**
	 * Khởi tạo trạng thái ghế trong phòng chiếu.
	 * 
	 * @param seatStatusId - Mã trạng thái ghế
	 * @param displayName - Tên hiển thị trạng thái ghế
	 */
	SeatStatus(int seatStatusId, String displayName) {
		this.seatStatusId = seatStatusId;
		this.displayName = displayName;
	}

	/**
	 * Lấy mã trạng thái ghế.
	 * 
	 * @return mã trạng thái ghế
	 */
	public int getSeatStatusId() {
		return seatStatusId;
	}

	/**
	 * Lấy tên hiển thị của trạng thái ghế.
	 * 
	 * @return tên hiển thị trạng thái ghế
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Chuyển mã trạng thái thành enum SeatStatus.
	 * 0 - AVAILABLE: Ghế hoạt động
	 * 1 - MAINTENANCE: Ghế bảo trì
	 * 
	 * @param id - Mã trạng thái ghế
	 * @return trạng thái ghế tương ứng
	 * @throws IllegalArgumentException nếu id không hợp lệ
	 */
	public static SeatStatus fromId(int id) {
		for (SeatStatus status : values()) {
			if (status.seatStatusId == id) {
				return status;
			}
		}
		throw new IllegalArgumentException("Mã trạng thái ghế không hợp lệ: " + id);
	}
}