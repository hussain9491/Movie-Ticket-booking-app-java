package com.example.cinebook.model;

import java.math.BigDecimal;

public class Seat {
    private int id;
    private int movieId;
    private String seatNumber;
    private boolean booked;
    private String seatType;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getMovieId() { return movieId; }
    public void setMovieId(int movieId) { this.movieId = movieId; }
    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }
    public boolean isBooked() { return booked; }
    public void setBooked(boolean booked) { this.booked = booked; }
    public String getSeatType() { return seatType; }
    public void setSeatType(String seatType) { this.seatType = seatType; }

    public BigDecimal multiplier() {
        return "vip".equalsIgnoreCase(seatType) ? new BigDecimal("1.5") : BigDecimal.ONE;
    }
}

