package com.cinema.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.cinema.config.DBConnection;
import com.cinema.entity.Movie;
import com.cinema.entity.MovieSession;
import com.cinema.entity.Screen;
import com.cinema.enums.MovieFormat;
import com.cinema.enums.MovieSessionStatus;


/**
 * DAO cho thực thể MovieSession
 * Chịu trách nhiệm thao tác dữ liệu với bảng suất chiếu trong MySQL
 * 
 * Bảng ánh xạ:
 * suat_chieu(
 *	ma_suat_chieu,
 *	ma_phim,
 *	ma_phong,
 *	ngon_ngu_trinh_chieu,
 *	tg_bat_dau,
 *	tg_ket_thuc,
 *	trang_thai,
 *	created_at,
 *	updated_at
 * )
 * 
 * @author Minh Huy (chính)
 */
public class MovieSessionDao {
	private static final String INSERT_MYSQL = """
			INSERT INTO suat_chieu (
				ma_phim,
				ma_phong,
				ngon_ngu_trinh_chieu,
				tg_bat_dau,
				tg_ket_thuc,
				trang_thai
			)
			VALUES (?, ?, ?, ?, ?, ?)
			""";
	
	private static final String UPDATE_MYSQL = """
			UPDATE suat_chieu
			SET ma_phim = ?,
				ma_phong = ?,
				ngon_ngu_trinh_chieu = ?,
				tg_bat_dau = ?,
				tg_ket_thuc = ?,
				trang_thai = ?
			WHERE ma_suat_chieu = ?
			""";
	
	private static final String DELETE_MYSQL = """
			DELETE FROM suat_chieu
			WHERE ma_suat_chieu = ?
			""";
	
	private static final String SELECT_BY_ID_MYSQL = """
			SELECT ma_suat_chieu,
				 ma_phim,
				 ma_phong,
				 ngon_ngu_trinh_chieu,
				 tg_bat_dau,
				 tg_ket_thuc,
				 trang_thai,
				 created_at,
				 updated_at
			FROM suat_chieu
			WHERE ma_suat_chieu = ?
			""";
	
	private static final String SELECT_ALL_MYSQL = """
			SELECT ma_suat_chieu,
				 ma_phim,
				 ma_phong,
				 ngon_ngu_trinh_chieu,
				 tg_bat_dau,
				 tg_ket_thuc,
				 trang_thai,
				 created_at,
				 updated_at
			FROM suat_chieu
			ORDER BY tg_bat_dau ASC
			""";
	
	private static final String SEARCH_BY_MOVIE_ID_MYSQL = """
			SELECT ma_suat_chieu,
				 ma_phim,
				 ma_phong,
				 ngon_ngu_trinh_chieu,
				 tg_bat_dau,
				 tg_ket_thuc,
				 trang_thai,
				 created_at,
				 updated_at
				 FROM suat_chieu
				 WHERE ma_phim = ?
				 ORDER BY tg_bat_dau ASC
			""";
	
	private static final String SEARCH_BY_SCREEN_ID_MYSQL= """
			SELECT ma_suat_chieu,
				 ma_phim,
				 ma_phong,
				 ngon_ngu_trinh_chieu,
				 tg_bat_dau,
				 tg_ket_thuc,
				 trang_thai,
				 created_at,
				 updated_at
				 FROM suat_chieu
				 WHERE ma_phong = ?
				 ORDER BY tg_bat_dau ASC
			""";
	
	private static final String SEARCH_BY_DATE_MYSQL = """
				SELECT ma_suat_chieu,
				       ma_phim,
				       ma_phong,
				       ngon_ngu_trinh_chieu,
				       tg_bat_dau,
				       tg_ket_thuc,
				       trang_thai,
				       created_at,
				       updated_at
				FROM suat_chieu
				WHERE DATE(tg_bat_dau) = ?
				ORDER BY tg_bat_dau ASC
			""";
	//WHERE tg_bat_dau BETWEEN ? AND ?: nếu muốn tìm trong khoảng thời gian
	
	private static final String EXISTS_TIME_CONFLICT_BY_SCREEN_MYSQL = """
				SELECT 1
				FROM suat_chieu
				WHERE ma_phong = ?
				  AND (? < tg_ket_thuc)
				  AND (? > tg_bat_dau)
				LIMIT 1
			""";
	
