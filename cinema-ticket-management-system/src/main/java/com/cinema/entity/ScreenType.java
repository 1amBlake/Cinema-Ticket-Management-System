package com.cinema.entity;

import java.time.LocalDateTime;

public class ScreenType {
	private int screenTypeId;
	private String screenTypeName;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	public ScreenType() {
		// TODO Auto-generated constructor stub
		super();
	}

	public String getScreenTypeName() {
		return screenTypeName;
	}

	public void setScreenTypeName(String screenTypeName) {
		this.screenTypeName = screenTypeName;
	}

	public int getScreenTypeId() {
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
		return "ScreenType [screenTypeId=" + screenTypeId + ", screenTypeName=" + screenTypeName + ", createdAt="
				+ createdAt + ", updatedAt=" + updatedAt + "]";
	}
	
	
}
