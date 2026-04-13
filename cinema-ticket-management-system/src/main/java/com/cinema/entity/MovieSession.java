package com.cinema.entity;

import java.time.LocalDateTime;

import com.cinema.enums.MovieFormat;
import com.cinema.enums.MovieSessionStatus;

/**
 * Đại diện cho suất chiếu trong hệ thống.
 * Chứa các thuộc tính suất chiếu.
 * 
 * @author Minh Huy (chính)
 */
public class MovieSession {

	private int movieSessionId; //Do database tự sinh
	private Movie movie; //not null
	private Screen screen; //not null
	private MovieFormat movieFormat; //not null
	private LocalDateTime startsAt; //not null
	private LocalDateTime endsAt; //not null
	private MovieSessionStatus movieSessionStatus; //not null
	private LocalDateTime createdAt; //Do database tự sinh
	private LocalDateTime updatedAt; //Do database tự sinh

	/**
	 * Constructor mặc định, không có dữ liệu để truyền
	 */
	public MovieSession() {
		super();
	}

	/**
	 * Constructor để khởi tạo thực thể theo mã, phục vụ truy vấn và ánh xạ quan hệ
	 * 
	 * @param movieSessionId - Mã suất chiếu
	 */
	public MovieSession(int movieSessionId) {
		super();
		this.movieSessionId = movieSessionId;
	}

	/**
	 * Constructor để thêm dữ liệu cho CSDL (các dữ liệu not null)
	 * 
	 * @param movie - Phim
	 * @param screen - Phòng chiếu
	 * @param movieFormat - Định dạng trình chiếu
	 * @param startsAt - Thời gian bắt đầu
	 * @param endsAt - Thời gian kết thúc
	 * @param movieSessionStatus - Trạng thái suất chiếu
	 */
	public MovieSession(Movie movie, Screen screen, MovieFormat movieFormat,
			LocalDateTime startsAt, LocalDateTime endsAt, MovieSessionStatus movieSessionStatus) {
		super();
		setMovie(movie);
		setScreen(screen);
		setMovieFormat(movieFormat);
		setStartsAt(startsAt);
		setEndsAt(endsAt);
		setMovieSessionStatus(movieSessionStatus);
	}

	/**
	 * Constructor truyền dữ liệu với các thông tin "not null"
	 * 
	 * @param movieSessionId - Mã suất chiếu
	 * @param movie - Phim
	 * @param screen - Phòng chiếu
	 * @param movieFormat - Định dạng trình chiếu
	 * @param startsAt - Thời gian bắt đầu
	 * @param endsAt - Thời gian kết thúc
	 * @param movieSessionStatus - Trạng thái suất chiếu
	 */
	public MovieSession(int movieSessionId, Movie movie, Screen screen, MovieFormat movieFormat,
			LocalDateTime startsAt, LocalDateTime endsAt, MovieSessionStatus movieSessionStatus) {
		super();
		this.movieSessionId = movieSessionId;
		setMovie(movie);
		setScreen(screen);
		setMovieFormat(movieFormat);
		setStartsAt(startsAt);
		setEndsAt(endsAt);
		setMovieSessionStatus(movieSessionStatus);
	}

	/**
	 * Constructor đầy đủ thông tin
	 * 
	 * @param movieSessionId - Mã suất chiếu
	 * @param movie - Phim
	 * @param screen - Phòng chiếu
	 * @param movieFormat - Định dạng trình chiếu
	 * @param startsAt - Thời gian bắt đầu
	 * @param endsAt - Thời gian kết thúc
	 * @param movieSessionStatus - Trạng thái suất chiếu
	 * @param createdAt - Thời điểm khởi tạo dữ liệu
	 * @param updatedAt - Thời điểm dữ liệu được cập nhật
	 */
	public MovieSession(int movieSessionId, Movie movie, Screen screen, MovieFormat movieFormat,
			LocalDateTime startsAt, LocalDateTime endsAt, MovieSessionStatus movieSessionStatus,
			LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.movieSessionId = movieSessionId;
		setMovie(movie);
		setScreen(screen);
		setMovieFormat(movieFormat);
		setStartsAt(startsAt);
		setEndsAt(endsAt);
		setMovieSessionStatus(movieSessionStatus);
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public int getMovieSessionId() {
		return movieSessionId;
	}

	public Movie getMovie() {
		return movie;
	}

	public Screen getScreen() {
		return screen;
	}

	public MovieFormat getMovieFormat() {
		return movieFormat;
	}

	public LocalDateTime getStartsAt() {
		return startsAt;
	}

	public LocalDateTime getEndsAt() {
		return endsAt;
	}

	public MovieSessionStatus getMovieSessionStatus() {
		return movieSessionStatus;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setMovie(Movie movie) {
		if (movie == null)
			throw new IllegalArgumentException("movie không được null");
		this.movie = movie;
	}

	public void setScreen(Screen screen) {
		if (screen == null)
			throw new IllegalArgumentException("screen không được null");
		this.screen = screen;
	}

	public void setMovieFormat(MovieFormat movieFormat) {
		if (movieFormat == null)
			throw new IllegalArgumentException("movieFormat không được null");
		this.movieFormat = movieFormat;
	}

	public void setStartsAt(LocalDateTime startsAt) {
		if (startsAt == null)
			throw new IllegalArgumentException("startsAt không được null");
		if (this.endsAt != null && !startsAt.isBefore(this.endsAt))
			throw new IllegalArgumentException("startsAt phải trước endsAt");
		this.startsAt = startsAt;
	}

	public void setEndsAt(LocalDateTime endsAt) {
		if (endsAt == null)
			throw new IllegalArgumentException("endsAt không được null");
		if (this.startsAt != null && !endsAt.isAfter(this.startsAt))
			throw new IllegalArgumentException("endsAt phải sau startsAt");
		this.endsAt = endsAt;
	}

	public void setMovieSessionStatus(MovieSessionStatus movieSessionStatus) {
		if (movieSessionStatus == null)
			throw new IllegalArgumentException("movieSessionStatus không được null");
		this.movieSessionStatus = movieSessionStatus;
	}

	/**
	 * Nếu đã có id hợp lệ thì hash theo id. Nếu chưa có id hợp lệ thì hash theo chính object
	 */
	@Override
	public int hashCode() {
		return (movieSessionId > 0) ? Integer.hashCode(movieSessionId) : System.identityHashCode(this);
	}

	/**
	 * Hai Object MovieSession được xem là bằng nhau khi có cùng movieSessionId hợp lệ
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		MovieSession other = (MovieSession) obj;
		if (this.movieSessionId <= 0 || other.movieSessionId <= 0)
			return false;
		return this.movieSessionId == other.movieSessionId;
	}

	@Override
	public String toString() {
		return "MovieSession [movieSessionId=" + movieSessionId + ", movie=" + movie + ", screen=" + screen
				+ ", movieFormat=" + movieFormat + ", startsAt=" + startsAt + ", endsAt=" + endsAt
				+ ", movieSessionStatus=" + movieSessionStatus + ", createdAt=" + createdAt + ", updatedAt="
				+ updatedAt + "]";
	}
}