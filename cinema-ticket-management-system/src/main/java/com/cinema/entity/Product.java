package com.cinema.entity;

import java.time.LocalDateTime;

import com.cinema.enums.ProductStatus;

public class Product {
	private int productId;
	private String productName;
	private ProductType productTypeId;
	private double price;
	private int stockQuantity;
	private ProductStatus productStatus;
	private String pictureUrl;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	public Product() {
		// TODO Auto-generated constructor stub
	}
//TODO: chưa có getter setter tostring
}
