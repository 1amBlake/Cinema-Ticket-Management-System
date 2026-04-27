package com.cinema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cinema.config.DBConnection;
import com.cinema.entity.Director;
import com.cinema.entity.Movie;
import com.cinema.entity.MovieDirector;
import com.cinema.validator.MovieDirectorValidator;

/**
 * DAO cho thực thể MovieDirector
 * Chịu trách nhiệm thao tác dữ liệu với bảng dao_dien_phim trong MySQL
 * 
 * Bảng ánh xạ:
 * dao_dien_phim(
 *     ma_phim,
 *     ma_dao_dien
 * )
 * 
 * Ghi chú:
 * - Bảng này dùng khóa chính kép: (ma_phim, ma_dao_dien)
 * - Đây là bảng liên kết nhiều - nhiều giữa Movie và Director
 * 
 * @author Trọng (làm)
 * @author Minh Huy (sửa theo mẫu)
 */
public class MovieDirectorDao {

    private static final String INSERT_MYSQL = """
            INSERT INTO dao_dien_phim (
                ma_phim,
                ma_dao_dien
            )
            VALUES (?, ?)
            """;

    private static final String DELETE_MYSQL = """
            DELETE FROM dao_dien_phim
            WHERE ma_phim = ?
              AND ma_dao_dien = ?
            """;

    private static final String SELECT_BY_IDS_MYSQL = """
            SELECT ma_phim,
                   ma_dao_dien
            FROM dao_dien_phim
            WHERE ma_phim = ?
              AND ma_dao_dien = ?
            """;

    private static final String SELECT_BY_MOVIE_ID_MYSQL = """
            SELECT ma_phim,
                   ma_dao_dien
            FROM dao_dien_phim
            WHERE ma_phim = ?
            ORDER BY ma_dao_dien ASC
            """;

    private static final String SELECT_BY_DIRECTOR_ID_MYSQL = """
            SELECT ma_phim,
                   ma_dao_dien
            FROM dao_dien_phim
            WHERE ma_dao_dien = ?
            ORDER BY ma_phim ASC
            """;

    private static final String SELECT_ALL_MYSQL = """
            SELECT ma_phim,
                   ma_dao_dien
            FROM dao_dien_phim
            ORDER BY ma_phim ASC, ma_dao_dien ASC
            """;

    private static final String EXISTS_BY_IDS_MYSQL = """
            SELECT 1
            FROM dao_dien_phim
            WHERE ma_phim = ?
              AND ma_dao_dien = ?
            LIMIT 1
            """;

    private static final String EXISTS_MOVIE_BY_ID_MYSQL = """
            SELECT 1
            FROM phim
            WHERE ma_phim = ?
            LIMIT 1
            """;

    private static final String EXISTS_DIRECTOR_BY_ID_MYSQL = """
            SELECT 1
            FROM dao_dien
            WHERE ma_dao_dien = ?
            LIMIT 1
            """;
    
