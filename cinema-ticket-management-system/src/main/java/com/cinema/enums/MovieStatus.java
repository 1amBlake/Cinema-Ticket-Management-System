package com.cinema.enums;

/**
 * Phân loại trạng thái của phim
 * 0 - COMMINGSOON: Phim sắp được chiếu
 * 1 - SHOWING: Phim đang được chiếu
 * 3 - STOPPED: Phim đã bị ngừng chiếu
 * @author minhhuy
 */
public enum MovieStatus {
	COMMINGSOON(0, "Sắp Chiếu"),
	SHOWING(1, "Đang Chiếu"),
	STOPPED(2, "Dừng Chiếu");
	
	private final int movieStatusId;
	private final String displayMovieStatus;
	
	/**
	 * Constructor
	 * @param movieStatusId
	 * @param displayMovieStatus
	 */
	MovieStatus(int movieStatusId, String displayMovieStatus){
		this.movieStatusId = movieStatusId;
		this.displayMovieStatus = displayMovieStatus;
	}
	
	/**
	 * Lấy mã id của trạng thái phim
	 * @return int movieStatusId
	 */
	public int getMovieStatusId() {
		return movieStatusId;
	}
	
	/**
	 * Dùng để hiển thị trạng thái phim.
	 * Ví dụ: "Sắp Chiếu"
	 * @return String displayMovieStatus
	 */
	public String getDisplayMovieStatus() {
		return displayMovieStatus;
	}

	/**
	 * Dùng để lấy được trạng thái phim thông qua id trạng thái
	 * @param msId
	 * @return MovieStatus status
	 */
	public static MovieStatus getMovieStatusFromId(int msId) {
		for (MovieStatus status : values()) {
			if (status.movieStatusId == msId)
				return status;
		}
		throw new IllegalArgumentException("Mã trạng thái phim không hợp lệ!");
	}
}
