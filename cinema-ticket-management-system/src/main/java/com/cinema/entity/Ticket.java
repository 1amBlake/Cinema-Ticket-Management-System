package com.cinema.entity;

import java.time.LocalDateTime;

import com.cinema.enums.TicketStatus;

/**
 * Đại diện cho vé trong hệ thống.
 * Chứa các thuộc tính vé.
 * 
 * @author Hải Anh (chính)
 */
public class Ticket {

	private int ticketId; //Do database tự sinh
	private MovieSession movieSession; //not null
	private Seat seat; //not null
	private TicketPricing ticketPricing; //not null
	private TicketStatus ticketStatus; //not null
	private LocalDateTime createdAt; //Do database tự sinh
	private LocalDateTime updatedAt; //Do database tự sinh

	/**
	 * Constructor mặc định, không có dữ liệu để truyền
	 */
	public Ticket() {
		super();
	}

	/**
	 * Constructor để khởi tạo thực thể theo mã, phục vụ truy vấn và ánh xạ quan hệ
	 * 
	 * @param ticketId - Mã vé
	 */
	public Ticket(int ticketId) {
		super();
		this.ticketId = ticketId;
	}

	/**
	 * Constructor để thêm dữ liệu cho CSDL (các dữ liệu not null)
	 * 
	 * @param movieSession - Suất chiếu
	 * @param seat - Ghế
	 * @param ticketPricing - Bảng giá vé
	 * @param ticketStatus - Trạng thái vé
	 */
	public Ticket(MovieSession movieSession, Seat seat, TicketPricing ticketPricing, TicketStatus ticketStatus) {
		super();
		setMovieSession(movieSession);
		setSeat(seat);
		setTicketPricing(ticketPricing);
		setTicketStatus(ticketStatus);
	}

	/**
	 * Constructor truyền dữ liệu với các thông tin "not null"
	 * 
	 * @param ticketId - Mã vé
	 * @param movieSession - Suất chiếu
	 * @param seat - Ghế
	 * @param ticketPricing - Bảng giá vé
	 * @param ticketStatus - Trạng thái vé
	 */
	public Ticket(int ticketId, MovieSession movieSession, Seat seat, TicketPricing ticketPricing,
			TicketStatus ticketStatus) {
		super();
		this.ticketId = ticketId;
		setMovieSession(movieSession);
		setSeat(seat);
		setTicketPricing(ticketPricing);
		setTicketStatus(ticketStatus);
	}

	/**
	 * Constructor đầy đủ thông tin
	 * 
	 * @param ticketId - Mã vé
	 * @param movieSession - Suất chiếu
	 * @param seat - Ghế
	 * @param ticketPricing - Bảng giá vé
	 * @param ticketStatus - Trạng thái vé
	 * @param createdAt - Thời điểm khởi tạo dữ liệu
	 * @param updatedAt - Thời điểm dữ liệu được cập nhật
	 */
	public Ticket(int ticketId, MovieSession movieSession, Seat seat, TicketPricing ticketPricing,
			TicketStatus ticketStatus, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.ticketId = ticketId;
		setMovieSession(movieSession);
		setSeat(seat);
		setTicketPricing(ticketPricing);
		setTicketStatus(ticketStatus);
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public int getTicketId() {
		return ticketId;
	}

	public MovieSession getMovieSession() {
		return movieSession;
	}

	public Seat getSeat() {
		return seat;
	}

	public TicketPricing getTicketPricing() {
		return ticketPricing;
	}

	public TicketStatus getTicketStatus() {
		return ticketStatus;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setMovieSession(MovieSession movieSession) {
		if (movieSession == null)
			throw new IllegalArgumentException("movieSession không được null");
		this.movieSession = movieSession;
	}

	public void setSeat(Seat seat) {
		if (seat == null)
			throw new IllegalArgumentException("seat không được null");
		this.seat = seat;
	}

	public void setTicketPricing(TicketPricing ticketPricing) {
		if (ticketPricing == null)
			throw new IllegalArgumentException("ticketPricing không được null");
		this.ticketPricing = ticketPricing;
	}

	public void setTicketStatus(TicketStatus ticketStatus) {
		if (ticketStatus == null)
			throw new IllegalArgumentException("ticketStatus không được null");
		this.ticketStatus = ticketStatus;
	}

	/**
	 * Nếu đã có id hợp lệ thì hash theo id. Nếu chưa có id hợp lệ thì hash theo chính object
	 */
	@Override
	public int hashCode() {
		return (ticketId > 0) ? Integer.hashCode(ticketId) : System.identityHashCode(this);
	}

	/**
	 * Hai Object Ticket được xem là bằng nhau khi có cùng ticketId hợp lệ
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Ticket other = (Ticket) obj;
		if (this.ticketId <= 0 || other.ticketId <= 0)
			return false;
		return this.ticketId == other.ticketId;
	}

	@Override
	public String toString() {
		return "Ticket [ticketId=" + ticketId + ", movieSession=" + movieSession + ", seat=" + seat
				+ ", ticketPricing=" + ticketPricing + ", ticketStatus=" + ticketStatus + ", createdAt=" + createdAt
				+ ", updatedAt=" + updatedAt + "]";
	}
}