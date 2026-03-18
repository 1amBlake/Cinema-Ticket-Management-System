package com.cinema.enums;

/**
 * Được dùng để định nghĩa cho trạng thái suất chiếu
 * 0 - CANCELLED: Hủy
 * 1 - UPCOMING: sắp chiếu (đã lên lịch)
 * 2 - NOW_SHOWING: đang chiếu
 * 3 - FINISHED: đã kết thúc
 * 
 * @author minhhuy (chính)
 */
public enum MovieSessionStatus {
	CANCELLED(0, "Hủy"),
	UPCOMING(1, "Sắp Chiếu"),
	NOW_SHOWING(2, "Đang Chiếu"),
	FINISHED(3, "Kết Thúc");
	
	private final int movieSessionStatusId;
	private final String displayName;
	
	/**
	 * Khởi tạo trạng thái suất chiếu
	 * 
	 * @param movieSessionStatusId mã trạng thái
	 * @param displayName tên hiển thị của trạng thái
	 */
	MovieSessionStatus(int movieSessionStatusId, String displayName){
		this.movieSessionStatusId = movieSessionStatusId;
		this.displayName = displayName;
	}
	
	/**
	 * Lấy mã trạng thái suất chiếu
	 * 
	 * @return movieSessionStatusId (int)
	 */
	public int getMovieSessionStatusId() {
		return movieSessionStatusId;
	}
	
	/**
	 * Lấy tên hiện thị của của trạng thái suất chiếu
	 * 
	 * @return displayName (String)
	 */
	public String getDisplayName() {
		return displayName;
	}
	
	/**
	 * Tìm trạng thái suất chiếu thông qua mã 
	 * @param id mã trạng thái (int)
	 * @return status (enums)
	 */
	public static MovieSessionStatus fromId (int id) {
		for (MovieSessionStatus status : values()) {
			return status;
		}
		throw new IllegalArgumentException("Mã định dạng trạng thái suất chiếu không hợp lệ!");
	}
}
