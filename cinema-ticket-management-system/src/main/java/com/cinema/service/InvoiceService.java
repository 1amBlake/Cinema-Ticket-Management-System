package com.cinema.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cinema.dao.InvoiceDao;
import com.cinema.dao.ProductInvoiceDao;
import com.cinema.dao.TicketInvoiceDao;
import com.cinema.entity.Employee;
import com.cinema.entity.Invoice;
import com.cinema.entity.Product;
import com.cinema.entity.ProductInvoice;
import com.cinema.entity.Ticket;
import com.cinema.entity.TicketInvoice;
import com.cinema.enums.InvoiceStatus;
import com.cinema.enums.PaymentMethod;

/**
 * Service cho hoá đơn.
 * Chứa các chức vụ liên quan đến hoá đơn.
 * 
 * @author Hải Anh (chính)
 */
public class InvoiceService {

	private final InvoiceDao invoiceDao;
	private final TicketInvoiceDao ticketInvoiceDao;
	private final ProductInvoiceDao productInvoiceDao;

	public InvoiceService() {
		this.invoiceDao = new InvoiceDao();
		this.ticketInvoiceDao = new TicketInvoiceDao();
		this.productInvoiceDao = new ProductInvoiceDao();
	}
	
	/**
	 * Tạo hoá đơn
	 * 
	 * @param employee - nhân viên
	 * @param paymentMethod - phương thức thanh toán
	 * @return true nếu tạo thành công
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean createInvoice(Employee employee, PaymentMethod paymentMethod) throws SQLException {
		Invoice invoice = new Invoice(employee, paymentMethod, InvoiceStatus.PENDING);
		return invoiceDao.addInvoice(invoice);
	}

	/**
	 * Thêm vé vào hoá đơn
	 * 
	 * @param invoiceId - Mã hoá đơn
	 * @param ticket - Vé
	 * @param price - Giá vé
	 * @return true nếu thêm vé thành công
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean addTicketToInvoice(int invoiceId, Ticket ticket, double price) throws SQLException {
		Invoice invoice = invoiceDao.findById(invoiceId);
		if(invoice == null) {
			throw new IllegalArgumentException("Hóa đơn không tồn tại!");
		}
		
		TicketInvoice ticketInvoice = new TicketInvoice(invoice, ticket, price);
		return ticketInvoiceDao.addTicketInvoice(ticketInvoice);
	}

	/**
	 * Thêm sản phẩm vào hoá đơn
	 * 
	 * @param invoiceId - Mã hoá đơn
	 * @param product - Sản phẩm
	 * @param quantity - Số lượng sản phẩm
	 * @param unitPrice - Đơn giá sản phẩm
	 * @return true nếu thêm sản phẩm thành công
	 * @throws SQLException nếu có lỗi SQL
	 */
	public boolean addProductToInvoice(int invoiceId, Product product, int quantity, double unitPrice) throws SQLException {
		Invoice invoice = invoiceDao.findById(invoiceId);
		if(invoice == null) {
			throw new IllegalArgumentException("Hóa đơn không tồn tại!");
		}
			
		ProductInvoice productInvoice = new ProductInvoice(invoice, product, quantity, unitPrice);
		return productInvoiceDao.addProductInvoice(productInvoice);
	}

	/**
	 * Lấy tất cả hóa đơn
	 * 
	 * @return danh sách hóa đơn
	 * @throws SQLException nếu có lỗi SQL
	 */
	public List<Invoice> getAllInvoices() throws SQLException {
		return invoiceDao.getAllInvoices();
	}

	/**
	 * Lấy hóa đơn theo mã nhân viên
	 * 
	 * @param employeeId - Mã nhân viên
	 * @return danh sách hóa đơn phù hợp
	 * @throws SQLException nếu có lỗi SQL
	 */
	public List<Invoice> getInvoicesByEmployee(int employeeId) throws SQLException {
		return invoiceDao.searchByEmployeeId(employeeId);
	}

