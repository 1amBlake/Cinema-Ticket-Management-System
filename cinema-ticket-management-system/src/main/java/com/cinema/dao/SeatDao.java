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
import com.cinema.validator.SeatValidator;

/**
 * DAO cho thực thể Seat
 * Chịu trách nhiệm thao tác dữ liệu với bảng ghe trong MySQL.
 * 
 * Bảng ánh xạ:
 * ghe(
 *     ma_ghe,
 *     ma_phong,
 *     ma_loai_ghe,
 *     hang,
 *     cot,
 *     trang_thai,
 *     created_at,
 *     updated_at
 * )
 * 
 * @author Hải Anh (chính)
 * @author Minh Huy (review/sửa)
 */
public class SeatDao {

    private static final String INSERT_MYSQL = """
            INSERT INTO ghe (
                ma_phong,
                ma_loai_ghe,
                hang,
                cot,
                trang_thai
            )
            VALUES (?, ?, ?, ?, ?)
            """;

    private static final String UPDATE_MYSQL = """
			UPDATE ghe
			SET ma_phong = ?,
			    ma_loai_ghe = ?,
			    hang = ?,
			    cot = ?
			WHERE ma_ghe = ?	
            """;

    private static final String UPDATE_STATUS_MYSQL = """
            UPDATE ghe
            SET trang_thai = ?
            WHERE ma_ghe = ?
            """;
    
    private static final String DELETE_MYSQL = """
            DELETE FROM ghe
            WHERE ma_ghe = ?
            """;

    private static final String SELECT_BY_ID_MYSQL = """
            SELECT ma_ghe,
                   ma_phong,
                   ma_loai_ghe,
                   hang,
                   cot,
                   trang_thai,
                   created_at,
                   updated_at
            FROM ghe
            WHERE ma_ghe = ?
            """;

    private static final String SELECT_ALL_MYSQL = """
            SELECT ma_ghe,
                   ma_phong,
                   ma_loai_ghe,
                   hang,
                   cot,
                   trang_thai,
                   created_at,
                   updated_at
            FROM ghe
            ORDER BY ma_phong ASC, hang ASC, cot ASC, ma_ghe ASC
            """;

