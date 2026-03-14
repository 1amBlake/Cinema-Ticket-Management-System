package com.cinema.enums;

/**
 * Phân loại trạng thái của ghế trong phòng chiếu
 * 0 - AVAILABLE: Hoạt động
 * 1 - MAINTENANCE: Bảo trì
 * 
 * @author minhhuy(chính)
 */
public enum SeatStatus {
	AVAILABLE(0, "Hoạt Động"),
	MAINTENANCE(1, "Bảo Trì");
	
	private final int seatStatusId;
	private final String displayName;
	
	/**
	 * Khởi tạo trạng thái ghế trong phòng chiếu
	 * 
	 * @param seatStatusId
	 * @param displayName
	 */
	SeatStatus(int seatStatusId, String displayName){
		this.seatStatusId = seatStatusId;
		this.displayName = displayName;
	}
	
	/**
	 * Lấy mã trạng thái
	 * @return seatStatusId (int)
	 */
	public int getSeatStatusId() {
		return seatStatusId;
	}
	
	/**
	 * Lấy tên hiển thị của trạng thái ghế
	 * @return displayName (String)
	 */
	public String getDisplayName() {
		return displayName;
	}
	
	/**
	 * Phân loại trạng thái của ghế trong phòng chiếu
	 * 0 - AVAILABLE: Hoạt động
	 * 1 - MAINTENANCE: Bảo trì
	 * 
	 * @param id
	 * @return status (SeatStatus)
	 * @throws IllegalArgumentException nếu id không hợp lệ
	 */
	public static SeatStatus fromId(int id) {
		for (SeatStatus status : values()) {
			if (status.seatStatusId == id)
				return status;
		}
		throw new IllegalArgumentException("Mã trạng thái ghế không hợp lệ: "+id);
	}
}
