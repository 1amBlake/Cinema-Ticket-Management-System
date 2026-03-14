package com.cinema.entity;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Đại diện cho loại phòng chiếu trong hệ thống
 * Chứa các thuộc tính định danh cho loại phòng
 * 
 * @author minhhuy (chính)
 */
public class ScreenType {
	private int screenTypeId;
	private String screenTypeName;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	/**
	 * Constructor mặc đinh, không truyền dữ liệu
	 */
	public ScreenType() {
		super();
	}
	
	/**
	 * Constructor sử dụng để thêm đạo diễn mới với sql tự gia tăng id
	 * @param screenTypeName
	 */
	public ScreenType(String screenTypeName) {
		super();
		setScreenTypeName(screenTypeName);
	}

	/**
	 * Constructor khởi tạo với các thông tin bắt buộc
	 * @param screenTypeId
	 * @param screenTypeName
	 */
	public ScreenType(int screenTypeId, String screenTypeName) {
		super();
		this.screenTypeId = screenTypeId;
		setScreenTypeName(screenTypeName);
	}

	/**
	 * Khởi tạo constructor với các thông tin đầy đủ
	 * @param screenTypeId
	 * @param screenTypeName
	 * @param createdAt
	 * @param updatedAt
	 */
	public ScreenType(int screenTypeId, String screenTypeName, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.screenTypeId = screenTypeId;
		setScreenTypeName(screenTypeName);
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public int getScreenTypeId() {
		return screenTypeId;
	}

	public String getScreenTypeName() {
		return screenTypeName;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setScreenTypeName(String screenTypeName) {
		this.screenTypeName = screenTypeName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(screenTypeId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ScreenType other = (ScreenType) obj;
		if (this.screenTypeId <= 0 || other.screenTypeId <= 0)
			return false;
		return screenTypeId == other.screenTypeId;
	}

	@Override
	public String toString() {
		return "ScreenType [screenTypeId=" + screenTypeId + ", screenTypeName=" + screenTypeName + ", createdAt="
				+ createdAt + ", updatedAt=" + updatedAt + "]";
	}
	
	
}
