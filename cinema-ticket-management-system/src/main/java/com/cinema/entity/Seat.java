package com.cinema.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import com.cinema.enums.SeatStatus;

/**
 * Đại diên cho ghế của phòng chiếu trong hệ thóng
 * Chứa các định nghĩa, thuộc tính cơ bản của ghê
 * 
 * @author minhhuy (chính)
 */
public class Seat {
	private int seatId; //not null
	private Screen screenId; //not null
	private SeatType seatTypeId; //not null
	private String seatRow; //not null
	private String seatColl; //not null
	private SeatStatus seatStatus; //not null
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	/**
	 * Constructor mặc định, không truyền dữ liệu
	 */
	public Seat() {
		super();
	}
	
	/**
	 * Constructor truyền đầy đủ dữ liệu tham số
	 * @param seatId
	 * @param screenId
	 * @param seatTypeId
	 * @param seatRow
	 * @param seatColl
	 * @param seatStatus
	 * @param createdAt
	 * @param updatedAt
	 */
	public Seat(int seatId, Screen screenId, SeatType seatTypeId, String seatRow, String seatColl,
			SeatStatus seatStatus, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.seatId = seatId;
		this.screenId = screenId;
		this.seatTypeId = seatTypeId;
		this.seatRow = seatRow;
		this.seatColl = seatColl;
		this.seatStatus = seatStatus;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	/**
	 * Constructor truyền dữ liệu tham số cho SQL
	 * @param screenId
	 * @param seatTypeId
	 * @param seatRow
	 * @param seatColl
	 * @param seatStatus
	 */
	public Seat(Screen screenId, SeatType seatTypeId, String seatRow, String seatColl, SeatStatus seatStatus) {
		super();
		this.screenId = screenId;
		this.seatTypeId = seatTypeId;
		this.seatRow = seatRow;
		this.seatColl = seatColl;
		this.seatStatus = seatStatus;
	}

	/**
	 * Constructor truyền các dữ liệu tham số bắt buộc (not null)
	 * @param seatId
	 * @param screenId
	 * @param seatTypeId
	 * @param seatRow
	 * @param seatColl
	 * @param seatStatus
	 */
	public Seat(int seatId, Screen screenId, SeatType seatTypeId, String seatRow, String seatColl,
			SeatStatus seatStatus) {
		super();
		this.seatId = seatId;
		this.screenId = screenId;
		this.seatTypeId = seatTypeId;
		this.seatRow = seatRow;
		this.seatColl = seatColl;
		this.seatStatus = seatStatus;
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

	public String getSeatRow() {
		return seatRow;
	}

	public String getSeatColl() {
		return seatColl;
	}

	public SeatStatus getSeatStatus() {
		return seatStatus;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setScreenId(Screen screenId) {
		this.screenId = screenId;
	}

	public void setSeatTypeId(SeatType seatTypeId) {
		this.seatTypeId = seatTypeId;
	}

	public void setSeatRow(String seatRow) {
		this.seatRow = seatRow;
	}

	public void setSeatColl(String seatColl) {
		this.seatColl = seatColl;
	}

	public void setSeatStatus(SeatStatus seatStatus) {
		this.seatStatus = seatStatus;
	}


	@Override
	public int hashCode() {
		return Objects.hash(seatColl);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Seat other = (Seat) obj;
		if (this.seatId <= 0 || other.seatId <= 0)
			return false;
		return Objects.equals(seatColl, other.seatColl);
	}


	@Override
	public String toString() {
		return "Seat [seatId=" + seatId + ", screenId=" + screenId + ", seatTypeId=" + seatTypeId + ", seatRow="
				+ seatRow + ", seatColl=" + seatColl + ", seatStatus=" + seatStatus + ", createdAt=" + createdAt
				+ ", updatedAt=" + updatedAt + "]";
	}
}
