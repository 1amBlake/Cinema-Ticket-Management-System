package com.cinema.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.cinema.config.DBConnection;
import com.cinema.entity.Movie;
import com.cinema.enums.MovieAgeRating;
import com.cinema.enums.MovieStatus;

/**
 * DAO cho thực thể Movie
 * Chịu trách nhiệm thao tác dữ liệu với bảng phim trong MySQL
 * 
 * Bảng ánh xạ:
 * phim(ma_phim, ten_phim, thoi_luong, ngay_phat_hanh, ngon_ngu,
 * trang_thai, gioi_han_tuoi, url_anh, created_at, updated_at)
 * 
 * @author Minh Huy (chính)
 */
public class MovieDao {
	private static final String INSERT_MYSQL = """
			INSERT INTO phim (
				ten_phim,
				thoi_luong,
				ngay_phat_hanh,
				ngon_ngu,
				trang_thai,
				gioi_han_tuoi,
				url_anh
			)
			VALUES (?, ?, ?, ?, ?, ?, ?)
			""";
	
	private static final String UPDATE_MYSQL = """
			UPDATE phim
			SET ten_phim = ?,
				thoi_luong = ?,
				ngay_phat_hanh = ?,
				ngon_ngu = ?,
				trang_thai = ?,
				gioi_han_tuoi = ?,
				url_anh = ?
			WHERE ma_phim = ?
			""";
	
	private static final String DELETE_MYSQL = """
			DELETE FROM phim
            WHERE ma_phim = ?
			""";
	
	private static final String SELECT_BY_ID_MYSQL = """
			SELECT ma_phim, 
				ten_phim,
				thoi_luong, 
				ngay_phat_hanh, 
				ngon_ngu,
                trang_thai, 
                gioi_han_tuoi, 
                url_anh, 
                created_at, 
                updated_at
			FROM phim
			WHERE ma_phim = ?
			""";
	
	private static final String SELECT_ALL_MYSQL = """
			SELECT ma_phim, 
				ten_phim,
				thoi_luong, 
				ngay_phat_hanh, 
				ngon_ngu,
                trang_thai, 
                gioi_han_tuoi, 
                url_anh, 
                created_at, 
                updated_at
			FROM phim
			ORDER BY ten_phim ASC
			""";
	
	private static final String SEARCH_BY_NAME_MYSQL = """
			SELECT ma_phim, 
				ten_phim,
				thoi_luong, 
				ngay_phat_hanh, 
				ngon_ngu,
                trang_thai, 
                gioi_han_tuoi, 
                url_anh, 
                created_at, 
                updated_at
			FROM phim
			WHERE ten_phim LIKE ?
			ORDER BY ten_phim ASC
			""";
	
    private static final String EXISTS_BY_NAME_AND_RELEASE_DATE_MYSQL = """
            SELECT 1
            FROM phim
            WHERE ten_phim = ? AND
                  ((ngay_phat_hanh IS NULL AND ? IS NULL) OR ngay_phat_hanh = ?)
            LIMIT 1
            """;
    
    private static final String EXISTS_BY_NAME_AND_RELEASE_DATE_EXCEPT_ID_MYSQL = """
            SELECT 1
            FROM phim
            WHERE ten_phim = ?
              AND ((ngay_phat_hanh IS NULL AND ? IS NULL) OR ngay_phat_hanh = ?)
              AND ma_phim <> ?
            LIMIT 1
            """;
    
    private static final String IS_USED_IN_MOVIE_SESSION_MYSQL = """
            SELECT 1
            FROM suat_chieu
            WHERE ma_phim = ?
            LIMIT 1
            """;
    
    private static final String IS_USED_IN_MOVIE_DIRECTOR_MYSQL = """
            SELECT 1
            FROM dao_dien_phim
            WHERE ma_phim = ?
            LIMIT 1
            """;
    
    private static final String IS_USED_IN_MOVIE_GENRE_MYSQL = """
            SELECT 1
            FROM the_loai_phim
            WHERE ma_phim = ?
            LIMIT 1
            """;
    
