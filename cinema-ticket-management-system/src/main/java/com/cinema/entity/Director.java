package com.cinema.entity;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Đại diện cho đạo diễn trong hệ thống.
 * Chứa các thuộc tính đạo diễn.
 * 
 * @author Minh Huy (chính, sửa ràng buộc)
 * @author Hải Anh (ràng buộc)
 */

public class Director { //đợi last check
	
	private int directorId; //Do database tự sinh
	private String directorName; //not null
	private LocalDateTime createdAt;//Do database tự sinh
	private LocalDateTime updatedAt;//Do database tự sinh
	
	/**
	 * Constructor mặc định, không có dữ liệu để truyền
	 */
	public Director() {
		super();
	}
	
	/**
	 * Constructor để thêm dữ liệu cho CSDL
	 * 
	 * @param directorName - Tên đạo diễn
	 */
	public Director(String directorName) {
		super();
		setDirectorName(directorName);
	}

	/**
	 * Constructor truyền dữ liệu với các thông tin "not null"
	 * 
	 * @param directorId - Mã đạo diễn
	 * @param directorName - Tên đạo diễn
	 */
	public Director(int directorId, String directorName) {
		super();
		this.directorId = directorId;
		setDirectorName(directorName);
	}
	
	/**
	 * Constructor đầy đủ thông rin
	 * @param directorId - Mã đạo diễn
	 * @param directorName - Tên đạo diễn
	 * @param createdAt - Thời điểm khởi tạo dữ liệu
	 * @param updatedAt - Thời điểm dữ liệu được cập nhật
	 */
	public Director(int directorId, String directorName, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.directorId = directorId;
		setDirectorName(directorName);
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public int getDirectorId() {
		return directorId;
	}

	public String getDirectorName() {
		return directorName;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setDirectorName(String directorName) {
		if(directorName == null || directorName.trim().isEmpty() || directorName.equals(""))
			throw new IllegalArgumentException("Đạo diễn không được bỏ trống");
		else
			this.directorName = directorName.trim();
	}
	
	/**
	 * Nếu đã có id hợp lệ thì hash theo id. Nếu chưa có id hợp lệ thì hash theo chính Object
	 */
	@Override
	public int hashCode() {
		return (directorId > 0) ? Integer.hashCode(directorId) : System.identityHashCode(this);
	}
	
	/**
	 * Hai đối tượng Director được xem là bằng nhau khi có cùng movieId hợp lệ
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Director other = (Director) obj;
		if (this.directorId <= 0 || other.directorId <= 0)
			return false;
		return directorId == other.directorId;
	}

	@Override
	public String toString() {
		return "Director [directorId=" + directorId + ", directorName=" + directorName + ", createdAt=" + createdAt
				+ ", updatedAt=" + updatedAt + "]";
	}
	
	
	
}
