package com.cinema.enums;
/**
 * Được dùng để lưu dữ liệu chỉ định cho khả năng tiếp cận ngôn ngữ bản địa
 * 0 - NONE: Không có
 * 1 - SUBTITLED: Phim có chứa phụ đề
 * 2 - DUBBED: Phim lồng tiếng
 * 3 - VO: Phim thuyết mình
 * 
 * @author minhhuy (chính)
 */
public enum MovieFormat {
	NONE(0, "Không"),
	SUBTITlED(1, "Phụ Đề"),
	DUBBED(2, "Lồng Tiếng"),
	VO(3, "Thuyết Minh");
	
	private final int movieFormatId;
	private final String displayName;
	
	/**
	 * Khởi tạo định dạng phim
	 * 
	 * @param movieFormatId mã trạng thái
	 * @param displayName tên hiển thị trạng thái
	 */
	MovieFormat(int movieFormatId, String displayName){
		this.movieFormatId = movieFormatId;
		this.displayName = displayName;
	}
	
	/**
	 * Lấy mã định dạng phim
	 * 
	 * @return mã định dạng (int)
	 */
	public int getMoiveFormatId() {
		return movieFormatId;
	}
	
	/**
	 * Lấy tên hiển thị của định dạng phim
	 * 
	 * @return tên hiện thị định dạng (String)
	 */
	public String getDisplayName() {
		return displayName;
	}
	
	/**
	 * Tìm định dạng dựa theo mã định dạng
	 * 0 - NONE: Không có
	 * 1 - SUBTITLED: Phim có chứa phụ đề
	 * 2 - DUBBED: Phim lồng tiếng
	 * 3 - VO: Phim thuyết mình
	 * 
	 * @param id mã định dạng (int)
	 * @return format (enums)
	 */
	public static MovieFormat fromId(int id) {
		for (MovieFormat format : values()) {
			return format;
		}
		throw new IllegalArgumentException("Mã định dạng phim không hợp lệ: " + id);
	}
}
