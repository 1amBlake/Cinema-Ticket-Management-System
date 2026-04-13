package com.cinema.entity;

import java.time.LocalDateTime;

import com.cinema.enums.ProductStatus;

/**
 * Đại diện cho sản phẩm trong hệ thống.
 * Chứa các thuộc tính sản phẩm.
 * 
 * @author Hải Anh (chính)
 */
public class Product {

	private int productId; //Do database tự sinh
	private String productName; //not null
	private ProductType productType; //not null
	private double price; //not null
	private int stockQuantity; //not null
	private ProductStatus productStatus; //not null
	private String pictureUrl;
	private LocalDateTime createdAt; //Do database tự sinh
	private LocalDateTime updatedAt; //Do database tự sinh

	/**
	 * Constructor mặc định, không có dữ liệu để truyền
	 */
	public Product() {
		super();
	}

	/**
	 * Constructor để khởi tạo thực thể theo mã, phục vụ truy vấn và ánh xạ quan hệ
	 * 
	 * @param productId - Mã sản phẩm
	 */
	public Product(int productId) {
		super();
		this.productId = productId;
	}

	/**
	 * Constructor để thêm dữ liệu cho CSDL (các dữ liệu not null)
	 * 
	 * @param productName - Tên sản phẩm
	 * @param productType - Loại sản phẩm
	 * @param price - Giá cơ bản
	 * @param stockQuantity - Số lượng tồn
	 * @param productStatus - Trạng thái sản phẩm
	 */
	public Product(String productName, ProductType productType, double price, int stockQuantity,
			ProductStatus productStatus) {
		super();
		setProductName(productName);
		setProductType(productType);
		setPrice(price);
		setStockQuantity(stockQuantity);
		setProductStatus(productStatus);
	}

	/**
	 * Constructor truyền dữ liệu với các thông tin "not null"
	 * 
	 * @param productId - Mã sản phẩm
	 * @param productName - Tên sản phẩm
	 * @param productType - Loại sản phẩm
	 * @param price - Giá cơ bản
	 * @param stockQuantity - Số lượng tồn
	 * @param productStatus - Trạng thái sản phẩm
	 */
	public Product(int productId, String productName, ProductType productType, double price, int stockQuantity,
			ProductStatus productStatus) {
		super();
		this.productId = productId;
		setProductName(productName);
		setProductType(productType);
		setPrice(price);
		setStockQuantity(stockQuantity);
		setProductStatus(productStatus);
	}

	/**
	 * Constructor để thêm dữ liệu cho CSDL (các dữ liệu có thể null)
	 * 
	 * @param productId - Mã sản phẩm
	 * @param productName - Tên sản phẩm
	 * @param productType - Loại sản phẩm
	 * @param price - Giá cơ bản
	 * @param stockQuantity - Số lượng tồn
	 * @param productStatus - Trạng thái sản phẩm
	 * @param pictureUrl - Đường dẫn hình ảnh sản phẩm
	 */
	public Product(int productId, String productName, ProductType productType, double price, int stockQuantity,
			ProductStatus productStatus, String pictureUrl) {
		super();
		this.productId = productId;
		setProductName(productName);
		setProductType(productType);
		setPrice(price);
		setStockQuantity(stockQuantity);
		setProductStatus(productStatus);
		setPictureUrl(pictureUrl);
	}

	/**
	 * Constructor đầy đủ thông tin
	 * 
	 * @param productId - Mã sản phẩm
	 * @param productName - Tên sản phẩm
	 * @param productType - Loại sản phẩm
	 * @param price - Giá cơ bản
	 * @param stockQuantity - Số lượng tồn
	 * @param productStatus - Trạng thái sản phẩm
	 * @param pictureUrl - Đường dẫn hình ảnh sản phẩm
	 * @param createdAt - Thời điểm khởi tạo dữ liệu
	 * @param updatedAt - Thời điểm dữ liệu được cập nhật
	 */
	public Product(int productId, String productName, ProductType productType, double price, int stockQuantity,
			ProductStatus productStatus, String pictureUrl, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.productId = productId;
		setProductName(productName);
		setProductType(productType);
		setPrice(price);
		setStockQuantity(stockQuantity);
		setProductStatus(productStatus);
		setPictureUrl(pictureUrl);
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public int getProductId() {
		return productId;
	}

	public String getProductName() {
		return productName;
	}

	public ProductType getProductType() {
		return productType;
	}

	public double getPrice() {
		return price;
	}

	public int getStockQuantity() {
		return stockQuantity;
	}

	public ProductStatus getProductStatus() {
		return productStatus;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setProductName(String productName) {
		if (productName == null || productName.trim().isEmpty())
			throw new IllegalArgumentException("productName không được để trống");
		if (productName.trim().length() > 255)
			throw new IllegalArgumentException("productName không được vượt quá 255 ký tự");
		this.productName = productName.trim();
	}

	public void setProductType(ProductType productType) {
		if (productType == null)
			throw new IllegalArgumentException("productType không được null");
		this.productType = productType;
	}

	public void setPrice(double price) {
		if (price <= 0)
			throw new IllegalArgumentException("price phải > 0");
		this.price = price;
	}

	public void setStockQuantity(int stockQuantity) {
		if (stockQuantity < 0)
			throw new IllegalArgumentException("stockQuantity phải >= 0");
		this.stockQuantity = stockQuantity;
	}

	public void setProductStatus(ProductStatus productStatus) {
		if (productStatus == null)
			throw new IllegalArgumentException("productStatus không được null");
		this.productStatus = productStatus;
	}

	public void setPictureUrl(String pictureUrl) {
		if (pictureUrl == null || pictureUrl.trim().isEmpty()) {
			this.pictureUrl = null;
			return;
		}
		if (pictureUrl.trim().length() > 255)
			throw new IllegalArgumentException("pictureUrl không được vượt quá 255 ký tự");
		this.pictureUrl = pictureUrl.trim();
	}

	/**
	 * Nếu đã có id hợp lệ thì hash theo id. Nếu chưa có id hợp lệ thì hash theo chính object
	 */
	@Override
	public int hashCode() {
		return (productId > 0) ? Integer.hashCode(productId) : System.identityHashCode(this);
	}

	/**
	 * Hai Object Product được xem là bằng nhau khi có cùng productId hợp lệ
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (this.productId <= 0 || other.productId <= 0)
			return false;
		return this.productId == other.productId;
	}

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", productName=" + productName + ", productType=" + productType
				+ ", price=" + price + ", stockQuantity=" + stockQuantity + ", productStatus=" + productStatus
				+ ", pictureUrl=" + pictureUrl + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
}