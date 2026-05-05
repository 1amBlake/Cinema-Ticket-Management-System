package com.cinema.controller;

import java.sql.SQLException;
import java.util.List;

import com.cinema.entity.Movie;
import com.cinema.service.MovieService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class MovieManagementScreenController {

    @FXML private TextField searchField;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private Button addMovieButton;
    @FXML private FlowPane movieFlowPane;

    private MovieService movieService;

    @FXML
    public void initialize() {
        // Khởi tạo Service của dự án
    	movieService = new MovieService();
        
        // Gọi hàm nạp dữ liệu sản phẩm lên Grid
        loadMoviesFromDatabase();

        // Xử lý sự kiện tìm kiếm khi nhấn Enter
        searchField.setOnAction(e -> {
            String keyword = searchField.getText();
            searchMovies(keyword);
        });

        // Xử lý nút Thêm mới
        addMovieButton.setOnAction(e -> {
            System.out.println("Sẽ mở một form JFX mới để điền thông tin thêm phim!");
        });
    }

    /**
     * Nạp toàn bộ dữ liệu lên FlowPane
     */
    private void loadMoviesFromDatabase() {
        try {
            // Lấy danh sách từ cơ sở dữ liệu
            List<Movie> movies = movieService.getAllMovies();
            displayMovies(movies);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Lỗi kết nối CSDL khi tải danh sách Phim!");
        }
    }

    /**
     * Tìm kiếm phim
     */
    private void searchMovies(String keyword) {
        try {
            List<Movie> movies = movieService.searchMoviesByName(keyword);
            displayMovies(movies);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Hàm dùng chung để in danh sách sản phẩm ra màn hình
     */
    private void displayMovies(List<Movie> movies) {
    	movieFlowPane.getChildren().clear(); // Xóa lưới cũ
        try {
            for (Movie p : movies) {
                // Tải khuôn mẫu ProductCard
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ProductCard.fxml"));
                VBox cardNode = loader.load();
                
                // Đẩy dữ liệu vào Controller của Card
                MovieCardController cardController = loader.getController();
                cardController.setMovieData(p);
                
                // Add vào FlowPane
                movieFlowPane.getChildren().add(cardNode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}