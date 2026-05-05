package com.cinema.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import java.util.*;

public class TicketSaleScreenController {

    // --- Pane điều hướng các bước ---
    @FXML private VBox step1Pane, step2Pane;
    @FXML private HBox step3Pane;
    @FXML private VBox step1Indicator, step2Indicator, step3Indicator;

    // --- Các vùng chứa dữ liệu ---
    @FXML private FlowPane movieFlowPane, sessionFlowPane, productFlowPane;
    @FXML private GridPane seatGrid;
    @FXML private VBox receiptItemsVBox;

    // --- Các nút và điều khiển ---
    @FXML private Button btnNextStep1, btnNextStep2, btnCheckout;
    @FXML private ComboBox<String> paymentMethodBox, theaterFilter;
    @FXML private DatePicker dateFilter;

    // --- Nhãn hiển thị trên hóa đơn ---
    @FXML private Label lblReceiptDate, lblReceiptEmployee, lblReceiptMovie, 
                        lblReceiptSession, lblReceiptSeats, lblReceiptTotal;

    // --- Biến trạng thái tạm thời ---
    private String selectedMovie = "";
    private String selectedSession = "";
    private Set<String> selectedSeats = new TreeSet<>();
    private double totalAmount = 0;

    @FXML
    public void initialize() {
        // Cấu hình ban đầu
        showStep(1);
        loadDummyMovies();
        loadDummyProducts();
        
        // Khởi tạo ComboBox
        paymentMethodBox.getItems().addAll("Tiền mặt", "Thẻ tín dụng", "Ví điện tử");
        theaterFilter.getItems().addAll("Rạp 01", "Rạp 02", "Rạp IMAX");
        
        // Lắng nghe để bật nút thanh toán
        paymentMethodBox.valueProperty().addListener((obs, oldVal, newVal) -> checkCheckoutEnable());
    }

   

    private void showStep(int step) {
        // Ẩn/Hiện các pane tương ứng
        step1Pane.setVisible(step == 1);
        step2Pane.setVisible(step == 2);
        step3Pane.setVisible(step == 3);

        
        updateStepIndicator(step);
    }

    private void updateStepIndicator(int activeStep) {
        step1Indicator.getStyleClass().remove("step-active");
        step2Indicator.getStyleClass().remove("step-active");
        step3Indicator.getStyleClass().remove("step-active");

        if (activeStep >= 1) step1Indicator.getStyleClass().add("step-active");
        if (activeStep >= 2) step2Indicator.getStyleClass().add("step-active");
        if (activeStep >= 3) step3Indicator.getStyleClass().add("step-active");
    }

    @FXML
    private void handleNextToStep2() {
        loadDummySessions();
        showStep(2);
    }

    @FXML
    private void handleNextToStep3() {
        loadDummySeats();
        updateReceipt();
        showStep(3);
    }

    @FXML private void handleBackToStep1() { showStep(1); }
    @FXML private void handleBackToStep2() { showStep(2); }

  

    private void loadDummyMovies() {
        movieFlowPane.getChildren().clear();
        String[] movies = {"Lật Mặt 7", "Doraemon", "Haikyuu!!", "Hành Tinh Khỉ"};
        
        for (String movie : movies) {
            VBox card = new VBox();
            card.getStyleClass().add("panel-card"); // Giả sử dùng style chung
            card.setSpacing(10);
            card.setAlignment(Pos.CENTER);
            card.setPrefSize(180, 250);
            card.setStyle("-fx-border-color: #ddd; -fx-cursor: hand; -fx-background-color: white;");
            
            card.getChildren().add(new Label(movie));
            card.setOnMouseClicked(e -> {
                selectedMovie = movie;
                btnNextStep1.setDisable(false); // Bật nút khi đã chọn phim
                movieFlowPane.getChildren().forEach(n -> n.setStyle("-fx-border-color: #ddd; -fx-background-color: white;"));
                card.setStyle("-fx-border-color: #E50914; -fx-background-color: #fce8e8;");
            });
            movieFlowPane.getChildren().add(card);
        }
    }

    private void loadDummySessions() {
        sessionFlowPane.getChildren().clear();
        String[] times = {"09:00", "12:30", "15:45", "19:00", "22:15"};
        
        for (String time : times) {
            Button btn = new Button(time);
            btn.setPrefSize(100, 40);
            btn.setOnAction(e -> {
                selectedSession = time;
                btnNextStep2.setDisable(false); // Bật nút khi đã chọn suất[cite: 13]
            });
            sessionFlowPane.getChildren().add(btn);
        }
    }

