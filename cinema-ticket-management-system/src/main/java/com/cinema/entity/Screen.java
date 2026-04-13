package com.cinema.entity;

import java.time.LocalDateTime;

import com.cinema.enums.ScreenStatus;

/**
 * Đại diện cho phòng chiếu trong hệ thống.
 * Chứa các thuộc tính phòng chiếu.
 * 
 * @author Minh Huy (chính)
 */
public class Screen {

	private int screenId; // Do database tự sinh
	private String screenName; // not null
	private Theater theater; // not null
	private ScreenType screenType; // not null
	private ScreenStatus screenStatus; // not null
	private LocalDateTime createdAt; // Do database tự sinh
	private LocalDateTime updatedAt; // Do database tự sinh

	/**
	 * Constructor mặc định, không có dữ liệu để truyền
	 */
	public Screen() {
		super();
	}

	/**
	 * Constructor để khởi tạo thực thể theo mã, phục vụ truy vấn và ánh xạ quan hệ
	 * 
	 * @param screenId - Mã phòng chiếu
	 */
	public Screen(int screenId) {
		super();
		this.screenId = screenId;
	}

	/**
	 * Constructor để thêm dữ liệu cho CSDL (các dữ liệu not null)
	 * 
	 * @param screenName - Tên phòng chiếu
	 * @param theater - Rạp phim
	 * @param screenType - Loại phòng chiếu
	 * @param screenStatus - Trạng thái phòng chiếu
	 */
	public Screen(String screenName, Theater theater, ScreenType screenType, ScreenStatus screenStatus) {
		super();
		setScreenName(screenName);
		setTheater(theater);
		setScreenType(screenType);
		setScreenStatus(screenStatus);
	}

	/**
	 * Constructor truyền dữ liệu với các thông tin "not null"
	 * 
	 * @param screenId - Mã phòng chiếu
	 * @param screenName - Tên phòng chiếu
	 * @param theater - Rạp phim
	 * @param screenType - Loại phòng chiếu
	 * @param screenStatus - Trạng thái phòng chiếu
	 */
	public Screen(int screenId, String screenName, Theater theater, ScreenType screenType, ScreenStatus screenStatus) {
		super();
		this.screenId = screenId;
		setScreenName(screenName);
		setTheater(theater);
		setScreenType(screenType);
		setScreenStatus(screenStatus);
	}

	/**
	 * Constructor đầy đủ thông tin
	 * 
	 * @param screenId - Mã phòng chiếu
	 * @param screenName - Tên phòng chiếu
	 * @param theater - Rạp phim
	 * @param screenType - Loại phòng chiếu
	 * @param screenStatus - Trạng thái phòng chiếu
	 * @param createdAt - Thời điểm khởi tạo dữ liệu
	 * @param updatedAt - Thời điểm dữ liệu được cập nhật
	 */
	public Screen(int screenId, String screenName, Theater theater, ScreenType screenType, ScreenStatus screenStatus,
			LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.screenId = screenId;
		setScreenName(screenName);
		setTheater(theater);
		setScreenType(screenType);
		setScreenStatus(screenStatus);
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public int getScreenId() {
		return screenId;
	}

	public String getScreenName() {
		return screenName;
	}

	public Theater getTheater() {
		return theater;
	}

	public ScreenType getScreenType() {
		return screenType;
	}

	public ScreenStatus getScreenStatus() {
		return screenStatus;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setScreenName(String screenName) {
		if (screenName == null || screenName.trim().isEmpty())
			throw new IllegalArgumentException("screenName không được để trống");
		else if (screenName.trim().length() > 255)
			throw new IllegalArgumentException("screenName không được vượt quá 255 ký tự");
		this.screenName = screenName.trim();
	}

	public void setTheater(Theater theater) {
		if (theater == null)
			throw new IllegalArgumentException("theater không được null");
		this.theater = theater;
	}

	public void setScreenType(ScreenType screenType) {
		if (screenType == null)
			throw new IllegalArgumentException("screenType không được null");
		this.screenType = screenType;
	}

	public void setScreenStatus(ScreenStatus screenStatus) {
		if (screenStatus == null)
			throw new IllegalArgumentException("screenStatus không được null");
		this.screenStatus = screenStatus;
	}

	/**
	 * Nếu đã có id hợp lệ thì hash theo id. Nếu chưa có id hợp lệ thì hash theo chính object
	 */
	@Override
	public int hashCode() {
		return (screenId > 0) ? Integer.hashCode(screenId) : System.identityHashCode(this);
	}

	/**
	 * Hai Object Screen được xem là bằng nhau khi có cùng screenId hợp lệ
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Screen other = (Screen) obj;
		if (this.screenId <= 0 || other.screenId <= 0)
			return false;
		return this.screenId == other.screenId;
	}

	@Override
	public String toString() {
		return "Screen [screenId=" + screenId + ", screenName=" + screenName
				+ ", theaterId=" + (theater != null ? theater.getTheaterId() : null)
				+ ", screenTypeId=" + (screenType != null ? screenType.getScreenTypeId() : null)
				+ ", screenStatus=" + screenStatus + ", createdAt=" + createdAt
				+ ", updatedAt=" + updatedAt + "]";
	}
}