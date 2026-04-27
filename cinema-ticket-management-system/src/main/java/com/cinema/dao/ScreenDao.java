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
import com.cinema.entity.ScreenType;
import com.cinema.entity.Theater;
import com.cinema.enums.ScreenStatus;
import com.cinema.validator.ScreenValidator;

/**
 * DAO cho thực thể Screen
 * Chịu trách nhiệm thao tác dữ liệu với bảng phong_chieu trong MySQL.
 * 
 * Bảng ánh xạ:
 * phong_chieu(
 *     ma_phong,
 *     ten_phong,
 *     ma_rap,
 *     ma_loai_phong,
 *     trang_thai,
 *     created_at,
 *     updated_at
 * )
 * 
 * @author Hải Anh (chính)
 * @author Minh Huy (review/sửa)
 */
public class ScreenDao {

    private static final String INSERT_MYSQL = """
            INSERT INTO phong_chieu (
                ten_phong,
                ma_rap,
                ma_loai_phong,
                trang_thai
            )
            VALUES (?, ?, ?, ?)
            """;

    private static final String UPDATE_MYSQL = """
            UPDATE phong_chieu
            SET ten_phong = ?,
                ma_rap = ?,
                ma_loai_phong = ?,
                trang_thai = ?
            WHERE ma_phong = ?
            """;

    private static final String DELETE_MYSQL = """
            DELETE FROM phong_chieu
            WHERE ma_phong = ?
            """;

    private static final String SELECT_BY_ID_MYSQL = """
            SELECT ma_phong,
                   ten_phong,
                   ma_rap,
                   ma_loai_phong,
                   trang_thai,
                   created_at,
                   updated_at
            FROM phong_chieu
            WHERE ma_phong = ?
            """;

    private static final String SELECT_ALL_MYSQL = """
            SELECT ma_phong,
                   ten_phong,
                   ma_rap,
                   ma_loai_phong,
                   trang_thai,
                   created_at,
                   updated_at
            FROM phong_chieu
            ORDER BY ten_phong ASC, ma_phong ASC
            """;

    private static final String SEARCH_BY_NAME_MYSQL = """
            SELECT ma_phong,
                   ten_phong,
                   ma_rap,
                   ma_loai_phong,
                   trang_thai,
                   created_at,
                   updated_at
            FROM phong_chieu
            WHERE ten_phong LIKE ?
            ORDER BY ten_phong ASC, ma_phong ASC
            """;

    private static final String EXISTS_BY_NAME_AND_THEATER_MYSQL = """
            SELECT 1
            FROM phong_chieu
            WHERE ten_phong = ?
              AND ma_rap = ?
            LIMIT 1
            """;

    private static final String EXISTS_BY_NAME_AND_THEATER_EXCEPT_ID_MYSQL = """
            SELECT 1
            FROM phong_chieu
            WHERE ten_phong = ?
              AND ma_rap = ?
              AND ma_phong <> ?
            LIMIT 1
            """;

    private static final String EXISTS_THEATER_BY_ID_MYSQL = """
            SELECT 1
            FROM rap
            WHERE ma_rap = ?
            LIMIT 1
            """;

    private static final String EXISTS_SCREEN_TYPE_BY_ID_MYSQL = """
            SELECT 1
            FROM loai_phong
            WHERE ma_loai_phong = ?
            LIMIT 1
            """;
    
    private static final String IS_USED_IN_MOVIE_SESSION_MYSQL = """
            SELECT 1
            FROM suat_chieu
            WHERE ma_phong = ?
            LIMIT 1
            """;

    private static final String IS_USED_IN_SEAT_MYSQL = """
            SELECT 1
            FROM ghe
            WHERE ma_phong = ?
            LIMIT 1
            """;

    /**
     * Ánh xạ một dòng dữ liệu ResultSet thành đối tượng Screen.
     * 
     * @param rs - ResultSet đang trỏ tới dòng dữ liệu hợp lệ
     * @return đối tượng Screen
     * @throws SQLException nếu có lỗi đọc dữ liệu
     */
    private Screen mapResultSetToScreen(ResultSet rs) throws SQLException {
        Timestamp createdAt = rs.getTimestamp("created_at");
        Timestamp updatedAt = rs.getTimestamp("updated_at");

        Theater theater = new Theater(rs.getInt("ma_rap"));
        ScreenType screenType = new ScreenType(rs.getInt("ma_loai_phong"));

        return new Screen(
                rs.getInt("ma_phong"),
                rs.getString("ten_phong"),
                theater,
                screenType,
                ScreenStatus.fromId(rs.getInt("trang_thai")),
                createdAt != null ? createdAt.toLocalDateTime() : null,
                updatedAt != null ? updatedAt.toLocalDateTime() : null
        );
    }