    /**
     * Kiểm tra dữ liệu đầu vào của Movie
     * 
     * @param movie - Đối tượng Movie để kiểm tra
     */
    public void validateMovie(Movie movie) {
    	if (movie == null) {
    		throw new IllegalArgumentException("Movie không được null");
    	}
    	
    	if (movie.getMovieName() == null || movie.getMovieName().trim().isEmpty()) {
    		throw new IllegalArgumentException("Tên phim không được để trống");
    	}
    	
    	if (movie.getMovieDuration() <= 0) {
    		throw new IllegalArgumentException("Thời lượng phim phải lớn hơn 0!");
    	}
    	
    	if (movie.getMovieStatus() == null) {
    		throw new IllegalArgumentException("Trạng thái/Tình trạng phim không được null!");
    	}
    	
    	if (movie.getMovieAgeRating() == null) {
    		throw new IllegalArgumentException("Giới hạn tuổi không được null!");
    	}
    		
    }
    
    /**
     * Kiểm tra phim đã tồn tại theo tên và ngày phát hành chưa
     * @param movieName - Tên phim
     * @param releaseDate - Ngày phát hành
     * @return true nếu đã tồn tại
     * @throws SQLException nếu có lỗi SQL
     */
    public boolean existsByNameAndReleaseDate(String movieName, LocalDate releaseDate) throws SQLException {
    	if (movieName == null || movieName.trim().isEmpty()) {
    		return false;
    	}
    	
    	try (Connection connection = DBConnection.getConnection();
    			PreparedStatement ps = connection.prepareStatement(EXISTS_BY_NAME_AND_RELEASE_DATE_MYSQL)){
    		
    		ps.setString(1, movieName.trim());
    		
    		if (releaseDate != null) {
    			Date sqlDate = Date.valueOf(releaseDate);
    			ps.setDate(2, sqlDate);
    			ps.setDate(3, sqlDate);
    		}
    		else {
    			ps.setDate(2, null);
    			ps.setDate(3, null);
    		}
    		
    		try (ResultSet rs = ps.executeQuery()){
    			return rs.next();
    		}
    	}
    }
    
    /**
     * Kiểm tra phim đã tồn tại ở bản ghi khác hay chưa
     * @param movieName - Tên phim
     * @param movieReleaseDate - Ngày phát hành
     * @param movieId - Mã phim
     * @return true nếu đã tồn tại ở bản ghi khác
     * @throws SQLException nếu có lỗi SQL
     */
    public boolean existsByNameAndReleaseDateExceptId(String movieName, LocalDate movieReleaseDate,
    		int movieId) throws SQLException{
    	try (Connection connection = DBConnection.getConnection();
    			PreparedStatement ps = connection.prepareStatement(EXISTS_BY_NAME_AND_RELEASE_DATE_EXCEPT_ID_MYSQL)){
    		
    		ps.setString(1, movieName.trim());
    		
    		if (movieReleaseDate != null) {
    			Date sqlDate = Date.valueOf(movieReleaseDate);
    			ps.setDate(2, sqlDate);
    			ps.setDate(3, sqlDate);
    		}
    		else {
    			ps.setDate(2, null);
    			ps.setDate(3, null);
    		}
    		
    		ps.setInt(4, movieId);
    		
    		try (ResultSet rs = ps.executeQuery()){
    			return rs.next();
    		}
    	}
    }
    
    /**
     * Kiểm tra phim có đang được sử dụng ở các bảng MovieSession hay không.
     * 
     * @param movieId - Mã phim
     * @return true nếu đang được sử dụng
     * @throws SQLException nếu có lỗi SQL
     */
    private boolean isUsedInMovieSession (int movieId) throws SQLException{
    	try (Connection connection = DBConnection.getConnection();
    			PreparedStatement ps = connection.prepareStatement(IS_USED_IN_MOVIE_SESSION_MYSQL)){
    		
    		ps.setInt(1, movieId);
    		
    		try (ResultSet rs = ps.executeQuery()){
    			return rs.next();
    		}
    	}
    }
    
    /**
     * Kiểm tra phim có đang được sử dụng ở các bảng MovieDirector hay không.
     * 
     * @param movieId - Mã phim
     * @return true nếu đang được sử dụng
     * @throws SQLException nếu có lỗi SQL
     */
    private boolean isUsedInMovieDirector(int movieId) throws SQLException{
    	try (Connection connection = DBConnection.getConnection();
    			PreparedStatement ps = connection.prepareStatement(IS_USED_IN_MOVIE_DIRECTOR_MYSQL)){
    		
    		ps.setInt(1, movieId);
    		
    		try (ResultSet rs = ps.executeQuery()){
    			return rs.next();
    		}
    	}
    }
    
