package com.cinema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.cinema.config.DBConnection;
import com.cinema.entity.Theater;

/**
 * DAO cho thực thể Theater
 * 
 * Bảng ánh xạ:
 * rap(ma_rap, ten_rap, dia_chi, created_at, updated_at)
 * 
 * @author Hải Anh (chính)
 */
public class TheaterDAO {

    private static final String INSERT_MYSQL = """
            INSERT INTO rap(
                ten_rap,
                dia_chi
            )
            VALUES(?, ?)
            """;

    private static final String UPDATE_MYSQL = """
            UPDATE rap
            SET ten_rap = ?,
                dia_chi = ?
            WHERE ma_rap = ?
            """;

    private static final String DELETE_MYSQL = """
            DELETE FROM rap
            WHERE ma_rap = ?
            """;

    private static final String SELECT_BY_ID_MYSQL = """
            SELECT *
            FROM rap
            WHERE ma_rap = ?
            """;

    private static final String SELECT_ALL_MYSQL = """
            SELECT *
            FROM rap
            ORDER BY ten_rap ASC
            """;

    private static final String SEARCH_BY_NAME_MYSQL = """
            SELECT *
            FROM rap
            WHERE ten_rap LIKE ?
            ORDER BY ten_rap ASC
            """;

    private static final String EXISTS_BY_NAME_MYSQL = """
            SELECT 1
            FROM rap
            WHERE ten_rap = ?
            LIMIT 1
            """;

    private static final String EXISTS_BY_NAME_EXCEPT_ID_MYSQL = """
            SELECT 1
            FROM rap
            WHERE ten_rap = ?
              AND ma_rap <> ?
            LIMIT 1
            """;

    private static final String IS_USED_IN_SCREEN_MYSQL = """
    	    SELECT 1
    	    FROM phong_chieu
    	    WHERE ma_rap = ?
    	    LIMIT 1
    	    """;
    
    /**
     * Kiểm tra dữ liệu đầu vào của Theater
     * 
     * @param theater - Đối tượng Theater để kiểm tra
     */
    private void validateTheater(Theater theater) { //TODO: làm validate package
    	//ScreenTypeValidator -> package Validator
        if(theater == null)
            throw new IllegalArgumentException("Rạp không hợp lệ!");
        
        if(theater.getTheaterName() == null || theater.getTheaterName().trim().isEmpty())
            throw new IllegalArgumentException("Tên rạp không được để trống!");
    }

    /**
     * Ánh xạ một dòng dữ liệu ResultSet thành đối tượng Theater.
     * 
     * @param rs - ResultSet đang trỏ tới dòng dữ liệu hợp lệ
     * @return đối tượng Theater
     * @throws SQLException nếu có lỗi đọc dữ liệu
     */
    private Theater mapResultSetToTheater(ResultSet rs) throws SQLException {
        Timestamp createdAt = rs.getTimestamp("created_at");
        Timestamp updatedAt = rs.getTimestamp("updated_at");

        return new Theater(
                rs.getInt("ma_rap"),
                rs.getString("ten_rap"),
                rs.getString("dia_chi"),
                createdAt != null ? createdAt.toLocalDateTime() : null,
                updatedAt != null ? updatedAt.toLocalDateTime() : null
        );
    }

