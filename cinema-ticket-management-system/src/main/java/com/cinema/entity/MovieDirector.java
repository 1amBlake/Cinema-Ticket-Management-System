package com.cinema.entity;

/**
 * Đại diện cho mối quan hệ giữa phim và đạo diễn trong hệ thống.
 * Dùng để ánh xạ dữ liệu cho bảng trung gian giữa Movie và Director.
 * 
 * @author Minh Huy (chính)
 */
public class MovieDirector {

	private Movie movie;
	private Director director;

	/**
	 * Constructor mặc định, không có dữ liệu để truyền.
	 */
	public MovieDirector() {
		super();
	}

	/**
	 * Constructor khởi tạo mối quan hệ giữa phim và đạo diễn.
	 * 
	 * @param movie - Phim
	 * @param director - Đạo diễn
	 */
	public MovieDirector(Movie movie, Director director) {
		super();
		setMovie(movie);
		setDirector(director);
	}

	/**
	 * Lấy phim trong mối quan hệ.
	 * 
	 * @return phim
	 */
	public Movie getMovie() {
		return movie;
	}

	/**
	 * Lấy đạo diễn trong mối quan hệ.
	 * 
	 * @return đạo diễn
	 */
	public Director getDirector() {
		return director;
	}

	/**
	 * Cập nhật phim trong mối quan hệ.
	 * 
	 * @param movie - Phim
	 * @throws IllegalArgumentException nếu movie là null
	 */
	public void setMovie(Movie movie) {
		if (movie == null) {
			throw new IllegalArgumentException("movie không được null");
		}
		this.movie = movie;
	}

	/**
	 * Cập nhật đạo diễn trong mối quan hệ.
	 * 
	 * @param director - Đạo diễn
	 * @throws IllegalArgumentException nếu director là null
	 */
	public void setDirector(Director director) {
		if (director == null) {
			throw new IllegalArgumentException("director không được null");
		}
		this.director = director;
	}

	/**
	 * Tính mã hash dựa trên mã phim và mã đạo diễn.
	 * 
	 * @return mã hash của đối tượng
	 */
	@Override
	public int hashCode() {
		int movieId = (movie != null) ? movie.getMovieId() : -1;
		int directorId = (director != null) ? director.getDirectorId() : -1;

		if (movieId > 0 && directorId > 0) {
			int result = Integer.hashCode(movieId);
			result = 31 * result + Integer.hashCode(directorId);
			return result;
		}

		return System.identityHashCode(this);
	}

	/**
	 * Hai object MovieDirector được xem là bằng nhau khi có cùng movieId và directorId hợp lệ.
	 * 
	 * @param obj - Đối tượng cần so sánh
	 * @return true nếu hai đối tượng bằng nhau, ngược lại là false
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		MovieDirector other = (MovieDirector) obj;

		if (this.movie == null || other.movie == null || this.director == null || other.director == null)
			return false;

		int thisMovieId = this.movie.getMovieId();
		int otherMovieId = other.movie.getMovieId();
		int thisDirectorId = this.director.getDirectorId();
		int otherDirectorId = other.director.getDirectorId();

		if (thisMovieId <= 0 || otherMovieId <= 0 || thisDirectorId <= 0 || otherDirectorId <= 0)
			return false;

		return thisMovieId == otherMovieId && thisDirectorId == otherDirectorId;
	}

	/**
	 * Trả về chuỗi biểu diễn đối tượng MovieDirector.
	 * 
	 * @return chuỗi thông tin mối quan hệ phim - đạo diễn
	 */
	@Override
	public String toString() {
		return "MovieDirector [movieId=" + (movie != null ? movie.getMovieId() : null)
				+ ", directorId=" + (director != null ? director.getDirectorId() : null) + "]";
	}
}