package com.cinema.entity;

import java.time.LocalDateTime;

public class TicketPricing {
	private int tickerPricingId;
	private	SeatType seatTypeId;
	private ScreenType screenTypeId;
	private double price;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	public TicketPricing() {
		// TODO Auto-generated constructor stub
		super();
	}

	public int getTickerPricingId() {
		return tickerPricingId;
	}

	public SeatType getSeatTypeId() {
		return seatTypeId;
	}

	public ScreenType getScreenTypeId() {
		return screenTypeId;
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

	public void setTickerPricingId(int tickerPricingId) {
		this.tickerPricingId = tickerPricingId;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "TicketPricing [tickerPricingId=" + tickerPricingId + ", seatTypeId=" + seatTypeId + ", screenTypeId="
				+ screenTypeId + ", price=" + price + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
	
}
