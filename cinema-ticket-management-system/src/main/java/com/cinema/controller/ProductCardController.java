package com.cinema.controller;

import com.cinema.entity.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.InputStream;
import java.text.DecimalFormat;

public class ProductCardController {

    @FXML private ImageView productImageView;
    @FXML private Label productNameLabel;
    @FXML private Label priceLabel;
    @FXML private Label statusLabel;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private Product product;

    public void setProductData(Product product) {
        this.product = product;
        productNameLabel.setText(product.getProductName());
        
        // Format tiền tệ VNĐ
        DecimalFormat df = new DecimalFormat("#,###");
        priceLabel.setText(df.format(product.getPrice()) + " đ");
        
        statusLabel.setText("Tồn kho: " + product.getStockQuantity());

        try {
            String pictureUrl = product.getPictureUrl();
            
            if (pictureUrl != null && !pictureUrl.isEmpty()) {

                String finalPath = pictureUrl.startsWith("/") ? pictureUrl : "/" + pictureUrl;
                
                InputStream is = getClass().getResourceAsStream(finalPath);
                
                if (is != null) {
                    Image img = new Image(is);
                    productImageView.setImage(img);
                } else {
                    System.err.println("Không tìm thấy file ảnh thực tế tại: resources" + finalPath);
                    setEmptyImage(); 
                }
            } else {
                setEmptyImage();
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi load ảnh cho: " + product.getProductName() + " - " + e.getMessage());
            setEmptyImage();
        }


        editButton.setOnAction(e -> {
            System.out.println("Đang bấm sửa sản phẩm ID: " + product.getProductId());
            // TODO: Mở Dialog sửa sản phẩm
        });

        deleteButton.setOnAction(e -> {
            System.out.println("Đang bấm xóa sản phẩm ID: " + product.getProductId());
            // TODO: Gọi ProductService để xóa
        });
    }

    // Hàm phụ để set ảnh mặc định (logo) khi lỗi
    private void setEmptyImage() {
        try {
            InputStream is = getClass().getResourceAsStream("/images/logo.png");
            if (is != null) {
                productImageView.setImage(new Image(is));
            } else {
                System.err.println("Không tìm thấy ảnh mặc định (logo.png) trong thư mục resources/images/");
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi load ảnh mặc định!");
        }
    }
}