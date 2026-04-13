package com.cinema.entity;

/**
 * Đại diện cho mối quan hệ giữa hóa đơn và vé trong hệ thống.
 * Chứa các thuộc tính liên kết giữa hóa đơn và vé.
 * 
 * @author Thanh Trọng (chính)
 * @author Minh Huy (sửa)
 */
public class TicketInvoice {

	private Invoice invoice; // not null
	private Ticket ticket; // not null
	private double price; // not null

	/**
	 * Constructor mặc định, không có dữ liệu để truyền
	 */
	public TicketInvoice() {
		super();
	}

	/**
	 * Constructor để khởi tạo mối quan hệ giữa hóa đơn và vé
	 * 
	 * @param invoice - Hóa đơn
	 * @param ticket - Vé
	 * @param price - Đơn giá vé
	 */
	public TicketInvoice(Invoice invoice, Ticket ticket, double price) {
		super();
		setInvoice(invoice);
		setTicket(ticket);
		setPrice(price);
	}

	/**
	 * Lấy hóa đơn
	 * 
	 * @return hóa đơn
	 */
	public Invoice getInvoice() {
		return invoice;
	}

	/**
	 * Lấy vé
	 * 
	 * @return vé
	 */
	public Ticket getTicket() {
		return ticket;
	}

	/**
	 * Lấy đơn giá vé
	 * 
	 * @return đơn giá vé
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * Cập nhật hóa đơn
	 * 
	 * @param invoice - Hóa đơn
	 * @throws IllegalArgumentException nếu invoice là null
	 */
	public void setInvoice(Invoice invoice) {
		if (invoice == null)
			throw new IllegalArgumentException("invoice không được null");
		this.invoice = invoice;
	}

	/**
	 * Cập nhật vé
	 * 
	 * @param ticket - Vé
	 * @throws IllegalArgumentException nếu ticket là null
	 */
	public void setTicket(Ticket ticket) {
		if (ticket == null)
			throw new IllegalArgumentException("ticket không được null");
		this.ticket = ticket;
	}

	/**
	 * Cập nhật đơn giá vé
	 * 
	 * @param price - Đơn giá vé
	 * @throws IllegalArgumentException nếu price không hợp lệ
	 */
	public void setPrice(double price) {
		if (price <= 0)
			throw new IllegalArgumentException("price phải > 0");
		this.price = price;
	}

	/**
	 * Tính mã hash dựa trên mã hóa đơn và mã vé
	 * 
	 * @return mã hash của đối tượng
	 */
	@Override
	public int hashCode() {
		int invoiceId = (invoice != null) ? invoice.getInvoiceId() : 0;
		int ticketId = (ticket != null) ? ticket.getTicketId() : 0;
		return 31 * Integer.hashCode(invoiceId) + Integer.hashCode(ticketId);
	}

	/**
	 * Hai object TicketInvoice được xem là bằng nhau khi có cùng invoiceId và ticketId hợp lệ
	 * 
	 * @param obj - Đối tượng cần so sánh
	 * @return true nếu hai đối tượng bằng nhau, ngược lại là false
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		TicketInvoice other = (TicketInvoice) obj;

		if (this.invoice == null || other.invoice == null || this.ticket == null || other.ticket == null)
			return false;

		return this.invoice.getInvoiceId() > 0 && other.invoice.getInvoiceId() > 0
				&& this.ticket.getTicketId() > 0 && other.ticket.getTicketId() > 0
				&& this.invoice.getInvoiceId() == other.invoice.getInvoiceId()
				&& this.ticket.getTicketId() == other.ticket.getTicketId();
	}

	/**
	 * Trả về chuỗi biểu diễn đối tượng TicketInvoice
	 * 
	 * @return chuỗi thông tin mối quan hệ hóa đơn - vé
	 */
	@Override
	public String toString() {
		return "TicketInvoice [invoiceId=" + (invoice != null ? invoice.getInvoiceId() : null)
				+ ", ticketId=" + (ticket != null ? ticket.getTicketId() : null)
				+ ", price=" + price + "]";
	}
}