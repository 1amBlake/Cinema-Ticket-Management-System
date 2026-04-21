package com.cinema.enums;

/**
 * Phân loại trạng thái của hóa đơn.
 * 0 - PENDING: Hóa đơn đang chờ xử lý / chưa thanh toán
 * 1 - SUCCESS: Hóa đơn thành công
 * 2 - FAILED: Hóa đơn thất bại
 * 3 - CANCELLED: Hóa đơn đã hủy
 * 
 * @author Minh Huy (chính)
 */
public enum InvoiceStatus {
	PENDING(0, "Chờ xử lý"),
	SUCCESS(1, "Thành công"),
	FAILED(2, "Thất bại"),
	CANCELLED(3, "Đã hủy");

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
	 * 0 - PENDING: Hóa đơn đang chờ xử lý / chưa thanh toán
	 * 1 - SUCCESS: Hóa đơn thành công
	 * 2 - FAILED: Hóa đơn thất bại
	 * 3 - CANCELLED: Hóa đơn đã hủy
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
/*
 * UPDATE: 20/04/2026
 * Cũ chỉ có FAILED và SUCCESS nên không biểu diễn được trạng thái trung gian
 * Thêm PENDING để biểu diễn hóa đơn đang lập, thêm CANCELLED để phân biệt với lỗi thanh toán. “Hủy” và “thất bại” không hoàn toàn giống nhau về nghiệp vụ.
 * */