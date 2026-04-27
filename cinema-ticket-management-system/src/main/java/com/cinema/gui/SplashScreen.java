package com.cinema.gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class SplashScreen {

    private static final int TOTAL_SECONDS = 5;

    public void show(Stage stage) {
        Label titleLabel = new Label("CINEMA TICKET MANAGEMENT");
        titleLabel.setFont(Font.font("Arial", 26));
        titleLabel.setTextFill(Color.WHITE);

        Label subtitleLabel = new Label("Đang khởi động hệ thống...");
        subtitleLabel.setFont(Font.font("Arial", 14));
        subtitleLabel.setTextFill(Color.WHITE);

        ProgressBar progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(320);

        Label percentLabel = new Label("0%");
        percentLabel.setFont(Font.font("Arial", 13));
        percentLabel.setTextFill(Color.WHITE);

        VBox root = new VBox(20, titleLabel, subtitleLabel, progressBar, percentLabel);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("""
            -fx-background-color: linear-gradient(to bottom right, #1e1e2f, #3a3a5f);
        """);

        Scene scene = new Scene(root, 600, 350);

        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();

        Timeline timeline = new Timeline();
        timeline.setCycleCount(TOTAL_SECONDS);

        final int[] currentSecond = {0};

        timeline.getKeyFrames().add(
            new KeyFrame(Duration.seconds(1), event -> {
                currentSecond[0]++;
                double progress = (double) currentSecond[0] / TOTAL_SECONDS;

                progressBar.setProgress(progress);
                percentLabel.setText((int) (progress * 100) + "%");

                if (currentSecond[0] == TOTAL_SECONDS) {
                    timeline.stop();
                    openMainScreen(stage);
                }
            })
        );

        timeline.play();
    }

    private void openMainScreen(Stage splashStage) {
        MainScreen mainScreen = new MainScreen();
        mainScreen.show();
        splashStage.close();
    }
}