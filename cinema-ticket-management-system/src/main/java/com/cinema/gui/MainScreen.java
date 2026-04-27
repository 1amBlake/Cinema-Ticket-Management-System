package com.cinema.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Màn hình chính sau khi splash screen kết thúc.
 */
public class MainScreen {

    /**
     * Hiển thị màn hình chính.
     */
    public void show() {
        Stage stage = new Stage();

        Label label = new Label("Đây là màn hình chính của hệ thống rạp phim");
        label.setFont(Font.font("Arial", 20));

        BorderPane root = new BorderPane();
        root.setCenter(label);
        BorderPane.setAlignment(label, Pos.CENTER);

        Scene scene = new Scene(root, 900, 600);

        stage.setTitle("Cinema Ticket Management System");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}