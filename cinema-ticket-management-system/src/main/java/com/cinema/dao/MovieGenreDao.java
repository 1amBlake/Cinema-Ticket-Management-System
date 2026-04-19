package main.java.com.cinema.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.java.com.cinema.config.DBConnection;

/**
 * DAO cho thực thể MovieGenreDao
 * Chịu trách nhiệm thao tác dữ liệu với bảng the_loai_phim trong MySQL
 *
 * Bảng ánh xạ:
 * the_loai_phim (
    ma_phim INT NOT NULL,
    ma_the_loai INT NOT NULL,
    PRIMARY KEY (ma_phim, ma_the_loai),
    CONSTRAINT fk_the_loai_phim_phim
        FOREIGN KEY (ma_phim) REFERENCES phim(ma_phim)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_the_loai_phim_the_loai
        FOREIGN KEY (ma_the_loai) REFERENCES the_loai(ma_the_loai)
        ON DELETE CASCADE
        ON UPDATE CASCADE
)
 */
public class MovieGenreDao {

    private static final String INSERT_MYSQL = """
        INSERT INTO the_loai_phim (ma_phim, ma_the_loai)
        VALUES (?, ?)
    """;

    private static final String DELETE_MYSQL = """
        DELETE FROM the_loai_phim
        WHERE ma_phim = ? AND ma_the_loai = ?
    """;

    private static final String SELECT_MYSQL = """
        SELECT ma_the_loai FROM the_loai_phim WHERE ma_phim = ?
    """;

    private static final String EXISTS_MYSQL = """
        SELECT 1 FROM the_loai_phim WHERE ma_phim=? AND ma_the_loai=? LIMIT 1
    """;

    /**
     * Thêm thể loại cho phim
     */
    public boolean add(int maPhim, int maTheLoai) throws SQLException {
        if (exists(maPhim, maTheLoai)) return false;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_MYSQL)) {

            ps.setInt(1, maPhim);
            ps.setInt(2, maTheLoai);
            return ps.executeUpdate() > 0;
        }
    }
    /**
     * Lấy danh sách mã thể loại theo phim
     */
    public List<Integer> getGenreIdsByMovie(int maPhim) throws SQLException {
        List<Integer> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_MYSQL)) {

            ps.setInt(1, maPhim);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(rs.getInt("ma_the_loai"));
            }
        }
        return list;
    }
    /**
     * Xóa thể loại khỏi phim
     */
    public boolean remove(int maPhim, int maTheLoai) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_MYSQL)) {

            ps.setInt(1, maPhim);
            ps.setInt(2, maTheLoai);
            return ps.executeUpdate() > 0;
        }
    }

    private boolean exists(int maPhim, int maTheLoai) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(EXISTS_MYSQL)) {

            ps.setInt(1, maPhim);
            ps.setInt(2, maTheLoai);
            return ps.executeQuery().next();
        }
    }
}