package com.cinema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.cinema.config.DBConnection;
import com.cinema.entity.ScreenType;
import com.cinema.entity.SeatType;
import com.cinema.entity.TicketPricing;

/**
 * DAO cho thực thể TicketPricing
 * Chịu trách nhiệm thao tác dữ liệu với bảng bang_gia_ve trong MySQL.
 *
 * Bảng ánh xạ:
 * bang_gia_ve(
 *   ma_bang_gia_ve,
 *   ma_loai_ghe,
 *   ma_loai_phong_chieu,
 *   gia_ve,
 *   created_at,
 *   updated_at
 * )
 *
 * @author Quốc Anh
 */
public class TicketPricingDao {

    private static final String INSERT_MYSQL = """
            INSERT INTO bang_gia_ve
            (ma_loai_ghe, ma_loai_phong_chieu, gia_ve)
            VALUES (?, ?, ?)
            """;

    private static final String UPDATE_MYSQL = """
            UPDATE bang_gia_ve
            SET ma_loai_ghe = ?,
                ma_loai_phong_chieu = ?,
                gia_ve = ?
            WHERE ma_bang_gia_ve = ?
            """;

    private static final String DELETE_MYSQL = """
            DELETE FROM bang_gia_ve
            WHERE ma_bang_gia_ve = ?
            """;

    private static final String SELECT_BY_ID_MYSQL = """
            SELECT ma_bang_gia_ve, ma_loai_ghe, ma_loai_phong_chieu,
                   gia_ve, created_at, updated_at
            FROM bang_gia_ve
            WHERE ma_bang_gia_ve = ?
            """;

    private static final String SELECT_ALL_MYSQL = """
            SELECT ma_bang_gia_ve, ma_loai_ghe, ma_loai_phong_chieu,
                   gia_ve, created_at, updated_at
            FROM bang_gia_ve
            ORDER BY ma_bang_gia_ve ASC
            """;

    private static final String EXISTS_COMBINATION_MYSQL = """
            SELECT 1
            FROM bang_gia_ve
            WHERE ma_loai_ghe = ?
              AND ma_loai_phong_chieu = ?
            LIMIT 1
            """;

    private static final String EXISTS_COMBINATION_EXCEPT_ID_MYSQL = """
            SELECT 1
            FROM bang_gia_ve
            WHERE ma_loai_ghe = ?
              AND ma_loai_phong_chieu = ?
              AND ma_bang_gia_ve <> ?
            LIMIT 1
            """;

    /**
     * Thêm bảng giá vé
     */
    public boolean addTicketPricing(TicketPricing ticketPricing) throws SQLException {
        validateTicketPricing(ticketPricing);

        if (existsCombination(
                ticketPricing.getSeatType().getSeatTypeId(),
                ticketPricing.getScreenType().getScreenTypeId())) {
            throw new IllegalArgumentException(
                    "Loại ghế và loại phòng chiếu này đã có bảng giá!");
        }

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(INSERT_MYSQL)
        ) {
            ps.setInt(1, ticketPricing.getSeatType().getSeatTypeId());
            ps.setInt(2, ticketPricing.getScreenType().getScreenTypeId());
            ps.setDouble(3, ticketPricing.getPrice());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Cập nhật bảng giá vé
     */
    public boolean updateTicketPricing(TicketPricing ticketPricing)
            throws SQLException {

        validateTicketPricing(ticketPricing);

        if (ticketPricing.getTicketPricingId() <= 0) {
            throw new IllegalArgumentException(
                    "Mã bảng giá vé không hợp lệ!");
        }

        if (existsCombinationExceptId(
                ticketPricing.getSeatType().getSeatTypeId(),
                ticketPricing.getScreenType().getScreenTypeId(),
                ticketPricing.getTicketPricingId())) {
            throw new IllegalArgumentException(
                    "Loại ghế và loại phòng chiếu này đã có bảng giá!");
        }

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(UPDATE_MYSQL)
        ) {
            ps.setInt(1, ticketPricing.getSeatType().getSeatTypeId());
            ps.setInt(2, ticketPricing.getScreenType().getScreenTypeId());
            ps.setDouble(3, ticketPricing.getPrice());
            ps.setInt(4, ticketPricing.getTicketPricingId());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Xóa bảng giá vé
     */
    public boolean deleteTicketPricingById(int ticketPricingId)
            throws SQLException {

        if (ticketPricingId <= 0) {
            throw new IllegalArgumentException(
                    "Mã bảng giá vé không hợp lệ!");
        }

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DELETE_MYSQL)
        ) {
            ps.setInt(1, ticketPricingId);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Tìm theo mã
     */
    public TicketPricing findById(int ticketPricingId)
            throws SQLException {

        if (ticketPricingId <= 0) {
            throw new IllegalArgumentException(
                    "Mã bảng giá vé không hợp lệ!");
        }

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(SELECT_BY_ID_MYSQL)
        ) {
            ps.setInt(1, ticketPricingId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTicketPricing(rs);
                }
            }
        }

        return null;
    }