    /**
     * Kiểm tra phim có đang được sử dụng ở các bảng MovieGenre hay không.
     * 
     * @param movieId - Mã phim
     * @return true nếu đang được sử dụng
     * @throws SQLException nếu có lỗi SQL
     */
    private boolean isUsedInMovieGenre(int movieId) throws SQLException{
    	try (Connection connection = DBConnection.getConnection();
    			PreparedStatement ps = connection.prepareStatement(IS_USED_IN_MOVIE_GENRE_MYSQL)){
    	
    		ps.setInt(1, movieId);
    		
    		try(ResultSet rs = ps.executeQuery()){
    			return rs.next();
    		}
    	}
    }
    /**
     * Kiểm tra phim có đang được sử dụng ở các bảng liên quan hay không.
     * 
     * @param movieId - Mã phim
     * @return true nếu đang được sử dụng
     * @throws SQLException nếu có lỗi SQL
     */
    public boolean isMovieUsed (int movieId) throws SQLException{
    	return isUsedInMovieSession(movieId) || isUsedInMovieDirector(movieId) || isUsedInMovieGenre(movieId);
    }
    
    /**
     * Ánh xạ một dòng dữ liệu ResultSet thành đối tượng Movie.
     * 
     * @param rs - ResultSet đang trỏ tới dòng dữ liệu hợp lệ
     * @return đối tượng Movie
     * @throws SQLException nếu có lỗi đọc dữ liệu
     */
    private Movie mapResultSetToMovie(ResultSet rs) throws SQLException{
    	Timestamp createdAt = rs.getTimestamp("created_at");
    	Timestamp updatedAt = rs.getTimestamp("updated_at");
    	Date releaseDate = rs.getDate("ngay_phat_hanh");
    	
    	Movie newMovie = new Movie(
    			rs.getInt("ma_phim"),
                rs.getString("ten_phim"),
                rs.getInt("thoi_luong"),
                releaseDate != null ? releaseDate.toLocalDate() : null,
                rs.getString("ngon_ngu"),
                MovieStatus.fromId(rs.getInt("trang_thai")),
                MovieAgeRating.fromId(rs.getInt("gioi_han_tuoi")),
                rs.getString("url_anh"),
                createdAt != null ? createdAt.toLocalDateTime() : null,
                updatedAt != null ? updatedAt.toLocalDateTime() : null
    			);
    	return newMovie;
    }

    /**
     * Thêm phim mới
     * @param movie - Phim cần thêm
     * @return true nếu thêm thành công
     * @throws SQLException nếu có lỗi SQL
     */
    public boolean addMovie(Movie movie) throws SQLException{
    	validateMovie(movie);
    	
    	if (existsByNameAndReleaseDate(movie.getMovieName(), movie.getMovieReleaseDate())) {
    		throw new IllegalArgumentException("Phim đã tồn tại");
    	}
    	
    	try (Connection connection = DBConnection.getConnection();
    			PreparedStatement ps = connection.prepareStatement(INSERT_MYSQL)){
    		
    		ps.setString(1, movie.getMovieName());
    		ps.setInt(2, movie.getMovieDuration());
    		
    		if (movie.getMovieReleaseDate() != null) {
    			ps.setDate(3, Date.valueOf(movie.getMovieReleaseDate()));
    		}else {
    			ps.setDate(3, null);
    		}
    		
    		ps.setString(4, movie.getMovieLanguage());
            ps.setInt(5, movie.getMovieStatus().getMovieStatusId());
            ps.setInt(6, movie.getMovieAgeRating().getAgeRatingId());
            ps.setString(7, movie.getPictureUrl());
            
            return ps.executeUpdate() > 0;
    	}
    }
    
