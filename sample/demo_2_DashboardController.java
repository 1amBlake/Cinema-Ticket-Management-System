package com.cinema.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class demo_2_DashboardController {

    @FXML
    private StackPane contentPane;

    @FXML
    private Node overviewContent;

    @FXML
    public void initialize() {
        loadOverviewData();
        showOverviewScreen();
    }

    private void loadOverviewData() {
        // gọi StatisticsService sau này
    }

    @FXML
    private void showOverviewScreen() {
        contentPane.getChildren().setAll(overviewContent);
    }

    @FXML
    private void showTicketSaleScreen() {
        loadPage("/fxml/SeatSelectionView.fxml");
    }

    private void loadPage(String fxmlPath) {
        try {
            Parent page = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentPane.getChildren().setAll(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void logout() {
        // quay lại LoginView sau này
        System.out.println("Logout clicked");
    }
}
