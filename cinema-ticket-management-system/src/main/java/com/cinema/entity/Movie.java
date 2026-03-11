package com.cinema.entity; //TODO: chưa xong

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import com.cinema.enums.MovieAgeRating;
import com.cinema.enums.MovieStatus;

/**
 * Đại diện cho phim trong hệ thống
 * Chứa các thuộc tính cơ bản của một phim
 * @author minhhuy
 */

public class Movie {
	private int movieId; //not null
	private String movieName; //not null
	private int movieDuration; //not null
	private LocalDate movieReleaseDate;
	private String movieLanguage;
	private MovieStatus movieStatus; //enums - not null
	private MovieAgeRating movieAgeRating; //enums - not null
	private String pictureUrl;
	private LocalDateTime createdAt; //tự khởi tạo dưới mysql
	private LocalDateTime updatedAt; //tự khơi tạo dưới mysql
	
	/**
	 *constructor default, không truyền dữ liệu
	 */
	public Movie() {
		super();
	}
	
	/**
	 * Khởi tạo phim với các thông tin bắt buộc.
	 *
	 * @param movieId mã phim
	 * @param movieName tên phim
	 * @param movieDuration thời lượng phim
	 * @param movieStatus trạng thái phim
	 * @param movieAgeRating độ tuổi giới hạn của phim
	 */
	public Movie(int movieId, String movieName, int movieDuration, MovieStatus movieStatus,
			MovieAgeRating movieAgeRating) {
		this.movieId = movieId;
		setMovieName(movieName);
		setMovieDuration(movieDuration);
		setMovieStatus(movieStatus);
		setMovieAgeRating(movieAgeRating);
	}

	/**
	 * Khởi tạo phim với đầy đủ thông tin.
	 *
	 * @param movieId mã phim
	 * @param movieName tên phim
	 * @param movieDuration thời lượng phim
	 * @param movieReleaseDate ngày khởi chiếu
	 * @param movieLanguage ngôn ngữ phim
	 * @param movieStatus trạng thái phim
	 * @param movieAgeRating độ tuổi giới hạn
	 * @param pictureUrl đường dẫn hình ảnh
	 * @param createdAt thời điểm tạo dữ liệu
	 * @param updatedAt thời điểm cập nhật dữ liệu
	 */
	public Movie(int movieId, String movieName, int movieDuration, LocalDate movieReleaseDate, String movieLanguage,
			MovieStatus movieStatus, MovieAgeRating movieAgeRating, String pictureUrl, LocalDateTime createdAt,
			LocalDateTime updatedAt) {
		setMovieName(movieName);
		setMovieDuration(movieDuration);
		setMovieReleaseDate(movieReleaseDate);
		setMovieLanguage(movieLanguage);
		setMovieStatus(movieStatus);
		setMovieAgeRating(movieAgeRating);
		setPictureUrl(pictureUrl);
	}

	public int getMovieId() {
		return movieId;
	}

	public String getMovieName() {
		return movieName;
	}

	public int getMovieDuration() {
		return movieDuration;
	}

	public LocalDate getMovieReleaseDate() {
		return movieReleaseDate;
	}

	public String getMovieLanguage() {
		return movieLanguage;
	}

	public MovieStatus getMovieStatus() {
		return movieStatus;
	}

	public MovieAgeRating getMovieAgeRating() {
		return movieAgeRating;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setMovieName(String movieName) {
		if (movieName == null || movieName.trim().isEmpty()) {
			throw new IllegalArgumentException("movieName không được để trống");
		}
		this.movieName = movieName.trim();
	}

	public void setMovieDuration(int movieDuration) {
		this.movieDuration = movieDuration;
	}

	public void setMovieReleaseDate(LocalDate movieReleaseDate) {
		this.movieReleaseDate = movieReleaseDate;
	}

	public void setMovieLanguage(String movieLanguage) {
		this.movieLanguage = movieLanguage;
	}

	public void setMovieStatus(MovieStatus movieStatus) {
		this.movieStatus = movieStatus;
	}

	public void setMovieAgeRating(MovieAgeRating movieAgeRating) {
		this.movieAgeRating = movieAgeRating;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	/**
	 * Trả về mã băm dựa trên movieId.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(movieId);
	}
	
	/**
	 * Hai đối tượng Movie được xem là bằng nhau khi có cùng movieId hợp lệ.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Movie other = (Movie) obj;

		if (this.movieId <= 0 || other.movieId <= 0)
			return false;

		return this.movieId == other.movieId;
	}

	@Override
	public String toString() {
		return "Movie [movieId=" + movieId + ", movieName=" + movieName + ", movieDuration=" + movieDuration
				+ ", movieReleaseDate=" + movieReleaseDate + ", movieLanguage=" + movieLanguage + ", movieStatus="
				+ movieStatus + ", movieAgeRating=" + movieAgeRating + ", pictureUrl=" + pictureUrl + ", createdAt="
				+ createdAt + ", updatedAt=" + updatedAt + "]";
	}
}