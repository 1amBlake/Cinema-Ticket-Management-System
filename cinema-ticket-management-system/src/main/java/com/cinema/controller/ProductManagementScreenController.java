package com.cinema.controller;

import com.cinema.entity.Product;
import com.cinema.service.ProductService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.List;

public class ProductManagementScreenController {

    @FXML private TextField searchField;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private Button addProductButton;
    @FXML private FlowPane productFlowPane;

    private ProductService productService;

    @FXML
    public void initialize() {
        // Khởi tạo Service của dự án
        productService = new ProductService();
        
        // Gọi hàm nạp dữ liệu sản phẩm lên Grid
        loadProductsFromDatabase();

        // Xử lý sự kiện tìm kiếm khi nhấn Enter
        searchField.setOnAction(e -> {
            String keyword = searchField.getText();
            searchProducts(keyword);
        });

        // Xử lý nút Thêm mới
        addProductButton.setOnAction(e -> {
            System.out.println("Sẽ mở một form JFX mới để điền thông tin thêm sản phẩm!");
        });
    }

    /**
     * Nạp toàn bộ dữ liệu lên FlowPane
     */
    private void loadProductsFromDatabase() {
        try {
            // Lấy danh sách từ cơ sở dữ liệu
            List<Product> products = productService.getAllProducts();
            displayProducts(products);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Lỗi kết nối CSDL khi tải danh sách Sản Phẩm!");
        }
    }

    /**
     * Tìm kiếm sản phẩm
     */
    private void searchProducts(String keyword) {
        try {
            List<Product> products = productService.searchProducts(keyword);
            displayProducts(products);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Hàm dùng chung để in danh sách sản phẩm ra màn hình
     */
    private void displayProducts(List<Product> products) {
        productFlowPane.getChildren().clear(); // Xóa lưới cũ
        try {
            for (Product p : products) {
                // Tải khuôn mẫu ProductCard
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ProductCard.fxml"));
                VBox cardNode = loader.load();
                
                // Đẩy dữ liệu vào Controller của Card
                ProductCardController cardController = loader.getController();
                cardController.setProductData(p);
                
                // Add vào FlowPane
                productFlowPane.getChildren().add(cardNode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}