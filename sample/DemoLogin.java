package com.cinema.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class DemoLogin {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    @FXML
    private Button closeButton;

    @FXML
    private void initialize() {
        messageLabel.setText("");

        closeButton.setOnAction(event -> {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        });
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty()) {
            messageLabel.setText("Vui lòng nhập tên đăng nhập.");
            usernameField.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            messageLabel.setText("Vui lòng nhập mật khẩu.");
            passwordField.requestFocus();
            return;
        }

        // Sau này gọi AuthenticationService ở đây
        System.out.println("Đăng nhập với tài khoản: " + username);
    }
}