    /**
     * Cập nhật thông tin phim
     * 
     * @param movie - Phim cần cập nhật
     * @return true nếu cập nhật thành công, false nếu không tìm thấy phim
     * @throws SQLException nếu có lỗi SQL
     */
    public boolean updateMovie(Movie movie) throws SQLException{
    	validateMovie(movie);
    	
    	if (movie.getMovieId() <= 0) {
    		throw new IllegalArgumentException("movieId phải lớn hơn 0!");
    	}
    	
    	if (existsByNameAndReleaseDateExceptId(movie.getMovieName(), movie.getMovieReleaseDate(), 
    			movie.getMovieId())) {
    		throw new IllegalArgumentException("Phim đã tồn tại ở bảng ghi khác!");
    	}
    	
    	try (Connection connection = DBConnection.getConnection();
                PreparedStatement ps = connection.prepareStatement(UPDATE_MYSQL)) {

               ps.setString(1, movie.getMovieName());
               ps.setInt(2, movie.getMovieDuration());

               if (movie.getMovieReleaseDate() != null) {
                   ps.setDate(3, Date.valueOf(movie.getMovieReleaseDate()));
               } else {
                   ps.setDate(3, null);
               }

               ps.setString(4, movie.getMovieLanguage());
               ps.setInt(5, movie.getMovieStatus().getMovieStatusId());
               ps.setInt(6, movie.getMovieAgeRating().getAgeRatingId());
               ps.setString(7, movie.getPictureUrl());
               ps.setInt(8, movie.getMovieId());

               return ps.executeUpdate() > 0;
    	}
    }
    
    /**
     * Xóa phim theo mã.
     * Không cho xóa nếu phim đang được dùng trong suất chiếu hoặc bảng liên kết.
     * 
     * @param movieId - Mã phim
     * @return true nếu xóa thành công, false nếu không xóa được
     * @throws SQLException nếu có lỗi SQL
     */
    public boolean deleteMovieById (int movieId) throws SQLException{
    	if (movieId <= 0) {
    		throw new IllegalArgumentException("movieId phải lớn hơn 0!");
    	}
    	
    	if (isMovieUsed(movieId)) {
    		throw new IllegalArgumentException("Phim đang được sử dụng, không thể xóa!");
    	}
    	
    	try(Connection connection = DBConnection.getConnection();
    			PreparedStatement ps = connection.prepareStatement(DELETE_MYSQL)){
    		
    		ps.setInt(1, movieId);
    		return ps.executeUpdate() > 0;
    	}
    }
    
    /**
     * Tìm phim theo mã.
     * 
     * @param movieId - Mã phim
     * @return đối tượng Movie nếu tìm thấy, ngược lại trả về null
     * @throws SQLException nếu có lỗi SQL
     */
    public Movie findById (int movieId) throws SQLException{
    	if (movieId <= 0) {
    		throw new IllegalArgumentException("movieId phải lớn hơn 0!");
    	}
    	
    	try (Connection connection = DBConnection.getConnection();
    			PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID_MYSQL)){
    		
    		ps.setInt(1, movieId);
    		
    		try(ResultSet rs = ps.executeQuery()){
    			if (rs.next()) {
    				return mapResultSetToMovie(rs);
    			}
    		}
    	}
    	
    	return null;
    }
    
    /**
     * Tìm phim theo tên gần đúng.
     * 
     * @param keyword - Từ khóa tên phim
     * @return danh sách phim phù hợp
     * @throws SQLException nếu có lỗi SQL
     */
    public List<Movie> searchByName(String keyword) throws SQLException{
    	List<Movie> movies = new ArrayList<Movie>();
    	
    	if (keyword == null) {
    		keyword = "";
    	}
    	
    	try (Connection connection = DBConnection.getConnection();
    			PreparedStatement ps = connection.prepareStatement(SEARCH_BY_NAME_MYSQL)){
    		
    		ps.setString(1, "%" + keyword.trim() + "%");
    		
    		try(ResultSet rs = ps.executeQuery()){
    			while (rs.next()) {
    				movies.add(mapResultSetToMovie(rs));
    			}
    		}
    	}
    	
    	return movies;
    }
    
    /**
     * Lấy tất cả phim.
     * 
     * @return danh sách phim
     * @throws SQLException nếu có lỗi SQL
     */
    public List<Movie> getAllMovies() throws SQLException{
    	List<Movie> movies = new ArrayList<>();
    	
    	try(Connection connection = DBConnection.getConnection();
    			PreparedStatement ps = connection.prepareStatement(SELECT_ALL_MYSQL);
    			ResultSet rs = ps.executeQuery()){
    		while (rs.next()) {
    			movies.add(mapResultSetToMovie(rs));
    		}
    	}
    	
    	return movies;
    }
}