    private void loadDummySeats() {
        seatGrid.getChildren().clear();
        selectedSeats.clear();
        
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 10; col++) {
                String seatCode = (char)('A' + row) + String.valueOf(col + 1);
                ToggleButton seatBtn = new ToggleButton(seatCode);
                seatBtn.setPrefSize(45, 45);
                
                seatBtn.setOnAction(e -> {
                    if (seatBtn.isSelected()) selectedSeats.add(seatCode);
                    else selectedSeats.remove(seatCode);
                    updateReceipt();
                    checkCheckoutEnable();
                });
                seatGrid.add(seatBtn, col, row);
            }
        }
    }

    private void loadDummyProducts() {
        productFlowPane.getChildren().clear();
        String[] products = {"Bắp rang bơ", "Coca Cola", "Combo 1", "Combo 2"};
        
        for (String p : products) {
            VBox card = new VBox(new Label(p), new Button("Thêm"));
            card.getStyleClass().add("panel-card");
            card.setPadding(new javafx.geometry.Insets(10));
            productFlowPane.getChildren().add(card);
        }
    }



    private void updateReceipt() {
        lblReceiptDate.setText("05/05/2026");
        lblReceiptEmployee.setText("Nhân viên bán hàng");
        lblReceiptMovie.setText("Phim: " + selectedMovie);
        lblReceiptSession.setText("Suất: " + selectedSession);
        lblReceiptSeats.setText("Ghế: " + String.join(", ", selectedSeats));
        
        totalAmount = selectedSeats.size() * 90000; // Giả định 90k/vé
        lblReceiptTotal.setText(String.format("%,.0f VNĐ", totalAmount));
    }

    private void checkCheckoutEnable() {
        // Nút thanh toán chỉ bật khi có ghế được chọn và đã chọn phương thức[cite: 13]
        boolean canPay = !selectedSeats.isEmpty() && paymentMethodBox.getValue() != null;
        btnCheckout.setDisable(!canPay);
    }

    @FXML
    private void handlePayment() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thanh toán");
        alert.setHeaderText(null);
        alert.setContentText("Giao dịch thành công!\nTổng tiền: " + lblReceiptTotal.getText());
        alert.showAndWait();
        
        // Reset về bước 1 sau khi xong
        selectedMovie = "";
        selectedSession = "";
        selectedSeats.clear();
        btnNextStep1.setDisable(true);
        btnNextStep2.setDisable(true);
        showStep(1);
    }
}

