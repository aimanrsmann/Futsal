package com.example.futsal;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ManageProfileController {

    @FXML
    private PasswordField currentPasswordField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/futsal";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private int loggedInUserId = 0; //

    public void setLoggedInUserId(int userId) {
        this.loggedInUserId = userId;
        System.out.println("Logged-in user ID set to: " + loggedInUserId); // Debugging log
    }

    @FXML
    private void handleChangePassword() {
        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Error", "All fields must be filled.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert("Error", "New password and confirmation do not match.");
            return;
        }

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.println("Connected to the database!");

            String query = "SELECT password FROM users WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, loggedInUserId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                System.out.println("User found. Password: " + storedPassword); // Debugging

                if (!storedPassword.equals(currentPassword)) {
                    showAlert("Error", "Current password is incorrect.");
                    return;
                }
            } else {
                System.out.println("User not found in the database."); // Debugging
                showAlert("Error", "User not found.");
                return;
            }

            String updateQuery = "UPDATE users SET password = ? WHERE id = ?";
            PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
            updateStmt.setString(1, newPassword); // Use hashing here if applicable
            updateStmt.setInt(2, loggedInUserId);
            int rowsUpdated = updateStmt.executeUpdate();

            if (rowsUpdated > 0) {
                showAlert("Success", "Password changed successfully.");
                clearFields();
                redirectToMainPage();
            } else {
                showAlert("Error", "Password update failed. Please try again.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred: " + e.getMessage());
        }
    }

    private void clearFields() {
        currentPasswordField.clear();
        newPasswordField.clear();
        confirmPasswordField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void redirectToMainPage() {
        try {
            // Load the main page FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/futsal/mainpage.fxml"));
            Scene scene = new Scene(loader.load());

            // Get the current stage and set the new scene
            Stage stage = (Stage) currentPasswordField.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load MainPage.fxml.");
        }
    }
}
