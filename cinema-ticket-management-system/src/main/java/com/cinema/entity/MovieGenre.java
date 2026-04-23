package com.cinema.entity;

/**
 * Đại diện cho mối quan hệ giữa phim và thể loại phim trong hệ thống.
 * Dùng để ánh xạ dữ liệu cho bảng trung gian giữa Movie và Genre.
 * 
 * @author Minh Huy (chính)
 */
public class MovieGenre {

	private Movie movie;
	private Genre genre;

	/**
	 * Constructor mặc định, không có dữ liệu để truyền.
	 */
	public MovieGenre() {
		super();
	}

	/**
	 * Constructor khởi tạo mối quan hệ giữa phim và thể loại phim.
	 * 
	 * @param movie - Phim
	 * @param genre - Thể loại phim
	 */
	public MovieGenre(Movie movie, Genre genre) {
		super();
		setMovie(movie);
		setGenre(genre);
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
	 * Lấy thể loại phim trong mối quan hệ.
	 * 
	 * @return thể loại phim
	 */
	public Genre getGenre() {
		return genre;
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
	 * Cập nhật thể loại phim trong mối quan hệ.
	 * 
	 * @param genre - Thể loại phim
	 * @throws IllegalArgumentException nếu genre là null
	 */
	public void setGenre(Genre genre) {
		if (genre == null) {
			throw new IllegalArgumentException("genre không được null");
		}
		this.genre = genre;
	}

	/**
	 * Tính mã hash dựa trên mã phim và mã thể loại phim.
	 * 
	 * @return mã hash của đối tượng
	 */
	@Override
	public int hashCode() {
		int movieId = (movie != null) ? movie.getMovieId() : -1;
		int genreId = (genre != null) ? genre.getGenreId() : -1;

		if (movieId > 0 && genreId > 0) {
			int result = Integer.hashCode(movieId);
			result = 31 * result + Integer.hashCode(genreId);
			return result;
		}

		return System.identityHashCode(this);
	}

	/**
	 * Hai object MovieGenre được xem là bằng nhau khi có cùng movieId và genreId hợp lệ.
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

		MovieGenre other = (MovieGenre) obj;

		if (this.movie == null || other.movie == null || this.genre == null || other.genre == null)
			return false;

		int thisMovieId = this.movie.getMovieId();
		int otherMovieId = other.movie.getMovieId();
		int thisGenreId = this.genre.getGenreId();
		int otherGenreId = other.genre.getGenreId();

		if (thisMovieId <= 0 || otherMovieId <= 0 || thisGenreId <= 0 || otherGenreId <= 0)
			return false;

		return thisMovieId == otherMovieId && thisGenreId == otherGenreId;
	}

	/**
	 * Trả về chuỗi biểu diễn đối tượng MovieGenre.
	 * 
	 * @return chuỗi thông tin mối quan hệ phim - thể loại phim
	 */
	@Override
	public String toString() {
		return "MovieGenre [movieId=" + (movie != null ? movie.getMovieId() : null)
				+ ", genreId=" + (genre != null ? genre.getGenreId() : null) + "]";
	}
}