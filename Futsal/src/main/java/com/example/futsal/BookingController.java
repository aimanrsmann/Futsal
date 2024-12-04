package com.example.futsal;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookingController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField dateField;

    @FXML
    private TextField timeField;

    @FXML
    private TextField courtTypeField;

    @FXML
    private TextField paymentField;

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

    // Method to submit a booking
    // Method to submit a booking
    @FXML
    public void submitBooking() {
        String name = nameField.getText();
        String date = dateField.getText();
        String time = timeField.getText();
        String courtType = courtTypeField.getText();
        String payment = paymentField.getText();

        if (name.isEmpty() || date.isEmpty() || time.isEmpty() || courtType.isEmpty() || payment.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "All fields are required.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO booking (name, date, time, court_type, payment) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, date);
            stmt.setString(3, time);
            stmt.setString(4, courtType);
            stmt.setString(5, payment);
            stmt.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Booking Added", "Booking successfully added.");
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to save booking.");
        }
    }

    // Method to clear input fields
    private void clearFields() {
        nameField.clear();
        dateField.clear();
        timeField.clear();
        courtTypeField.clear();
        paymentField.clear();
    }

    // Method to show alerts
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to load bookings from the database and populate the table
    public void loadBookings() {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT name, date, time, court_type, payment FROM booking";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("name");
                String date = rs.getString("date");
                String time = rs.getString("time");
                String courtType = rs.getString("court_type");
                String payment = rs.getString("payment");

                Booking booking = new Booking(name, date, time, courtType, payment);
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load bookings.");
        }

        // Initialize columns and set data to the table
        if (nameColumn != null && dateColumn != null && timeColumn != null && courtTypeColumn != null && paymentColumn != null) {
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
            timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
            courtTypeColumn.setCellValueFactory(new PropertyValueFactory<>("courtType"));
            paymentColumn.setCellValueFactory(new PropertyValueFactory<>("payment"));

            bookingTable.getItems().setAll(bookings);
        } else {
            showAlert(Alert.AlertType.ERROR, "Initialization Error", "Table columns are not properly initialized.");
        }
    }
}