/*
package com.cinema.controller;

import com.cinema.entity.*;
import com.cinema.service.*;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TicketSaleScreenController {

    // --- UI ---
    @FXML private VBox step1Pane, step2Pane;
    @FXML private HBox step3Pane;
    @FXML private FlowPane movieFlowPane, sessionFlowPane, productFlowPane;
    @FXML private GridPane seatGrid;

    @FXML private Label lblReceiptDate, lblReceiptMovie, lblReceiptSession, lblReceiptSeats, lblReceiptTotal;
    @FXML private VBox receiptItemsVBox;

    // --- SERVICE ---
    private final MovieService movieService = new MovieService();
    private final MovieSessionService sessionService = new MovieSessionService();
    private final SeatService seatService = new SeatService();
    private final ProductService productService = new ProductService();
    private final TicketSaleService ticketSaleService = new TicketSaleService();

    // --- STATE ---
    private Movie selectedMovie;
    private MovieSession selectedSession;
    private List<Seat> selectedSeats = new ArrayList<>();
    private Map<Product, Integer> cart = new LinkedHashMap<>();

    @FXML
    public void initialize() {
        showStep(1);
        loadMovies();
    }

    // ================= STEP =================
    private void showStep(int step) {
        step1Pane.setVisible(step == 1);
        step2Pane.setVisible(step == 2);
        step3Pane.setVisible(step == 3);
    }

    // ================= MOVIE =================
    private void loadMovies() {
        movieFlowPane.getChildren().clear();

        List<Movie> movies = movieService.findNowShowing();

        for (Movie m : movies) {
            VBox card = createMovieCard(m);
            movieFlowPane.getChildren().add(card);
        }
    }

    private VBox createMovieCard(Movie movie) {
        Label name = new Label(movie.getTitle());

        VBox box = new VBox(name);
        box.getStyleClass().add("movie-card");

        box.setOnMouseClicked(e -> {
            selectedMovie = movie;
            loadSessions(movie);
        });

        return box;
    }

    // ================= SESSION =================
    private void loadSessions(Movie movie) {
        sessionFlowPane.getChildren().clear();

        List<MovieSession> sessions = sessionService.findByMovie(movie.getId());

        for (MovieSession s : sessions) {
            Button btn = new Button(s.getStartTime().toString());

            btn.setOnAction(e -> {
                selectedSession = s;
            });

            sessionFlowPane.getChildren().add(btn);
        }

        showStep(2);
    }

    // ================= SEAT =================
    private void loadSeats(MovieSession session) {
        seatGrid.getChildren().clear();
        selectedSeats.clear();

        List<Seat> seats = seatService.findByScreen(session.getScreenId());
        List<Seat> occupied = ticketSaleService.getOccupiedSeats(session.getId());

        for (Seat seat : seats) {
            Button b = new Button(seat.getCode());

            if (occupied.contains(seat)) {
                b.setDisable(true);
                b.setStyle("-fx-background-color: red;");
            }

            b.setOnAction(e -> toggleSeat(seat, b));

            seatGrid.add(b, seat.getColumnIndex(), seat.getRowIndex());
        }
    }

    private void toggleSeat(Seat seat, Button b) {
        if (selectedSeats.contains(seat)) {
            selectedSeats.remove(seat);
            b.setStyle("");
        } else {
            selectedSeats.add(seat);
            b.setStyle("-fx-background-color: yellow;");
        }
        updateInvoice();
    }

    // ================= PRODUCT =================
    private void loadProducts() {
        productFlowPane.getChildren().clear();

        List<Product> products = productService.findAllAvailable();

        for (Product p : products) {
            VBox card = new VBox();

            Label name = new Label(p.getName());
            Label price = new Label(p.getPrice() + " đ");

            Button add = new Button("+");
            add.setOnAction(e -> {
                cart.put(p, cart.getOrDefault(p, 0) + 1);
                updateInvoice();
            });

            card.getChildren().addAll(name, price, add);
            productFlowPane.getChildren().add(card);
        }
    }

    // ================= NAV =================
    @FXML
    private void handleNextToStep3() {
        if (selectedSession == null) {
            showAlert("Lỗi", "Chọn suất chiếu!");
            return;
        }

        loadSeats(selectedSession);
        loadProducts();
        showStep(3);
    }

    // ================= INVOICE =================
    private void updateInvoice() {
        lblReceiptDate.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

        lblReceiptMovie.setText(selectedMovie != null ? selectedMovie.getTitle() : "--");
        lblReceiptSession.setText(selectedSession != null ? selectedSession.getStartTime().toString() : "--");

        lblReceiptSeats.setText(selectedSeats.stream().map(Seat::getCode).reduce("", (a, b) -> a + " " + b));

        receiptItemsVBox.getChildren().clear();

        double total = 0;

        if (!selectedSeats.isEmpty()) {
            double price = ticketSaleService.calculateSeatPrice(selectedSession, selectedSeats);
            total += price;
            receiptItemsVBox.getChildren().add(createItem("Vé", price));
        }

        for (Product p : cart.keySet()) {
            int qty = cart.get(p);
            double price = qty * p.getPrice();
            total += price;

            receiptItemsVBox.getChildren().add(createItem(p.getName() + " x" + qty, price));
        }

        lblReceiptTotal.setText(String.format("%,.0f VNĐ", total));
    }

    private HBox createItem(String name, double price) {
        HBox row = new HBox();

        Label n = new Label(name);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label p = new Label(String.format("%,.0f", price));

        row.getChildren().addAll(n, spacer, p);
        return row;
    }

    // ================= PAYMENT =================
    @FXML
    private void handlePayment() {
        if (selectedSeats.isEmpty()) {
            showAlert("Lỗi", "Chọn ghế!");
            return;
        }

        try {
            ticketSaleService.processSale(
                    selectedSession,
                    selectedSeats,
                    cart
            );

            showAlert("Thành công", "Thanh toán OK!");
            reset();

        } catch (Exception e) {
            showAlert("Lỗi", e.getMessage());
        }
    }

    private void reset() {
        selectedMovie = null;
        selectedSession = null;
        selectedSeats.clear();
        cart.clear();
        showStep(1);
        loadMovies();
    }

    private void showAlert(String t, String c) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(t);
        a.setContentText(c);
        a.show();
    }
} */