	private static final String EXISTS_TIME_CONFLICT_BY_SCREEN_EXCEPT_ID_MYSQL = """
			SELECT 1
			FROM suat_chieu
			WHERE ma_phong = ?
			  AND (? < tg_ket_thuc)
			  AND (? > tg_bat_dau)
			  AND ma_suat_chieu <> ?
			LIMIT 1
			""";
	
	private static final String IN_USED_MYSQL = """
			SELECT 1
			FROM ve
			WHERE ma_suat_chieu = ?
			LIMIT 1
			""";
	
	/**
	 * Kiểm tra dữ liệu đầu vào của MovieSession
	 * 
	 * @param movieSession - Đối tượng MovieSession để kiểm tra
	 */
	private void validateMovieSession (MovieSession movieSession) { //TODO: làm validate internal và package

	}
	
	/**
	 * Kiểm tra xem suất chiếu có bị trùng lịch theo phòng hay không
	 * 
	 * @param screenId - Mã phòng
	 * @param startsAt - Thời gian bắt đầu
	 * @param endsAt - Thời gian kết thúc
	 * @return true nếu đã tồn tại
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean existsTimeConflictByScreenId (int screenId, LocalDateTime startsAt, LocalDateTime endsAt) throws SQLException{
		if (screenId <= 0 || startsAt == null || endsAt == null) {
			return false;
		}
		
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(EXISTS_TIME_CONFLICT_BY_SCREEN_MYSQL)){
			
			ps.setInt(1, screenId);
			ps.setTimestamp(2, Timestamp.valueOf(endsAt));
			ps.setTimestamp(3, Timestamp.valueOf(startsAt));
			
			try (ResultSet rs = ps.executeQuery()){
				return rs.next();
			}	
		}
	}
	
	/**
	 * Kiểm tra trung lịch trong cùng phòng ngoại trừ hính bản ghi đang update
	 * 
	 * @param screenId - Mã phòng
	 * @param startsAt - Thời gian bắt đầu
	 * @param endsAt - Thời gian kết thúc
	 * @param movieSessionId - Mã suất chiếu
	 * @return true nếu đã tồn tại
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean existsTimeConflictByScreenIdExceptId 
	(int screenId, LocalDateTime startsAt, LocalDateTime endsAt, int movieSessionId) throws SQLException{
		if (screenId <= 0 || startsAt == null || endsAt == null || movieSessionId <= 0) {
			return false;
		}
		
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(EXISTS_TIME_CONFLICT_BY_SCREEN_EXCEPT_ID_MYSQL)){
			
			ps.setInt(1, screenId);
			ps.setTimestamp(2, Timestamp.valueOf(endsAt));
			ps.setTimestamp(3, Timestamp.valueOf(startsAt));
			ps.setInt(4, movieSessionId);
			
			try (ResultSet rs = ps.executeQuery()){
				return rs.next();
			}	
		}
	}
	
	/**
	 * Kiểm tra liêu suất chiếu có đang được sử dụng ở bảng Ticket hay không
	 * @param movieSessionId - Mã suất chiếu
	 * @return true nếu đang được sử dung
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean isUsedInTicket (int movieSessionId) throws SQLException{
		if (movieSessionId <= 0) {
			throw new IllegalArgumentException("Dữ liệu vào không hợp lệ - isUsedInTicket");
		}
		
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(IN_USED_MYSQL)){
			
			ps.setInt(1, movieSessionId);
			
			try (ResultSet rs = ps.executeQuery()){
				return rs.next();
			}
		}
	}
	
	/**
	 * Kiểm tra xem liệu suất chiếu có đang được sử dụng ở các bảng liên quan hay không
	 * 
	 * @param movieSessionId - Mã suất chiếu
	 * @return true nếu đang được sử dụng
	 * @throws SQLException nếu có lỗi SQL
	 */
	private boolean isMovieSessionUsed (int movieSessionId) throws SQLException{
		return  isUsedInTicket(movieSessionId);
	}
	
