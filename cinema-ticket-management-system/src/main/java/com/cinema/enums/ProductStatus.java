package com.cinema.enums;

/**
 * Phân loại trạng thái của sản phẩm.
 * 0 - STOPPED: Sản phẩm ngừng bán
 * 1 - SELLING: Sản phẩm đang bán
 * 2 - SOLD_OUT: Sản phẩm hết hàng
 * 
 * @author Hải Anh (chính)
 */
public enum ProductStatus {
	STOPPED(0, "Ngừng bán"),
	SELLING(1, "Đang bán"),
	SOLD_OUT(2, "Hết hàng");

	private final int productStatusId;
	private final String displayName;

	/**
	 * Khởi tạo trạng thái sản phẩm.
	 * 
	 * @param productStatusId - Mã trạng thái sản phẩm
	 * @param displayName - Tên hiển thị trạng thái sản phẩm
	 */
	ProductStatus(int productStatusId, String displayName) {
		this.productStatusId = productStatusId;
		this.displayName = displayName;
	}

	/**
	 * Lấy mã trạng thái sản phẩm.
	 * 
	 * @return mã trạng thái sản phẩm
	 */
	public int getProductStatusId() {
		return productStatusId;
	}

	/**
	 * Lấy tên hiển thị của trạng thái sản phẩm.
	 * 
	 * @return tên hiển thị trạng thái sản phẩm
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Chuyển mã trạng thái thành enum ProductStatus.
	 * 0 - STOPPED: Sản phẩm ngừng bán
	 * 1 - SELLING: Sản phẩm đang bán
	 * 2 - SOLD_OUT: Sản phẩm hết hàng
	 * 
	 * @param id - Mã trạng thái sản phẩm
	 * @return trạng thái sản phẩm tương ứng
	 * @throws IllegalArgumentException nếu id không hợp lệ
	 */
	public static ProductStatus fromId(int id) {
		for (ProductStatus status : values()) {
			if (status.productStatusId == id) {
				return status;
			}
		}
		throw new IllegalArgumentException("Mã trạng thái sản phẩm không hợp lệ: " + id);
	}
}