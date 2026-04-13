package com.cinema.entity;

import java.time.LocalDateTime;

/**
 * Đại diện cho rạp phim trong hệ thống.
 * Chứa các thuộc tính rạp phim.
 * 
 * @author Minh Huy (chính, ràng buộc)
 */
public class Theater {
	private int theaterId; //Do database tự sinh
	private String theaterName; //not null
	private String theaterAddress;
	private LocalDateTime createdAt; //Do database tự sinh
	private LocalDateTime updatedAt; //Do database tự sinh
	
	/**
	 * Constructor mặc định, không có dữ liệu để truyền
	 */
	public Theater() {
		super();
	}

	/**
	 * Constructor để khởi tạo thực thể theo mã, phục vụ truy vấn và ánh xạ quan hệ
	 * 
	 * @param theaterId - Mã rạp phim
	 */
	public Theater(int theaterId) {
		super();
		this.theaterId = theaterId;
	}
	
	/**
	 * Constructor để thêm dữ liệu cho CSDL (các dữ liệu not null)
	 * 
	 * @param theaterName - Tên rạp phim
	 */
	public Theater(String theaterName) {
		super();
		setTheaterName(theaterName);
	}
	
	/**
	 * Constructor để thêm dữ liệu cho CSDL (tất cả dữ liệu)
	 * 
	 * @param theaterName - Tên rạp phim
	 * @param theaterAddress - Địa chỉ rạp phim
	 */
	public Theater(String theaterName, String theaterAddress) {
		super();
		setTheaterName(theaterName);
		setTheaterAddress(theaterAddress);
	}

	/**
	 * Constructor truyền dữ liệu với các thông tin "not null"
	 * 
	 * @param theaterId - Mã rạp phim
	 * @param theaterName - Tên rạp phim
	 */
	public Theater(int theaterId, String theaterName) {
		super();
		this.theaterId = theaterId;
		setTheaterName(theaterName);
	}

	/**
	 * Constructor đầy đủ thông tin
	 * @param theaterId - Mã rạp phim
	 * @param theaterName - Tên rạp phim
	 * @param theaterAddress - Địa chỉ rạp phim
	 * @param createdAt - Thời điểm khởi tạo dữ liệu
	 * @param updatedAt - Thời điểm dữ liệu được cập nhật
	 */
	public Theater(int theaterId, String theaterName, String theaterAddress, LocalDateTime createdAt,
			LocalDateTime updatedAt) {
		super();
		this.theaterId = theaterId;
		setTheaterName(theaterName);
		setTheaterAddress(theaterAddress);
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
		if (theaterName == null || theaterName.trim().isEmpty())
			throw new IllegalArgumentException("theaterName không được để trống");
		else if (theaterName.trim().length() > 255)
			throw new IllegalArgumentException("theaterName không được vượt quá 255 ký tự");
		this.theaterName = theaterName.trim();
	}

	public void setTheaterAddress(String theaterAddress) {
	    if (theaterAddress == null || theaterAddress.trim().isEmpty()) {
	        this.theaterAddress = null;
	        return;
	    }
	    if (theaterAddress.trim().length() > 255) {
	        throw new IllegalArgumentException("theaterAddress không được vượt quá 255 ký tự");
	    }
	    this.theaterAddress = theaterAddress.trim();
	}

	/**
	 * Nếu đã có id hợp lệ thì hash theo id. Nếu chưa có id hợp lệ thì hash theo chính object
	 */
	@Override
	public int hashCode() {
		return (theaterId > 0) ? Integer.hashCode(theaterId) : System.identityHashCode(this);
	}

	/**
	 * Hai Object Theater được xem là bằng nhau khi có cùng theaterId hợp lệ
	 */
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
