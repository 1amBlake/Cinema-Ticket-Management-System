package com.cinema.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import com.cinema.enums.ScreenStatus;

/**
 * Đại diện cho phòng chiếu trong hệ thống
 * Chứa các thuộc tính cơ bản của phòng chiếu
 * 
 * @author minhhuy (chính)
 */
public class Screen {
	private int screenId; //not null
	private String screenName; //not null
	private Theater theaterId; //not null
	private ScreenType screenTypeId; //not null
	private ScreenStatus screenStatus; //not null
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	/**
	 * Constructor mặc đinh, không truyền dữ liệu
	 */
	public Screen() {
		super();
	}
	
	/**
	 * Constructor cần truyền đầy đủ dữ liệu
	 * 
	 * @param screenId
	 * @param screenName
	 * @param theaterId
	 * @param screenTypeId
	 * @param screenStatus
	 * @param createdAt
	 * @param updatedAt
	 */
	public Screen(int screenId, String screenName, Theater theaterId, ScreenType screenTypeId,
			ScreenStatus screenStatus, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.screenId = screenId;
		setScreenName(screenName);
		setTheaterId(theaterId);
		setScreenTypeId(screenTypeId);
		setScreenStatus(screenStatus);
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	/**
	 * Constructor cho việc tạo đối tượng với Id gia tăng tự động trong SQL
	 * @param screenName
	 * @param theaterId
	 * @param screenTypeId
	 * @param screenStatus
	 */
	public Screen(String screenName, Theater theaterId, ScreenType screenTypeId, ScreenStatus screenStatus) {
		super();
		setScreenName(screenName);
		setTheaterId(theaterId);
		setScreenTypeId(screenTypeId);
		setScreenStatus(screenStatus);
	}

	/**
	 * Constructor truyền những dữ liệu bắt buộc
	 * @param screenId
	 * @param screenName
	 * @param theaterId
	 * @param screenTypeId
	 * @param screenStatus
	 */
	public Screen(int screenId, String screenName, Theater theaterId, ScreenType screenTypeId,
			ScreenStatus screenStatus) {
		super();
		this.screenId = screenId;
		setScreenName(screenName);
		setTheaterId(theaterId);
		setScreenTypeId(screenTypeId);
		setScreenStatus(screenStatus);
	}

	public int getScreenId() {
		return screenId;
	}

	public String getScreenName() {
		return screenName;
	}

	public Theater getTheaterId() {
		return theaterId;
	}

	public ScreenType getScreenTypeId() {
		return screenTypeId;
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
		this.screenName = screenName;
	}

	public void setTheaterId(Theater theaterId) {
		this.theaterId = theaterId;
	}

	public void setScreenTypeId(ScreenType screenTypeId) {
		this.screenTypeId = screenTypeId;
	}

	public void setScreenStatus(ScreenStatus screenStatus) {
		this.screenStatus = screenStatus;
	}

	@Override
	public int hashCode() {
		return Objects.hash(screenId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Screen other = (Screen) obj;
		if (this.screenId <= 0 || other.screenId <= 0)
			return false;
		return screenId == other.screenId;
	}
	
	
}
