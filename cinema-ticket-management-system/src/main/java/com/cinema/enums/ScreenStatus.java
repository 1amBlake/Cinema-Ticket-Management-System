package com.cinema.enums;

/**
 * Phân loại trạng thái của phòng chiếu
 * 0 - AVAILABLE: Hoạt động
 * 1 - MAINTENANCE: Bảo trì
 * 2 - UNVAILABLE: Ngưng hoạt động
 * 
 * @author minhhuy (chính)
 */
public enum ScreenStatus {
	AVAILABLE(0, "Hoạt Động"),
	MAINTENANCE(1, "Bảo Trì"),
	UNAVAILABLE(2, "Ngưng Hoạt Động");
	
	private final int screenStatusId;
	private final String displayName;
	
	/**
	 * Khởi tạo trạng thái phòng chiếu
	 * 
	 * @param screenStatusId
	 * @param displayName
	 */
	ScreenStatus(int screenStatusId, String displayName){
		this.screenStatusId = screenStatusId;
		this.displayName = displayName;
	}
	
	/**
	 * Lấy mã trạng thái
	 * @return mã trạng thái (int)
	 */
	public int getScreenStatusId() {
		return screenStatusId;
	}
	
	/**
	 * Lấy tên hiển thị của trạng thái phim
	 * @return displayName (String)
	 */
	public String getDisplayName() {
		return displayName;
	}
	
	/**
	 * Chuyển mã trạng thái thành enum ScreenStatus
	 * 0 - AVAILABLE: Hoạt động
	 * 1 - MAINTENANCE: Bảo trì
	 * 2 - UNVAILABLE: Ngưng hoạt động
	 * @param id
	 * @return status (ScreenStatus)
	 * @throws IllegalArgumentException nếu id không hợp lệ
	 */
	public static ScreenStatus fromId(int id) {
		for (ScreenStatus status : values()) {
			if (status.screenStatusId == id) {
				return status;
			}
		}
		throw new IllegalArgumentException("Mã trạng thái phong chiếu không hợp lệ: " + id);
	}
}
