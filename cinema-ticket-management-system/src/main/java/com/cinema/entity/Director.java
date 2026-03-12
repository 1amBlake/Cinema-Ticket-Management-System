package com.cinema.entity; //TODO: chưa làm validate

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Đại diện cho đạo diễn của phim trong hệ thống
 * Chứa các thuộc tính định danh đạo diễn
 * 
 * @author minhhuy (chính)
 */

public class Director {
	private int directorId; //not null
	private String directorName; //not null
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	/**
	 * constructor mặc định, không truyền dữ liệu
	 */
	public Director() {
		super();
	}
	
	/**
	 * Constructor sử dụng để thêm một đạo diễn mới, id sẽ tư tăng trong mysql
	 * @param directorName tên đạo diễn
	 */
	public Director(String directorName) {
		super();
		setDirectorName(directorName);
	}

	/**
	 * Constructor khởi tạo với các thông tin bắt buộc
	 * 
	 * @param directorId mã đạo diễn
	 * @param directorName tên đạo diễn
	 */
	public Director(int directorId, String directorName) {
		super();
		this.directorId = directorId;
		setDirectorName(directorName);
	}
	
	/**
	 * Constructor đầy đủ
	 * @param directorId
	 * @param directorName
	 * @param createdAt
	 * @param updatedAt
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
		this.directorName = directorName;
	}
	
	/**
	 * Trả về hashcode dựa trên directorId
	 */
	@Override
	public int hashCode() {
		return Objects.hash(directorId);
	}
	
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
