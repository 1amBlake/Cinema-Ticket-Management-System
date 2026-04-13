package com.cinema.entity;

import java.time.LocalDateTime;

/**
 * Đại diện cho loại phòng chiếu trong hệ thống.
 * Chứa các thuộc tính loại phòng chiếu.
 * 
 * @author Minh Huy (chính, ràng buộc)
 */
public class ScreenType {

	private int screenTypeId; //Do database tự sinh
	private String screenTypeName; //not null
	private LocalDateTime createdAt; //Do database tự sinh
	private LocalDateTime updatedAt; //Do database tự sinh

	/**
	 * Constructor mặc định, không có dữ liệu để truyền
	 */
	public ScreenType() {
		super();
	}
	
	/**
	 * Constructor để khởi tạo thực thể theo mã, phục vụ truy vấn và ánh xạ quan hệ
	 * 
	 * @param screenTypeId - Mã loại phòng chiếu
	 */
	public ScreenType(int screenTypeId) {
		super();
		this.screenTypeId = screenTypeId;
	}

	/**
	 * Constructor để thêm dữ liệu cho CSDL
	 * 
	 * @param screenTypeName - Tên loại phòng chiếu
	 */
	public ScreenType(String screenTypeName) {
		super();
		setScreenTypeName(screenTypeName);
	}

	/**
	 * Constructor truyền dữ liệu với các thông tin "not null"
	 * 
	 * @param screenTypeId - Mã loại phòng chiếu
	 * @param screenTypeName - Tên loại phòng chiếu
	 */
	public ScreenType(int screenTypeId, String screenTypeName) {
		super();
		this.screenTypeId = screenTypeId;
		setScreenTypeName(screenTypeName);
	}

	/**
	 * Constructor đầy đủ thông tin
	 * 
	 * @param screenTypeId - Mã loại phòng chiếu
	 * @param screenTypeName - Tên loại phòng chiếu
	 * @param createdAt - Thời điểm khởi tạo dữ liệu
	 * @param updatedAt - Thời điểm dữ liệu được cập nhật
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
		if (screenTypeName == null || screenTypeName.trim().isEmpty())
			throw new IllegalArgumentException("screenTypeName không được để trống");
		else if (screenTypeName.trim().length() > 100)
			throw new IllegalArgumentException("screenTypeName không được vượt quá 100 ký tự");
		this.screenTypeName = screenTypeName.trim();
	}

	/**
	 * Nếu đã có id hợp lệ thì hash theo id. Nếu chưa có id hợp lệ thì hash theo chính object
	 */
	@Override
	public int hashCode() {
		return (screenTypeId > 0) ? Integer.hashCode(screenTypeId) : System.identityHashCode(this);
	}

	/**
	 * Hai Object ScreenType được xem là bằng nhau khi có cùng screenTypeId hợp lệ
	 */
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