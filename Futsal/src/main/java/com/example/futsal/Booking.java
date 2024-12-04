package com.example.futsal;

public class Booking {
    private String name;
    private String date;
    private String time;
    private String courtType;
    private String payment;

    public Booking(String name, String date, String time, String courtType, String payment) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.courtType = courtType;
        this.payment = payment;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCourtType() {
        return courtType;
    }

    public void setCourtType(String courtType) {
        this.courtType = courtType;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }
}
