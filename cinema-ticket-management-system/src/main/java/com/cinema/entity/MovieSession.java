package com.cinema.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

import com.cinema.enums.MovieFormat;
import com.cinema.enums.MovieSessionStatus;

/**
 * Đại diện cho suât chiếu trong hệ thống
 * Chúa các thuộc tính cở bản của một suất chiếu
 * 
 * @author minhhuy (chính)
 */
public class MovieSession {
	private int movieSessionId; //not null
	private Movie movieId; //not null
	private Screen screenId; //not null
	private MovieFormat movieFormat; //not null
	private LocalTime startsAt; //not null
	private LocalTime endsAt; //not null
	private MovieSessionStatus movieSessionStatus; //not null
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	/**
	 * Constructor mặc định, không dùng để truyền dữ liệu
	 */
	public MovieSession() {
		super();
	}

	/**
	 * Constructor truyền tất cả dữ liệu
	 * 
	 * @param movieSessionId mã suất chiếu
	 * @param movieId mã phim
	 * @param screenId mã phòng chiếu
	 * @param movieFormat mã định dạng phim
	 * @param startsAt thời gian bắt đầu
	 * @param endsAt thời gian kết thúc
	 * @param movieSessionStatus trạng thái suất chiếu
	 * @param createdAt thời gian tạo dữ liệu
	 * @param updatedAt thờ gian dữ liệu được cập nhật
	 */
	public MovieSession(int movieSessionId, Movie movieId, Screen screenId, MovieFormat movieFormat, LocalTime startsAt,
			LocalTime endsAt, MovieSessionStatus movieSessionStatus, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.movieSessionId = movieSessionId;
		setMovieId(movieId);
		setScreenId(screenId);
		setMovieFormat(movieFormat);
		setStartsAt(startsAt);
		setEndsAt(endsAt);
		setMovieSessionStatus(movieSessionStatus);
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	/**
	 * Constructor sử dụng cho SQL
	 * 
	 * @param movieId mã phim
	 * @param screenId mã phòng chiếu
	 * @param movieFormat mã định dạng phim
	 * @param startsAt thời gian bắt đầu
	 * @param endsAt thời gian kết thúc
	 * @param movieSessionStatus trạng thái suất chiếu
	 */
	public MovieSession(Movie movieId, Screen screenId, MovieFormat movieFormat, LocalTime startsAt, LocalTime endsAt,
			MovieSessionStatus movieSessionStatus) {
		super();
		setMovieId(movieId);
		setScreenId(screenId);
		setMovieFormat(movieFormat);
		setStartsAt(startsAt);
		setEndsAt(endsAt);
		setMovieSessionStatus(movieSessionStatus);
	}

	/**
	 * Constuctor đầy đủ thông tin bắt buộc
	 * 
	 * @param movieSessionId mã suất chiếu
	 * @param movieId mã phim
	 * @param screenId mã phòng chiếu
	 * @param movieFormat mã định dạng phim
	 * @param startsAt thời gian bắt đầu
	 * @param endsAt thời gian kết thúc
	 * @param movieSessionStatus trạng thái suất chiếu
	 */
	public MovieSession(int movieSessionId, Movie movieId, Screen screenId, MovieFormat movieFormat, LocalTime startsAt,
			LocalTime endsAt, MovieSessionStatus movieSessionStatus) {
		super();
		this.movieSessionId = movieSessionId;
		setMovieId(movieId);
		setScreenId(screenId);
		setMovieFormat(movieFormat);
		setStartsAt(startsAt);
		setEndsAt(endsAt);
		setMovieSessionStatus(movieSessionStatus);
	}


	public Movie getMovieId() {
		return movieId;
	}

	public void setMovieId(Movie movieId) {
		this.movieId = movieId;
	}

	public Screen getScreenId() {
		return screenId;
	}

	public void setScreenId(Screen screenId) {
		this.screenId = screenId;
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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	@Override
	public int hashCode() {
		return Objects.hash(movieSessionId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MovieSession other = (MovieSession) obj;
		return movieSessionId == other.movieSessionId;
	}

	@Override
	public String toString() {
		return "MovieSession [movieSessionId=" + movieSessionId + ", movieId=" + movieId + ", screenId=" + screenId
				+ ", movieFormat=" + movieFormat + ", startsAt=" + startsAt + ", endsAt=" + endsAt
				+ ", movieSessionStatus=" + movieSessionStatus + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt
				+ "]";
	}
	
}
