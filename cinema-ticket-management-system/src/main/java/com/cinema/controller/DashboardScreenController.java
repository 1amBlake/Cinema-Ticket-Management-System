package com.cinema.controller;

import java.io.IOException;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 * Controller của màn hình Dashboard chính.
 * <p>
 * DashboardScreen đóng vai trò là khung giao diện chính sau khi đăng nhập.
 * Bên trái là thanh menu điều hướng, bên trên là thanh tiêu đề,
 * còn vùng giữa {@code contentArea} dùng để load các màn hình con như
 * Tổng quan, Bán vé, Hóa đơn, Phim, Thống kê...
 * </p>
 *
 * @author Minh Huy
 */
public class DashboardScreenController {

    @FXML
    private StackPane contentArea;

    @FXML
    private Label pageTitleLabel;

    @FXML
    private Label pageDescriptionLabel;

    @FXML
    private Button overviewButton;

    @FXML
    private Button ticketSaleButton;

    @FXML
    private Button invoiceButton;

    @FXML
    private Button movieButton;

    @FXML
    private Button movieSessionButton;

    @FXML
    private Button theaterButton;

    @FXML
    private Button productButton;

    @FXML
    private Button employeeButton;

    @FXML
    private Button statisticsButton;

    /**
     * Khởi tạo dữ liệu và trạng thái ban đầu cho Dashboard.
     * <p>
     * Method này tự động được JavaFX gọi sau khi load xong file FXML.
     * Ở đây ta có thể chọn màn hình mặc định muốn hiển thị khi vừa vào Dashboard.
     * </p>
     * <p>
     * Nếu muốn vừa vào Dashboard là thấy màn Tổng quan, gọi {@link #handleOverview()}.
     * Nếu muốn giữ màn chào mừng ban đầu, không cần gọi gì thêm.
     * </p>
     */
    @FXML
    private void initialize() {
        /*
         * Cách 1: Vừa vào Dashboard là hiện màn Tổng quan.
         * Bỏ comment dòng dưới nếu bạn muốn load OverviewScreen.fxml ngay từ đầu.
         */
        // handleOverview();

        /*
         * Cách 2: Vừa vào Dashboard chỉ hiện màn chào mừng.
         * Khi người dùng bấm nút "Tổng quan" thì mới load OverviewScreen.fxml.
         * Hiện tại mình để theo cách 2.
         */
    }

    /**
     * Xử lý sự kiện khi người dùng bấm nút Tổng quan trên sidebar.
     * <p>
     * Method này sẽ:
     * </p>
     * <ul>
     *     <li>Load file {@code OverviewScreen.fxml} vào vùng giữa Dashboard.</li>
     *     <li>Cập nhật tiêu đề top bar thành "Tổng quan".</li>
     *     <li>Cập nhật mô tả ngắn của màn hình.</li>
     *     <li>Đánh dấu nút Tổng quan là nút đang được chọn.</li>
     * </ul>
     */
    @FXML
    private void handleOverview() {
        loadScreen("OverviewScreen.fxml");
        updatePageHeader("Tổng quan", "Theo dõi nhanh hoạt động bán vé trong ngày");
        setActiveButton(overviewButton);
    }

    /**
     * Xử lý sự kiện khi người dùng bấm nút Bán vé.
     * <p>
     * Hiện tại màn {@code TicketSaleScreen.fxml} có thể chưa tồn tại,
     * nên khi bạn tạo file đó thì method này sẽ dùng để load màn bán vé vào Dashboard.
     * </p>
     */
    @FXML
    private void handleTicketSale() {
        loadScreen("TicketSaleScreen.fxml");
        updatePageHeader("Bán vé", "Chọn phim, suất chiếu, ghế và lập hóa đơn bán vé");
        setActiveButton(ticketSaleButton);
    }

    /**
     * Xử lý sự kiện khi người dùng bấm nút Hóa đơn.
     * <p>
     * Màn này dùng để xem danh sách hóa đơn, xem chi tiết hóa đơn
     * và hỗ trợ xuất hoặc in hóa đơn nếu có làm thêm.
     * </p>
     */
    @FXML
    private void handleInvoice() {
        loadScreen("InvoiceScreen.fxml");
        updatePageHeader("Hóa đơn", "Tra cứu, xem chi tiết và quản lý hóa đơn bán vé");
        setActiveButton(invoiceButton);
    }

    /**
     * Xử lý sự kiện khi người dùng bấm nút Phim.
     * <p>
     * Màn này nên gom các chức năng quản lý phim, đạo diễn,
     * thể loại, gán đạo diễn và gán thể loại cho phim.
     * </p>
     */
    @FXML
    private void handleMovieManagement() {
        loadScreen("MovieManagementScreen.fxml");
        updatePageHeader("Quản lý phim", "Quản lý phim, đạo diễn, thể loại và liên kết dữ liệu phim");
        setActiveButton(movieButton);
    }

