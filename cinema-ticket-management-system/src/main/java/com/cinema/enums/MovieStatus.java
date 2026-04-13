package com.cinema.enums;

/**
 * Phân loại trạng thái của phim.
 * 0 - COMING_SOON: Phim sắp chiếu
 * 1 - SHOWING: Phim đang chiếu
 * 2 - DISCONTINUED: Phim dừng chiếu
 * 
 * @author Minh Huy (chính)
 */
public enum MovieStatus {
    COMING_SOON(0, "Sắp Chiếu"),
    SHOWING(1, "Đang Chiếu"),
    DISCONTINUED(2, "Dừng Chiếu");

    private final int movieStatusId;
    private final String displayName;

    /**
     * Khởi tạo trạng thái phim.
     * 
     * @param movieStatusId - Mã trạng thái
     * @param displayName - Tên hiển thị trạng thái
     */
    MovieStatus(int movieStatusId, String displayName) {
        this.movieStatusId = movieStatusId;
        this.displayName = displayName;
    }

    /**
     * Lấy mã trạng thái phim.
     * 
     * @return mã trạng thái
     */
    public int getMovieStatusId() {
        return movieStatusId;
    }

    /**
     * Lấy tên hiển thị của trạng thái phim.
     * Ví dụ: "Sắp Chiếu".
     * 
     * @return tên hiển thị trạng thái
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Chuyển mã trạng thái thành enum MovieStatus.
     * 0 - COMING_SOON: Phim sắp chiếu
     * 1 - SHOWING: Phim đang chiếu
     * 2 - DISCONTINUED: Phim dừng chiếu
     * @param id - Mã trạng thái
     * @return trạng thái phim tương ứng
     * @throws IllegalArgumentException nếu id không hợp lệ
     */
    public static MovieStatus fromId(int id) {
        for (MovieStatus status : values()) {
            if (status.movieStatusId == id) {
                return status;
            }
        }
        throw new IllegalArgumentException("Mã trạng thái phim không hợp lệ: " + id);
    }
}