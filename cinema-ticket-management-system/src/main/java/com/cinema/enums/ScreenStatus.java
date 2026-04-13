package com.cinema.enums;

/**
 * Phân loại trạng thái của phòng chiếu.
 * 0 - AVAILABLE: Phòng chiếu hoạt động
 * 1 - MAINTENANCE: Phòng chiếu bảo trì
 * 2 - UNAVAILABLE: Phòng chiếu ngưng hoạt động
 * 
 * @author Minh Huy (chính)
 */
public enum ScreenStatus {
	AVAILABLE(0, "Hoạt động"),
	MAINTENANCE(1, "Bảo trì"),
	UNAVAILABLE(2, "Ngưng hoạt động");

	private final int screenStatusId;
	private final String displayName;

	/**
	 * Khởi tạo trạng thái phòng chiếu.
	 * 
	 * @param screenStatusId - Mã trạng thái phòng chiếu
	 * @param displayName - Tên hiển thị trạng thái phòng chiếu
	 */
	ScreenStatus(int screenStatusId, String displayName) {
		this.screenStatusId = screenStatusId;
		this.displayName = displayName;
	}

	/**
	 * Lấy mã trạng thái phòng chiếu.
	 * 
	 * @return mã trạng thái phòng chiếu
	 */
	public int getScreenStatusId() {
		return screenStatusId;
	}

	/**
	 * Lấy tên hiển thị của trạng thái phòng chiếu.
	 * 
	 * @return tên hiển thị trạng thái phòng chiếu
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Chuyển mã trạng thái thành enum ScreenStatus.
	 * 0 - AVAILABLE: Phòng chiếu hoạt động
	 * 1 - MAINTENANCE: Phòng chiếu bảo trì
	 * 2 - UNAVAILABLE: Phòng chiếu ngưng hoạt động
	 * 
	 * @param id - Mã trạng thái phòng chiếu
	 * @return trạng thái phòng chiếu tương ứng
	 * @throws IllegalArgumentException nếu id không hợp lệ
	 */
	public static ScreenStatus fromId(int id) {
		for (ScreenStatus status : values()) {
			if (status.screenStatusId == id) {
				return status;
			}
		}
		throw new IllegalArgumentException("Mã trạng thái phòng chiếu không hợp lệ: " + id);
	}
}