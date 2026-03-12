package com.cinema.enums;

/**
 * Phân loại độ tuổi của phim.
 * 0 - P : Mọi lứa tuổi
 * 1 - K : Trẻ dưới 13 tuổi có thể xem được (có người giám hộ)
 * 2 - T13: Cấm khán giả dưới 13 tuổi
 * 3 - T16: Cấm khán giả dưới 16 tuổi
 * 4 - T18: Cấm khán giả dưới 18 tuổi
 * 5 - C: Cấm phổ biến
 * @author minhhuy
 */
public enum MovieAgeRating {
    P(0, "P"),
    K(1, "K"),
    T13(2, "T13"),
    T16(3, "T16"),
    T18(4, "T18"),
	C(5, "C");
	
    private final int ageRatingID;
    private final String displayMovieAgeRating;

    /**
     * Constructor
     * @param ageRatingId
     * @param displayName
     */
    MovieAgeRating(int ageRatingId, String displayName) {
        this.ageRatingID = ageRatingId;
        this.displayMovieAgeRating = displayName;
    }
    
    /**
     * Lấy mã/id của phần tử, dùng trên hệ thống và lưu trữ dưới database
     * 
     * @return int - mã
     */
    public int getMovieAgeRatingId() {
        return ageRatingID;
    }
    
    /**
     * Phương thức dùng để hiển thị giới hạn tuổi
     * @return String - Xếp loại tuổi
     */
    public String getDisplayMovieAgeRating() {
        return displayMovieAgeRating;
    }
    
    /**
     * Phương thúc để đổi id thành giá trị Enum tương ứng
     * @param arId
     * @return
     */
    public static MovieAgeRating getMovieAgeRatingFromId(int arId) {
        for (MovieAgeRating rating : values()) {
            if (rating.ageRatingID == arId) {
                return rating;
            }
        }
        throw new IllegalArgumentException("Mã giới hạn tuổi không hợp lệ: " + arId);
    }
}