package com.cinema.dao; 

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cinema.config.DBConnection;
import com.cinema.entity.Genre;

//sample

/**
 * DAO cho thực thể Genre
 * Chịu trách nhiệm thao tác dữ liệu với bảng the_loai trong MySQL.
 * 
 * Bảng ánh xạ:
 * the_loai(ma_the_loai, ten_the_loai, created_at, updated_at)
 * 
 * @author Minh Huy (chính)
 */
public class GenreDao {
	private static final String INSERT_MYSQL = """
			INSERT INTO the_loai (ten_the_loai)
			VALUES (?)
			""";
		
	private static final String UPDATE_MYSQL = """
			UPDATE the_loai
			SET ten_the_loai = ?
			WHERE ma_the_loai = ?
			""";
	
	private static final String DELETE_MYSQL = """
            DELETE FROM the_loai
            WHERE ma_the_loai = ?
            """;

    private static final String SELECT_BY_ID_MYSQL = """
            SELECT ma_the_loai, ten_the_loai, created_at, updated_at
            FROM the_loai
            WHERE ma_the_loai = ?
            """;

    private static final String SELECT_ALL_MYSQL = """
            SELECT ma_the_loai, ten_the_loai, created_at, updated_at
            FROM the_loai
            ORDER BY ten_the_loai ASC, ma_the_loai ASC
            """;

    private static final String SEARCH_BY_NAME_MYSQL = """
            SELECT ma_the_loai, ten_the_loai, created_at, updated_at
            FROM the_loai
            WHERE ten_the_loai LIKE ?
            ORDER BY ten_the_loai ASC, ma_the_loai ASC
            """;
    
    private static final String EXISTS_BY_NAME_MYSQL = """
            SELECT 1
            FROM the_loai
            WHERE ten_the_loai = ?
            LIMIT 1
            """;

    private static final String EXISTS_BY_NAME_EXCEPT_ID_MYSQL = """
            SELECT 1
            FROM the_loai
            WHERE ten_the_loai = ? AND ma_the_loai <> ?
            LIMIT 1
            """;

    private static final String IS_USED_MYSQL = """
            SELECT 1
            FROM the_loai_phim
            WHERE ma_the_loai = ?
            LIMIT 1
            """;
    
    /**
     * Thêm một thể loại mới
     * @param genre - Thể loại cần thêm
     * @return true nếu thêm thành công, false nếu thất bại
     * @throws SQLException nếu có lỗi SQL
     */
    public boolean addGenre(Genre genre) throws SQLException{
    	//GenreValidator.validateForCreate(genre);
    	
    	if (existsByName(genre.getGenreName())) {
    		throw new IllegalArgumentException("Thể loại đã tồn tại!");
    	}
    	
    	try (Connection connection = DBConnection.getConnection();
    			PreparedStatement ps = connection.prepareStatement(INSERT_MYSQL)){
    		ps.setString(1, genre.getGenreName().trim());
    		return ps.executeUpdate() > 0;
    	}
    }
    
    /**
     * Cập nhật thông tin thế loại
     * 
     * @param genre - Thể loại cần cập nhật
     * @return true nếu cập nhật thành công, false nếu không tìm thấy thể loại cần cập nhật
     * @throws SQLException nếu có lỗi SQL
     */
    public boolean updateGenre(Genre genre) throws SQLException{
    	//GenreValidator.validateForUpdate(genre);
    	
    	if (genre.getGenreId() <= 0) {
    		throw new IllegalArgumentException("genreId phải lớn hơn 0!");
    	}
    	
    	if (existsByNameExceptId(genre.getGenreName(), genre.getGenreId())) {
    		throw new IllegalArgumentException("Thể loại đã tồn tại!");
    	}
    	
    	try (Connection connection = DBConnection.getConnection();
    			PreparedStatement ps = connection.prepareStatement(UPDATE_MYSQL)){
    		
    		ps.setString(1, genre.getGenreName().trim());
    		ps.setInt(2, genre.getGenreId());
    		
    		return ps.executeUpdate() > 0;
    	}
    }
    
    /**
     * Xóa thể loại theo mã.
     * Không cho xóa nếu thể loại đang được sử dụng ở bảng trung gian the_loai_phim.
     * 
     * @param genreId - Mã thể loại
     * @return true nếu xóa thành công, false nếu không xóa được
     * @throws SQLException nếu có lỗi SQL
     */
    public boolean deleteGenreById (int genreId) throws SQLException{
    	if (genreId <= 0) {
    		throw new IllegalArgumentException("genreId phải lớn hơn 0");
    	}
    	
    	if (isGenreUsed(genreId)) {
    		throw new IllegalArgumentException("Thể loại đang được gán cho phim, không thể xóa");
    	}
    	
    	try (Connection connection = DBConnection.getConnection();
    			PreparedStatement ps = connection.prepareStatement(DELETE_MYSQL)){
    		
    		ps.setInt(1, genreId);
    		return ps.executeUpdate() > 0;
    	}
    }
    
