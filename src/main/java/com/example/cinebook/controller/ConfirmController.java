package com.example.cinebook.controller;

import com.example.cinebook.dao.BookingDAO;
import com.example.cinebook.model.Movie;
import com.example.cinebook.model.Seat;
import com.example.cinebook.util.AlertHelper;
import com.example.cinebook.util.AppState;
import com.example.cinebook.util.SceneManager;
import com.example.cinebook.util.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ConfirmController {
    @FXML private Label movieLabel;
    @FXML private Label showtimeLabel;
    @FXML private Label seatsLabel;
    @FXML private Label totalLabel;
    @FXML private TextField paymentRefField;

    private final BookingDAO bookingDAO = new BookingDAO();
    private Movie movie;
    private List<Seat> seats;

    @FXML
    private void initialize() {
        movie = AppState.getInstance().getSelectedMovie();
        seats = AppState.getInstance().getSelectedSeats();
        if (movie == null || seats.isEmpty()) {
            SceneManager.switchScene("/com/example/cinebook/view/home.fxml", "CineBook - Home", 1200, 760);
            return;
        }
        movieLabel.setText(movie.getTitle());
        showtimeLabel.setText(movie.getShowTime().format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm")));
        seatsLabel.setText(seats.stream().map(Seat::getSeatNumber).collect(Collectors.joining(", ")));
        BigDecimal total = seats.stream().map(s -> movie.getPrice().multiply(s.multiplier())).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
        totalLabel.setText("$" + total);
    }

    @FXML private void onBack() { SceneManager.switchScene("/com/example/cinebook/view/seat.fxml", "CineBook - Seat Selection", 1200, 760); }

    @FXML
    private void onConfirmBooking() {
        String paymentRef = paymentRefField.getText().trim();
        if (paymentRef.isBlank()) {
            AlertHelper.error("Payment Ref", "Enter payment reference");
            return;
        }
        try {
            String code = bookingDAO.createBooking(SessionManager.getInstance().getCurrentUser(), movie, seats, paymentRef);
            AlertHelper.info("Booking Confirmed", "Booking Code: " + code);
            AppState.getInstance().clearBookingFlow();
            SceneManager.switchScene("/com/example/cinebook/view/bookings.fxml", "CineBook - My Bookings", 1200, 760);
        } catch (SQLException e) {
            AlertHelper.error("Booking Failed", "Seat may have been booked by another user");
        }
    }
}

