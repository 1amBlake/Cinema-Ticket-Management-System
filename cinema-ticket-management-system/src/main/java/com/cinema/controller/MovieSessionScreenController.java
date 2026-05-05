package com.cinema.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class MovieSessionScreenController implements Initializable {

    // --- CÁC THÀNH PHẦN BỘ LỌC (FILTER) ---
    @FXML private ComboBox<String> filterMovie;
    @FXML private ComboBox<String> filterRoom;
    @FXML private DatePicker filterDate;
    @FXML private Button btnRefresh;

    // --- BẢNG DANH SÁCH SUẤT CHIẾU ---
    @FXML private TableView<MovieSessionModel> tableSessions;
    @FXML private TableColumn<MovieSessionModel, Integer> colId;
    @FXML private TableColumn<MovieSessionModel, String> colMovie;
    @FXML private TableColumn<MovieSessionModel, String> colRoom;
    @FXML private TableColumn<MovieSessionModel, String> colStartTime;
    @FXML private TableColumn<MovieSessionModel, String> colEndTime;
    @FXML private TableColumn<MovieSessionModel, String> colFormat;
    @FXML private TableColumn<MovieSessionModel, String> colStatus;

    // --- FORM NHẬP THÔNG TIN CHI TIẾT ---
    @FXML private ComboBox<String> cbMovie;
    @FXML private ComboBox<String> cbRoom;
    @FXML private DatePicker dpDate;
    @FXML private TextField txtTime;
    @FXML private ComboBox<String> cbFormat;
    @FXML private ComboBox<String> cbStatus;
    @FXML private Button btnSave;
    @FXML private Button btnDelete;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTableColumns();
        loadDummyData();
        setupFormOptions();

        // Lắng nghe sự kiện chọn dòng trong bảng để hiển thị lên form
        tableSessions.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) showDetail(newVal);
        });
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMovie.setCellValueFactory(new PropertyValueFactory<>("movie"));
        colRoom.setCellValueFactory(new PropertyValueFactory<>("room"));
        colStartTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        colEndTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        colFormat.setCellValueFactory(new PropertyValueFactory<>("format"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void setupFormOptions() {
        // Nạp dữ liệu cho các ComboBox lựa chọn
        ObservableList<String> movies = FXCollections.observableArrayList("Lật Mặt 7", "Doraemon", "Haikyuu!!");
        filterMovie.setItems(movies);
        cbMovie.setItems(movies);

        ObservableList<String> rooms = FXCollections.observableArrayList("Phòng 01", "Phòng 02", "IMAX");
        filterRoom.setItems(rooms);
        cbRoom.setItems(rooms);

        cbFormat.setItems(FXCollections.observableArrayList("2D", "3D", "4DX"));
        cbStatus.setItems(FXCollections.observableArrayList("Sắp chiếu", "Đang chiếu", "Kết thúc"));
    }

    private void loadDummyData() {
        ObservableList<MovieSessionModel> data = FXCollections.observableArrayList(
            new MovieSessionModel(1, "Lật Mặt 7", "Phòng 01", "19:30", "21:45", "2D", "Đang bán"),
            new MovieSessionModel(2, "Doraemon", "Phòng 02", "14:00", "15:45", "2D", "Sắp chiếu"),
            new MovieSessionModel(3, "Haikyuu!!", "IMAX", "20:00", "21:30", "3D", "Hết chỗ")
        );
        tableSessions.setItems(data);
    }

    private void showDetail(MovieSessionModel session) {
        cbMovie.setValue(session.getMovie());
        cbRoom.setValue(session.getRoom());
        txtTime.setText(session.getStartTime());
        cbFormat.setValue(session.getFormat());
        cbStatus.setValue(session.getStatus());
        dpDate.setValue(LocalDate.now()); // Giả định ngày hiện tại
    }

    // --- XỬ LÝ SỰ KIỆN (EVENT HANDLERS) ---

    @FXML
    private void handleSearch() {
        System.out.println("Đang tìm kiếm suất chiếu...");
    }

    @FXML
    private void handleRefresh() {
        loadDummyData();
        handleClear();
        System.out.println("Đã làm mới danh sách.");
    }

    @FXML
    private void handleSave() {
        System.out.println("Đã lưu thông tin suất chiếu: " + cbMovie.getValue());
    }

    @FXML
    private void handleDelete() {
        MovieSessionModel selected = tableSessions.getSelectionModel().getSelectedItem();
        if (selected != null) {
            tableSessions.getItems().remove(selected);
            handleClear();
        }
    }

    @FXML
    private void handleClear() {
        cbMovie.setValue(null);
        cbRoom.setValue(null);
        dpDate.setValue(null);
        txtTime.clear();
        cbFormat.setValue(null);
        cbStatus.setValue(null);
        tableSessions.getSelectionModel().clearSelection();
    }

    // --- LỚP MODEL TẠM THỜI ---
    public static class MovieSessionModel {
        private int id;
        private String movie, room, startTime, endTime, format, status;

        public MovieSessionModel(int id, String movie, String room, String start, String end, String format, String status) {
            this.id = id; this.movie = movie; this.room = room;
            this.startTime = start; this.endTime = end; this.format = format; this.status = status;
        }

        public int getId() { return id; }
        public String getMovie() { return movie; }
        public String getRoom() { return room; }
        public String getStartTime() { return startTime; }
        public String getEndTime() { return endTime; }
        public String getFormat() { return format; }
        public String getStatus() { return status; }
    }
}