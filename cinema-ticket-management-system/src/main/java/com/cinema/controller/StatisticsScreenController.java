package com.cinema.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.URL;
import java.util.ResourceBundle;

public class StatisticsScreenController implements Initializable {

    // --- REVENUE LABELS ---
    @FXML private Label todayRevenueValueLabel;
    @FXML private Label weekRevenueValueLabel;
    @FXML private Label monthRevenueValueLabel;
    @FXML private AreaChart<String, Number> revenueTrendChart;

    // --- INSIGHT LABELS ---
    @FXML private Label todayVsYesterdayValueLabel;
    @FXML private Label bestMovieValueLabel;
    @FXML private Label peakSessionValueLabel;
    @FXML private Label monthTargetValueLabel;

    // --- TICKET STATS ---
    @FXML private Label soldTicketsValueLabel;
    @FXML private Label canceledTicketsValueLabel;
    @FXML private Label ticketCancelRateValueLabel;

    // --- CHARTS ---
    @FXML private LineChart<String, Number> ticketByDayChart;
    @FXML private BarChart<String, Number> ticketByMovieChart;
    @FXML private BarChart<String, Number> ticketBySessionChart;
    @FXML private BarChart<String, Number> ticketByRoomChart;

    // --- TABLES ---
    @FXML private TableView<MovieStat> topRevenueMovieTable;
    @FXML private TableColumn<MovieStat, String> topRevenueMovieNameColumn;
    @FXML private TableColumn<MovieStat, Double> topRevenueMovieAmountColumn;
    @FXML private TableColumn<MovieStat, Integer> topRevenueMovieTicketsColumn;

    @FXML private TableView<MovieStat> topTicketMovieTable;
    @FXML private TableColumn<MovieStat, String> topTicketMovieNameColumn;
    @FXML private TableColumn<MovieStat, Integer> topTicketMovieSoldColumn;
    @FXML private TableColumn<MovieStat, String> topTicketMovieOccupancyColumn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupDummyData();
        setupCharts();
        setupTables();
    }

    private void setupDummyData() {
        // Gán giá trị tạm thời cho các thẻ thống kê
        todayRevenueValueLabel.setText("5.450.000 đ");
        weekRevenueValueLabel.setText("38.200.000 đ");
        monthRevenueValueLabel.setText("152.000.000 đ");
        
        todayVsYesterdayValueLabel.setText("+12.5%");
        bestMovieValueLabel.setText("Lật Mặt 7");
        peakSessionValueLabel.setText("19:00 - 21:00");
        monthTargetValueLabel.setText("75%");

        soldTicketsValueLabel.setText("1,240");
        canceledTicketsValueLabel.setText("12");
        ticketCancelRateValueLabel.setText("0.96%");
    }

    private void setupCharts() {
        // Biểu đồ xu hướng doanh thu (AreaChart)
        XYChart.Series<String, Number> revenueSeries = new XYChart.Series<>();
        revenueSeries.getData().add(new XYChart.Data<>("Thứ 2", 2100000));
        revenueSeries.getData().add(new XYChart.Data<>("Thứ 3", 2800000));
        revenueSeries.getData().add(new XYChart.Data<>("Thứ 4", 2500000));
        revenueSeries.getData().add(new XYChart.Data<>("Thứ 5", 4200000));
        revenueSeries.getData().add(new XYChart.Data<>("Thứ 6", 5800000));
        revenueSeries.getData().add(new XYChart.Data<>("Thứ 7", 8500000));
        revenueSeries.getData().add(new XYChart.Data<>("CN", 7200000));
        revenueTrendChart.getData().add(revenueSeries);

        // Biểu đồ vé theo ngày (LineChart)
        XYChart.Series<String, Number> ticketSeries = new XYChart.Series<>();
        ticketSeries.getData().add(new XYChart.Data<>("Thứ 2", 45));
        ticketSeries.getData().add(new XYChart.Data<>("Thứ 7", 180));
        ticketByDayChart.getData().add(ticketSeries);
    }

    private void setupTables() {
        // Cấu hình các cột cho bảng
        topRevenueMovieNameColumn.setCellValueFactory(new PropertyValueFactory<>("movieName"));
        topRevenueMovieAmountColumn.setCellValueFactory(new PropertyValueFactory<>("revenue"));
        topRevenueMovieTicketsColumn.setCellValueFactory(new PropertyValueFactory<>("ticketsSold"));

        topTicketMovieNameColumn.setCellValueFactory(new PropertyValueFactory<>("movieName"));
        topTicketMovieSoldColumn.setCellValueFactory(new PropertyValueFactory<>("ticketsSold"));
        topTicketMovieOccupancyColumn.setCellValueFactory(new PropertyValueFactory<>("occupancy"));

        // Dữ liệu mẫu cho bảng
        ObservableList<MovieStat> data = FXCollections.observableArrayList(
            new MovieStat("Lật Mặt 7", 45000000.0, 500, "92%"),
            new MovieStat("Doraemon", 12000000.0, 210, "65%"),
            new MovieStat("Haikyuu!!", 8500000.0, 150, "40%")
        );

        topRevenueMovieTable.setItems(data);
        topTicketMovieTable.setItems(data);
    }

    // Lớp hỗ trợ dữ liệu hiển thị trên TableView
    public static class MovieStat {
        private String movieName;
        private double revenue;
        private int ticketsSold;
        private String occupancy;

        public MovieStat(String movieName, double revenue, int ticketsSold, String occupancy) {
            this.movieName = movieName;
            this.revenue = revenue;
            this.ticketsSold = ticketsSold;
            this.occupancy = occupancy;
        }

        public String getMovieName() { return movieName; }
        public double getRevenue() { return revenue; }
        public int getTicketsSold() { return ticketsSold; }
        public String getOccupancy() { return occupancy; }
    }
}