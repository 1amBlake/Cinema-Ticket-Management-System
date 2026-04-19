package com.cinema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.cinema.config.DBConnection;
import com.cinema.entity.Screen;
import com.cinema.entity.Seat;
import com.cinema.entity.SeatType;
import com.cinema.enums.SeatStatus;

/**
 * DAO cho thực thể Seat
 * 
 * Bảng ánh xạ:
 * ghe(ma_ghe, ma_phong, ma_loai_ghe, hang, cot, trang_thai, created_at, updated_at)
 * 
 * @author Hải Anh (chính)
 */
public class SeatDAO {

    private static final String INSERT_MYSQL = """
            INSERT INTO ghe(
                ma_phong,
                ma_loai_ghe,
                hang,
                cot,
                trang_thai
            )
            VALUES(?, ?, ?, ?, ?)
            """;

    private static final String UPDATE_MYSQL = """
            UPDATE ghe
            SET ma_phong = ?,
                ma_loai_ghe = ?,
                hang = ?,
                cot = ?,
                trang_thai = ?
            WHERE ma_ghe = ?
            """;

    private static final String DELETE_MYSQL = """
            DELETE FROM ghe
            WHERE ma_ghe = ?
            """;

    private static final String SELECT_BY_ID_MYSQL = """
            SELECT *
            FROM ghe
            WHERE ma_ghe = ?
            """;

    private static final String SELECT_ALL_MYSQL = """
            SELECT *
            FROM ghe
            ORDER BY hang ASC, cot ASC
            """;

    private static final String SEARCH_BY_SCREEN_MYSQL = """
            SELECT *
            FROM ghe
            WHERE ma_phong = ?
            ORDER BY hang ASC, cot ASC
            """;

    private static final String EXISTS_BY_POSITION_MYSQL = """
            SELECT 1
            FROM ghe
            WHERE ma_phong = ?
              AND hang = ?
              AND cot = ?
            LIMIT 1
            """;

    private static final String EXISTS_BY_POSITION_EXCEPT_ID_MYSQL = """
            SELECT 1
            FROM ghe
            WHERE ma_phong = ?
              AND hang = ?
              AND cot = ?
              AND ma_ghe <> ?
            LIMIT 1
            """;

    private static final String IS_USED_IN_TICKET_MYSQL = """
	        SELECT 1
	        FROM ve
	        WHERE ma_ghe = ?
	        LIMIT 1
	        """;
    
    /**
     * Kiểm tra dữ liệu đầu vào của Seat
     * 
     * @param seat - Đối tượng Seat để kiểm tra
     */
    private void validateSeat(Seat seat) { //TODO: làm validate package
    	//ScreenTypeValidator -> package Validator
        if(seat == null)
            throw new IllegalArgumentException("Ghế không hợp lệ!");
        if(seat.getScreen() == null || seat.getScreen().getScreenId() <= 0)
            throw new IllegalArgumentException("Phòng chiếu không hợp lệ!");
        if(seat.getSeatType() == null || seat.getSeatType().getSeatTypeId() <= 0)
            throw new IllegalArgumentException("Loại ghế không hợp lệ!");
        if(seat.getSeatRow() == null || seat.getSeatRow().trim().isEmpty())
            throw new IllegalArgumentException("Hàng ghế không được để trống!");
        if(seat.getSeatCol() == null || seat.getSeatCol().trim().isEmpty())
            throw new IllegalArgumentException("Cột ghế không được để trống!");
        if(seat.getSeatStatus() == null)
            throw new IllegalArgumentException("Trạng thái ghế không hợp lệ!");
    }

    /**
     * Ánh xạ một dòng dữ liệu ResultSet thành đối tượng Seat.
     * 
     * @param rs - ResultSet đang trỏ tới dòng dữ liệu hợp lệ
     * @return đối tượng Seat
     * @throws SQLException nếu có lỗi đọc dữ liệu
     */
    private Seat mapResultSetToSeat(ResultSet rs) throws SQLException {
        Timestamp createdAt = rs.getTimestamp("created_at");
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        Screen screen = new Screen(rs.getInt("ma_phong"));
        SeatType seatType = new SeatType(rs.getInt("ma_loai_ghe"));

        return new Seat(
                rs.getInt("ma_ghe"),
                screen,
                seatType,
                rs.getString("hang"),
                rs.getString("cot"),
                SeatStatus.fromId(rs.getInt("trang_thai")),
                createdAt != null ? createdAt.toLocalDateTime() : null,
                updatedAt != null ? updatedAt.toLocalDateTime() : null
        );
    }

