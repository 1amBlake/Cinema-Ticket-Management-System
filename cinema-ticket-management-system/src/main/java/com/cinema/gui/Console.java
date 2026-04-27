package com.cinema.gui;

import java.sql.Connection;
import java.sql.SQLException;

import com.cinema.config.DBConnection;

public class Console {

	public static void main(String[] args) {
	    try (Connection conn = DBConnection.getConnection()) {
	        System.out.println("Connect succeeded");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

}
