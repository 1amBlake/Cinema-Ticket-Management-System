package com.cinema.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class LoginScreenController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private void initialize() {
        errorLabel.setText("");
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (isBlank(username) || isBlank(password)) {
            errorLabel.setText("Vui lòng nhập đầy đủ tài khoản và mật khẩu.");
            openDashboard(); //test
            return;
        }

        /*
         * Demo tạm thời.
         * Sau này thay đoạn này bằng AuthenticationService.
         */
        boolean loginSuccess = username.equals("admin") && password.equals("123456");

        if (loginSuccess) {
            openDashboard();
        } else {
            errorLabel.setText("Tài khoản hoặc mật khẩu không đúng.");
        }
    }

    @FXML
    private void handleExit() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }

    private void openDashboard() {
        try {
            Parent dashboardRoot = FXMLLoader.load(
                    getClass().getResource("/fxml/DashboardScreen.fxml")
            );

            Scene dashboardScene = new Scene(dashboardRoot);

            Stage oldStage = (Stage) usernameField.getScene().getWindow();
            oldStage.close();

            Stage dashboardStage = new Stage();
            dashboardStage.setTitle("Cinema Ticket Management System");
            dashboardStage.setScene(dashboardScene);

            /*
             * Cho phép Stage maximize.
             * Nếu setResizable(false) trước thì đôi khi maximize không đúng.
             */
            dashboardStage.setResizable(true);

            /*
             * Maximized: có thanh bar trên cùng, có taskbar Windows,
             * giống app desktop bình thường.
             */
            dashboardStage.setMaximized(true);

            dashboardStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Không thể mở màn hình chính.");
        }
    }
    
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}