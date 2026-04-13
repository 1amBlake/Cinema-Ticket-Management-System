package com.cinema.entity;

import java.time.LocalDateTime;

import com.cinema.enums.SeatStatus;

/**
 * Đại diện cho ghế trong hệ thống.
 * Chứa các thuộc tính ghế.
 * 
 * @author Minh Huy (chính)
 */
public class Seat {

	private int seatId; // Do database tự sinh
	private Screen screen; // not null
	private SeatType seatType; // not null
	private String seatRow; // not null
	private String seatCol; // not null
	private SeatStatus seatStatus; // not null
	private LocalDateTime createdAt; // Do database tự sinh
	private LocalDateTime updatedAt; // Do database tự sinh

	/**
	 * Constructor mặc định, không có dữ liệu để truyền
	 */
	public Seat() {
		super();
	}

	/**
	 * Constructor để khởi tạo thực thể theo mã, phục vụ truy vấn và ánh xạ quan hệ
	 * 
	 * @param seatId - Mã ghế
	 */
	public Seat(int seatId) {
		super();
		this.seatId = seatId;
	}

	/**
	 * Constructor để thêm dữ liệu cho CSDL (các dữ liệu not null)
	 * 
	 * @param screen - Phòng chiếu
	 * @param seatType - Loại ghế
	 * @param seatRow - Hàng ghế
	 * @param seatCol - Cột ghế
	 * @param seatStatus - Trạng thái ghế
	 */
	public Seat(Screen screen, SeatType seatType, String seatRow, String seatCol, SeatStatus seatStatus) {
		super();
		setScreen(screen);
		setSeatType(seatType);
		setSeatRow(seatRow);
		setSeatCol(seatCol);
		setSeatStatus(seatStatus);
	}

	/**
	 * Constructor truyền dữ liệu với các thông tin "not null"
	 * 
	 * @param seatId - Mã ghế
	 * @param screen - Phòng chiếu
	 * @param seatType - Loại ghế
	 * @param seatRow - Hàng ghế
	 * @param seatCol - Cột ghế
	 * @param seatStatus - Trạng thái ghế
	 */
	public Seat(int seatId, Screen screen, SeatType seatType, String seatRow, String seatCol, SeatStatus seatStatus) {
		super();
		this.seatId = seatId;
		setScreen(screen);
		setSeatType(seatType);
		setSeatRow(seatRow);
		setSeatCol(seatCol);
		setSeatStatus(seatStatus);
	}

	/**
	 * Constructor đầy đủ thông tin
	 * 
	 * @param seatId - Mã ghế
	 * @param screen - Phòng chiếu
	 * @param seatType - Loại ghế
	 * @param seatRow - Hàng ghế
	 * @param seatCol - Cột ghế
	 * @param seatStatus - Trạng thái ghế
	 * @param createdAt - Thời điểm khởi tạo dữ liệu
	 * @param updatedAt - Thời điểm dữ liệu được cập nhật
	 */
	public Seat(int seatId, Screen screen, SeatType seatType, String seatRow, String seatCol, SeatStatus seatStatus,
			LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.seatId = seatId;
		setScreen(screen);
		setSeatType(seatType);
		setSeatRow(seatRow);
		setSeatCol(seatCol);
		setSeatStatus(seatStatus);
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public int getSeatId() {
		return seatId;
	}

	public Screen getScreen() {
		return screen;
	}

	public SeatType getSeatType() {
		return seatType;
	}

	public String getSeatRow() {
		return seatRow;
	}

	public String getSeatCol() {
		return seatCol;
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

	public void setScreen(Screen screen) {
		if (screen == null)
			throw new IllegalArgumentException("screen không được null");
		this.screen = screen;
	}

	public void setSeatType(SeatType seatType) {
		if (seatType == null)
			throw new IllegalArgumentException("seatType không được null");
		this.seatType = seatType;
	}

	public void setSeatRow(String seatRow) {
		if (seatRow == null || seatRow.trim().isEmpty())
			throw new IllegalArgumentException("seatRow không được để trống");
		else if (seatRow.trim().length() > 10)
			throw new IllegalArgumentException("seatRow không được vượt quá 10 ký tự");
		this.seatRow = seatRow.trim();
	}

	public void setSeatCol(String seatCol) {
		if (seatCol == null || seatCol.trim().isEmpty())
			throw new IllegalArgumentException("seatCol không được để trống");
		else if (seatCol.trim().length() > 10)
			throw new IllegalArgumentException("seatCol không được vượt quá 10 ký tự");
		this.seatCol = seatCol.trim();
	}

	public void setSeatStatus(SeatStatus seatStatus) {
		if (seatStatus == null)
			throw new IllegalArgumentException("seatStatus không được null");
		this.seatStatus = seatStatus;
	}

	/**
	 * Nếu đã có id hợp lệ thì hash theo id. Nếu chưa có id hợp lệ thì hash theo chính object
	 */
	@Override
	public int hashCode() {
		return (seatId > 0) ? Integer.hashCode(seatId) : System.identityHashCode(this);
	}

	/**
	 * Hai Object Seat được xem là bằng nhau khi có cùng seatId hợp lệ
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Seat other = (Seat) obj;
		if (this.seatId <= 0 || other.seatId <= 0)
			return false;
		return this.seatId == other.seatId;
	}

	@Override
	public String toString() {
		return "Seat [seatId=" + seatId + ", screen=" + screen + ", seatType=" + seatType + ", seatRow=" + seatRow
				+ ", seatCol=" + seatCol + ", seatStatus=" + seatStatus + ", createdAt=" + createdAt + ", updatedAt="
				+ updatedAt + "]";
	}
}