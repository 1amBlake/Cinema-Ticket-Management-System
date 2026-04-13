package com.cinema.entity;

import java.time.LocalDateTime;

import com.cinema.enums.InvoiceStatus;
import com.cinema.enums.PaymentMethod;

/**
 * Đại diện cho hóa đơn trong hệ thống.
 * Chứa các thuộc tính hóa đơn.
 * 
 * @author Thanh Trọng (chính)
 * @author Minh Huy (sửa)
 */
public class Invoice {

	private int invoiceId; //Do database tự sinh
	private Employee employee; //not null
	private PaymentMethod paymentMethod; //not null
	private double totalAmount; //not null
	private InvoiceStatus invoiceStatus; //not null
	private LocalDateTime paymentTime;
	private LocalDateTime createdAt; //Do database tự sinh
	private LocalDateTime updatedAt; //Do database tự sinh

	/**
	 * Constructor mặc định, không có dữ liệu để truyền
	 */
	public Invoice() {
		super();
	}

	/**
	 * Constructor để khởi tạo thực thể theo mã, phục vụ truy vấn và ánh xạ quan hệ
	 * 
	 * @param invoiceId - Mã hóa đơn
	 */
	public Invoice(int invoiceId) {
		super();
		this.invoiceId = invoiceId;
	}

	/**
	 * Constructor để thêm dữ liệu cho CSDL (các dữ liệu not null)
	 * 
	 * @param employee - Nhân viên lập hóa đơn
	 * @param paymentMethod - Phương thức thanh toán
	 * @param totalAmount - Tổng tiền hóa đơn
	 * @param invoiceStatus - Trạng thái hóa đơn
	 */
	public Invoice(Employee employee, PaymentMethod paymentMethod, double totalAmount, InvoiceStatus invoiceStatus) {
		super();
		setEmployee(employee);
		setPaymentMethod(paymentMethod);
		setTotalAmount(totalAmount);
		setInvoiceStatus(invoiceStatus);
	}

	/**
	 * Constructor truyền dữ liệu với các thông tin "not null"
	 * 
	 * @param invoiceId - Mã hóa đơn
	 * @param employee - Nhân viên lập hóa đơn
	 * @param paymentMethod - Phương thức thanh toán
	 * @param totalAmount - Tổng tiền hóa đơn
	 * @param invoiceStatus - Trạng thái hóa đơn
	 */
	public Invoice(int invoiceId, Employee employee, PaymentMethod paymentMethod, double totalAmount,
			InvoiceStatus invoiceStatus) {
		super();
		this.invoiceId = invoiceId;
		setEmployee(employee);
		setPaymentMethod(paymentMethod);
		setTotalAmount(totalAmount);
		setInvoiceStatus(invoiceStatus);
	}

	/**
	 * Constructor đầy đủ thông tin
	 * 
	 * @param invoiceId - Mã hóa đơn
	 * @param employee - Nhân viên lập hóa đơn
	 * @param paymentMethod - Phương thức thanh toán
	 * @param totalAmount - Tổng tiền hóa đơn
	 * @param invoiceStatus - Trạng thái hóa đơn
	 * @param paymentTime - Thời gian thanh toán
	 * @param createdAt - Thời điểm khởi tạo dữ liệu
	 * @param updatedAt - Thời điểm dữ liệu được cập nhật
	 */
	public Invoice(int invoiceId, Employee employee, PaymentMethod paymentMethod, double totalAmount,
			InvoiceStatus invoiceStatus, LocalDateTime paymentTime, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.invoiceId = invoiceId;
		setEmployee(employee);
		setPaymentMethod(paymentMethod);
		setTotalAmount(totalAmount);
		setInvoiceStatus(invoiceStatus);
		setPaymentTime(paymentTime);
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public int getInvoiceId() {
		return invoiceId;
	}

	public Employee getEmployee() {
		return employee;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public InvoiceStatus getInvoiceStatus() {
		return invoiceStatus;
	}

	public LocalDateTime getPaymentTime() {
		return paymentTime;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setEmployee(Employee employee) {
		if (employee == null)
			throw new IllegalArgumentException("employee không được null");
		this.employee = employee;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		if (paymentMethod == null)
			throw new IllegalArgumentException("paymentMethod không được null");
		this.paymentMethod = paymentMethod;
	}

	public void setTotalAmount(double totalAmount) {
		if (totalAmount < 0)
			throw new IllegalArgumentException("totalAmount phải >= 0");
		this.totalAmount = totalAmount;
	}

	public void setInvoiceStatus(InvoiceStatus invoiceStatus) {
		if (invoiceStatus == null)
			throw new IllegalArgumentException("invoiceStatus không được null");
		this.invoiceStatus = invoiceStatus;
	}

	public void setPaymentTime(LocalDateTime paymentTime) {
		this.paymentTime = paymentTime;
	}

	/**
	 * Nếu đã có id hợp lệ thì hash theo id. Nếu chưa có id hợp lệ thì hash theo chính object
	 */
	@Override
	public int hashCode() {
		return (invoiceId > 0) ? Integer.hashCode(invoiceId) : System.identityHashCode(this);
	}

	/**
	 * Hai Object Invoice được xem là bằng nhau khi có cùng invoiceId hợp lệ
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Invoice other = (Invoice) obj;
		if (this.invoiceId <= 0 || other.invoiceId <= 0)
			return false;
		return this.invoiceId == other.invoiceId;
	}

	@Override
	public String toString() {
		return "Invoice [invoiceId=" + invoiceId + ", employee=" + employee + ", paymentMethod=" + paymentMethod
				+ ", totalAmount=" + totalAmount + ", invoiceStatus=" + invoiceStatus + ", paymentTime=" + paymentTime
				+ ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
}