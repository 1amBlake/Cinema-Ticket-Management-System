package com.cinema.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.cinema.enums.MovieFormat;
import com.cinema.enums.MovieSessionStatus;

public class MovieSession {
	private int movieSessionId;
	private Movie movieId;
	private Screen screenId;
	private MovieFormat movieFormat;
	private LocalTime startsAt;
	private LocalTime endsAt;
	private MovieSessionStatus movieSessionStatus;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	public MovieSession() {
		// TODO Auto-generated constructor stub
		super();
	}

	public MovieFormat getMovieFormat() {
		return movieFormat;
	}

	public void setMovieFormat(MovieFormat movieFormat) {
		this.movieFormat = movieFormat;
	}

	public LocalTime getStartsAt() {
		return startsAt;
	}

	public void setStartsAt(LocalTime startsAt) {
		this.startsAt = startsAt;
	}

	public LocalTime getEndsAt() {
		return endsAt;
	}

	public void setEndsAt(LocalTime endsAt) {
		this.endsAt = endsAt;
	}

	public MovieSessionStatus getMovieSessionStatus() {
		return movieSessionStatus;
	}

	public void setMovieSessionStatus(MovieSessionStatus movieSessionStatus) {
		this.movieSessionStatus = movieSessionStatus;
	}

	public int getMovieSessionId() {
		return movieSessionId;
	}

	public Movie getMovieId() {
		return movieId;
	}

	public Screen getScreenId() {
		return screenId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	@Override
	public String toString() {
		return "MovieSession [movieSessionId=" + movieSessionId + ", movieId=" + movieId + ", screenId=" + screenId
				+ ", movieFormat=" + movieFormat + ", startsAt=" + startsAt + ", endsAt=" + endsAt
				+ ", movieSessionStatus=" + movieSessionStatus + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt
				+ "]";
	}

}