	/**
	 * Lấy hóa đơn theo trạng thái
	 * 
	 * @param invoiceStatus - Trạng thái hóa đơn
	 * @return danh sách hóa đơn phù hợp
	 * @throws SQLException nếu có lỗi SQL
	 */
	public List<Invoice> getInvoicesByStatus(InvoiceStatus invoiceStatus) throws SQLException {
		return invoiceDao.searchByStatus(invoiceStatus);
	}

	/**
	 * Class DTO cho chi tiết hoá đơn
	 * đùng để gom các đối tượng Invoice, TicketInvoice, ProductInvoice thành một để dễ lấy
	 */
	public static class InvoiceDetail {
		private Invoice invoice;
		private List<TicketInvoice> ticketInvoices;
		private List<ProductInvoice> productInvoices;

		public InvoiceDetail(Invoice invoice, List<TicketInvoice> ticketInvoices, List<ProductInvoice> productInvoices) {
			this.invoice = invoice;
			this.ticketInvoices = ticketInvoices != null ? ticketInvoices : new ArrayList<>();
			this.productInvoices = productInvoices != null ? productInvoices : new ArrayList<>();
		}

		public Invoice getInvoice() {
			return invoice;
		}

		public List<TicketInvoice> getTicketInvoices() {
			return ticketInvoices;
		}
		
		public List<ProductInvoice> getProductInvoices() {
			return productInvoices;
		}
	}
	
	/**
	 * Lấy chi tiết hóa đơn
	 * 
	 * @param invoiceId - Mã hoá đơn
	 * @return đối tượng InvoiceDetail
	 * @throws SQLException nếu có lỗi SQL
	 */
	public InvoiceDetail getInvoiceDetail(int invoiceId) throws SQLException {
		Invoice invoice = invoiceDao.findById(invoiceId);
		if(invoice == null) return null;

		List<TicketInvoice> tickets = ticketInvoiceDao.searchByInvoiceId(invoiceId);
		List<ProductInvoice> products = productInvoiceDao.searchByInvoiceId(invoiceId);

		return new InvoiceDetail(invoice, tickets, products);
	}

	/**
	 * Xuất hoá đơn theo text
	 * 
	 * @param invoiceId - Mã hoá đơn
	 * @return chuỗi text
	 * @throws SQLException nếu có lỗi SQL
	 */
	public String exportInvoiceText(int invoiceId) throws SQLException {
		InvoiceDetail invoiceDetail = getInvoiceDetail(invoiceId);
		if (invoiceDetail == null) return "Không tìm thấy hóa đơn!";

		StringBuilder sb = new StringBuilder();
		Invoice invoice = invoiceDetail.getInvoice();

		sb.append("====== HÓA  ĐƠN ======\n");
		sb.append("Mã: ").append(invoice.getInvoiceId()).append("\n");
		sb.append("Nhân viên: ").append(invoice.getEmployee().getEmployeeId()).append("\n");
		sb.append("Trạng thái: ").append(invoice.getInvoiceStatus()).append("\n");
		sb.append("Phương thức: ").append(invoice.getPaymentMethod()).append("\n");
		sb.append("Thời gian: ").append(invoice.getPaymentTime()).append("\n");

		double total = 0;

		sb.append("\n== CHI TIẾT HOÁ ĐƠN ==\n");
		sb.append("--- VÉ ---\n");
		for(TicketInvoice ticketInvoice : invoiceDetail.getTicketInvoices()) {
			sb.append("Ticket ID: ").append(ticketInvoice.getTicket().getTicketId())
					.append(" | Giá: ").append(ticketInvoice.getPrice()).append("\n");
			total += ticketInvoice.getPrice();
		}

		sb.append("\n--- SẢN PHẨM ---\n");
		for(ProductInvoice productInvoice : invoiceDetail.getProductInvoices()) {
			sb.append("Product ID: ").append(productInvoice.getProduct().getProductId())
					.append(" | SL: ").append(productInvoice.getQuantity())
					.append(" | Giá: ").append(productInvoice.getUnitPrice())
					.append(" | Thành tiền: ").append(productInvoice.getTotalPrice())
					.append("\n");
			total += productInvoice.getTotalPrice();
		}

		sb.append("\n======================\n");
		sb.append("TỔNG TIỀN: ").append(total);

		return sb.toString();
	}
}