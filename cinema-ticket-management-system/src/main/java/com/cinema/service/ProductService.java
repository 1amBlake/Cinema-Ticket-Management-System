package com.cinema.service;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import com.cinema.dao.ProductDao;
import com.cinema.entity.Product;
import com.cinema.enums.ProductStatus;

public class ProductService {

    private final ProductDao productDao;

    public ProductService() {
        this.productDao = new ProductDao();
    }


    public List<Product> getAllProducts() throws SQLException {
        return productDao.getAllProducts();
    }


    public Product findProductById(int id) throws SQLException {
        return productDao.findById(id);
    }


    public boolean addProduct(Product product) throws SQLException {
        validate(product);
        return productDao.addProduct(product);
    }


    public boolean updateProduct(Product product) throws SQLException {
        validate(product);
        return productDao.updateProduct(product);
    }


    public boolean deleteProduct(int id) throws SQLException {
        return productDao.deleteProductById(id);
    }


    public List<Product> searchProducts(String keyword) throws SQLException {
        return productDao.searchByName(keyword);
    }


    public List<Product> getAvailableProducts() throws SQLException {
        return productDao.getAllProducts()
                .stream()
                .filter(p -> p.getProductStatus() == ProductStatus.ACTIVE)
                .filter(p -> p.getStockQuantity() > 0)
                .collect(Collectors.toList());
    }


    public boolean updateProductStock(int productId, int quantityChange) throws SQLException {

        if (productId <= 0) {
            throw new IllegalArgumentException("Invalid productId");
        }

        Product product = productDao.findById(productId);

        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }

        int newStock = product.getStockQuantity() + quantityChange;

        if (newStock < 0) {
            throw new IllegalArgumentException("Not enough stock");
        }

        product.setStockQuantity(newStock);

        return productDao.updateProduct(product);
    }


    private void validate(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product is null");
        }

        if (product.getProductName() == null || product.getProductName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name is required");
        }

        if (product.getProductType() == null) {
            throw new IllegalArgumentException("Product type is required");
        }

        if (product.getPrice() < 0) {
            throw new IllegalArgumentException("Price must be >= 0");
        }
    }
}