package com.cinema.entity;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Đại diện cho rạp phim trong hệ thống
 * Chứa các thuộc tính định danh cho rạp phim
 * 
 * @author minhhuy (chính)
 */
public class Theater {
	private int theaterId;
	private String theaterName; //not null
	private String theaterAddress; //not null
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	/**
	 * Constructor mặc định, không truyền dữ liệu
	 */
	public Theater() {
		super();
	}

	/**
	 * Constructor sử dụng để truyền một rạp phim mới, dùng cho SQL với id tự tăng
	 * @param theaterName //tên rạp phim
	 */
	public Theater(String theaterName) {
		super();
		setTheaterName(theaterName);
	}

	/**
	 * Constructor khởi tạo với các thông tin bắt buộc
	 * @param theaterId mã rạp
	 * @param theaterName tên rạp
	 */
	public Theater(int theaterId, String theaterName) {
		super();
		this.theaterId = theaterId;
		setTheaterName(theaterName);
	}

	/**
	 * Constructor đầy đủ
	 * @param theaterId
	 * @param theaterName
	 * @param theaterAddress
	 * @param createdAt
	 * @param updatedAt
	 */
	public Theater(int theaterId, String theaterName, String theaterAddress, LocalDateTime createdAt,
			LocalDateTime updatedAt) {
		super();
		this.theaterId = theaterId;
		this.theaterName = theaterName;
		this.theaterAddress = theaterAddress;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public int getTheaterId() {
		return theaterId;
	}

	public String getTheaterName() {
		return theaterName;
	}

	public String getTheaterAddress() {
		return theaterAddress;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setTheaterName(String theaterName) {
		this.theaterName = theaterName;
	}

	public void setTheaterAddress(String theaterAddress) {
		this.theaterAddress = theaterAddress;
	}

	@Override
	public int hashCode() {
		return Objects.hash(theaterId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Theater other = (Theater) obj;
		if (this.theaterId <= 0 || other.theaterId <= 0)
			return false;
		return theaterId == other.theaterId;
	}

	@Override
	public String toString() {
		return "Theater [theaterId=" + theaterId + ", theaterName=" + theaterName + ", theaterAddress=" + theaterAddress
				+ ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
	
	
}