    /**
     * Lấy toàn bộ bảng giá vé
     */
    public List<TicketPricing> getAllTicketPricing()
            throws SQLException {

        List<TicketPricing> list = new ArrayList<>();

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(SELECT_ALL_MYSQL);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                list.add(mapResultSetToTicketPricing(rs));
            }
        }

        return list;
    }

    /**
     * Tìm theo loại ghế
     */
    public List<TicketPricing> findBySeatType(int seatTypeId)
            throws SQLException {

        String sql = """
                SELECT ma_bang_gia_ve, ma_loai_ghe,
                       ma_loai_phong_chieu, gia_ve,
                       created_at, updated_at
                FROM bang_gia_ve
                WHERE ma_loai_ghe = ?
                """;

        List<TicketPricing> list = new ArrayList<>();

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, seatTypeId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToTicketPricing(rs));
                }
            }
        }

        return list;
    }

    /**
     * Tìm theo loại phòng chiếu
     */
    public List<TicketPricing> findByScreenType(int screenTypeId)
            throws SQLException {

        String sql = """
                SELECT ma_bang_gia_ve, ma_loai_ghe,
                       ma_loai_phong_chieu, gia_ve,
                       created_at, updated_at
                FROM bang_gia_ve
                WHERE ma_loai_phong_chieu = ?
                """;

        List<TicketPricing> list = new ArrayList<>();

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, screenTypeId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToTicketPricing(rs));
                }
            }
        }

        return list;
    }

    /**
     * Kiểm tra tồn tại cặp ghế + phòng chiếu
     */
    public boolean existsCombination(int seatTypeId, int screenTypeId)
            throws SQLException {

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps =
                    con.prepareStatement(EXISTS_COMBINATION_MYSQL)
        ) {
            ps.setInt(1, seatTypeId);
            ps.setInt(2, screenTypeId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Kiểm tra tồn tại cặp ghế + phòng chiếu ở bản ghi khác
     */
    public boolean existsCombinationExceptId(
            int seatTypeId,
            int screenTypeId,
            int ticketPricingId) throws SQLException {

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps =
                    con.prepareStatement(
                            EXISTS_COMBINATION_EXCEPT_ID_MYSQL)
        ) {
            ps.setInt(1, seatTypeId);
            ps.setInt(2, screenTypeId);
            ps.setInt(3, ticketPricingId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Map ResultSet -> TicketPricing
     */
    private TicketPricing mapResultSetToTicketPricing(ResultSet rs)
            throws SQLException {

        Timestamp createdAt = rs.getTimestamp("created_at");
        Timestamp updatedAt = rs.getTimestamp("updated_at");

        return new TicketPricing(
            rs.getInt("ma_bang_gia_ve"),
            new SeatType(rs.getInt("ma_loai_ghe")),
            new ScreenType(rs.getInt("ma_loai_phong_chieu")),
            rs.getDouble("gia_ve"),
            createdAt != null ? createdAt.toLocalDateTime() : null,
            updatedAt != null ? updatedAt.toLocalDateTime() : null
        );
    }

    /**
     * Validate dữ liệu đầu vào
     */
    private void validateTicketPricing(
            TicketPricing ticketPricing) {

        if (ticketPricing == null) {
            throw new IllegalArgumentException(
                    "TicketPricing không được null!");
        }

        if (ticketPricing.getSeatType() == null
                || ticketPricing.getSeatType()
                        .getSeatTypeId() <= 0) {
            throw new IllegalArgumentException(
                    "Loại ghế không hợp lệ!");
        }

        if (ticketPricing.getScreenType() == null
                || ticketPricing.getScreenType()
                        .getScreenTypeId() <= 0) {
            throw new IllegalArgumentException(
                    "Loại phòng chiếu không hợp lệ!");
        }

        if (ticketPricing.getPrice() <= 0) {
            throw new IllegalArgumentException(
                    "Giá vé phải lớn hơn 0!");
        }
    }
}