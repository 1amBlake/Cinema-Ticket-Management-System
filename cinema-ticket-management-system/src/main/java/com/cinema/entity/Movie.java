package com.cinema.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.cinema.enums.MovieAgeRating;
import com.cinema.enums.MovieStatus;

/**
 * Đại diện cho phim trong hệ thống.
 * Chứa các thuộc tính phim.
 * 
 * @author Minh Huy (chính, góp ý, sửa ràng buộc)
 * @author Thanh Trọng (ràng buộc)
 */
public class Movie {

	private int movieId; // Do database tự sinh
	private String movieName; // not null
	private int movieDuration; // not null
	private LocalDate movieReleaseDate;
	private String movieLanguage;
	private MovieStatus movieStatus; // enums - not null
	private MovieAgeRating movieAgeRating; // enums - not null
	private String pictureUrl;
	private LocalDateTime createdAt; // Do database tự sinh
	private LocalDateTime updatedAt; // Do database tự sinh

	/**
	 * Constructor mặc định, không có dữ liệu để truyền
	 */
	public Movie() {
		super();
	}

	/**
	 * Constructor để khởi tạo thực thể theo mã, phục vụ truy vấn và ánh xạ quan hệ
	 * 
	 * @param movieId - Mã phim
	 */
	public Movie(int movieId) {
		super();
		this.movieId = movieId;
	}

	/**
	 * Constructor để thêm dữ liệu cho CSDL (các dữ liệu not null)
	 * 
	 * @param movieName - Tên phim
	 * @param movieDuration - Thời lượng
	 * @param movieStatus - Tình trạng phim
	 * @param movieAgeRating - Giới hạn tuổi
	 */
	public Movie(String movieName, int movieDuration, MovieStatus movieStatus, MovieAgeRating movieAgeRating) {
		super();
		setMovieName(movieName);
		setMovieDuration(movieDuration);
		setMovieStatus(movieStatus);
		setMovieAgeRating(movieAgeRating);
	}

	/**
	 * Constructor truyền dữ liệu với các thông tin not null
	 * 
	 * @param movieId - Mã phim
	 * @param movieName - Tên phim
	 * @param movieDuration - Thời lượng
	 * @param movieStatus - Tình trạng phim
	 * @param movieAgeRating - Giới hạn tuổi
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
	 * Constructor để thêm dữ liệu cho CSDL (tất cả dữ liệu nhập vào)
	 * 
	 * @param movieName - Tên phim
	 * @param movieDuration - Thời lượng
	 * @param movieReleaseDate - Ngày công chiếu
	 * @param movieLanguage - Ngôn ngữ
	 * @param movieStatus - Tình trạng phim
	 * @param movieAgeRating - Giới hạn tuổi
	 * @param pictureUrl - Đường dẫn poster
	 */
	public Movie(String movieName, int movieDuration, LocalDate movieReleaseDate, String movieLanguage,
			MovieStatus movieStatus, MovieAgeRating movieAgeRating, String pictureUrl) {
		super();
		setMovieName(movieName);
		setMovieDuration(movieDuration);
		setMovieReleaseDate(movieReleaseDate);
		setMovieLanguage(movieLanguage);
		setMovieStatus(movieStatus);
		setMovieAgeRating(movieAgeRating);
		setPictureUrl(pictureUrl);
	}

	/**
	 * Constructor đầy đủ thông tin
	 * 
	 * @param movieId - Mã phim
	 * @param movieName - Tên phim
	 * @param movieDuration - Thời lượng
	 * @param movieReleaseDate - Ngày công chiếu
	 * @param movieLanguage - Ngôn ngữ
	 * @param movieStatus - Tình trạng phim
	 * @param movieAgeRating - Giới hạn tuổi
	 * @param pictureUrl - Đường dẫn poster
	 * @param createdAt - Thời điểm khởi tạo dữ liệu
	 * @param updatedAt - Thời điểm dữ liệu được cập nhật
	 */
	public Movie(int movieId, String movieName, int movieDuration, LocalDate movieReleaseDate, String movieLanguage,
			MovieStatus movieStatus, MovieAgeRating movieAgeRating, String pictureUrl, LocalDateTime createdAt,
			LocalDateTime updatedAt) {
		this.movieId = movieId;
		setMovieName(movieName);
		setMovieDuration(movieDuration);
		setMovieReleaseDate(movieReleaseDate);
		setMovieLanguage(movieLanguage);
		setMovieStatus(movieStatus);
		setMovieAgeRating(movieAgeRating);
		setPictureUrl(pictureUrl);
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	// Getter / Setter

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
		if (movieName.trim().length() > 255) {
			throw new IllegalArgumentException("movieName không được vượt quá 255 ký tự");
		}
		this.movieName = movieName.trim();
	}

	public void setMovieDuration(int movieDuration) {
		if (movieDuration <= 0 || movieDuration > 600) {
			throw new IllegalArgumentException("movieDuration phải > 0 và <= 600 phút");
		}
		this.movieDuration = movieDuration;
	}

	public void setMovieReleaseDate(LocalDate movieReleaseDate) {
		if (movieReleaseDate != null
				&& (movieReleaseDate.getYear() < 1800 || movieReleaseDate.getYear() > (LocalDate.now().getYear() + 2))) {
			throw new IllegalArgumentException("movieReleaseDate không hợp lệ!");
		}
		this.movieReleaseDate = movieReleaseDate;
	}

	public void setMovieLanguage(String movieLanguage) {
		if (movieLanguage == null || movieLanguage.trim().isEmpty()) {
			this.movieLanguage = null;
			return;
		}
		if (movieLanguage.trim().length() > 100) {
			throw new IllegalArgumentException("movieLanguage không được vượt quá 100 ký tự");
		}
		this.movieLanguage = movieLanguage.trim();
	}

	public void setMovieStatus(MovieStatus movieStatus) {
		if (movieStatus == null) {
			throw new IllegalArgumentException("movieStatus không được null");
		}
		this.movieStatus = movieStatus;
	}

	public void setMovieAgeRating(MovieAgeRating movieAgeRating) {
		if (movieAgeRating == null) {
			throw new IllegalArgumentException("movieAgeRating không được null");
		}
		this.movieAgeRating = movieAgeRating;
	}

	public void setPictureUrl(String pictureUrl) {
		if (pictureUrl == null || pictureUrl.trim().isEmpty()) {
			this.pictureUrl = null;
			return;
		}
		if (pictureUrl.trim().length() > 255) {
			throw new IllegalArgumentException("pictureUrl không được vượt quá 255 ký tự");
		}
		this.pictureUrl = pictureUrl.trim();
	}

	/**
	 * Nếu đã có id hợp lệ thì hash theo id. Nếu chưa có id hợp lệ thì hash theo chính object
	 */
	@Override
	public int hashCode() {
		return (movieId > 0) ? Integer.hashCode(movieId) : System.identityHashCode(this);
	}

	/**
	 * Hai Object Movie được xem là bằng nhau khi có cùng movieId hợp lệ.
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