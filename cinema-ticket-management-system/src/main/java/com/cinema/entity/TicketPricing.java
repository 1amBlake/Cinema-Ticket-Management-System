package com.cinema.entity;

import java.time.LocalDateTime;

/**
 * Đại diện cho bảng giá vé trong hệ thống.
 * Chứa các thuộc tính bảng giá vé.
 * 
 * @author Hải Anh (chính)
 */
public class TicketPricing {

	private int ticketPricingId; //Do database tự sinh
	private SeatType seatType; //not null
	private ScreenType screenType; //not null
	private double price; //not null
	private LocalDateTime createdAt; //Do database tự sinh
	private LocalDateTime updatedAt; //Do database tự sinh

	/**
	 * Constructor mặc định, không có dữ liệu để truyền
	 */
	public TicketPricing() {
		super();
	}

	/**
	 * Constructor để khởi tạo thực thể theo mã, phục vụ truy vấn và ánh xạ quan hệ
	 * 
	 * @param ticketPricingId - Mã bảng giá vé
	 */
	public TicketPricing(int ticketPricingId) {
		super();
		this.ticketPricingId = ticketPricingId;
	}

	/**
	 * Constructor để thêm dữ liệu cho CSDL (các dữ liệu not null)
	 * 
	 * @param seatType - Loại ghế
	 * @param screenType - Loại phòng chiếu
	 * @param price - Giá vé
	 */
	public TicketPricing(SeatType seatType, ScreenType screenType, double price) {
		super();
		setSeatType(seatType);
		setScreenType(screenType);
		setPrice(price);
	}

	/**
	 * Constructor truyền dữ liệu với các thông tin "not null"
	 * 
	 * @param ticketPricingId - Mã bảng giá vé
	 * @param seatType - Loại ghế
	 * @param screenType - Loại phòng chiếu
	 * @param price - Giá vé
	 */
	public TicketPricing(int ticketPricingId, SeatType seatType, ScreenType screenType, double price) {
		super();
		this.ticketPricingId = ticketPricingId;
		setSeatType(seatType);
		setScreenType(screenType);
		setPrice(price);
	}

	/**
	 * Constructor đầy đủ thông tin
	 * 
	 * @param ticketPricingId - Mã bảng giá vé
	 * @param seatType - Loại ghế
	 * @param screenType - Loại phòng chiếu
	 * @param price - Giá vé
	 * @param createdAt - Thời điểm khởi tạo dữ liệu
	 * @param updatedAt - Thời điểm dữ liệu được cập nhật
	 */
	public TicketPricing(int ticketPricingId, SeatType seatType, ScreenType screenType, double price,
			LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.ticketPricingId = ticketPricingId;
		setSeatType(seatType);
		setScreenType(screenType);
		setPrice(price);
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public int getTicketPricingId() {
		return ticketPricingId;
	}

	public SeatType getSeatType() {
		return seatType;
	}

	public ScreenType getScreenType() {
		return screenType;
	}

	public double getPrice() {
		return price;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setSeatType(SeatType seatType) {
		if (seatType == null)
			throw new IllegalArgumentException("seatType không được null");
		this.seatType = seatType;
	}

	public void setScreenType(ScreenType screenType) {
		if (screenType == null)
			throw new IllegalArgumentException("screenType không được null");
		this.screenType = screenType;
	}

	public void setPrice(double price) {
		if (price <= 0)
			throw new IllegalArgumentException("price phải > 0");
		this.price = price;
	}

	/**
	 * Nếu đã có id hợp lệ thì hash theo id. Nếu chưa có id hợp lệ thì hash theo chính object
	 */
	@Override
	public int hashCode() {
		return (ticketPricingId > 0) ? Integer.hashCode(ticketPricingId) : System.identityHashCode(this);
	}

	/**
	 * Hai Object TicketPricing được xem là bằng nhau khi có cùng ticketPricingId hợp lệ
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		TicketPricing other = (TicketPricing) obj;
		if (this.ticketPricingId <= 0 || other.ticketPricingId <= 0)
			return false;
		return this.ticketPricingId == other.ticketPricingId;
	}

	@Override
	public String toString() {
		return "TicketPricing [ticketPricingId=" + ticketPricingId + ", seatType=" + seatType + ", screenType="
				+ screenType + ", price=" + price + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
}