    private static final String SEARCH_BY_SCREEN_ID_MYSQL = """
            SELECT ma_ghe,
                   ma_phong,
                   ma_loai_ghe,
                   hang,
                   cot,
                   trang_thai,
                   created_at,
                   updated_at
            FROM ghe
            WHERE ma_phong = ?
            ORDER BY hang ASC, cot ASC, ma_ghe ASC
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

    private static final String EXISTS_SCREEN_BY_ID_MYSQL = """
            SELECT 1
            FROM phong_chieu
            WHERE ma_phong = ?
            LIMIT 1
            """;

    private static final String EXISTS_SEAT_TYPE_BY_ID_MYSQL = """
            SELECT 1
            FROM loai_ghe
            WHERE ma_loai_ghe = ?
            LIMIT 1
            """;
    
    private static final String IS_USED_IN_TICKET_MYSQL = """
            SELECT 1
            FROM ve
            WHERE ma_ghe = ?
            LIMIT 1
            """;

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
     * Kiểm tra ghế đã tồn tại theo vị trí trong cùng phòng chiếu hay chưa.
     * 
     * @param screenId - Mã phòng chiếu
     * @param seatRow - Hàng ghế
     * @param seatCol - Cột ghế
     * @return true nếu đã tồn tại
     * @throws SQLException nếu có lỗi SQL
     */
    private boolean existsByPosition(int screenId, String seatRow, String seatCol) throws SQLException {
        if (screenId <= 0) {
            throw new IllegalArgumentException("screenId phải lớn hơn 0!");
        }

        if (seatRow == null || seatRow.trim().isEmpty()) {
            throw new IllegalArgumentException("seatRow không được để trống!");
        }

        if (seatCol == null || seatCol.trim().isEmpty()) {
            throw new IllegalArgumentException("seatCol không được để trống!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(EXISTS_BY_POSITION_MYSQL)) {

            ps.setInt(1, screenId);
            ps.setString(2, seatRow.trim());
            ps.setString(3, seatCol.trim());

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Kiểm tra ghế đã tồn tại ở bản ghi khác trong cùng vị trí hay chưa.
     * 
     * @param screenId - Mã phòng chiếu
     * @param seatRow - Hàng ghế
     * @param seatCol - Cột ghế
     * @param seatId - Mã ghế
     * @return true nếu đã tồn tại ở bản ghi khác
     * @throws SQLException nếu có lỗi SQL
     */
    private boolean existsByPositionExceptId(int screenId, String seatRow, String seatCol, int seatId)
            throws SQLException {
        if (screenId <= 0 || seatId <= 0) {
            throw new IllegalArgumentException("screenId và seatId phải lớn hơn 0!");
        }

        if (seatRow == null || seatRow.trim().isEmpty()) {
            throw new IllegalArgumentException("seatRow không được để trống!");
        }

        if (seatCol == null || seatCol.trim().isEmpty()) {
            throw new IllegalArgumentException("seatCol không được để trống!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(EXISTS_BY_POSITION_EXCEPT_ID_MYSQL)) {

            ps.setInt(1, screenId);
            ps.setString(2, seatRow.trim());
            ps.setString(3, seatCol.trim());
            ps.setInt(4, seatId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
    
    /**
     * Kiểm tra phòng chiếu có tồn tại hay không.
     * 
     * @param screenId - Mã phòng chiếu
     * @return true nếu phòng chiếu tồn tại
     * @throws SQLException nếu có lỗi SQL
     */
    private boolean existsScreenById(int screenId) throws SQLException {
        if (screenId <= 0) {
            throw new IllegalArgumentException("screenId phải lớn hơn 0!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(EXISTS_SCREEN_BY_ID_MYSQL)) {

            ps.setInt(1, screenId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Kiểm tra loại ghế có tồn tại hay không.
     * 
     * @param seatTypeId - Mã loại ghế
     * @return true nếu loại ghế tồn tại
     * @throws SQLException nếu có lỗi SQL
     */
    private boolean existsSeatTypeById(int seatTypeId) throws SQLException {
        if (seatTypeId <= 0) {
            throw new IllegalArgumentException("seatTypeId phải lớn hơn 0!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(EXISTS_SEAT_TYPE_BY_ID_MYSQL)) {

            ps.setInt(1, seatTypeId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Kiểm tra ghế có đang được sử dụng ở bảng Ticket hay không.
     * 
     * @param seatId - Mã ghế
     * @return true nếu đang được sử dụng
     * @throws SQLException nếu có lỗi SQL
     */
    private boolean isUsedInTicket(int seatId) throws SQLException {
        if (seatId <= 0) {
            throw new IllegalArgumentException("seatId phải lớn hơn 0!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(IS_USED_IN_TICKET_MYSQL)) {

            ps.setInt(1, seatId);

            try (ResultSet rs = ps.executeQuery()) {
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
        if (seatId <= 0) {
            throw new IllegalArgumentException("seatId phải lớn hơn 0!");
        }

        return isUsedInTicket(seatId);
    }

    /**
     * Thêm ghế mới.
     * 
     * @param seat - Ghế cần thêm
     * @return true nếu thêm thành công
     * @throws SQLException nếu có lỗi SQL
     */
    public boolean addSeat(Seat seat) throws SQLException {
        SeatValidator.validateForCreate(seat);

        if (!existsScreenById(seat.getScreen().getScreenId())) {
            throw new IllegalArgumentException("Phòng chiếu không tồn tại!");
        }

        if (!existsSeatTypeById(seat.getSeatType().getSeatTypeId())) {
            throw new IllegalArgumentException("Loại ghế không tồn tại!");
        }
        
        if (existsByPosition(
                seat.getScreen().getScreenId(),
                seat.getSeatRow(),
                seat.getSeatCol())) {
            throw new IllegalArgumentException("Ghế đã tồn tại tại vị trí này trong phòng chiếu!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_MYSQL)) {

            ps.setInt(1, seat.getScreen().getScreenId());
            ps.setInt(2, seat.getSeatType().getSeatTypeId());
            ps.setString(3, seat.getSeatRow().trim());
            ps.setString(4, seat.getSeatCol().trim());
            ps.setInt(5, seat.getSeatStatus().getSeatStatusId());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Cập nhật thông tin ghế.
     * 
     * Nghiệp vụ đề xuất:
     * - Không cho cập nhật ghế khi ghế đã phát sinh vé,
     *   để tránh lệch dữ liệu lịch sử bán vé.
     * 
     * @param seat - Ghế cần cập nhật
     * @return true nếu cập nhật thành công, false nếu không tìm thấy ghế
     * @throws SQLException nếu có lỗi SQL
     */
    public boolean updateSeat(Seat seat) throws SQLException {
        SeatValidator.validateForUpdate(seat);

        if (seat.getSeatId() <= 0) {
            throw new IllegalArgumentException("seatId phải lớn hơn 0!");
        }

        if (isSeatUsed(seat.getSeatId())) {
            throw new IllegalArgumentException("Ghế đã phát sinh vé, không thể cập nhật!");
        }
        
        if (!existsScreenById(seat.getScreen().getScreenId())) {
            throw new IllegalArgumentException("Phòng chiếu không tồn tại!");
        }

        if (!existsSeatTypeById(seat.getSeatType().getSeatTypeId())) {
            throw new IllegalArgumentException("Loại ghế không tồn tại!");
        }

        if (existsByPositionExceptId(
                seat.getScreen().getScreenId(),
                seat.getSeatRow(),
                seat.getSeatCol(),
                seat.getSeatId())) {
            throw new IllegalArgumentException("Đã tồn tại ghế khác ở vị trí này trong phòng chiếu!");
        }

        try (Connection connection = DBConnection.getConnection();
        	     PreparedStatement ps = connection.prepareStatement(UPDATE_MYSQL)) {

        	    ps.setInt(1, seat.getScreen().getScreenId());
        	    ps.setInt(2, seat.getSeatType().getSeatTypeId());
        	    ps.setString(3, seat.getSeatRow().trim());
        	    ps.setString(4, seat.getSeatCol().trim());
        	    ps.setInt(5, seat.getSeatId());

        	    return ps.executeUpdate() > 0;
        	}
    }
    
    /**
     * Cập nhật trạng thái vận hành của ghế.
     *
     * Method này chỉ cập nhật cột trang_thai, không thay đổi phòng chiếu,
     * loại ghế, hàng hoặc cột. Dùng cho nghiệp vụ chuyển ghế sang bảo trì
     * hoặc mở lại ghế hoạt động.
     *
     * @param seatId - Mã ghế cần cập nhật
     * @param status - Trạng thái mới của ghế
     * @return true nếu cập nhật thành công, false nếu không tìm thấy ghế
     * @throws SQLException nếu có lỗi SQL
     */
    public boolean updateSeatStatusById(int seatId, SeatStatus status) throws SQLException {
        if (seatId <= 0) {
            throw new IllegalArgumentException("seatId phải lớn hơn 0!");
        }

        if (status == null) {
            throw new IllegalArgumentException("seatStatus không được null!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_STATUS_MYSQL)) {

            ps.setInt(1, status.getSeatStatusId());
            ps.setInt(2, seatId);

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Xóa ghế theo mã.
     * Không cho xóa nếu ghế đang được dùng trong bảng liên quan.
     * 
     * @param seatId - Mã ghế
     * @return true nếu xóa thành công, false nếu không xóa được
     * @throws SQLException nếu có lỗi SQL
     */
    public boolean deleteSeatById(int seatId) throws SQLException {
        if (seatId <= 0) {
            throw new IllegalArgumentException("seatId phải lớn hơn 0!");
        }

        if (isSeatUsed(seatId)) {
            throw new IllegalArgumentException("Ghế đang được sử dụng, không thể xóa!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_MYSQL)) {

            ps.setInt(1, seatId);

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Tìm ghế theo mã.
     * 
     * @param seatId - Mã ghế
     * @return đối tượng Seat nếu tìm thấy, ngược lại trả về null
     * @throws SQLException nếu có lỗi SQL
     */
    public Seat findById(int seatId) throws SQLException {
        if (seatId <= 0) {
            throw new IllegalArgumentException("seatId phải lớn hơn 0!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID_MYSQL)) {

            ps.setInt(1, seatId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSeat(rs);
                }
            }
        }

        return null;
    }

    /**
     * Tìm ghế theo mã phòng chiếu.
     * 
     * @param screenId - Mã phòng chiếu
     * @return danh sách ghế phù hợp
     * @throws SQLException nếu có lỗi SQL
     */
    public List<Seat> searchByScreenId(int screenId) throws SQLException {
        if (screenId <= 0) {
            throw new IllegalArgumentException("screenId phải lớn hơn 0!");
        }

        List<Seat> seats = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SEARCH_BY_SCREEN_ID_MYSQL)) {

            ps.setInt(1, screenId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
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
        List<Seat> seats = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_ALL_MYSQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                seats.add(mapResultSetToSeat(rs));
            }
        }

        return seats;
    }
}