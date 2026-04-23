package com.cinema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cinema.config.DBConnection;
import com.cinema.entity.Genre;
import com.cinema.entity.Movie;
import com.cinema.entity.MovieGenre;

/**
 * DAO cho thực thể MovieGenre
 * Chịu trách nhiệm thao tác dữ liệu với bảng the_loai_phim trong MySQL
 * 
 * Bảng ánh xạ:
 * the_loai_phim(
 *     ma_phim,
 *     ma_the_loai
 * )
 * 
 * Ghi chú:
 * - Bảng này dùng khóa chính kép: (ma_phim, ma_the_loai)
 * - Đây là bảng liên kết nhiều - nhiều giữa Movie và Genre
 * 
 * @author Trọng (làm)
 * @author Minh Huy (sửa theo style nhóm)
 */
public class MovieGenreDao {

    private static final String INSERT_MYSQL = """
            INSERT INTO the_loai_phim (
                ma_phim,
                ma_the_loai
            )
            VALUES (?, ?)
            """;

    private static final String DELETE_MYSQL = """
            DELETE FROM the_loai_phim
            WHERE ma_phim = ?
              AND ma_the_loai = ?
            """;

    private static final String SELECT_BY_IDS_MYSQL = """
            SELECT ma_phim,
                   ma_the_loai
            FROM the_loai_phim
            WHERE ma_phim = ?
              AND ma_the_loai = ?
            """;

    private static final String SELECT_BY_MOVIE_ID_MYSQL = """
            SELECT ma_phim,
                   ma_the_loai
            FROM the_loai_phim
            WHERE ma_phim = ?
            ORDER BY ma_the_loai ASC
            """;

    private static final String SELECT_BY_GENRE_ID_MYSQL = """
            SELECT ma_phim,
                   ma_the_loai
            FROM the_loai_phim
            WHERE ma_the_loai = ?
            ORDER BY ma_phim ASC
            """;

    private static final String SELECT_ALL_MYSQL = """
            SELECT ma_phim,
                   ma_the_loai
            FROM the_loai_phim
            ORDER BY ma_phim ASC, ma_the_loai ASC
            """;

    private static final String EXISTS_BY_IDS_MYSQL = """
            SELECT 1
            FROM the_loai_phim
            WHERE ma_phim = ?
              AND ma_the_loai = ?
            LIMIT 1
            """;

    private static final String EXISTS_MOVIE_BY_ID_MYSQL = """
            SELECT 1
            FROM phim
            WHERE ma_phim = ?
            LIMIT 1
            """;

    private static final String EXISTS_GENRE_BY_ID_MYSQL = """
            SELECT 1
            FROM the_loai
            WHERE ma_the_loai = ?
            LIMIT 1
            """;

