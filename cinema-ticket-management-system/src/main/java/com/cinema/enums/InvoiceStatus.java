package com.cinema.enums;

/**
 * Phân loại trạng thái của hóa đơn.
 * 0 - FAILED: Hóa đơn thất bại
 * 1 - SUCCESS: Hóa đơn thành công
 * 
 * @author Minh Huy (chính)
 */
public enum InvoiceStatus {
	FAILED(0, "Thất bại"),
	SUCCESS(1, "Thành công");

	private final int invoiceStatusId;
	private final String displayName;

	/**
	 * Khởi tạo trạng thái hóa đơn.
	 * 
	 * @param invoiceStatusId - Mã trạng thái hóa đơn
	 * @param displayName - Tên hiển thị trạng thái hóa đơn
	 */
	InvoiceStatus(int invoiceStatusId, String displayName) {
		this.invoiceStatusId = invoiceStatusId;
		this.displayName = displayName;
	}

	/**
	 * Lấy mã trạng thái hóa đơn.
	 * 
	 * @return mã trạng thái hóa đơn
	 */
	public int getInvoiceStatusId() {
		return invoiceStatusId;
	}

	/**
	 * Lấy tên hiển thị của trạng thái hóa đơn.
	 * 
	 * @return tên hiển thị trạng thái hóa đơn
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Chuyển mã trạng thái thành enum InvoiceStatus.
	 * 0 - FAILED: Hóa đơn thất bại
	 * 1 - SUCCESS: Hóa đơn thành công
	 * 
	 * @param id - Mã trạng thái hóa đơn
	 * @return trạng thái hóa đơn tương ứng
	 * @throws IllegalArgumentException nếu id không hợp lệ
	 */
	public static InvoiceStatus fromId(int id) {
		for (InvoiceStatus status : values()) {
			if (status.invoiceStatusId == id) {
				return status;
			}
		}
		throw new IllegalArgumentException("Mã trạng thái hóa đơn không hợp lệ: " + id);
	}
}