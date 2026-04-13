package com.cinema.enums;

/**
 * Phân loại trạng thái của vé.
 * 0 - NOT_SOLD: Vé chưa bán
 * 1 - SOLD: Vé đã bán
 * 2 - CHANGED: Vé đã đổi
 * 3 - CANCELLED: Vé đã hủy
 * 
 * @author Hải Anh (chính)
 */
public enum TicketStatus {
	NOT_SOLD(0, "Chưa bán"),
	SOLD(1, "Đã bán"),
	CHANGED(2, "Đã đổi"),
	CANCELLED(3, "Đã hủy");

	private final int ticketStatusId;
	private final String displayName;

	/**
	 * Khởi tạo trạng thái vé.
	 * 
	 * @param ticketStatusId - Mã trạng thái vé
	 * @param displayName - Tên hiển thị trạng thái vé
	 */
	TicketStatus(int ticketStatusId, String displayName) {
		this.ticketStatusId = ticketStatusId;
		this.displayName = displayName;
	}

	/**
	 * Lấy mã trạng thái vé.
	 * 
	 * @return mã trạng thái vé
	 */
	public int getTicketStatusId() {
		return ticketStatusId;
	}

	/**
	 * Lấy tên hiển thị của trạng thái vé.
	 * 
	 * @return tên hiển thị trạng thái vé
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Chuyển mã trạng thái thành enum TicketStatus.
	 * 0 - NOT_SOLD: Vé chưa bán
	 * 1 - SOLD: Vé đã bán
	 * 2 - CHANGED: Vé đã đổi
	 * 3 - CANCELLED: Vé đã hủy
	 * 
	 * @param id - Mã trạng thái vé
	 * @return trạng thái vé tương ứng
	 * @throws IllegalArgumentException nếu id không hợp lệ
	 */
	public static TicketStatus fromId(int id) {
		for (TicketStatus status : values()) {
			if (status.ticketStatusId == id) {
				return status;
			}
		}
		throw new IllegalArgumentException("Mã trạng thái vé không hợp lệ: " + id);
	}
}