package com.cinema.testutil;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.cinema.config.DBConnection;

/**
 * Hỗ trợ làm sạch dữ liệu test trong database testing.
 * Chỉ dùng cho test DAO.
 */
public final class ClearDb {

    private ClearDb() {
    }

    public static void cleanDatabase() {
        String[] tables = {
            "hoa_don_san_pham",
            "hoa_don_ve",
            "dao_dien_phim",
            "the_loai_phim",
            "ve",
            "hoa_don",
            "suat_chieu",
            "ghe",
            "tai_khoan",
            "nhan_vien",
            "san_pham",
            "phong_chieu",
            "phim",
            "bang_gia_ve",
            "chuc_vu",
            "dao_dien",
            "the_loai",
            "loai_san_pham",
            "loai_ghe",
            "loai_phong",
            "rap"
        };

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute("SET FOREIGN_KEY_CHECKS = 0");

            for (String table : tables) {
                statement.executeUpdate("TRUNCATE TABLE " + table);
            }

            statement.execute("SET FOREIGN_KEY_CHECKS = 1");

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi clean database test", e);
        }
    }
}