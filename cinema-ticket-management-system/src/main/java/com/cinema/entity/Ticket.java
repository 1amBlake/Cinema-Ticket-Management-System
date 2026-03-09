package com.cinema.entity;

import java.time.LocalDateTime;

import com.cinema.enums.TicketStatus;

public class Ticket {
	private int ticketId;
	private MovieSession movieSessionId;
	private Seat seatId;
	private TicketPricing ticketPricingId;
	private TicketStatus ticketStatus;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	public Ticket() {
		// TODO Auto-generated constructor stub
	}
//TODO: còn thiếu getter/setter, chưa có tostring
	
}
