package com.cinema.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class InvoiceScreenController implements Initializable {


    @FXML private Label totalInvoicesValueLabel;
    @FXML private Label successfulInvoicesValueLabel;
    @FXML private Label todayInvoicesValueLabel;
    @FXML private Label totalAmountValueLabel;


    @FXML private TextField invoiceSearchField;
    @FXML private Label searchMessageLabel;


    @FXML private TableView<InvoiceModel> invoiceTable;
    @FXML private TableColumn<InvoiceModel, String> invoiceIdColumn;
    @FXML private TableColumn<InvoiceModel, String> invoiceEmployeeColumn;
    @FXML private TableColumn<InvoiceModel, String> invoicePaymentMethodColumn;
    @FXML private TableColumn<InvoiceModel, String> invoiceStatusColumn;
    @FXML private TableColumn<InvoiceModel, String> invoiceTimeColumn;
    @FXML private TableColumn<InvoiceModel, String> invoiceTotalColumn;


    @FXML private Label detailStatusValueLabel;
    @FXML private Label detailInvoiceIdValueLabel;
    @FXML private Label detailEmployeeValueLabel;
    @FXML private Label detailPaymentMethodValueLabel;
    @FXML private Label detailPaymentTimeValueLabel;
    @FXML private Label detailCreatedAtValueLabel;

    @FXML private Label detailTicketCountValueLabel;
    @FXML private Label detailTicketTotalValueLabel;
    @FXML private Label detailProductCountValueLabel;
    @FXML private Label detailProductTotalValueLabel;
    @FXML private Label detailGrandTotalValueLabel;


    @FXML private TableView<TicketDetailModel> ticketDetailTable;
    @FXML private TableColumn<TicketDetailModel, String> ticketIdColumn;
    @FXML private TableColumn<TicketDetailModel, String> ticketPriceColumn;

    @FXML private TableView<ProductDetailModel> productDetailTable;
    @FXML private TableColumn<ProductDetailModel, String> productIdColumn;
    @FXML private TableColumn<ProductDetailModel, Integer> productQuantityColumn;
    @FXML private TableColumn<ProductDetailModel, String> productUnitPriceColumn;
    @FXML private TableColumn<ProductDetailModel, String> productTotalColumn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupDummySummaryData();
        setupInvoiceTable();
        setupDetailTables();
        
        // Lắng nghe sự kiện click vào một hàng trên bảng hóa đơn chính
        invoiceTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> showInvoiceDetails(newValue)
        );
    }

    private void setupDummySummaryData() {
        totalInvoicesValueLabel.setText("142");
        successfulInvoicesValueLabel.setText("138");
        todayInvoicesValueLabel.setText("15");
        totalAmountValueLabel.setText("12.450.000 đ");
    }

    private void setupInvoiceTable() {
        // Ánh xạ cột với thuộc tính của Model
        invoiceIdColumn.setCellValueFactory(new PropertyValueFactory<>("invoiceId"));
        invoiceEmployeeColumn.setCellValueFactory(new PropertyValueFactory<>("employee"));
        invoicePaymentMethodColumn.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        invoiceStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        invoiceTimeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        invoiceTotalColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

        // Tạo dữ liệu mẫu cho danh sách hóa đơn
        ObservableList<InvoiceModel> data = FXCollections.observableArrayList(
            new InvoiceModel("HD-1001", "Nguyễn Minh Huy", "Tiền mặt", "Hoàn tất", "05/05/2026 18:30", "240.000 đ"),
            new InvoiceModel("HD-1002", "Lê Thị Lan", "Chuyển khoản", "Hoàn tất", "05/05/2026 19:15", "450.000 đ"),
            new InvoiceModel("HD-1003", "Trần Văn B", "Thẻ tín dụng", "Đã hủy", "05/05/2026 20:00", "0 đ")
        );

        invoiceTable.setItems(data);
    }

    private void setupDetailTables() {
        // Bảng chi tiết Vé
        ticketIdColumn.setCellValueFactory(new PropertyValueFactory<>("ticketId"));
        ticketPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Bảng chi tiết Sản phẩm
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
        productQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        productUnitPriceColumn.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        productTotalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
    }

    private void showInvoiceDetails(InvoiceModel invoice) {
        if (invoice != null) {
            // Cập nhật thông tin tổng quan
            detailStatusValueLabel.setText(invoice.getStatus());
            detailInvoiceIdValueLabel.setText(invoice.getInvoiceId());
            detailEmployeeValueLabel.setText(invoice.getEmployee());
            detailPaymentMethodValueLabel.setText(invoice.getPaymentMethod());
            detailPaymentTimeValueLabel.setText(invoice.getTime());
            detailCreatedAtValueLabel.setText(invoice.getTime());
            
            // Cập nhật giá trị mẫu
            detailTicketCountValueLabel.setText("2 vé");
            detailTicketTotalValueLabel.setText("180.000 đ");
            detailProductCountValueLabel.setText("1 món");
            detailProductTotalValueLabel.setText("60.000 đ");
            detailGrandTotalValueLabel.setText(invoice.getTotalAmount());

            // Nạp dữ liệu mẫu cho 2 bảng chi tiết khi click
            ObservableList<TicketDetailModel> tickets = FXCollections.observableArrayList(
                new TicketDetailModel("VE-9981 (Ghé F4)", "90.000 đ"),
                new TicketDetailModel("VE-9982 (Ghé F5)", "90.000 đ")
            );
            ticketDetailTable.setItems(tickets);

            ObservableList<ProductDetailModel> products = FXCollections.observableArrayList(
                new ProductDetailModel("Combo 1 Bắp 1 Nước", 1, "60.000 đ", "60.000 đ")
            );
            productDetailTable.setItems(products);
            
            searchMessageLabel.setText("Đang xem chi tiết mã: " + invoice.getInvoiceId());
        }
    }


    
    @FXML
    private void handleSearch() {
        String keyword = invoiceSearchField.getText();
        searchMessageLabel.setText("Đang tìm kiếm mã hóa đơn: " + keyword);
        // Logic lọc bảng ở đây...
    }

    @FXML
    private void handleResetSearch() {
        invoiceSearchField.clear();
        searchMessageLabel.setText("Đã xóa bộ lọc, hiển thị tất cả hóa đơn.");
        // Logic reset dữ liệu bảng...
    }

    @FXML
    private void handleRefreshInvoices() {
        searchMessageLabel.setText("Đang tải lại dữ liệu mới nhất...");
        setupDummySummaryData(); // Giả lập refresh
    }



    public static class InvoiceModel {
        private String invoiceId, employee, paymentMethod, status, time, totalAmount;

        public InvoiceModel(String id, String emp, String pay, String stat, String t, String total) {
            this.invoiceId = id; this.employee = emp; this.paymentMethod = pay; 
            this.status = stat; this.time = t; this.totalAmount = total;
        }

        public String getInvoiceId() { return invoiceId; }
        public String getEmployee() { return employee; }
        public String getPaymentMethod() { return paymentMethod; }
        public String getStatus() { return status; }
        public String getTime() { return time; }
        public String getTotalAmount() { return totalAmount; }
    }

    public static class TicketDetailModel {
        private String ticketId, price;

        public TicketDetailModel(String id, String price) {
            this.ticketId = id; this.price = price;
        }

        public String getTicketId() { return ticketId; }
        public String getPrice() { return price; }
    }

    public static class ProductDetailModel {
        private String productId, unitPrice, total;
        private int quantity;

        public ProductDetailModel(String id, int qty, String unit, String total) {
            this.productId = id; this.quantity = qty; this.unitPrice = unit; this.total = total;
        }

        public String getProductId() { return productId; }
        public int getQuantity() { return quantity; }
        public String getUnitPrice() { return unitPrice; }
        public String getTotal() { return total; }
    }
}