    /**
     * Kiểm tra ghế đã tồn tại theo vị trí phòng chiếu
     * @param screenId - Mã phòng chiếu
     * @param seatRow - Hàng
     * @param seatCol - Cột
     * @return true nếu đã tồn tại
     * @throws SQLException nếu có lỗi SQL
     */
    private boolean existsByPosition(int screenId, String seatRow, String seatCol) throws SQLException {
        if(seatRow == null || seatRow.trim().isEmpty())
            throw new IllegalArgumentException("Hàng không được để trống!");

        if(seatCol == null || seatCol.trim().isEmpty())
            throw new IllegalArgumentException("Cột không được để trống!");
        
        try(Connection connection = DBConnection.getConnection();
        		PreparedStatement ps = connection.prepareStatement(EXISTS_BY_POSITION_MYSQL)) {

            ps.setInt(1, screenId);
            ps.setString(2, seatRow.trim());
            ps.setString(3, seatCol.trim());

            try(ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Kiểm tra ghế đã tồn tại ở bản ghi khác hay chưa
     * @param screenId - Mã phòng chiếu
     * @param seatRow - Hàng
     * @param seatCol - Cột
     * @param seatId - Mã ghế
     * @return true nếu đã tồn tại
     * @throws SQLException nếu có lỗi SQL
     */
    private boolean existsByPositionExceptId(int screenId, String seatRow, String seatCol, int seatId) throws SQLException {
        if(seatRow == null || seatRow.trim().isEmpty())
            throw new IllegalArgumentException("Hàng không được để trống!");

        if(seatCol == null || seatCol.trim().isEmpty())
            throw new IllegalArgumentException("Cột không được để trống!");
        
        try(Connection connection = DBConnection.getConnection();
        		PreparedStatement ps = connection.prepareStatement(EXISTS_BY_POSITION_EXCEPT_ID_MYSQL)) {

            ps.setInt(1, screenId);
            ps.setString(2, seatRow.trim());
            ps.setString(3, seatCol.trim());
            ps.setInt(4, seatId);

            try(ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Kiểm tra ghế có đang được sử dụng ở các bảng Ticket hay không.
     * 
     * @param seatId - Mã ghế
     * @return true nếu đang được sử dụng
     * @throws SQLException nếu có lỗi SQL
     */
    private boolean isUsedInTicket(int seatId) throws SQLException {
        try(Connection connection = DBConnection.getConnection();
        		PreparedStatement ps = connection.prepareStatement(IS_USED_IN_TICKET_MYSQL)) {

            ps.setInt(1, seatId);

            try(ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Kiểm tra ghế có đang được sử dụng ở các bảng liên quan hay không.
     * 
     * @param seatId - Mã ghế
     * @return true nếu đang được sử dụng
     * @throws SQLException nếu có lỗi SQL
     */
    private boolean isSeatUsed(int seatId) throws SQLException {
        if(seatId <= 0)
            throw new IllegalArgumentException("Mã loại phòng phải lớn hơn 0!");
        
        return isUsedInTicket(seatId);
    }
    
    /**
     * Thêm ghế mới
     * @param seat - ghế cần thêm
     * @return true nếu thêm thành công
     * @throws SQLException nếu có lỗi SQL
     */
    public boolean addSeat(Seat seat) throws SQLException {
        validateSeat(seat);

        if(seat.getScreen().getScreenId() <= 0)
            throw new IllegalArgumentException("Mã phòng chiếu lớn hơn 0!");

        if(existsByPosition(seat.getScreen().getScreenId(), seat.getSeatRow(), seat.getSeatCol()))
            throw new IllegalArgumentException("Ghế đã tồn tại tại vị trí này!");

        try(Connection connection = DBConnection.getConnection();
        		PreparedStatement ps = connection.prepareStatement(INSERT_MYSQL)) {

            ps.setInt(1, seat.getScreen().getScreenId());
            ps.setInt(2, seat.getSeatType().getSeatTypeId());
            ps.setString(3, seat.getSeatRow());
            ps.setString(4, seat.getSeatCol());
            ps.setInt(5, seat.getSeatStatus().getSeatStatusId());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Cập nhật thông tin ghế
     * 
     * @param seat - ghế cần cập nhật
     * @return true nếu cập nhật thành công, false nếu không tìm thấy rạp
     * @throws SQLException nếu có lỗi SQL
     */
    public boolean updateSeat(Seat seat) throws SQLException {
        validateSeat(seat);

        if(seat.getSeatId() <= 0)
            throw new IllegalArgumentException("Mã ghế phải lớn hơn 0!");

        if(seat.getScreen().getScreenId() <= 0)
            throw new IllegalArgumentException("Mã phòng chiếu lớn hơn 0!");

        if(existsByPositionExceptId(seat.getScreen().getScreenId(), seat.getSeatRow(), seat.getSeatCol(), seat.getSeatId()))
            throw new IllegalArgumentException("Ghế đã tồn tại ở vị trí khác!");

        try(Connection connection = DBConnection.getConnection();
        		PreparedStatement ps = connection.prepareStatement(UPDATE_MYSQL)) {

            ps.setInt(1, seat.getScreen().getScreenId());
            ps.setInt(2, seat.getSeatType().getSeatTypeId());
            ps.setString(3, seat.getSeatRow());
            ps.setString(4, seat.getSeatCol());
            ps.setInt(5, seat.getSeatStatus().getSeatStatusId());
            ps.setInt(6, seat.getSeatId());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Xóa ghế theo mã.
     * Không cho xóa nếu ghế đang được dùng trong bảng liên kết.
     * 
     * @param seatId - Mã ghế
     * @return true nếu xóa thành công, false nếu không xóa được
     * @throws SQLException nếu có lỗi SQL
     */
    public boolean deleteSeatById(int seatId) throws SQLException {
        if(seatId <= 0)
            throw new IllegalArgumentException("Mã ghế phải lớn hơn 0!");

        if(isSeatUsed(seatId))
            throw new IllegalArgumentException("Ghế đang được sử dụng, không thể xóa!");

        try(Connection connection = DBConnection.getConnection();
        		PreparedStatement ps = connection.prepareStatement(DELETE_MYSQL)) {

            ps.setInt(1, seatId);

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Tìm ghế theo mã.
     * 
     * @param seatId - Mã ghế
     * @return đối tượng Theater nếu tìm thấy, ngược lại trả về null
     * @throws SQLException nếu có lỗi SQL
     */
    public Seat findById(int seatId) throws SQLException {
        if(seatId <= 0)
            throw new IllegalArgumentException("Mã ghế phải lớn hơn 0!");

        try(Connection connection = DBConnection.getConnection();
        		PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID_MYSQL)) {

            ps.setInt(1, seatId);

            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    return mapResultSetToSeat(rs);
                }
            }
        }

        return null;
    }

    /**
     * Tìm ghế theo phòng chiếu.
     * 
     * @param screenId - Mã phòng chiếu
     * @return danh sách ghế phù hợp
     * @throws SQLException nếu có lỗi SQL
     */
    public List<Seat> findByScreen(int screenId) throws SQLException {
        List<Seat> seats = new ArrayList<Seat>();

        try(Connection connection = DBConnection.getConnection();
        		PreparedStatement ps = connection.prepareStatement(SEARCH_BY_SCREEN_MYSQL)) {

            ps.setInt(1, screenId);

            try(ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                	seats.add(mapResultSetToSeat(rs));
                }
            }
        }

        return seats;
    }

    /**
     * Lấy tất cả ghế.
     * 
     * @return danh sách ghế
     * @throws SQLException nếu có lỗi SQL
     */
    public List<Seat> getAllSeats() throws SQLException {
        List<Seat> seats = new ArrayList<Seat>();

        try(Connection connection = DBConnection.getConnection();
        		PreparedStatement ps = connection.prepareStatement(SELECT_ALL_MYSQL);
        		ResultSet rs = ps.executeQuery()) {
            while(rs.next()) {
            	seats.add(mapResultSetToSeat(rs));
            }
        }

        return seats;
    }
}
