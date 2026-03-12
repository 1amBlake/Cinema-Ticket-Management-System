package com.cinema.enums;

/**
 * Phân loại độ tuổi của phim.
 * 0 - P: Mọi lứa tuổi
 * 1 - K: Trẻ dưới 13 tuổi xem cùng người giám hộ
 * 2 - T13: Cấm khán giả dưới 13 tuổi
 * 3 - T16: Cấm khán giả dưới 16 tuổi
 * 4 - T18: Cấm khán giả dưới 18 tuổi
 * 5 - C: Cấm phổ biến
 * 
 * @author minhhuy (chính)
 */
public enum MovieAgeRating {
    P(0, "P"),
    K(1, "K"),
    T13(2, "T13"),
    T16(3, "T16"),
    T18(4, "T18"),
    C(5, "C");

    private final int ageRatingId;
    private final String displayName;

    /**
     * Khởi tạo phân loại độ tuổi của phim.
     * 
     * @param ageRatingId mã phân loại độ tuổi
     * @param displayName tên hiển thị
     */
    MovieAgeRating(int ageRatingId, String displayName) {
        this.ageRatingId = ageRatingId;
        this.displayName = displayName;
    }

    /**
     * Lấy mã phân loại độ tuổi.
     * 
     * @return mã phân loại độ tuổi
     */
    public int getAgeRatingId() {
        return ageRatingId;
    }

    /**
     * Lấy tên hiển thị của phân loại độ tuổi.
     * Ví dụ: "T13".
     * 
     * @return tên hiển thị
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Chuyển mã phân loại độ tuổi thành enum tương ứng.
     * 0 - P: Mọi lứa tuổi
     * 1 - K: Trẻ dưới 13 tuổi xem cùng người giám hộ
     * 2 - T13: Cấm khán giả dưới 13 tuổi
     * 3 - T16: Cấm khán giả dưới 16 tuổi
     * 4 - T18: Cấm khán giả dưới 18 tuổi
     * 5 - C: Cấm phổ biến
     * @param id mã phân loại độ tuổi
     * @return giá trị MovieAgeRating tương ứng
     * @throws IllegalArgumentException nếu id không hợp lệ
     */
    public static MovieAgeRating fromId(int id) {
        for (MovieAgeRating rating : values()) {
            if (rating.ageRatingId == id) {
                return rating;
            }
        }
        throw new IllegalArgumentException("Mã giới hạn tuổi không hợp lệ: " + id);
    }
}