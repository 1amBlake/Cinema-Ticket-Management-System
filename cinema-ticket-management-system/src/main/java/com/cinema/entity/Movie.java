package com.cinema.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.cinema.enums.MovieAgeRating;
import com.cinema.enums.MovieStatus;


public class Movie {
	private int movieId;
	private String movieName;
	private int movieDuration;
	private LocalDate movieReleaseDate;
	private String movieLanguage;
	private MovieStatus movieStatus; //TODO: chưa làm enums
	private MovieAgeRating movieAgeRating; //TODO: chưa làm enums
	private String pictureUrl;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	public Movie() {
		
	}


	public int getMovieId() {
		return movieId;
	}

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	public int getMovieDuration() {
		return movieDuration;
	}

	public void setMovieDuration(int movieDuration) {
		this.movieDuration = movieDuration;
	}

	public LocalDate getMovieReleaseDate() {
		return movieReleaseDate;
	}

	public void setMovieReleaseDate(LocalDate movieReleaseDate) {
		this.movieReleaseDate = movieReleaseDate;
	}

	public String getMovieLanguages() {
		return movieLanguage;
	}

	public void setMovieLanguages(String movieLanguages) {
		this.movieLanguage = movieLanguages;
	}

	public MovieStatus getMovieStatus() {
		return movieStatus;
	}

	public void setMovieStatus(MovieStatus movieStatus) {
		this.movieStatus = movieStatus;
	}

	public MovieAgeRating getMovieAgeRating() {
		return movieAgeRating;
	}

	public void setMovieAgeRating(MovieAgeRating movieAgeRating) {
		this.movieAgeRating = movieAgeRating;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureURL) {
		this.pictureUrl = pictureURL;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	//TODO: chưa làm get/set
	
	@Override
	public String toString() {
		return "Movie [movieId=" + movieId + ", movieName=" + movieName + ", movieDuration=" + movieDuration
				+ ", movieReleaseDate=" + movieReleaseDate + ", movieLanguages=" + movieLanguage + ", movieStatus="
				+ movieStatus + ", movieAgeRating=" + movieAgeRating + ", pictureURL=" + pictureUrl + ", createdAt="
				+ createdAt + ", updatedAt=" + updatedAt + "]";
	}
	
}
