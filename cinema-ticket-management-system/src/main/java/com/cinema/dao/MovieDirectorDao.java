package main.java.com.cinema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import main.java.com.cinema.config.DBConnection;
/**
 * DAO cho thực thể MovieDirectorDao
 * Chịu trách nhiệm thao tác dữ liệu với bảng dao_dien_phim trong MySQL
 *
 * Bảng ánh xạ:
 * dao_dien_phim (
    ma_phim INT NOT NULL,
    ma_dao_dien INT NOT NULL,
    PRIMARY KEY (ma_phim, ma_dao_dien),
    CONSTRAINT fk_dao_dien_phim_phim
        FOREIGN KEY (ma_phim) REFERENCES phim(ma_phim)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_dao_dien_phim_dao_dien
        FOREIGN KEY (ma_dao_dien) REFERENCES dao_dien(ma_dao_dien)
        ON DELETE CASCADE
        ON UPDATE CASCADE
)
 */
public class MovieDirectorDao {

	private static final String INSERT_MYSQL = """
	        INSERT INTO dao_dien_phim (
	        ma_phim,
	        ma_dao_dien)
	        VALUES (?, ?)
	        """;

	    private static final String DELETE_MYSQL = """
	        DELETE FROM dao_dien_phim
	        WHERE ma_phim = ?
	        AND ma_dao_dien = ?
	    	""";

	    private static final String SELECT_MYSQL = """
	        SELECT ma_dao_dien
	        FROM dao_dien_phim
	        WHERE ma_phim = ?
	    	""";

	    private static final String EXISTS = """
	        SELECT 1 FROM dao_dien_phim
	        WHERE ma_phim=?
	        AND ma_dao_dien=?
	        LIMIT 1
	    	""";

	    public boolean add(int maPhim, int maDaoDien) throws SQLException {
	        if (exists(maPhim, maDaoDien)) return false;

	        try (Connection conn = DBConnection.getConnection();
	             PreparedStatement ps = conn.prepareStatement(INSERT_MYSQL)) {

	            ps.setInt(1, maPhim);
	            ps.setInt(2, maDaoDien);
	            return ps.executeUpdate() > 0;
	        }
	    }

	    public boolean remove(int maPhim, int maDaoDien) throws SQLException {
	        try (Connection conn = DBConnection.getConnection();
	             PreparedStatement ps = conn.prepareStatement(DELETE_MYSQL)) {

	            ps.setInt(1, maPhim);
	            ps.setInt(2, maDaoDien);
	            return ps.executeUpdate() > 0;
	        }
	    }



	    private boolean exists(int maPhim, int maDaoDien) throws SQLException {
	        try (Connection conn = DBConnection.getConnection();
	             PreparedStatement ps = conn.prepareStatement(EXISTS)) {

	            ps.setInt(1, maPhim);
	            ps.setInt(2, maDaoDien);
	            return ps.executeQuery().next();
	        }
	    }
}