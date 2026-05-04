package com.cinema.controller;

import java.sql.SQLException;
import java.util.List;

import com.cinema.entity.Employee;
import com.cinema.service.EmployeeService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class EmployeeManagementScreenController {

    @FXML private TextField searchField;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private Button addEmployeeButton;
    @FXML private FlowPane employeeFlowPane;

    private EmployeeService employeeService;

    @FXML
    public void initialize() {
        // Khởi tạo Service của dự án
    	employeeService = new EmployeeService();
        
        // Gọi hàm nạp dữ liệu sản phẩm lên Grid
    	loadEmployeesFromDatabase();

        // Xử lý sự kiện tìm kiếm khi nhấn Enter
        searchField.setOnAction(e -> {
            String keyword = searchField.getText();
            searchEmployees(keyword);
        });

        // Xử lý nút Thêm mới
        addEmployeeButton.setOnAction(e -> {
            System.out.println("Sẽ mở một form JFX mới để điền thông tin thêm nhân viên!");
        });
    }

    /**
     * Nạp toàn bộ dữ liệu lên FlowPane
     */
    private void loadEmployeesFromDatabase() {
        try {
            // Lấy danh sách từ cơ sở dữ liệu
            List<Employee> employees = employeeService.getAllEmployees();
            displayEmployees(employees);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Lỗi kết nối CSDL khi tải danh sách Nhân Viên!");
        }
    }

    /**
     * Tìm kiếm nhân viên
     */
    private void searchEmployees(String keyword) {
        try {
            List<Employee> employees = employeeService.searchEmployees(keyword);
            displayEmployees(employees);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Hàm dùng chung để in danh sách nhân viên ra màn hình
     */
    private void displayEmployees(List<Employee> employees) {
    	employeeFlowPane.getChildren().clear(); // Xóa lưới cũ
        try {
            for (Employee em : employees) {
                // Tải khuôn mẫu ProductCard
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EmployeeCard.fxml"));
                VBox cardNode = loader.load();
                
                // Đẩy dữ liệu vào Controller của Card
                EmployeeCardController cardController = loader.getController();
                cardController.setMovieData(em);
                
                // Add vào FlowPane
                employeeFlowPane.getChildren().add(cardNode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
