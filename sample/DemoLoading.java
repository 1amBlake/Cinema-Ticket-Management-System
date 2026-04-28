package com.cinema.gui;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Điểm khởi chạy chương trình.
 */
public class DemoLoading extends Application {

    @Override
    public void start(Stage primaryStage) {
        SplashScreen splashScreen = new SplashScreen();
        splashScreen.show(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}