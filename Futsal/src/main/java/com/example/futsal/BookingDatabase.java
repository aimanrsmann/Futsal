package com.example.futsal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookingDatabase {

    // Database URL, username, and password should be updated to match your database configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/futsal_db";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "password"; // Change this to your password

    // Method to get all bookings from the database
    public static List<Booking> getBookings() {
        List<Booking> bookings = new ArrayList<>();

        String query = "SELECT name, date, time, court, payment FROM bookings"; // Adjust this query to match your table structure

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String date = resultSet.getString("date");
                String time = resultSet.getString("time");
                String court = resultSet.getString("court");
                String payment = resultSet.getString("payment");

                Booking booking = new Booking(name, date, time, court, payment);
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error retrieving bookings: " + e.getMessage());
        }

        return bookings;
    }
}