    /**
     * Kiểm tra rạp đã tồn tại theo tên
     * @param theaterName - Tên rạp
     * @return true nếu đã tồn tại
     * @throws SQLException nếu có lỗi SQL
     */
    private boolean existsByName(String theaterName) throws SQLException {
        if(theaterName == null || theaterName.trim().isEmpty())
            throw new IllegalArgumentException("Tên rạp không được để trống!");

        try(Connection connection = DBConnection.getConnection();
        		PreparedStatement ps = connection.prepareStatement(EXISTS_BY_NAME_MYSQL)) {

            ps.setString(1, theaterName.trim());

            try(ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Kiểm tra rạp đã tồn tại ở bản ghi khác hay chưa
     * @param theaterName - Tên rạp
     * @param theaterId - Mã rạp
     * @return true nếu đã tồn tại ở bản ghi khác
     * @throws SQLException nếu có lỗi SQL
     */
    private boolean existsByNameExceptId(String theaterName, int theaterId) throws SQLException {
        if(theaterName == null || theaterName.trim().isEmpty())
            throw new IllegalArgumentException("Tên rạp không được để trống!");

        try(Connection connection = DBConnection.getConnection();
        		PreparedStatement ps = connection.prepareStatement(EXISTS_BY_NAME_EXCEPT_ID_MYSQL)) {

            ps.setString(1, theaterName.trim());
            ps.setInt(2, theaterId);

            try(ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Kiểm tra rạp có đang được sử dụng ở các bảng Screen hay không.
     * 
     * @param theaterId - Mã rạp
     * @return true nếu đang được sử dụng
     * @throws SQLException nếu có lỗi SQL
     */
    private boolean isUsedInScreen(int theaterId) throws SQLException {
        try(Connection connection = DBConnection.getConnection();
        		PreparedStatement ps = connection.prepareStatement(IS_USED_IN_SCREEN_MYSQL)) {

            ps.setInt(1, theaterId);

            try(ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Kiểm tra rạp có đang được sử dụng ở các bảng liên quan hay không.
     * 
     * @param screenTypeId - Mã phòng chiếu
     * @return true nếu đang được sử dụng
     * @throws SQLException nếu có lỗi SQL
     */
    private boolean isTheaterUsed(int screenTypeId) throws SQLException {
        if(screenTypeId <= 0)
            throw new IllegalArgumentException("Mã loại phòng phải lớn hơn 0!");
        
        return isUsedInScreen(screenTypeId);
    }
    
    /**
     * Thêm rạp mới
     * @param theater - rạp cần thêm
     * @return true nếu thêm thành công
     * @throws SQLException nếu có lỗi SQL
     */
    public boolean addTheater(Theater theater) throws SQLException {
        validateTheater(theater);

        if(existsByName(theater.getTheaterName()))
            throw new IllegalArgumentException("Rạp đã tồn tại!");

        try(Connection connection = DBConnection.getConnection();
        		PreparedStatement ps = connection.prepareStatement(INSERT_MYSQL)) {

            ps.setString(1, theater.getTheaterName());
            ps.setString(2, theater.getTheaterAddress());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Cập nhật thông tin rạp
     * 
     * @param theater - rạp cần cập nhật
     * @return true nếu cập nhật thành công, false nếu không tìm thấy rạp
     * @throws SQLException nếu có lỗi SQL
     */
    public boolean updateTheater(Theater theater) throws SQLException {
        validateTheater(theater);

        if(theater.getTheaterId() <= 0)
            throw new IllegalArgumentException("Mã rạp phải lớn hơn 0!");

        if(existsByNameExceptId(theater.getTheaterName(), theater.getTheaterId()))
            throw new IllegalArgumentException("Tên rạp đã tồn tại ở bản ghi khác!");

        try(Connection connection = DBConnection.getConnection();
        		PreparedStatement ps = connection.prepareStatement(UPDATE_MYSQL)) {

            ps.setString(1, theater.getTheaterName());
            ps.setString(2, theater.getTheaterAddress());
            ps.setInt(3, theater.getTheaterId());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Xóa rạp theo mã.
     * Không cho xóa nếu rạp đang được dùng trong bảng liên kết.
     * 
     * @param theaterId - Mã rạp
     * @return true nếu xóa thành công, false nếu không xóa được
     * @throws SQLException nếu có lỗi SQL
     */
    public boolean deleteTheaterById(int theaterId) throws SQLException {
        if(theaterId <= 0)
            throw new IllegalArgumentException("Mã rạp phải lớn hơn 0!");

        if(isTheaterUsed(theaterId))
            throw new IllegalArgumentException("Loại phòng đang được sử dụng, không thể xóa!");
        
        try(Connection connection = DBConnection.getConnection();
        		PreparedStatement ps = connection.prepareStatement(DELETE_MYSQL)) {

            ps.setInt(1, theaterId);

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Tìm rạp theo mã.
     * 
     * @param theaterId - Mã rạp
     * @return đối tượng Theater nếu tìm thấy, ngược lại trả về null
     * @throws SQLException nếu có lỗi SQL
     */
    public Theater findById(int theaterId) throws SQLException {
        if(theaterId <= 0)
            throw new IllegalArgumentException("Mã rạp phải lớn hơn 0!");

        try(Connection connection = DBConnection.getConnection();
        		PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID_MYSQL)) {

            ps.setInt(1, theaterId);

            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    return mapResultSetToTheater(rs);
                }
            }
        }

        return null;
    }

    /**
     * Tìm rạp theo tên gần đúng.
     * 
     * @param keyword - Từ khóa tên rạp
     * @return danh sách rạp phù hợp
     * @throws SQLException nếu có lỗi SQL
     */
    public List<Theater> findByName(String keyword) throws SQLException {
        List<Theater> theaters = new ArrayList<Theater>();

        try(Connection connection = DBConnection.getConnection();
        		PreparedStatement ps = connection.prepareStatement(SEARCH_BY_NAME_MYSQL)) {

            keyword = (keyword == null) ? "" : keyword.trim();
            ps.setString(1, "%" + keyword + "%");

            try(ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                	theaters.add(mapResultSetToTheater(rs));
                }
            }
        }

        return theaters;
    }

    /**
     * Lấy tất cả rạp.
     * 
     * @return danh sách rạp
     * @throws SQLException nếu có lỗi SQL
     */
    public List<Theater> getAllTheaters() throws SQLException {
        List<Theater> theaters = new ArrayList<Theater>();

        try(Connection connection = DBConnection.getConnection();
        		PreparedStatement ps = connection.prepareStatement(SELECT_ALL_MYSQL);
        		ResultSet rs = ps.executeQuery()) {
            while(rs.next()) {
            	theaters.add(mapResultSetToTheater(rs));
            }
        }

        return theaters;
    }
}
