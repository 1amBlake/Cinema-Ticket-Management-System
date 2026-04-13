package com.cinema.enums;

/**
 * Phân loại ngôn ngữ trình chiếu của suất chiếu.
 * 0 - ORIGINAL: Bản gốc
 * 1 - DUBBED: Lồng tiếng
 * 2 - VOICE_OVER: Thuyết minh
 * 3 - SUBTITLED: Phụ đề
 * 
 * @author Minh Huy (chính)
 */
public enum MovieFormat {
	ORIGINAL(0, "Bản gốc"),
	DUBBED(1, "Lồng tiếng"),
	VOICE_OVER(2, "Thuyết minh"),
	SUBTITLED(3, "Phụ đề");

	private final int movieFormatId;
	private final String displayName;

	/**
	 * Khởi tạo định dạng trình chiếu của phim.
	 * 
	 * @param movieFormatId - Mã định dạng trình chiếu
	 * @param displayName - Tên hiển thị định dạng trình chiếu
	 */
	MovieFormat(int movieFormatId, String displayName) {
		this.movieFormatId = movieFormatId;
		this.displayName = displayName;
	}

	/**
	 * Lấy mã định dạng trình chiếu.
	 * 
	 * @return mã định dạng trình chiếu
	 */
	public int getMovieFormatId() {
		return movieFormatId;
	}

	/**
	 * Lấy tên hiển thị của định dạng trình chiếu.
	 * 
	 * @return tên hiển thị định dạng trình chiếu
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Chuyển mã định dạng thành enum MovieFormat.
	 * 0 - ORIGINAL: Bản gốc
	 * 1 - DUBBED: Lồng tiếng
	 * 2 - VOICE_OVER: Thuyết minh
	 * 3 - SUBTITLED: Phụ đề
	 * 
	 * @param id - Mã định dạng trình chiếu
	 * @return định dạng trình chiếu tương ứng
	 * @throws IllegalArgumentException nếu id không hợp lệ
	 */
	public static MovieFormat fromId(int id) {
		for (MovieFormat format : values()) {
			if (format.movieFormatId == id) {
				return format;
			}
		}
		throw new IllegalArgumentException("Mã định dạng phim không hợp lệ: " + id);
	}
}