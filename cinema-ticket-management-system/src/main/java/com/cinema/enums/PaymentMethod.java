package com.cinema.enums;

/**
 * Phân loại phương thức thanh toán của hóa đơn.
 * 0 - CASH: Thanh toán tiền mặt
 * 1 - BANK_TRANSFER: Thanh toán chuyển khoản
 * 
 * @author Minh Huy (chính)
 */
public enum PaymentMethod {
	CASH(0, "Tiền mặt"),
	BANK_TRANSFER(1, "Chuyển khoản");

	private final int paymentMethodId;
	private final String displayName;

	/**
	 * Khởi tạo phương thức thanh toán.
	 * 
	 * @param paymentMethodId - Mã phương thức thanh toán
	 * @param displayName - Tên hiển thị phương thức thanh toán
	 */
	PaymentMethod(int paymentMethodId, String displayName) {
		this.paymentMethodId = paymentMethodId;
		this.displayName = displayName;
	}

	/**
	 * Lấy mã phương thức thanh toán.
	 * 
	 * @return mã phương thức thanh toán
	 */
	public int getPaymentMethodId() {
		return paymentMethodId;
	}

	/**
	 * Lấy tên hiển thị của phương thức thanh toán.
	 * 
	 * @return tên hiển thị phương thức thanh toán
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Chuyển mã phương thức thanh toán thành enum PaymentMethod.
	 * 0 - CASH: Thanh toán tiền mặt
	 * 1 - BANK_TRANSFER: Thanh toán chuyển khoản
	 * 
	 * @param id - Mã phương thức thanh toán
	 * @return phương thức thanh toán tương ứng
	 * @throws IllegalArgumentException nếu id không hợp lệ
	 */
	public static PaymentMethod fromId(int id) {
		for (PaymentMethod method : values()) {
			if (method.paymentMethodId == id) {
				return method;
			}
		}
		throw new IllegalArgumentException("Mã phương thức thanh toán không hợp lệ: " + id);
	}
}