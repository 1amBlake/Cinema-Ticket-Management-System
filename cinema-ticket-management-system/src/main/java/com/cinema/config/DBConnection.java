package com.cinema.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/cinema_db_test";
    private static final String USER = "root";
    private static final String PASSWORD = "root123";

    /**
     * Tạo và trả về kết nối đến cơ sở dữ liệu
     * 
     * @return đối tượng connection
     * @throws SQLException lỗi khi kết nối database
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}