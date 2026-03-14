package com.cinema.entity;

import java.time.LocalDateTime;
import java.util.Objects;
/**
 * Đại diện cho loại ghế trong hệ thống
 * Chứa các định nghĩa cho loại ghế
 * 
 * @author minhhuy (chính)
 */
public class SeatType {
	private int seatTypeId; //not null
	private String seatTypeName; //not null
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	/**
	 * Constructor mặc định, không truyền dữ liệu
	 */
	public SeatType() {
		super();
	}
	
	/**
	 * Constructor phải truyền đầy đủ dữ liệu
	 * @param seatTypeId
	 * @param seatTypeName
	 * @param createdAt
	 * @param updatedAt
	 */
	public SeatType(int seatTypeId, String seatTypeName, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.seatTypeId = seatTypeId;
		this.seatTypeName = seatTypeName;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	/**
	 * Constructor yêu cầu truyền các dữ liệu bắt buộc
	 * @param seatTypeId
	 * @param seatTypeName
	 */
	public SeatType(int seatTypeId, String seatTypeName) {
		super();
		this.seatTypeId = seatTypeId;
		this.seatTypeName = seatTypeName;
	}

	/**
	 * Constructor truyền dữ liêu vào SQL với Id tăng tự động
	 * @param seatTypeName
	 */
	public SeatType(String seatTypeName) {
		super();
		this.seatTypeName = seatTypeName;
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
		this.seatTypeName = seatTypeName;
	}


	@Override
	public int hashCode() {
		return Objects.hash(seatTypeId);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SeatType other = (SeatType) obj;
		if (this.seatTypeId <= 0 || other.seatTypeId <= 0)
			return false;
		return seatTypeId == other.seatTypeId;
	}


	@Override
	public String toString() {
		return "SeatType [seatTypeId=" + seatTypeId + ", seatTypeName=" + seatTypeName + ", createdAt=" + createdAt
				+ ", updatedAt=" + updatedAt + "]";
	}
	
}