    /**
     * Kiểm tra liên kết phim - thể loại đã tồn tại hay chưa
     * 
     * @param movieId - Mã phim
     * @param genreId - Mã thể loại
     * @return true nếu đã tồn tại
     * @throws SQLException nếu có lỗi SQL
     */
    private boolean existsByIds(int movieId, int genreId) throws SQLException {
        if (movieId <= 0 || genreId <= 0) {
            throw new IllegalArgumentException("movieId và genreId phải lớn hơn 0!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(EXISTS_BY_IDS_MYSQL)) {

            ps.setInt(1, movieId);
            ps.setInt(2, genreId);

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
     * Kiểm tra thể loại có tồn tại hay không
     * 
     * @param genreId - Mã thể loại
     * @return true nếu tồn tại
     * @throws SQLException nếu có lỗi SQL
     */
    private boolean existsGenreById(int genreId) throws SQLException {
        if (genreId <= 0) {
            throw new IllegalArgumentException("genreId phải lớn hơn 0!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(EXISTS_GENRE_BY_ID_MYSQL)) {

            ps.setInt(1, genreId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Ánh xạ một dòng dữ liệu ResultSet thành đối tượng MovieGenre
     * 
     * @param rs - ResultSet đang trỏ tới dòng dữ liệu hợp lệ
     * @return đối tượng MovieGenre
     * @throws SQLException nếu có lỗi đọc dữ liệu
     */
    private MovieGenre mapResultSetToMovieGenre(ResultSet rs) throws SQLException {
        Movie movie = new Movie(rs.getInt("ma_phim"));
        Genre genre = new Genre(rs.getInt("ma_the_loai"));

        return new MovieGenre(movie, genre);
    }

    /**
     * Thêm liên kết phim - thể loại
     * 
     * @param movieGenre - Đối tượng liên kết phim - thể loại
     * @return true nếu thêm thành công
     * @throws SQLException nếu có lỗi SQL
     */
    public boolean addMovieGenre(MovieGenre movieGenre) throws SQLException {
        //MovieGenreValidator.validateForCreate(movieGenre);

        int movieId = movieGenre.getMovie().getMovieId();
        int genreId = movieGenre.getGenre().getGenreId();

        if (!existsMovieById(movieId)) {
            throw new IllegalArgumentException("Phim không tồn tại!");
        }

        if (!existsGenreById(genreId)) {
            throw new IllegalArgumentException("Thể loại không tồn tại!");
        }

        if (existsByIds(movieId, genreId)) {
            throw new IllegalArgumentException("Liên kết phim - thể loại đã tồn tại!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_MYSQL)) {

            ps.setInt(1, movieId);
            ps.setInt(2, genreId);

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Xóa liên kết phim - thể loại theo khóa kép
     * 
     * @param movieId - Mã phim
     * @param genreId - Mã thể loại
     * @return true nếu xóa thành công
     * @throws SQLException nếu có lỗi SQL
     */
    public boolean deleteMovieGenre(int movieId, int genreId) throws SQLException {
        if (movieId <= 0 || genreId <= 0) {
            throw new IllegalArgumentException("movieId và genreId phải lớn hơn 0!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_MYSQL)) {

            ps.setInt(1, movieId);
            ps.setInt(2, genreId);

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Tìm liên kết phim - thể loại theo khóa kép
     * 
     * @param movieId - Mã phim
     * @param genreId - Mã thể loại
     * @return đối tượng MovieGenre nếu tìm thấy, ngược lại trả về null
     * @throws SQLException nếu có lỗi SQL
     */
    public MovieGenre findByMovieIdAndGenreId(int movieId, int genreId) throws SQLException {
        if (movieId <= 0 || genreId <= 0) {
            throw new IllegalArgumentException("movieId và genreId phải lớn hơn 0!");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_BY_IDS_MYSQL)) {

            ps.setInt(1, movieId);
            ps.setInt(2, genreId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMovieGenre(rs);
                }
            }
        }

        return null;
    }

    /**
     * Tìm danh sách liên kết phim - thể loại theo mã phim
     * 
     * @param movieId - Mã phim
     * @return danh sách liên kết phim - thể loại
     * @throws SQLException nếu có lỗi SQL
     */
    public List<MovieGenre> findByMovieId(int movieId) throws SQLException {
        if (movieId <= 0) {
            throw new IllegalArgumentException("movieId phải lớn hơn 0!");
        }

        List<MovieGenre> movieGenres = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_BY_MOVIE_ID_MYSQL)) {

            ps.setInt(1, movieId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    movieGenres.add(mapResultSetToMovieGenre(rs));
                }
            }
        }

        return movieGenres;
    }

    /**
     * Tìm danh sách liên kết phim - thể loại theo mã thể loại
     * 
     * @param genreId - Mã thể loại
     * @return danh sách liên kết phim - thể loại
     * @throws SQLException nếu có lỗi SQL
     */
    public List<MovieGenre> findByGenreId(int genreId) throws SQLException {
        if (genreId <= 0) {
            throw new IllegalArgumentException("genreId phải lớn hơn 0!");
        }

        List<MovieGenre> movieGenres = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_BY_GENRE_ID_MYSQL)) {

            ps.setInt(1, genreId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    movieGenres.add(mapResultSetToMovieGenre(rs));
                }
            }
        }

        return movieGenres;
    }

    /**
     * Lấy tất cả liên kết phim - thể loại
     * 
     * @return danh sách liên kết phim - thể loại
     * @throws SQLException nếu có lỗi SQL
     */
    public List<MovieGenre> getAllMovieGenres() throws SQLException {
        List<MovieGenre> movieGenres = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_ALL_MYSQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                movieGenres.add(mapResultSetToMovieGenre(rs));
            }
        }

        return movieGenres;
    }
}

/* bản cũ
package main.java.com.cinema.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.java.com.cinema.config.DBConnection;


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


    public boolean add(int maPhim, int maTheLoai) throws SQLException {
        if (exists(maPhim, maTheLoai)) return false;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_MYSQL)) {

            ps.setInt(1, maPhim);
            ps.setInt(2, maTheLoai);
            return ps.executeUpdate() > 0;
        }
    }

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
*/