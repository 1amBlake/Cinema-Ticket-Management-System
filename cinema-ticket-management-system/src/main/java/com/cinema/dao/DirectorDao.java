package com.cinema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.cinema.config.DBConnection;
import com.cinema.entity.Director;
import com.cinema.validator.DirectorValidator;

/**
 * DAO cho thực thể Director
 * Chịu trách nhiệm thao tác dữ liệu với bảng dao_dien trong MySQL.
 * 
 * Bảng ánh xạ:
 * dao_dien(ma_dao_dien, ten_dao_dien, created_at, updated_at)
 * 
 * @author Trọng (chính)
 * @author Minh Huy (sửa một số chi tiết)
 */
public class DirectorDao {

    private static final String INSERT_MYSQL = """
            INSERT INTO dao_dien (
                ten_dao_dien
            )
            VALUES (?)
            """;

    private static final String UPDATE_MYSQL = """
            UPDATE dao_dien
            SET ten_dao_dien = ?
            WHERE ma_dao_dien = ?
            """;

    private static final String DELETE_MYSQL = """
            DELETE FROM dao_dien
            WHERE ma_dao_dien = ?
            """;

    private static final String SELECT_BY_ID_MYSQL = """
            SELECT ma_dao_dien,
                   ten_dao_dien,
                   created_at,
                   updated_at
            FROM dao_dien
            WHERE ma_dao_dien = ?
            """;

    private static final String SELECT_ALL_MYSQL = """
            SELECT ma_dao_dien,
                   ten_dao_dien,
                   created_at,
                   updated_at
            FROM dao_dien
            ORDER BY ten_dao_dien ASC, ma_dao_dien ASC
            """;

    private static final String SEARCH_BY_NAME_MYSQL = """
            SELECT ma_dao_dien,
                   ten_dao_dien,
                   created_at,
                   updated_at
            FROM dao_dien
            WHERE ten_dao_dien LIKE ?
            ORDER BY ten_dao_dien ASC, ma_dao_dien ASC
            """;

    private static final String IS_USED_IN_MOVIE_DIRECTOR_MYSQL = """
            SELECT 1
            FROM dao_dien_phim
            WHERE ma_dao_dien = ?
            LIMIT 1
            """;

