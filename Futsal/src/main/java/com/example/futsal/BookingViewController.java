package com.example.futsal;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookingViewController {

    @FXML
    private TableView<Booking> bookingTable;

    @FXML
    private TableColumn<Booking, String> nameColumn;
    @FXML
    private TableColumn<Booking, String> dateColumn;
    @FXML
    private TableColumn<Booking, String> timeColumn;
    @FXML
    private TableColumn<Booking, String> courtTypeColumn;
    @FXML
    private TableColumn<Booking, String> paymentColumn;

    // Database connection details (adjust these as necessary)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/futsal";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public void initialize() {
        // Set up the table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        courtTypeColumn.setCellValueFactory(new PropertyValueFactory<>("courtType"));
        paymentColumn.setCellValueFactory(new PropertyValueFactory<>("payment"));

        // Load data from the database
        loadBookings();
    }

    public void loadBookings() {
        ObservableList<Booking> bookings = FXCollections.observableArrayList();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM booking");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String date = resultSet.getString("date");
                String time = resultSet.getString("time");
                String courtType = resultSet.getString("court_type");
                String payment = resultSet.getString("payment");

                bookings.add(new Booking(name, date, time, courtType, payment));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Error while fetching data from the database: " + e.getMessage());
        }

        bookingTable.setItems(bookings);
    }

    @FXML
    private void handleBackButton() {
        // Get the current stage from the booking table's scene
        Stage stage = (Stage) bookingTable.getScene().getWindow();
        try {
            // Load the main page FXML without creating a new instance of the main stage
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/futsal/mainpage.fxml"));
            Pane mainPagePane = loader.load();

            // Set the new scene for the stage
            stage.setScene(new Scene(mainPagePane));
            stage.setTitle("Futsal Booking System - Main Page");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load main page: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
