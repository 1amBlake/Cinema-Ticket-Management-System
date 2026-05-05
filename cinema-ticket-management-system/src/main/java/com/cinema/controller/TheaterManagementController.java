package com.cinema.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class TheaterManagementController implements Initializable {


    @FXML private TableView<ScreenModel> tableScreens;
    @FXML private TableColumn<ScreenModel, Integer> colRoomId;
    @FXML private TableColumn<ScreenModel, String> colRoomName;
    @FXML private TableColumn<ScreenModel, String> colRoomType;
    @FXML private TableColumn<ScreenModel, String> colRoomStatus;

    @FXML private TextField txtRoomName;
    @FXML private ComboBox<String> cbRoomType;
    @FXML private ComboBox<String> cbRoomStatus;


    @FXML private ComboBox<String> cbSelectRoomForSeats;
    @FXML private TextField txtNumRows;
    @FXML private TextField txtNumCols;
    @FXML private GridPane gridSeatEditor;
    @FXML private StackPane containerSeatMap;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupRoomTable();
        setupComboBoxes();
        loadDummyRooms();
    }

    private void setupRoomTable() {
        colRoomId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colRoomName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colRoomType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colRoomStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Lắng nghe sự kiện chọn dòng để điền thông tin vào form
        tableScreens.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtRoomName.setText(newSelection.getName());
                cbRoomType.setValue(newSelection.getType());
                cbRoomStatus.setValue(newSelection.getStatus());
            }
        });
    }

    private void setupComboBoxes() {
        cbRoomType.setItems(FXCollections.observableArrayList("2D", "3D", "IMAX", "4DX"));
        cbRoomStatus.setItems(FXCollections.observableArrayList("Đang hoạt động", "Bảo trì", "Ngừng sử dụng"));
        
        // Dữ liệu cho Tab sơ đồ ghế
        cbSelectRoomForSeats.setItems(FXCollections.observableArrayList("Phòng 01", "Phòng 02", "Phòng IMAX"));
    }

    private void loadDummyRooms() {
        ObservableList<ScreenModel> rooms = FXCollections.observableArrayList(
                new ScreenModel(1, "Phòng 01", "2D", "Đang hoạt động"),
                new ScreenModel(2, "Phòng 02", "3D", "Đang hoạt động"),
                new ScreenModel(3, "Phòng IMAX", "IMAX", "Bảo trì")
        );
        tableScreens.setItems(rooms);
    }

    // --- CÁC SỰ KIỆN TAB 1 ---

    @FXML
    private void handleSaveRoom() {
        System.out.println("Đang lưu phòng: " + txtRoomName.getText());
        // Thêm logic cập nhật vào danh sách hoặc DB tại đây
    }

    @FXML
    private void handleDeleteRoom() {
        ScreenModel selected = tableScreens.getSelectionModel().getSelectedItem();
        if (selected != null) {
            tableScreens.getItems().remove(selected);
            System.out.println("Đã xóa phòng ID: " + selected.getId());
        }
    }

    // --- CÁC SỰ KIỆN TAB 2 (SƠ ĐỒ GHẾ) ---

    @FXML
    private void handleBatchCreateSeats() {
        gridSeatEditor.getChildren().clear(); // Xóa sơ đồ cũ
        try {
            int rows = Integer.parseInt(txtNumRows.getText());
            int cols = Integer.parseInt(txtNumCols.getText());

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    String seatName = (char) ('A' + r) + String.valueOf(c + 1);
                    ToggleButton seatBtn = new ToggleButton(seatName);
                    seatBtn.setPrefSize(50, 50);
                    seatBtn.getStyleClass().add("seat-normal"); // Style mặc định
                    
                    // Logic đổi màu/loại ghế khi click
                    seatBtn.setOnAction(e -> {
                        if (seatBtn.isSelected()) {
                            seatBtn.getStyleClass().removeAll("seat-normal");
                            seatBtn.getStyleClass().add("seat-vip");
                        } else {
                            seatBtn.getStyleClass().removeAll("seat-vip");
                            seatBtn.getStyleClass().add("seat-normal");
                        }
                    });

                    gridSeatEditor.add(seatBtn, c, r);
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("Vui lòng nhập số hàng và cột hợp lệ.");
        }
    }

    @FXML
    private void handleSaveSeatLayout() {
        String room = cbSelectRoomForSeats.getValue();
        if (room != null) {
            System.out.println("Đã lưu sơ đồ ghế cho " + room);
        } else {
            System.err.println("Chưa chọn phòng để lưu!");
        }
    }

  
    public static class ScreenModel {
        private int id;
        private String name, type, status;

        public ScreenModel(int id, String name, String type, String status) {
            this.id = id; this.name = name; this.type = type; this.status = status;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getType() { return type; }
        public String getStatus() { return status; }
    }
}