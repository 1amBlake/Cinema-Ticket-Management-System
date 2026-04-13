package com.cinema.enums;

/**
 * Phân loại trạng thái của suất chiếu.
 * 0 - CANCELLED: Suất chiếu bị hủy
 * 1 - UPCOMING: Suất chiếu sắp chiếu
 * 2 - NOW_SHOWING: Suất chiếu đang chiếu
 * 3 - FINISHED: Suất chiếu đã kết thúc
 * 
 * @author Minh Huy (chính)
 */
public enum MovieSessionStatus {
	CANCELLED(0, "Hủy"),
	UPCOMING(1, "Sắp chiếu"),
	NOW_SHOWING(2, "Đang chiếu"),
	FINISHED(3, "Kết thúc");

	private final int movieSessionStatusId;
	private final String displayName;

	/**
	 * Khởi tạo trạng thái suất chiếu.
	 * 
	 * @param movieSessionStatusId - Mã trạng thái suất chiếu
	 * @param displayName - Tên hiển thị trạng thái suất chiếu
	 */
	MovieSessionStatus(int movieSessionStatusId, String displayName) {
		this.movieSessionStatusId = movieSessionStatusId;
		this.displayName = displayName;
	}

	/**
	 * Lấy mã trạng thái suất chiếu.
	 * 
	 * @return mã trạng thái suất chiếu
	 */
	public int getMovieSessionStatusId() {
		return movieSessionStatusId;
	}

	/**
	 * Lấy tên hiển thị của trạng thái suất chiếu.
	 * 
	 * @return tên hiển thị trạng thái suất chiếu
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Chuyển mã trạng thái thành enum MovieSessionStatus.
	 * 0 - CANCELLED: Suất chiếu bị hủy
	 * 1 - UPCOMING: Suất chiếu sắp chiếu
	 * 2 - NOW_SHOWING: Suất chiếu đang chiếu
	 * 3 - FINISHED: Suất chiếu đã kết thúc
	 * 
	 * @param id - Mã trạng thái suất chiếu
	 * @return trạng thái suất chiếu tương ứng
	 * @throws IllegalArgumentException nếu id không hợp lệ
	 */
	public static MovieSessionStatus fromId(int id) {
		for (MovieSessionStatus status : values()) {
			if (status.movieSessionStatusId == id) {
				return status;
			}
		}
		throw new IllegalArgumentException("Mã trạng thái suất chiếu không hợp lệ: " + id);
	}
}