    /**
     * Kiểm tra liên kết phim - đạo diễn đã tồn tại hay chưa
     * 
     * @param movieId - Mã phim
     * @param directorId - Mã đạo diễn
     * @return true nếu đã tồn tại
     * @throws SQLException nếu có lỗi SQL
     */
    private boolean existsByIds(int movieId, int directorId) throws SQLException {
        if (movieId <= 0 || directorId <= 0) {
            throw new IllegalArgumentException("movieId và directorId phải lớn hơn 0!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(EXISTS_BY_IDS_MYSQL)) {

            ps.setInt(1, movieId);
            ps.setInt(2, directorId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Kiểm tra phim có tồn tại hay không
     * 
     * @param movieId - Mã phim
     * @return true nếu tồn tại
     * @throws SQLException nếu có lỗi SQL
     */
    private boolean existsMovieById(int movieId) throws SQLException {
        if (movieId <= 0) {
            throw new IllegalArgumentException("movieId phải lớn hơn 0!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(EXISTS_MOVIE_BY_ID_MYSQL)) {

            ps.setInt(1, movieId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Kiểm tra đạo diễn có tồn tại hay không
     * 
     * @param directorId - Mã đạo diễn
     * @return true nếu tồn tại
     * @throws SQLException nếu có lỗi SQL
     */
    private boolean existsDirectorById(int directorId) throws SQLException {
        if (directorId <= 0) {
            throw new IllegalArgumentException("directorId phải lớn hơn 0!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(EXISTS_DIRECTOR_BY_ID_MYSQL)) {

            ps.setInt(1, directorId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Ánh xạ một dòng dữ liệu ResultSet thành đối tượng MovieDirector
     * 
     * @param rs - ResultSet đang trỏ tới dòng dữ liệu hợp lệ
     * @return đối tượng MovieDirector
     * @throws SQLException nếu có lỗi đọc dữ liệu
     */
    private MovieDirector mapResultSetToMovieDirector(ResultSet rs) throws SQLException {
        Movie movie = new Movie(rs.getInt("ma_phim"));
        Director director = new Director(rs.getInt("ma_dao_dien"));

        return new MovieDirector(movie, director);
    }

    /**
     * Thêm liên kết phim - đạo diễn
     * 
     * @param movieDirector - Đối tượng liên kết phim - đạo diễn
     * @return true nếu thêm thành công
     * @throws SQLException nếu có lỗi SQL
     */
    public boolean addMovieDirector(MovieDirector movieDirector) throws SQLException {
        MovieDirectorValidator.validateForCreate(movieDirector);

        int movieId = movieDirector.getMovie().getMovieId();
        int directorId = movieDirector.getDirector().getDirectorId();

        if (!existsMovieById(movieId)) {
            throw new IllegalArgumentException("Phim không tồn tại!");
        }

        if (!existsDirectorById(directorId)) {
            throw new IllegalArgumentException("Đạo diễn không tồn tại!");
        }

        if (existsByIds(movieId, directorId)) {
            throw new IllegalArgumentException("Liên kết phim - đạo diễn đã tồn tại!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_MYSQL)) {

            ps.setInt(1, movieId);
            ps.setInt(2, directorId);

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Xóa liên kết phim - đạo diễn theo khóa kép
     * 
     * @param movieId - Mã phim
     * @param directorId - Mã đạo diễn
     * @return true nếu xóa thành công, false nếu không tìm thấy liên kết
     * @throws SQLException nếu có lỗi SQL
     */
    public boolean deleteMovieDirector(int movieId, int directorId) throws SQLException {
        if (movieId <= 0 || directorId <= 0) {
            throw new IllegalArgumentException("movieId và directorId phải lớn hơn 0!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_MYSQL)) {

            ps.setInt(1, movieId);
            ps.setInt(2, directorId);

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Tìm liên kết phim - đạo diễn theo khóa kép
     * 
     * @param movieId - Mã phim
     * @param directorId - Mã đạo diễn
     * @return đối tượng MovieDirector nếu tìm thấy, ngược lại trả về null
     * @throws SQLException nếu có lỗi SQL
     */
    public MovieDirector findByMovieIdAndDirectorId(int movieId, int directorId) throws SQLException {
        if (movieId <= 0 || directorId <= 0) {
            throw new IllegalArgumentException("movieId và directorId phải lớn hơn 0!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_BY_IDS_MYSQL)) {

            ps.setInt(1, movieId);
            ps.setInt(2, directorId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMovieDirector(rs);
                }
            }
        }

        return null;
    }

    /**
     * Tìm danh sách liên kết phim - đạo diễn theo mã phim
     * 
     * @param movieId - Mã phim
     * @return danh sách liên kết phim - đạo diễn
     * @throws SQLException nếu có lỗi SQL
     */
    public List<MovieDirector> findByMovieId(int movieId) throws SQLException {
        if (movieId <= 0) {
            throw new IllegalArgumentException("movieId phải lớn hơn 0!");
        }

        List<MovieDirector> movieDirectors = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_BY_MOVIE_ID_MYSQL)) {

            ps.setInt(1, movieId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    movieDirectors.add(mapResultSetToMovieDirector(rs));
                }
            }
        }

        return movieDirectors;
    }

    /**
     * Tìm danh sách liên kết phim - đạo diễn theo mã đạo diễn
     * 
     * @param directorId - Mã đạo diễn
     * @return danh sách liên kết phim - đạo diễn
     * @throws SQLException nếu có lỗi SQL
     */
    public List<MovieDirector> findByDirectorId(int directorId) throws SQLException {
        if (directorId <= 0) {
            throw new IllegalArgumentException("directorId phải lớn hơn 0!");
        }

        List<MovieDirector> movieDirectors = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_BY_DIRECTOR_ID_MYSQL)) {

            ps.setInt(1, directorId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    movieDirectors.add(mapResultSetToMovieDirector(rs));
                }
            }
        }

        return movieDirectors;
    }

    /**
     * Lấy tất cả liên kết phim - đạo diễn
     * 
     * @return danh sách liên kết phim - đạo diễn
     * @throws SQLException nếu có lỗi SQL
     */
    public List<MovieDirector> getAllMovieDirectors() throws SQLException {
        List<MovieDirector> movieDirectors = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_ALL_MYSQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                movieDirectors.add(mapResultSetToMovieDirector(rs));
            }
        }

        return movieDirectors;
    }
}

/** Bản cũ
package com.cinema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.cinema.config.DBConnection;
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
*/