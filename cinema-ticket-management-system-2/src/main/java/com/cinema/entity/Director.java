package com.cinema.entity;

import java.time.LocalDateTime;

public class Director {
	private int directorId;
	private String directorName;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	public Director() {
		super();
	}

	public String getDirectorName() {
		return directorName;
	}

	public void setDirectorName(String directorName) {
		this.directorName = directorName;
	}

	public int getDirectorId() {
		return directorId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	@Override
	public String toString() {
		return "Director [directorId=" + directorId + ", directorName=" + directorName + ", createdAt=" + createdAt
				+ ", updatedAt=" + updatedAt + "]";
	}
	
	
}
