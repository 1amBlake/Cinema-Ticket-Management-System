package com.cinema.controller;

import java.io.InputStream;
import java.time.format.DateTimeFormatter;

import com.cinema.entity.Movie;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MovieCardController {

    @FXML private ImageView movieImageView;
    @FXML private Label movieNameLabel;
    @FXML private Label releaseDateLabel;
    @FXML private Label statusLabel;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private Movie movie;

    public void setMovieData(Movie movie) {
        this.movie = movie;
        movieNameLabel.setText(movie.getMovieName());
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        releaseDateLabel.setText(dtf.format(movie.getMovieReleaseDate()));
        
        statusLabel.setText(movie.getMovieStatus().toString());

        try {
            String pictureUrl = movie.getPictureUrl();
            
            if (pictureUrl != null && !pictureUrl.isEmpty()) {

                String finalPath = pictureUrl.startsWith("/") ? pictureUrl : "/" + pictureUrl;
                
                InputStream is = getClass().getResourceAsStream(finalPath);
                
                if (is != null) {
                    Image img = new Image(is);
                    movieImageView.setImage(img);
                } else {
                    System.err.println("Không tìm thấy file ảnh thực tế tại: resources" + finalPath);
                    setEmptyImage(); 
                }
            } else {
                setEmptyImage();
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi load ảnh cho: " + movie.getMovieName() + " - " + e.getMessage());
            setEmptyImage();
        }


        editButton.setOnAction(e -> {
            System.out.println("Đang bấm sửa sản phẩm ID: " + movie.getMovieId());
            // TODO: Mở Dialog sửa phim
        });

        deleteButton.setOnAction(e -> {
            System.out.println("Đang bấm xóa sản phẩm ID: " + movie.getMovieId());
            // TODO: Gọi MovieService để xóa
        });
    }

    // Hàm phụ để set ảnh mặc định (logo) khi lỗi
    private void setEmptyImage() {
        try {
            InputStream is = getClass().getResourceAsStream("/images/logo.png");
            if (is != null) {
                movieImageView.setImage(new Image(is));
            } else {
                System.err.println("Không tìm thấy ảnh mặc định (logo.png) trong thư mục resources/images/");
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi load ảnh mặc định!");
        }
    }
}