	/**
	 * Ánh xạ một dòng dữ liệu ResultSet thành đối tượng MovieSession
	 * 
	 * @param rs - ResultSet đang trỏ tới dòng dữ liệu hợp lẹ
	 * @return đối tượng MovieSession
	 * @throws SQLException nếu có lỗi SQL
	 */
	private MovieSession mapResultSetToMovieSession (ResultSet rs) throws SQLException{
		Timestamp createdAt = rs.getTimestamp("created_at");
		Timestamp updatedAt = rs.getTimestamp("updated_at");
		Movie movie = new Movie(rs.getInt("ma_phim"));
		Screen screen = new Screen(rs.getInt("ma_phong"));
		Timestamp startsAt = rs.getTimestamp("tg_bat_dau");
		Timestamp endsAt = rs.getTimestamp("tg_ket_thuc");
		
		MovieSession aMovieSession =  new MovieSession(
				rs.getInt("ma_suat_chieu"),
				movie,
				screen,
				MovieFormat.fromId(rs.getInt("ngon_ngu_trinh_chieu")),
				startsAt != null ? startsAt.toLocalDateTime() : null,
				endsAt != null ? endsAt.toLocalDateTime() : null,
				MovieSessionStatus.fromId(rs.getInt("trang_thai")),
				createdAt != null ? createdAt.toLocalDateTime() : null,
				updatedAt != null ? updatedAt.toLocalDateTime() : null
				);
		
		return aMovieSession;
	}
	
