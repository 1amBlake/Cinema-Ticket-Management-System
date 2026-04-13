package com.cinema.entity;

/**
 * Đại diện cho mối quan hệ giữa hóa đơn và sản phẩm trong hệ thống.
 * Chứa các thuộc tính liên kết giữa hóa đơn và sản phẩm.
 * 
 * @author Thanh Trọng (chính)
 * @author Minh Huy (sửa)
 */
public class ProductInvoice {

	private Invoice invoice; // not null
	private Product product; // not null
	private int quantity; // not null
	private double unitPrice; // not null
	private double totalPrice; // not null

	/**
	 * Constructor mặc định, không có dữ liệu để truyền
	 */
	public ProductInvoice() {
		super();
	}

	/**
	 * Constructor để khởi tạo mối quan hệ giữa hóa đơn và sản phẩm
	 * 
	 * @param invoice - Hóa đơn
	 * @param product - Sản phẩm
	 * @param quantity - Số lượng sản phẩm
	 * @param unitPrice - Đơn giá sản phẩm
	 */
	public ProductInvoice(Invoice invoice, Product product, int quantity, double unitPrice) {
		super();
		setInvoice(invoice);
		setProduct(product);
		setQuantity(quantity);
		setUnitPrice(unitPrice);
		recalculateTotalPrice();
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
	 * Lấy sản phẩm
	 * 
	 * @return sản phẩm
	 */
	public Product getProduct() {
		return product;
	}

	/**
	 * Lấy số lượng sản phẩm
	 * 
	 * @return số lượng sản phẩm
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * Lấy đơn giá sản phẩm
	 * 
	 * @return đơn giá sản phẩm
	 */
	public double getUnitPrice() {
		return unitPrice;
	}

	/**
	 * Lấy thành tiền
	 * 
	 * @return thành tiền
	 */
	public double getTotalPrice() {
		return totalPrice;
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
	 * Cập nhật sản phẩm
	 * 
	 * @param product - Sản phẩm
	 * @throws IllegalArgumentException nếu product là null
	 */
	public void setProduct(Product product) {
		if (product == null)
			throw new IllegalArgumentException("product không được null");
		this.product = product;
	}

	/**
	 * Cập nhật số lượng sản phẩm
	 * 
	 * @param quantity - Số lượng sản phẩm
	 * @throws IllegalArgumentException nếu quantity không hợp lệ
	 */
	public void setQuantity(int quantity) {
		if (quantity <= 0)
			throw new IllegalArgumentException("quantity phải > 0");
		this.quantity = quantity;
		recalculateTotalPrice();
	}

	/**
	 * Cập nhật đơn giá sản phẩm
	 * 
	 * @param unitPrice - Đơn giá sản phẩm
	 * @throws IllegalArgumentException nếu unitPrice không hợp lệ
	 */
	public void setUnitPrice(double unitPrice) {
		if (unitPrice <= 0)
			throw new IllegalArgumentException("unitPrice phải > 0");
		this.unitPrice = unitPrice;
		recalculateTotalPrice();
	}

	/**
	 * Tự động tính lại thành tiền theo số lượng và đơn giá
	 */
	private void recalculateTotalPrice() {
		if (this.quantity > 0 && this.unitPrice > 0) {
			this.totalPrice = this.quantity * this.unitPrice;
		}
	}

	/**
	 * Tính mã hash dựa trên mã hóa đơn và mã sản phẩm
	 * 
	 * @return mã hash của đối tượng
	 */
	@Override
	public int hashCode() {
		int invoiceId = (invoice != null) ? invoice.getInvoiceId() : 0;
		int productId = (product != null) ? product.getProductId() : 0;
		return 31 * Integer.hashCode(invoiceId) + Integer.hashCode(productId);
	}

	/**
	 * Hai object ProductInvoice được xem là bằng nhau khi có cùng invoiceId và productId hợp lệ
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

		ProductInvoice other = (ProductInvoice) obj;

		if (this.invoice == null || other.invoice == null || this.product == null || other.product == null)
			return false;

		return this.invoice.getInvoiceId() > 0 && other.invoice.getInvoiceId() > 0
				&& this.product.getProductId() > 0 && other.product.getProductId() > 0
				&& this.invoice.getInvoiceId() == other.invoice.getInvoiceId()
				&& this.product.getProductId() == other.product.getProductId();
	}

	/**
	 * Trả về chuỗi biểu diễn đối tượng ProductInvoice
	 * 
	 * @return chuỗi thông tin mối quan hệ hóa đơn - sản phẩm
	 */
	@Override
	public String toString() {
		return "ProductInvoice [invoiceId=" + (invoice != null ? invoice.getInvoiceId() : null)
				+ ", productId=" + (product != null ? product.getProductId() : null)
				+ ", quantity=" + quantity + ", unitPrice=" + unitPrice
				+ ", totalPrice=" + totalPrice + "]";
	}
}