    /**
     * Xử lý sự kiện khi người dùng bấm nút Suất chiếu.
     * <p>
     * Màn này dùng để quản lý suất chiếu và giá vé theo từng suất,
     * từng loại ghế hoặc từng định dạng phim.
     * </p>
     */
    @FXML
    private void handleMovieSession() {
        loadScreen("MovieSessionScreen.fxml");
        updatePageHeader("Suất chiếu", "Quản lý lịch chiếu, phòng chiếu và giá vé");
        setActiveButton(movieSessionButton);
    }

    /**
     * Xử lý sự kiện khi người dùng bấm nút Phòng & ghế.
     * <p>
     * Màn này nên gom các chức năng quản lý rạp, phòng chiếu,
     * loại phòng, ghế và loại ghế.
     * </p>
     */
    @FXML
    private void handleTheaterManagement() {
        loadScreen("TheaterManagementScreen.fxml");
        updatePageHeader("Phòng & ghế", "Quản lý rạp, phòng chiếu, sơ đồ ghế và loại ghế");
        setActiveButton(theaterButton);
    }

    /**
     * Xử lý sự kiện khi người dùng bấm nút Sản phẩm.
     * <p>
     * Màn này dùng để quản lý sản phẩm bán kèm như nước uống,
     * bắp rang, combo và loại sản phẩm.
     * </p>
     */
    @FXML
    private void handleProductManagement() {
        loadScreen("ProductManagementScreen.fxml");
        updatePageHeader("Sản phẩm", "Quản lý sản phẩm bán kèm và loại sản phẩm");
        setActiveButton(productButton);
    }

    /**
     * Xử lý sự kiện khi người dùng bấm nút Nhân viên.
     * <p>
     * Màn này nên gom các chức năng quản lý nhân viên,
     * tài khoản nhân viên và chức vụ/quyền của nhân viên.
     * </p>
     */
    @FXML
    private void handleEmployeeManagement() {
        loadScreen("EmployeeManagementScreen.fxml");
        updatePageHeader("Nhân viên", "Quản lý nhân viên, tài khoản và phân quyền");
        setActiveButton(employeeButton);
    }

    /**
     * Xử lý sự kiện khi người dùng bấm nút Thống kê.
     * <p>
     * Màn này dùng để xem báo cáo doanh thu, số vé bán,
     * top phim bán chạy và các số liệu thống kê theo khoảng thời gian.
     * </p>
     */
    @FXML
    private void handleStatistics() {
        loadScreen("StatisticsScreen.fxml");
        updatePageHeader("Thống kê", "Xem báo cáo doanh thu, vé bán và hiệu quả kinh doanh");
        setActiveButton(statisticsButton);
    }

    /**
     * Load một màn hình con vào vùng nội dung chính của Dashboard.
     * <p>
     * File FXML con sẽ được tìm trong thư mục {@code src/main/resources/fxml}.
     * Sau khi load thành công, toàn bộ nội dung cũ trong {@code contentArea}
     * sẽ bị thay bằng màn hình mới.
     * </p>
     *
     * @param fxmlFileName tên file FXML cần load, ví dụ {@code OverviewScreen.fxml}
     */
    private void loadScreen(String fxmlFileName) {
        try {
            Parent screen = FXMLLoader.load(getClass().getResource("/fxml/" + fxmlFileName));
            contentArea.getChildren().setAll(screen);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cập nhật tiêu đề và mô tả ngắn trên top bar.
     * <p>
     * Method này giúp top bar thay đổi theo màn hình đang được chọn.
     * Ví dụ khi vào Tổng quan thì title là "Tổng quan",
     * khi vào Bán vé thì title là "Bán vé".
     * </p>
     *
     * @param title tiêu đề màn hình
     * @param description mô tả ngắn của màn hình
     */
    private void updatePageHeader(String title, String description) {
        pageTitleLabel.setText(title);
        pageDescriptionLabel.setText(description);
    }

    /**
     * Đánh dấu nút menu đang được chọn trên sidebar.
     * <p>
     * Method này sẽ xóa style active khỏi toàn bộ nút menu,
     * sau đó gán style active cho nút vừa được chọn.
     * Nhờ vậy sidebar luôn thể hiện đúng màn hình hiện tại.
     * </p>
     *
     * @param activeButton nút menu đang được chọn
     */
    private void setActiveButton(Button activeButton) {
        List<Button> menuButtons = List.of(
                overviewButton,
                ticketSaleButton,
                invoiceButton,
                movieButton,
                movieSessionButton,
                theaterButton,
                productButton,
                employeeButton,
                statisticsButton
        );

        for (Button button : menuButtons) {
            button.getStyleClass().remove("sidebar-active-button");

            if (!button.getStyleClass().contains("sidebar-button")) {
                button.getStyleClass().add("sidebar-button");
            }
        }

        activeButton.getStyleClass().remove("sidebar-button");

        if (!activeButton.getStyleClass().contains("sidebar-active-button")) {
            activeButton.getStyleClass().add("sidebar-active-button");
        }
    }
}