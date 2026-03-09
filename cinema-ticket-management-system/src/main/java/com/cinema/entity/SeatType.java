package com.cinema.entity;

import java.time.LocalDateTime;

public class SeatType {
	private int seatTypeId;
	private String seatTypeName;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	public SeatType() {
		// TODO Auto-generated constructor stub
		super();
	}

	public String getSeatTypeName() {
		return seatTypeName;
	}

	public void setSeatTypeName(String seatTypeName) {
		this.seatTypeName = seatTypeName;
	}

	public int getSeatTypeId() {
		return seatTypeId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	@Override
	public String toString() {
		return "SeatType [seatTypeId=" + seatTypeId + ", seatTypeName=" + seatTypeName + ", createdAt=" + createdAt
				+ ", updatedAt=" + updatedAt + "]";
	}
	
}
