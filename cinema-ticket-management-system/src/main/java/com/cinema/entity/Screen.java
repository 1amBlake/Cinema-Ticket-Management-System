package com.cinema.entity;

import java.time.LocalDateTime;

import com.cinema.enums.ScreenStatus;

public class Screen {
	private int screenId;
	private String screenName;
	private Theater theaterId;
	private ScreenType screenTypeId;
	private ScreenStatus screenStatus;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	public Screen() {
		// TODO Auto-generated constructor stub
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public ScreenStatus getScreenStatus() {
		return screenStatus;
	}

	public void setScreenStatus(ScreenStatus screenStatus) {
		this.screenStatus = screenStatus;
	}

	public int getScreenId() {
		return screenId;
	}

	public Theater getTheaterId() {
		return theaterId;
	}

	public ScreenType getScreenTypeId() {
		return screenTypeId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	@Override
	public String toString() {
		return "Screen [screenId=" + screenId + ", screenName=" + screenName + ", theaterId=" + theaterId
				+ ", screenTypeId=" + screenTypeId + ", screenStatus=" + screenStatus + ", createdAt=" + createdAt
				+ ", updatedAt=" + updatedAt + "]";
	}

}