	/**
	 * Thêm suất chiếu mới
	 * 
	 * @param movieSession - Đối tượng suất chiếu
	 * @return true nếu thêm thành công
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean addMovieSession (MovieSession movieSession) throws SQLException{
		validateMovieSession(movieSession);
		
		if (existsTimeConflictByScreenId(movieSession.getScreen().getScreenId(), movieSession.getStartsAt(), movieSession.getEndsAt())) {
			throw new IllegalArgumentException("Phòng có id " + movieSession.getScreen().getScreenId() + " đã có xuất chiếu!"); 
		}
		
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(INSERT_MYSQL)){
			
			ps.setInt(1, movieSession.getMovie().getMovieId());
			ps.setInt(2, movieSession.getScreen().getScreenId());
			ps.setInt(3, movieSession.getMovieFormat().getMovieFormatId());
			ps.setTimestamp(4, Timestamp.valueOf(movieSession.getStartsAt()));
			ps.setTimestamp(5, Timestamp.valueOf(movieSession.getEndsAt()));
			ps.setInt(6, movieSession.getMovieSessionStatus().getMovieSessionStatusId());
			
			return ps.executeUpdate() > 0;
		}
	}
	
	/**
	 * Cập nhật thông tin suất chiếu
	 * @param movieSession
	 * @return
	 * @throws SQLException
	 */
	public boolean updateMovieSession (MovieSession movieSession) throws SQLException{
		validateMovieSession(movieSession);
		
		if (isMovieSessionUsed(movieSession.getMovieSessionId())) {
		    throw new IllegalArgumentException("Suất chiếu đã phát sinh vé, không thể cập nhật!");
		}
		
		if (movieSession.getMovieSessionId() <= 0) {
		    throw new IllegalArgumentException("movieSessionId phải lớn hơn 0!");
		}

		if (existsTimeConflictByScreenIdExceptId(
		        movieSession.getScreen().getScreenId(),
		        movieSession.getStartsAt(),
		        movieSession.getEndsAt(),
		        movieSession.getMovieSessionId())) {
		    throw new IllegalArgumentException("Phòng đã có suất chiếu bị trùng lịch ở bản ghi khác!");
		}
		
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(UPDATE_MYSQL)){
			ps.setInt(1, movieSession.getMovie().getMovieId());
			ps.setInt(2, movieSession.getScreen().getScreenId());
			ps.setInt(3, movieSession.getMovieFormat().getMovieFormatId());
			ps.setTimestamp(4, Timestamp.valueOf(movieSession.getStartsAt()));
			ps.setTimestamp(5, Timestamp.valueOf(movieSession.getEndsAt()));
			ps.setInt(6, movieSession.getMovieSessionStatus().getMovieSessionStatusId());
			ps.setInt(7, movieSession.getMovieSessionId());
			
			return ps.executeUpdate() > 0;
		}
	}
	
	/**
	 * Xóa suất chiếu theo mã. 
	 * Không thể xóa nếu suất chiếu được sử dụng trong vé hoặc các bảng liên quan
	 * 
	 * @param movieSessionId - Mã suất chiếu
	 * @return true nếu xóa thành công
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean deleteMovieSessionById (int movieSessionId) throws SQLException {
		if (movieSessionId <= 0) {
			throw new IllegalArgumentException("movieSessionId không được rỗng!");
		}
		
		if (isMovieSessionUsed(movieSessionId)) {
			throw new IllegalArgumentException("Suất chiếu đang được sử dụng, không thể xóa!");
		}
		
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(DELETE_MYSQL)){
			
			ps.setInt(1, movieSessionId);
			
			return ps.executeUpdate() > 0;
		}
	}
	
	/**
	 * Tìm suất chiếu theo mã
	 * 
	 * @param movieSessionId - Mã suất chiếu
	 * @return đối tượng MovieSession nếu tìm thấy, null nếu không
	 * @throws SQLException nếu có lỗi SQL
	 */
	public MovieSession findById (int movieSessionId) throws SQLException{
		if (movieSessionId <= 0) {
			throw new IllegalArgumentException("movieSessionId không được rỗng!");
		}
		
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID_MYSQL)){
			
			ps.setInt(1, movieSessionId);
			
			try (ResultSet rs = ps.executeQuery()){
				if (rs.next()) {
					return mapResultSetToMovieSession(rs);
				}
			}
		}
		return null;
	}
	
	/**
	 * Lấy tất cả suất chiếu
	 * 
	 * @return danh sách suất chiếu
	 * @throws SQLException nếu có lỗi SQL
	 */
	public List<MovieSession> getAllMovieSessions() throws SQLException{
		List<MovieSession> movieSessions = new ArrayList<MovieSession>();
		
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_ALL_MYSQL);
				ResultSet rs = ps.executeQuery()){
			
			while (rs.next()) {
				movieSessions.add(mapResultSetToMovieSession(rs));
			}
		}
		
		return movieSessions;
	}
	
	/**
	 * Tìm suất chiếu theo mã phim
	 * 
	 * @param movieId - Mã phim
	 * @return danh sách suất chiếu theo mã phim
	 * @throws SQLException nếu có lỗi SQL
	 */
	public List<MovieSession> searchByMovieId(int movieId) throws SQLException{
		if (movieId <= 0) {
			throw new IllegalArgumentException("movieId phải lớn hơn 0!");
		}
		
		List<MovieSession> movieSessions = new ArrayList<MovieSession>();
		
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SEARCH_BY_MOVIE_ID_MYSQL)){
			
			ps.setInt(1, movieId);
			
			try (ResultSet rs = ps.executeQuery()){
				while (rs.next()) {
					movieSessions.add(mapResultSetToMovieSession(rs));
				}
			}
			
			return movieSessions;
		}
	}
	
	/**
	 * Tìm suất chiếu theo mã phòng
	 * 
	 * @param screenId - Mã phòng
	 * @return danh sách suất chiếu theo mã phòng
	 * @throws SQLException nếu có lỗi SQL
	 */
	public List<MovieSession> searchByScreenId(int screenId) throws SQLException{
		if (screenId <= 0) {
			throw new IllegalArgumentException("screenId phải lớn hơn 0!");
		}
		
		List<MovieSession> movieSessions = new ArrayList<MovieSession>();
		
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SEARCH_BY_SCREEN_ID_MYSQL)){
			
			ps.setInt(1, screenId);
			
			try (ResultSet rs = ps.executeQuery()){
				while (rs.next()) {
					movieSessions.add(mapResultSetToMovieSession(rs));
				}
			}
		}
		
		return movieSessions;
	}
	
	/**
	 * Tìm suất chiếu theo ngày
	 * 
	 * @param searchDate - Ngày chiếu
	 * @return danh sách suất chiếu phù hợp
	 * @throws SQLException nếu có lỗi SQL
	 */
	public List<MovieSession> searchByDate(LocalDate searchDate) throws SQLException{
		if (searchDate == null) {
			throw new IllegalArgumentException("date không được rỗng");
		}
		
		List<MovieSession> movieSessions = new ArrayList<MovieSession>();
		
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(SEARCH_BY_DATE_MYSQL)){
			
			ps.setDate(1, Date.valueOf(searchDate));
			
			try (ResultSet rs = ps.executeQuery()){
				while (rs.next()) {
					movieSessions.add(mapResultSetToMovieSession(rs));
				}
			}
		}
		
		return movieSessions;	
	}
}
