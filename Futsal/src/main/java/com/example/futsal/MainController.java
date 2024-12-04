package com.example.futsal;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    private Stage mainStage; // Reference to the main stage

    public void setMainStage(Stage stage) {
        this.mainStage = stage;
    }

    @FXML
    private void handleViewBookings() {
        loadNewWindow("/com/example/futsal/bookingview.fxml", "View Bookings");
    }

    @FXML
    private void handleManageProfile() {
        // Close the current main page before opening the new window
        if (mainStage != null) {
            mainStage.close();
        }

        // Load the Manage Profile page in a new window
        loadNewWindow("/com/example/futsal/manageprofile.fxml", "Manage Profile");
    }

    @FXML
    private void handleSettings() {
        showAlert("Settings", "This feature is under development.");
    }

    @FXML
    private void handleHelp() {
        showAlert("Help", "Contact support at support@gmail.com for assistance.");
    }

    @FXML
    private void handleLogout() {
        navigateTo("/com/example/futsal/login.fxml", "Futsal Booking System - Login");
    }

    @FXML
    private void handleExit() {
        if (mainStage != null) {
            mainStage.close();
        } else {
            System.exit(0);
        }
    }

    @FXML
    private void showAbout() {
        showAlert("About", "Futsal Booking System\nVersion 1.0\n© 2024 Futsal Booking System.");
    }

    @FXML
    private void handleBookCourt() {
        loadNewWindow("/com/example/futsal/bookingform.fxml", "Booking Form", true);
    }

    private void loadNewWindow(String fxmlPath, String title) {
        loadNewWindow(fxmlPath, title, false);
    }

    private void loadNewWindow(String fxmlPath, String title, boolean isModal) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Pane pane = loader.load();

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(pane));

            if (mainStage != null) {
                stage.initOwner(mainStage);
            }

            if (isModal) {
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
            } else {
                stage.initModality(Modality.WINDOW_MODAL);
                stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load the requested page: " + e.getMessage());
        }
    }

    public void navigateTo(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Pane pane = loader.load();

            if (mainStage != null) {
                mainStage.setScene(new Scene(pane));
                mainStage.setTitle(title);
                mainStage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load the requested page: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