    /**
     * Kiểm tra phòng chiếu đã tồn tại theo tên trong cùng rạp hay chưa.
     * 
     * @param screenName - Tên phòng chiếu
     * @param theaterId - Mã rạp
     * @return true nếu đã tồn tại
     * @throws SQLException nếu có lỗi SQL
     */
    private boolean existsByNameAndTheaterId(String screenName, int theaterId) throws SQLException {
        if (screenName == null || screenName.trim().isEmpty()) {
            return false;
        }

        if (theaterId <= 0) {
            throw new IllegalArgumentException("theaterId phải lớn hơn 0!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(EXISTS_BY_NAME_AND_THEATER_MYSQL)) {

            ps.setString(1, screenName.trim());
            ps.setInt(2, theaterId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Kiểm tra phòng chiếu đã tồn tại ở bản ghi khác trong cùng rạp hay chưa.
     * 
     * @param screenName - Tên phòng chiếu
     * @param theaterId - Mã rạp
     * @param screenId - Mã phòng chiếu
     * @return true nếu đã tồn tại ở bản ghi khác
     * @throws SQLException nếu có lỗi SQL
     */
    private boolean existsByNameAndTheaterIdExceptId(String screenName, int theaterId, int screenId)
            throws SQLException {
        if (screenName == null || screenName.trim().isEmpty()) {
            throw new IllegalArgumentException("screenName không được để trống!");
        }

        if (theaterId <= 0 || screenId <= 0) {
            throw new IllegalArgumentException("theaterId và screenId phải lớn hơn 0!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(EXISTS_BY_NAME_AND_THEATER_EXCEPT_ID_MYSQL)) {

            ps.setString(1, screenName.trim());
            ps.setInt(2, theaterId);
            ps.setInt(3, screenId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Kiểm tra rạp có tồn tại hay không.
     * 
     * @param theaterId - Mã rạp
     * @return true nếu tồn tại
     * @throws SQLException nếu có lỗi SQL
     */
    private boolean existsTheaterById(int theaterId) throws SQLException {
        if (theaterId <= 0) {
            throw new IllegalArgumentException("theaterId phải lớn hơn 0!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(EXISTS_THEATER_BY_ID_MYSQL)) {

            ps.setInt(1, theaterId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Kiểm tra loại phòng chiếu có tồn tại hay không.
     * 
     * @param screenTypeId - Mã loại phòng chiếu
     * @return true nếu tồn tại
     * @throws SQLException nếu có lỗi SQL
     */
    private boolean existsScreenTypeById(int screenTypeId) throws SQLException {
        if (screenTypeId <= 0) {
            throw new IllegalArgumentException("screenTypeId phải lớn hơn 0!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(EXISTS_SCREEN_TYPE_BY_ID_MYSQL)) {

            ps.setInt(1, screenTypeId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
    
    /**
     * Kiểm tra phòng chiếu có đang được sử dụng ở bảng MovieSession hay không.
     * 
     * @param screenId - Mã phòng chiếu
     * @return true nếu đang được sử dụng
     * @throws SQLException nếu có lỗi SQL
     */
    private boolean isUsedInMovieSession(int screenId) throws SQLException {
        if (screenId <= 0) {
            throw new IllegalArgumentException("screenId phải lớn hơn 0!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(IS_USED_IN_MOVIE_SESSION_MYSQL)) {

            ps.setInt(1, screenId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Kiểm tra phòng chiếu có đang được sử dụng ở bảng Seat hay không.
     * 
     * @param screenId - Mã phòng chiếu
     * @return true nếu đang được sử dụng
     * @throws SQLException nếu có lỗi SQL
     */
    private boolean isUsedInSeat(int screenId) throws SQLException {
        if (screenId <= 0) {
            throw new IllegalArgumentException("screenId phải lớn hơn 0!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(IS_USED_IN_SEAT_MYSQL)) {

            ps.setInt(1, screenId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Kiểm tra phòng chiếu có đang được sử dụng ở các bảng liên quan hay không.
     * 
     * @param screenId - Mã phòng chiếu
     * @return true nếu đang được sử dụng
     * @throws SQLException nếu có lỗi SQL
     */
    private boolean isScreenUsed(int screenId) throws SQLException {
        if (screenId <= 0) {
            throw new IllegalArgumentException("screenId phải lớn hơn 0!");
        }

        return isUsedInMovieSession(screenId) || isUsedInSeat(screenId);
    }

    /**
     * Thêm phòng chiếu mới.
     * 
     * @param screen - Phòng chiếu cần thêm
     * @return true nếu thêm thành công
     * @throws SQLException nếu có lỗi SQL
     */
    public boolean addScreen(Screen screen) throws SQLException {
        ScreenValidator.validateForCreate(screen);

        int theaterId = screen.getTheater().getTheaterId();
        int screenTypeId = screen.getScreenType().getScreenTypeId();
        
        if (!existsTheaterById(theaterId)) {
            throw new IllegalArgumentException("Rạp không tồn tại!");
        }

        if (!existsScreenTypeById(screenTypeId)) {
            throw new IllegalArgumentException("Loại phòng chiếu không tồn tại!");
        }
        
        if (existsByNameAndTheaterId(screen.getScreenName(), screen.getTheater().getTheaterId())) {
            throw new IllegalArgumentException("Tên phòng đã tồn tại trong rạp này!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_MYSQL)) {

            ps.setString(1, screen.getScreenName().trim());
            ps.setInt(2, screen.getTheater().getTheaterId());
            ps.setInt(3, screen.getScreenType().getScreenTypeId());
            ps.setInt(4, screen.getScreenStatus().getScreenStatusId());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Cập nhật thông tin phòng chiếu.
     * 
     * @param screen - Phòng chiếu cần cập nhật
     * @return true nếu cập nhật thành công, false nếu không tìm thấy phòng chiếu
     * @throws SQLException nếu có lỗi SQL
     */
    public boolean updateScreen(Screen screen) throws SQLException {
    	ScreenValidator.validateForUpdate(screen);

        if (screen.getScreenId() <= 0) {
            throw new IllegalArgumentException("screenId phải lớn hơn 0!");
        }

        int theaterId = screen.getTheater().getTheaterId();
        int screenTypeId = screen.getScreenType().getScreenTypeId();

        if (!existsTheaterById(theaterId)) {
            throw new IllegalArgumentException("Rạp không tồn tại!");
        }

        if (!existsScreenTypeById(screenTypeId)) {
            throw new IllegalArgumentException("Loại phòng chiếu không tồn tại!");
        }
        
        if (existsByNameAndTheaterIdExceptId(
                screen.getScreenName(),
                screen.getTheater().getTheaterId(),
                screen.getScreenId())) {
            throw new IllegalArgumentException("Tên phòng đã tồn tại ở bản ghi khác trong cùng rạp!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_MYSQL)) {

            ps.setString(1, screen.getScreenName().trim());
            ps.setInt(2, screen.getTheater().getTheaterId());
            ps.setInt(3, screen.getScreenType().getScreenTypeId());
            ps.setInt(4, screen.getScreenStatus().getScreenStatusId());
            ps.setInt(5, screen.getScreenId());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Xóa phòng chiếu theo mã.
     * Không cho xóa nếu phòng chiếu đang được dùng trong bảng liên quan.
     * 
     * @param screenId - Mã phòng chiếu
     * @return true nếu xóa thành công, false nếu không xóa được
     * @throws SQLException nếu có lỗi SQL
     */
    public boolean deleteScreenById(int screenId) throws SQLException {
        if (screenId <= 0) {
            throw new IllegalArgumentException("screenId phải lớn hơn 0!");
        }

        if (isScreenUsed(screenId)) {
            throw new IllegalArgumentException("Phòng chiếu đang được sử dụng, không thể xóa!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_MYSQL)) {

            ps.setInt(1, screenId);

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Tìm phòng chiếu theo mã.
     * 
     * @param screenId - Mã phòng chiếu
     * @return đối tượng Screen nếu tìm thấy, ngược lại trả về null
     * @throws SQLException nếu có lỗi SQL
     */
    public Screen findById(int screenId) throws SQLException {
        if (screenId <= 0) {
            throw new IllegalArgumentException("screenId phải lớn hơn 0!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID_MYSQL)) {

            ps.setInt(1, screenId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToScreen(rs);
                }
            }
        }

        return null;
    }

    /**
     * Tìm phòng chiếu theo tên gần đúng.
     * 
     * @param keyword - Từ khóa tên phòng chiếu
     * @return danh sách phòng chiếu phù hợp
     * @throws SQLException nếu có lỗi SQL
     */
    public List<Screen> searchByName(String keyword) throws SQLException {
        List<Screen> screens = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SEARCH_BY_NAME_MYSQL)) {

            keyword = (keyword == null) ? "" : keyword.trim();
            ps.setString(1, "%" + keyword + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    screens.add(mapResultSetToScreen(rs));
                }
            }
        }

        return screens;
    }

    /**
     * Lấy tất cả phòng chiếu.
     * 
     * @return danh sách phòng chiếu
     * @throws SQLException nếu có lỗi SQL
     */
    public List<Screen> getAllScreens() throws SQLException {
        List<Screen> screens = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_ALL_MYSQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                screens.add(mapResultSetToScreen(rs));
            }
        }

        return screens;
    }
}