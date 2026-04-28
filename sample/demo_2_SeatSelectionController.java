package com.cinema.controller;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

public class demo_2_SeatSelectionController {

    @FXML
    private GridPane seatGrid;

    @FXML
    private FlowPane selectedSeatPane;

    @FXML
    private Label totalSeatLabel;

    @FXML
    private Label totalPriceLabel;

    private final List<String> selectedSeats = new ArrayList<>();

    private static final int ROW_COUNT = 8;
    private static final int COLUMN_COUNT = 12;
    private static final double TICKET_PRICE = 50000;

    @FXML
    public void initialize() {
        renderSeatMap();
        updateSummary();
    }

    private void renderSeatMap() {
        seatGrid.getChildren().clear();

        for (int row = 0; row < ROW_COUNT; row++) {
            String rowName = String.valueOf((char) ('A' + row));

            Label rowLabelLeft = new Label(rowName);
            rowLabelLeft.getStyleClass().add("row-label");
            seatGrid.add(rowLabelLeft, 0, row);

            for (int col = 1; col <= COLUMN_COUNT; col++) {
                String seatCode = rowName + col;

                Button seatButton = new Button(seatCode);
                seatButton.getStyleClass().add("seat-button");

                SeatDemoStatus status = getDemoSeatStatus(row, col);

                applySeatStyle(seatButton, status);

                if (status == SeatDemoStatus.AVAILABLE) {
                    seatButton.setOnAction(event -> handleSelectSeat(seatButton, seatCode));
                } else {
                    seatButton.setDisable(true);
                }

                seatGrid.add(seatButton, col, row);
            }

            Label rowLabelRight = new Label(rowName);
            rowLabelRight.getStyleClass().add("row-label");
            seatGrid.add(rowLabelRight, COLUMN_COUNT + 1, row);
        }
    }

    private SeatDemoStatus getDemoSeatStatus(int row, int col) {
        // Demo ghế đã bán
        if ((row == 1 && col >= 3 && col <= 5)
                || (row == 3 && col >= 7 && col <= 9)
                || (row == 5 && col == 2)
                || (row == 6 && col >= 10 && col <= 12)) {
            return SeatDemoStatus.SOLD;
        }

        // Demo ghế bảo trì
        if ((row == 2 && col == 11)
                || (row == 4 && col == 4)
                || (row == 7 && col == 6)) {
            return SeatDemoStatus.MAINTENANCE;
        }

        return SeatDemoStatus.AVAILABLE;
    }

    private void handleSelectSeat(Button seatButton, String seatCode) {
        if (selectedSeats.contains(seatCode)) {
            selectedSeats.remove(seatCode);
            seatButton.getStyleClass().remove("selected-seat");
            seatButton.getStyleClass().add("available-seat");
        } else {
            selectedSeats.add(seatCode);
            seatButton.getStyleClass().remove("available-seat");
            seatButton.getStyleClass().add("selected-seat");
        }

        updateSummary();
    }

    private void applySeatStyle(Button seatButton, SeatDemoStatus status) {
        switch (status) {
            case AVAILABLE:
                seatButton.getStyleClass().add("available-seat");
                break;
            case SOLD:
                seatButton.getStyleClass().add("sold-seat");
                break;
            case MAINTENANCE:
                seatButton.getStyleClass().add("maintenance-seat");
                break;
            default:
                break;
        }
    }

    private void updateSummary() {
        selectedSeatPane.getChildren().clear();

        for (String seatCode : selectedSeats) {
            Label label = new Label(seatCode);
            label.getStyleClass().add("selected-seat-tag");
            selectedSeatPane.getChildren().add(label);
        }

        totalSeatLabel.setText("Số lượng: " + selectedSeats.size());

        double totalPrice = selectedSeats.size() * TICKET_PRICE;
        totalPriceLabel.setText("Tổng tiền: " + formatCurrency(totalPrice));
    }

    @FXML
    private void handleRefresh() {
        selectedSeats.clear();
        renderSeatMap();
        updateSummary();
    }

    private String formatCurrency(double amount) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        return formatter.format(amount) + "đ";
    }

    private enum SeatDemoStatus {
        AVAILABLE,
        SOLD,
        MAINTENANCE
    }
}