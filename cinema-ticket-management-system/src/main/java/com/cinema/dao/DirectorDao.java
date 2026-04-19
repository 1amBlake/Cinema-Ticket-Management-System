package main.java.com.cinema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import main.java.com.cinema.config.DBConnection;
import main.java.com.cinema.entity.Director;

/**
 * DAO cho thực thể Director
 * Chịu trách nhiệm thao tác dữ liệu với bảng dao_dien trong MySQL
 *
 * Bảng ánh xạ:
 * dao_dien(ma_dao_dien, ten_dao_dien, created_at, updated_at)
 */
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
	    /**
	     * Kiểm tra đạo diễn có đang được sử dụng trong bảng dao_dien_phim hay không
	     *
	     * @param directorId - Mã đạo diễn
	     * @return true nếu đang được sử dụng
	     * @throws SQLException 
	     */
	    private boolean isUsed(int id) throws SQLException {
	        try (Connection conn = DBConnection.getConnection();
	             PreparedStatement ps = conn.prepareStatement(IS_USED)) {

	            ps.setInt(1, id);
	            return ps.executeQuery().next();
	        }
	    }
	    /**
	     * Thêm một đạo diễn mới
	     *
	     * @param director - Đạo diễn cần thêm
	     * @return true nếu thêm thành công, false nếu thất bại
	     * @throws SQLException
	     */
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
	    /**
	     * Cập nhật thông tin đạo diễn
	     *
	     * @param director - Đạo diễn cần cập nhật
	     * @return true nếu cập nhật thành công, false nếu không tìm thấy
	     * @throws SQLException nếu có lỗi SQL
	     */
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
	    /**
	     * Xóa đạo diễn theo mã
	     * Không cho xóa nếu đạo diễn đang được sử dụng trong bảng dao_dien_phim
	     *
	     * @param directorId - Mã đạo diễn
	     * @return true nếu xóa thành công
	     * @throws SQLException nếu có lỗi SQL
	     */
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
	    /**
	     * Tìm đạo diễn theo mã
	     *
	     * @param directorId - Mã đạo diễn
	     * @return đối tượng Director nếu tìm thấy, null nếu không
	     * @throws SQLException nếu có lỗi SQL
	     */
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
