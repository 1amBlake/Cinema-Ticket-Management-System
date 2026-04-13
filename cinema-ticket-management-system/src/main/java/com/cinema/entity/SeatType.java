package com.cinema.entity;

import java.time.LocalDateTime;

/**
 * Đại diện cho loại ghế trong hệ thống.
 * Chứa các thuộc tính loại ghế.
 * 
 * @author Minh Huy (chính)
 */
public class SeatType {

	private int seatTypeId; //Do database tự sinh
	private String seatTypeName; //not null
	private LocalDateTime createdAt; //Do database tự sinh
	private LocalDateTime updatedAt; //Do database tự sinh

	/**
	 * Constructor mặc định, không có dữ liệu để truyền
	 */
	public SeatType() {
		super();
	}

	/**
	 * Constructor để khởi tạo thực thể theo mã, phục vụ truy vấn và ánh xạ quan hệ
	 * 
	 * @param seatTypeId - Mã loại ghế
	 */
	public SeatType(int seatTypeId) {
		super();
		this.seatTypeId = seatTypeId;
	}

	/**
	 * Constructor để thêm dữ liệu cho CSDL
	 * 
	 * @param seatTypeName - Tên loại ghế
	 */
	public SeatType(String seatTypeName) {
		super();
		setSeatTypeName(seatTypeName);
	}

	/**
	 * Constructor truyền dữ liệu với các thông tin "not null"
	 * 
	 * @param seatTypeId - Mã loại ghế
	 * @param seatTypeName - Tên loại ghế
	 */
	public SeatType(int seatTypeId, String seatTypeName) {
		super();
		this.seatTypeId = seatTypeId;
		setSeatTypeName(seatTypeName);
	}

	/**
	 * Constructor đầy đủ thông tin
	 * 
	 * @param seatTypeId - Mã loại ghế
	 * @param seatTypeName - Tên loại ghế
	 * @param createdAt - Thời điểm khởi tạo dữ liệu
	 * @param updatedAt - Thời điểm dữ liệu được cập nhật
	 */
	public SeatType(int seatTypeId, String seatTypeName, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.seatTypeId = seatTypeId;
		setSeatTypeName(seatTypeName);
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public int getSeatTypeId() {
		return seatTypeId;
	}

	public String getSeatTypeName() {
		return seatTypeName;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setSeatTypeName(String seatTypeName) {
		if (seatTypeName == null || seatTypeName.trim().isEmpty())
			throw new IllegalArgumentException("seatTypeName không được để trống");
		else if (seatTypeName.trim().length() > 255)
			throw new IllegalArgumentException("seatTypeName không được vượt quá 255 ký tự");
		this.seatTypeName = seatTypeName.trim();
	}

	/**
	 * Nếu đã có id hợp lệ thì hash theo id. Nếu chưa có id hợp lệ thì hash theo chính object
	 */
	@Override
	public int hashCode() {
		return (seatTypeId > 0) ? Integer.hashCode(seatTypeId) : System.identityHashCode(this);
	}

	/**
	 * Hai Object SeatType được xem là bằng nhau khi có cùng seatTypeId hợp lệ
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		SeatType other = (SeatType) obj;
		if (this.seatTypeId <= 0 || other.seatTypeId <= 0)
			return false;
		return this.seatTypeId == other.seatTypeId;
	}

	@Override
	public String toString() {
		return "SeatType [seatTypeId=" + seatTypeId + ", seatTypeName=" + seatTypeName + ", createdAt=" + createdAt
				+ ", updatedAt=" + updatedAt + "]";
	}
}