    /**
     * Kiểm tra đạo diễn có đang được sử dụng trong bảng liên kết phim - đạo diễn hay không.
     * 
     * @param directorId - Mã đạo diễn
     * @return true nếu đang được sử dụng
     * @throws SQLException nếu có lỗi SQL
     */
    private boolean isUsedInMovieDirector(int directorId) throws SQLException {
        if (directorId <= 0) {
            throw new IllegalArgumentException("directorId phải lớn hơn 0!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(IS_USED_IN_MOVIE_DIRECTOR_MYSQL)) {

            ps.setInt(1, directorId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Kiểm tra đạo diễn có đang được sử dụng ở các bảng liên quan hay không.
     * 
     * @param directorId - Mã đạo diễn
     * @return true nếu đang được sử dụng
     * @throws SQLException nếu có lỗi SQL
     */
    private boolean isDirectorUsed(int directorId) throws SQLException {
        if (directorId <= 0) {
            throw new IllegalArgumentException("directorId phải lớn hơn 0!");
        }

        return isUsedInMovieDirector(directorId);
    }

    /**
     * Ánh xạ một dòng dữ liệu ResultSet thành đối tượng Director.
     * 
     * @param rs - ResultSet đang trỏ tới dòng dữ liệu hợp lệ
     * @return đối tượng Director
     * @throws SQLException nếu có lỗi đọc dữ liệu
     */
    private Director mapResultSetToDirector(ResultSet rs) throws SQLException {
        Timestamp createdAt = rs.getTimestamp("created_at");
        Timestamp updatedAt = rs.getTimestamp("updated_at");

        return new Director(
                rs.getInt("ma_dao_dien"),
                rs.getString("ten_dao_dien"),
                createdAt != null ? createdAt.toLocalDateTime() : null,
                updatedAt != null ? updatedAt.toLocalDateTime() : null
        );
    }

    /**
     * Thêm một đạo diễn mới.
     * 
     * Lưu ý:
     * - Không chặn trùng tên tuyệt đối vì ngoài đời có thể có nhiều đạo diễn trùng tên.
     * - Phần validate chi tiết sẽ bổ sung sau ở validator.
     * 
     * @param director - Đạo diễn cần thêm
     * @return true nếu thêm thành công
     * @throws SQLException nếu có lỗi SQL
     */
    public boolean addDirector(Director director) throws SQLException {
        DirectorValidator.validateForCreate(director);
         
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_MYSQL)) {

            ps.setString(1, director.getDirectorName().trim());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Cập nhật thông tin đạo diễn.
     * 
     * @param director - Đạo diễn cần cập nhật
     * @return true nếu cập nhật thành công, false nếu không tìm thấy bản ghi
     * @throws SQLException nếu có lỗi SQL
     */
    public boolean updateDirector(Director director) throws SQLException {
        DirectorValidator.validateForUpdate(director);

        if (director == null) {
            throw new IllegalArgumentException("director không được null!");
        }

        if (director.getDirectorId() <= 0) {
            throw new IllegalArgumentException("directorId phải lớn hơn 0!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_MYSQL)) {

            ps.setString(1, director.getDirectorName().trim());
            ps.setInt(2, director.getDirectorId());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Xóa đạo diễn theo mã.
     * Không cho xóa nếu đạo diễn đang được sử dụng trong bảng liên kết dao_dien_phim.
     * 
     * @param directorId - Mã đạo diễn
     * @return true nếu xóa thành công
     * @throws SQLException nếu có lỗi SQL
     */
    public boolean deleteDirectorById(int directorId) throws SQLException {
        if (directorId <= 0) {
            throw new IllegalArgumentException("directorId phải lớn hơn 0!");
        }

        if (isDirectorUsed(directorId)) {
            throw new IllegalArgumentException("Đạo diễn đang được gán cho phim, không thể xóa!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_MYSQL)) {

            ps.setInt(1, directorId);

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Tìm đạo diễn theo mã.
     * 
     * @param directorId - Mã đạo diễn
     * @return đối tượng Director nếu tìm thấy, ngược lại trả về null
     * @throws SQLException nếu có lỗi SQL
     */
    public Director findById(int directorId) throws SQLException {
        if (directorId <= 0) {
            throw new IllegalArgumentException("directorId phải lớn hơn 0!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID_MYSQL)) {

            ps.setInt(1, directorId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDirector(rs);
                }
            }
        }

        return null;
    }

    /**
     * Lấy tất cả đạo diễn.
     * 
     * @return danh sách đạo diễn
     * @throws SQLException nếu có lỗi SQL
     */
    public List<Director> getAllDirectors() throws SQLException {
        List<Director> directors = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_ALL_MYSQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                directors.add(mapResultSetToDirector(rs));
            }
        }

        return directors;
    }

    /**
     * Tìm đạo diễn theo tên gần đúng.
     * 
     * @param keyword - Từ khóa tên đạo diễn
     * @return danh sách đạo diễn phù hợp
     * @throws SQLException nếu có lỗi SQL
     */
    public List<Director> searchByName(String keyword) throws SQLException {
        keyword = (keyword == null) ? "" : keyword.trim();

        if (keyword.length() > 255) {
            throw new IllegalArgumentException("keyword không được vượt quá 255 ký tự!");
        }

        List<Director> directors = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SEARCH_BY_NAME_MYSQL)) {

            ps.setString(1, "%" + keyword + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    directors.add(mapResultSetToDirector(rs));
                }
            }
        }

        return directors;
    }
}


/* Bản cũ
package main.java.com.cinema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import main.java.com.cinema.config.DBConnection;
import main.java.com.cinema.entity.Director;


public class DirectorDao {
	public static final String INSERT_MYSQL = """
			INSERT INTO dao_dien (
					ten_dao_dien
			)
			VALUES (?)
			""";
	public static final String UPDATE_MYSQL = """
			UPDATE dao_dien
			SET ten_dao_dien = ?
			WHERE ma_dao_dien =?
			""";
	private static final String DELETE_MYSQL = """
	        DELETE FROM dao_dien
	        WHERE ma_dao_dien = ?
			""";
	private static final String SELECT_BY_ID = """
	        SELECT ma_dao_dien,
	        ten_dao_dien,
	        created_at,
	        updated_at
	        FROM dao_dien
	        WHERE ma_dao_dien = ?
			""";
	private static final String SELECT_ALL_MYSQL = """
	        SELECT ma_dao_dien,
	        ten_dao_dien,
	        created_at,
	        updated_at
	        FROM dao_dien
	        ORDER BY ten_dao_dien ASC
			""";
	private static final String EXISTS_BY_NAME_MYSQL = """
	        SELECT 1 FROM dao_dien
	        WHERE ten_dao_dien = ?
	        LIMIT 1
			""";

	    private static final String EXISTS_BY_NAME_EXCEPT_ID = """
	        SELECT 1
	        FROM dao_dien
	        WHERE ten_dao_dien = ?
	    		    AND ma_dao_dien <> ?
	        LIMIT 1
	    		""";

	    private static final String IS_USED = """
	        SELECT 1 
	        FROM dao_dien_phim
	        WHERE ma_dao_dien = ?
	        LIMIT 1
	    	""";
	    private void validate(Director d) {
	        if (d == null || d.getDirectorName() == null || d.getDirectorName().trim().isEmpty()) {
	            throw new IllegalArgumentException("Tên đạo diễn không hợp lệ");
	        }
	    }
	    
	    private boolean existsByName(String name) throws SQLException {
	        try (Connection conn = DBConnection.getConnection();
	             PreparedStatement ps = conn.prepareStatement(EXISTS_BY_NAME_MYSQL)) {

	            ps.setString(1, name.trim());
	            return ps.executeQuery().next();
	        }
	    }

	    private boolean existsByNameExceptId(String name, int id) throws SQLException {
	        try (Connection conn = DBConnection.getConnection();
	             PreparedStatement ps = conn.prepareStatement(EXISTS_BY_NAME_EXCEPT_ID)) {

	            ps.setString(1, name.trim());
	            ps.setInt(2, id);
	            return ps.executeQuery().next();
	        }
	    }

	    private boolean isUsed(int id) throws SQLException {
	        try (Connection conn = DBConnection.getConnection();
	             PreparedStatement ps = conn.prepareStatement(IS_USED)) {

	            ps.setInt(1, id);
	            return ps.executeQuery().next();
	        }
	    }

	    public boolean add(Director d) throws SQLException {
	        validate(d);

	        if (existsByName(d.getDirectorName())) {
	            throw new IllegalArgumentException("Đạo diễn đã tồn tại!");
	        }

	        try (Connection conn = DBConnection.getConnection();
	             PreparedStatement ps = conn.prepareStatement(INSERT_MYSQL)) {

	            ps.setString(1, d.getDirectorName().trim());
	            return ps.executeUpdate() > 0;
	        }
	    }

	    public boolean update(Director d) throws SQLException {
	        validate(d);

	        if (existsByNameExceptId(d.getDirectorName(), d.getDirectorId())) {
	            throw new IllegalArgumentException("Tên bị trùng!");
	        }

	        try (Connection conn = DBConnection.getConnection();
	             PreparedStatement ps = conn.prepareStatement(UPDATE_MYSQL)) {

	            ps.setString(1, d.getDirectorName());
	            ps.setInt(2, d.getDirectorId());
	            return ps.executeUpdate() > 0;
	        }
	    }

	    public boolean delete(int id) throws SQLException {
	        if (isUsed(id)) {
	            throw new IllegalArgumentException("Đạo diễn đang được sử dụng!");
	        }

	        try (Connection conn = DBConnection.getConnection();
	             PreparedStatement ps = conn.prepareStatement(DELETE_MYSQL)) {

	            ps.setInt(1, id);
	            return ps.executeUpdate() > 0;
	        }
	    }

	    public Director findById(int id) throws SQLException {
	        try (Connection conn = DBConnection.getConnection();
	             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {

	            ps.setInt(1, id);
	            ResultSet rs = ps.executeQuery();

	            if (rs.next()) {
	                return new Director(
	                    rs.getInt("ma_dao_dien"),
	                    rs.getString("ten_dao_dien"),
	                    rs.getTimestamp("created_at").toLocalDateTime(),
	                    rs.getTimestamp("updated_at").toLocalDateTime()
	                );
	            }
	        }
	        return null;
	    }
	    
}
*/