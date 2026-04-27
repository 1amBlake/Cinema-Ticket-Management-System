package com.cinema.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoadingScreenController {

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label loadingLabel;

    private int progressValue = 0;

    @FXML
    private void initialize() {
        progressBar.setProgress(0);

        Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(50), event -> {
                progressValue++;

                double progress = progressValue / 100.0;
                progressBar.setProgress(progress);

                if (progressValue < 35) {
                    loadingLabel.setText("Đang khởi động hệ thống...");
                } else if (progressValue < 70) {
                    loadingLabel.setText("Đang tải giao diện...");
                } else if (progressValue < 100) {
                    loadingLabel.setText("Chuẩn bị hoàn tất...");
                } else {
                    loadingLabel.setText("Hoàn tất!");
                }
            })
        );

        timeline.setCycleCount(100);

        timeline.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Parent loginRoot = FXMLLoader.load(
                        getClass().getResource("/fxml/logindemo.fxml")
                    );

                    Scene loginScene = new Scene(loginRoot, 900, 550);

                    Stage stage = (Stage) progressBar.getScene().getWindow();
                    stage.setScene(loginScene);
                    stage.centerOnScreen();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        timeline.play();
    }
}