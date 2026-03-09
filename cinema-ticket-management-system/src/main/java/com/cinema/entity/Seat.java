package com.cinema.entity;

import java.time.LocalDateTime;

import com.cinema.enums.SeatStatus;

public class Seat {
	private int seatId;
	private Screen screenId;
	private SeatType seatTypeId;
	private String seatRow;
	private String seatColl;
	private SeatStatus seatStatus;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	public Seat() {
		// TODO Auto-generated constructor stub
		super();
	}

	public String getSeatRow() {
		return seatRow;
	}

	public void setSeatRow(String seatRow) {
		this.seatRow = seatRow;
	}

	public String getSeatColl() {
		return seatColl;
	}

	public void setSeatColl(String seatColl) {
		this.seatColl = seatColl;
	}

	public int getSeatId() {
		return seatId;
	}

	public Screen getScreenId() {
		return screenId;
	}

	public SeatType getSeatTypeId() {
		return seatTypeId;
	}

	public SeatStatus getSeatStatus() {
		return seatStatus;
	}

	public void setSeatStatus(SeatStatus seatStatus) {
		this.seatStatus = seatStatus;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	@Override
	public String toString() {
		return "Seat [seatId=" + seatId + ", screenId=" + screenId + ", seatTypeId=" + seatTypeId + ", seatRow="
				+ seatRow + ", seatColl=" + seatColl + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}

}
