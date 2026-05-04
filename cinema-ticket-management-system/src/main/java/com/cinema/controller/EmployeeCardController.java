package com.cinema.controller;

import com.cinema.entity.Employee;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class EmployeeCardController {

    @FXML private ImageView employeeImageView;
    @FXML private Label employeeNameLabel;
    @FXML private Label employeeIDLabel;
    @FXML private Label statusLabel;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private Employee employee;

    public void setEmployeeData(Employee employee) {
        this.employee = employee;
        employeeNameLabel.setText(employee.getEmployeeName());
        
        employeeIDLabel.setText(employee.getEmployeeId() + "");
        
        statusLabel.setText(employee.getEmployeeStatus().toString());

        editButton.setOnAction(e -> {
            System.out.println("Đang bấm sửa sản phẩm ID: " + employee.getEmployeeId());
            // TODO: Mở Dialog sửa nhân viên
        });

        deleteButton.setOnAction(e -> {
            System.out.println("Đang bấm xóa sản phẩm ID: " + employee.getEmployeeId());
            // TODO: Gọi EmployeeService để xóa
        });
    }
}