    /**
     * Tìm thể theo mã thể loại
     * 
     * @param genreId - Mã thể loại
     * @return đối tượng Genre nếu tìm thấy, ngược lại trả về null
     * @throws SQLException nếu có lỗi SQL
     */
    public Genre findById(int genreId) throws SQLException{
    	if (genreId <= 0) {
    		throw new IllegalArgumentException("genreId phải lớn hơn 0");
    	}
    	
    	try (Connection connection = DBConnection.getConnection();
    			PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID_MYSQL)){
    		
    		ps.setInt(1, genreId);
    		
    		try (ResultSet rs = ps.executeQuery()){
    			if (rs.next()) {
    				return mapResultSetToGenre(rs);
    			}
    		}
    	}
    	
    	return null;
    }
    
    /**
     * Lấy danh sách tất cả thể loại
     * 
     * @return danh sách thế loại
     * @throws SQLException nếu có lỗi SQL
     */
    public List<Genre> getAllGenres() throws SQLException{
    	List<Genre> genres = new ArrayList<>();
    	
    	try (Connection connection = DBConnection.getConnection();
    			PreparedStatement ps = connection.prepareStatement(SELECT_ALL_MYSQL);
    			ResultSet rs = ps.executeQuery()){
    		
    		while (rs.next()) {
    			genres.add(mapResultSetToGenre(rs));
    		}
    	}
    	
    	return genres;
    }
    
    /**
     * Tìm kiếm thế loại theo tên gần đúng
     * 
     * @param keyword từ khóa tên thể loại
     * @return danh sách thể loại phù hợp
     * @throws SQLException nếu có lỗi SQL
     */
    public List<Genre> searchByName (String keyword) throws SQLException{
    	List<Genre> genres = new ArrayList<>();
    	
    	try (Connection connection = DBConnection.getConnection();
    			PreparedStatement ps = connection.prepareStatement(SEARCH_BY_NAME_MYSQL)){
    		
    		keyword = (keyword == null) ? "" : keyword.trim();
    		ps.setString(1, "%" + keyword + "%");
    		
    		try (ResultSet rs = ps.executeQuery()){
    			while (rs.next()) {
    				genres.add(mapResultSetToGenre(rs));
    			}
    		}
    	}
    	
    	return genres;
    }
    
    /**
     * Kiểm tra tên đã tồn tại hay chưa
     * @param genreName - Tên thể loại đã tồn tại
     * @return true nếu đã tồn tại, false nếu chưa
     * @throws SQLException nếu có lỗi SQL
     */
    private boolean existsByName (String genreName) throws SQLException{
    	if (genreName == null || genreName.trim().isEmpty()) {
    		return false;
    	}
    	
    	try (Connection connection = DBConnection.getConnection();
    			PreparedStatement ps = connection.prepareStatement(EXISTS_BY_NAME_MYSQL)){
    		ps.setString(1, genreName.trim());
    		
    		try(ResultSet rs = ps.executeQuery()){
    			return rs.next();
    		}
    	}
    }
    
    /**
     * Kiểm tra tên thể loại đã tồn tại ở record khác hay chưa
     * 
     * @param genreName - Tên thể loại
     * @param genreId - Mã thể loại
     * @return true nếu tên đã tồn tại
     * @throws SQLException nếu SQL có lỗi
     */
    private boolean  existsByNameExceptId(String genreName, int genreId) throws SQLException{
    	if (genreId <= 0 || genreName == null || genreName.trim().isEmpty()) {
    	    throw new IllegalArgumentException("genreId phải lớn hơn 0 và genreName không được để trống!");
    	}
    	
    	try (Connection connection = DBConnection.getConnection();
    			PreparedStatement ps = connection.prepareStatement(EXISTS_BY_NAME_EXCEPT_ID_MYSQL)){
    		
    		ps.setString(1, genreName.trim());
    		ps.setInt(2, genreId);
    		
    		try (ResultSet rs = ps.executeQuery()){
    			return rs.next();
    		}
    	}
    }
    
    /**
     * Kiểm tra thể loại có đang được dùng trong liên kết phim - thể loại hay không
     * 
     * @param genreId - Mã thể loại
     * @return true nếu đang được dùng
     * @throws SQLException nếu có lỗi SQL
     */
    private boolean isGenreUsed(int genreId) throws SQLException{
    	if (genreId <= 0) {
    		throw new IllegalArgumentException("genreId phải lớn hơn 0!");
    	}
    	
    	try(Connection connection = DBConnection.getConnection();
    			PreparedStatement ps = connection.prepareStatement(IS_USED_MYSQL)){
    		
    		ps.setInt(1, genreId);
    		
    		try (ResultSet rs = ps.executeQuery()){
    			return rs.next();
    		}
    	}
    }
    
    /**
     * Ánh xạ một dòng dữ liệu ResultSet thành đối tượng Genre
     * 
     * @param rs - ResultSet đang trỏ tới dòng dữ liệu hợp lệ
     * @return đối tượng Genre
     * @throws SQLException nếu có lỗi đọc dữ liệu
     */
    private Genre mapResultSetToGenre(ResultSet rs) throws SQLException {
        Timestamp createdAt = rs.getTimestamp("created_at");
        Timestamp updatedAt = rs.getTimestamp("updated_at");

        return new Genre(
                rs.getInt("ma_the_loai"),
                rs.getString("ten_the_loai"),
                createdAt != null ? createdAt.toLocalDateTime() : null,
                updatedAt != null ? updatedAt.toLocalDateTime() : null
        );
    }
}
