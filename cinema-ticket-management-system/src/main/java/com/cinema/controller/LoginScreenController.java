package com.cinema.controller;

import com.cinema.entity.EmployeeAccount;
import com.cinema.service.EmployeeAccountService;
import java.sql.SQLException;
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
    
 // Khởi tạo Service để xử lý nghiệp vụ 
    private final EmployeeAccountService accountService = new EmployeeAccountService();
    
    @FXML
    private void initialize() {
        errorLabel.setText("");
    }

    /*@FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (isBlank(username) || isBlank(password)) {
            errorLabel.setText("Vui lòng nhập đầy đủ tài khoản và mật khẩu.");
            openDashboard(); //test
            return;
        }


        boolean loginSuccess = username.equals("admin") && password.equals("123456");

        if (loginSuccess) {
            openDashboard();
        } else {
            errorLabel.setText("Tài khoản hoặc mật khẩu không đúng.");
        }
    }*/

    /**
     * Xử lý sự kiện khi người dùng nhấn nút Đăng nhập.
     */
    @FXML
    private void handleLogin() {
        // 1. Lấy dữ liệu từ giao diện
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            // 2. Gọi phương thức login thay vì authenticate 
            EmployeeAccount account = accountService.login(username, password);
            
            // 3. Kiểm tra kết quả
            if (account != null) {
                openDashboard(account); // 🔥 truyền vào đây
            } else {
                errorLabel.setText("Tên đăng nhập hoặc mật khẩu không chính xác.");
            }
            
        } catch (IllegalArgumentException e) {
            // Bắt lỗi khi người dùng để trống tên đăng nhập hoặc mật khẩu 
            errorLabel.setText(e.getMessage());
        } catch (SQLException e) {
            // Xử lý lỗi kết nối cơ sở dữ liệu
            e.printStackTrace();
            errorLabel.setText("Lỗi hệ thống: Không thể kết nối cơ sở dữ liệu.");
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Đã xảy ra lỗi không xác định.");
        }
    }
    
    @FXML
    private void handleExit() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }

    private void openDashboard(EmployeeAccount account) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/DashboardScreen.fxml")
            );

            Parent dashboardRoot = loader.load();

            // 🔥 LẤY CONTROLLER
            DashboardScreenController controller = loader.getController();

            // 🔥 TRUYỀN ACCOUNT
            controller.setCurrentAccount(account);

            Scene dashboardScene = new Scene(dashboardRoot);

            Stage oldStage = (Stage) usernameField.getScene().getWindow();
            oldStage.close();

            Stage dashboardStage = new Stage();
            dashboardStage.setTitle("Cinema Ticket Management System");
            dashboardStage.setScene(dashboardScene);
            dashboardStage.setResizable(true);
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