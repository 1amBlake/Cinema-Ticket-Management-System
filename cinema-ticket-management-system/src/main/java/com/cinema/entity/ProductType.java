package com.cinema.entity;

import java.time.LocalDateTime;

/**
 * Đại diện cho loại sản phẩm trong hệ thống.
 * Chứa các thuộc tính loại sản phẩm.
 * 
 * @author Hải Anh (chính)
 */
public class ProductType {

	private int productTypeId; //Do database tự sinh
	private String productTypeName; //not null
	private LocalDateTime createdAt; //Do database tự sinh
	private LocalDateTime updatedAt; //Do database tự sinh

	/**
	 * Constructor mặc định, không có dữ liệu để truyền
	 */
	public ProductType() {
		super();
	}

	/**
	 * Constructor để khởi tạo thực thể theo mã, phục vụ truy vấn và ánh xạ quan hệ
	 * 
	 * @param productTypeId - Mã loại sản phẩm
	 */
	public ProductType(int productTypeId) {
		super();
		this.productTypeId = productTypeId;
	}

	/**
	 * Constructor để thêm dữ liệu cho CSDL (các dữ liệu not null)
	 * 
	 * @param productTypeName - Tên loại sản phẩm
	 */
	public ProductType(String productTypeName) {
		super();
		setProductTypeName(productTypeName);
	}

	/**
	 * Constructor truyền dữ liệu với các thông tin "not null"
	 * 
	 * @param productTypeId - Mã loại sản phẩm
	 * @param productTypeName - Tên loại sản phẩm
	 */
	public ProductType(int productTypeId, String productTypeName) {
		super();
		this.productTypeId = productTypeId;
		setProductTypeName(productTypeName);
	}

	/**
	 * Constructor đầy đủ thông tin
	 * 
	 * @param productTypeId - Mã loại sản phẩm
	 * @param productTypeName - Tên loại sản phẩm
	 * @param createdAt - Thời điểm khởi tạo dữ liệu
	 * @param updatedAt - Thời điểm dữ liệu được cập nhật
	 */
	public ProductType(int productTypeId, String productTypeName, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.productTypeId = productTypeId;
		setProductTypeName(productTypeName);
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public int getProductTypeId() {
		return productTypeId;
	}

	public String getProductTypeName() {
		return productTypeName;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setProductTypeName(String productTypeName) {
		if (productTypeName == null || productTypeName.trim().isEmpty())
			throw new IllegalArgumentException("productTypeName không được để trống");
		if (productTypeName.trim().length() > 255)
			throw new IllegalArgumentException("productTypeName không được vượt quá 255 ký tự");
		this.productTypeName = productTypeName.trim();
	}

	/**
	 * Nếu đã có id hợp lệ thì hash theo id. Nếu chưa có id hợp lệ thì hash theo chính object
	 */
	@Override
	public int hashCode() {
		return (productTypeId > 0) ? Integer.hashCode(productTypeId) : System.identityHashCode(this);
	}

	/**
	 * Hai Object ProductType được xem là bằng nhau khi có cùng productTypeId hợp lệ
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		ProductType other = (ProductType) obj;
		if (this.productTypeId <= 0 || other.productTypeId <= 0)
			return false;
		return this.productTypeId == other.productTypeId;
	}

	@Override
	public String toString() {
		return "ProductType [productTypeId=" + productTypeId + ", productTypeName=" + productTypeName
				+ ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
}