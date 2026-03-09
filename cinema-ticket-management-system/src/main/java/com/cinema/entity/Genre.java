package com.cinema.entity;

import java.time.LocalDateTime;

public class Genre {
	private int genreId;
	private String genreName;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	public Genre() {
		// TODO Auto-generated constructor stub
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public int getGenreId() {
		return genreId;
	}

	public String getGenreName() {
		return genreName;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	@Override
	public String toString() {
		return "Genre [genreId=" + genreId + ", genreName=" + genreName + ", createdAt=" + createdAt + ", updatedAt="
				+ updatedAt + "]";
	}
	
	
}
