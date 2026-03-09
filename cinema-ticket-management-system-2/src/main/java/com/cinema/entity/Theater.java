package com.cinema.entity;

import java.time.LocalDateTime;

public class Theater {
	private int theaterId;
	private String theaterName;
	private String theaterAddress;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	public Theater() {
		// TODO Auto-generated constructor stub
	}

	public String getTheaterName() {
		return theaterName;
	}

	public void setTheaterName(String theaterName) {
		this.theaterName = theaterName;
	}

	public String getTheaterAddress() {
		return theaterAddress;
	}

	public void setTheaterAddress(String theaterAddress) {
		this.theaterAddress = theaterAddress;
	}

	public int getTheaterId() {
		return theaterId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	
	
}
