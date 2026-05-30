package com.example.cinebook.controller;

import com.example.cinebook.dao.SeatDAO;
import com.example.cinebook.model.Movie;
import com.example.cinebook.model.Seat;
import com.example.cinebook.util.AlertHelper;
import com.example.cinebook.util.AppState;
import com.example.cinebook.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SeatController {
    @FXML private Label movieTitleLabel;
    @FXML private GridPane seatGrid;
    @FXML private Label selectedSeatsLabel;
    @FXML private Label totalLabel;

    private final SeatDAO seatDAO = new SeatDAO();
    private final List<Seat> selected = new ArrayList<>();
    private Movie movie;

    @FXML
    private void initialize() {
        movie = AppState.getInstance().getSelectedMovie();
        if (movie == null) {
            SceneManager.switchScene("/com/example/cinebook/view/home.fxml", "CineBook - Home", 1200, 760);
            return;
        }
        movieTitleLabel.setText(movie.getTitle() + " - Seat Selection");
        loadSeats();
    }

    @FXML private void onBack() { SceneManager.switchScene("/com/example/cinebook/view/home.fxml", "CineBook - Home", 1200, 760); }

    @FXML
    private void onProceed() {
        if (selected.isEmpty()) {
            AlertHelper.error("No Seats", "Select at least one seat");
            return;
        }
        AppState.getInstance().setSelectedSeats(selected);
        SceneManager.switchScene("/com/example/cinebook/view/confirm.fxml", "CineBook - Confirm", 920, 620);
    }

    private void loadSeats() {
        try {
            List<Seat> seats = seatDAO.getSeatsByMovie(movie.getId());
            int row = 0;
            int col = 0;
            for (Seat seat : seats) {
                ToggleButton button = new ToggleButton(seat.getSeatNumber());
                button.getStyleClass().add("seat-button");
                if (seat.isBooked()) {
                    button.getStyleClass().add("seat-booked");
                    button.setDisable(true);
                } else if ("vip".equalsIgnoreCase(seat.getSeatType())) {
                    button.getStyleClass().add("seat-vip");
                } else {
                    button.getStyleClass().add("seat-standard");
                }

                button.selectedProperty().addListener((obs, oldVal, selectedNow) -> {
                    if (selectedNow) {
                        selected.add(seat);
                        button.getStyleClass().add("seat-selected");
                    } else {
                        selected.removeIf(s -> s.getId() == seat.getId());
                        button.getStyleClass().remove("seat-selected");
                    }
                    refreshSummary();
                });

                seatGrid.add(button, col, row);
                col++;
                if (col == 12) {
                    col = 0;
                    row++;
                }
            }
            refreshSummary();
        } catch (SQLException e) {
            AlertHelper.error("Seat Error", "Unable to load seats");
        }
    }

    private void refreshSummary() {
        selectedSeatsLabel.setText(selected.stream().map(Seat::getSeatNumber).collect(Collectors.joining(", ")));
        BigDecimal total = selected.stream()
                .map(s -> movie.getPrice().multiply(s.multiplier()))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
        totalLabel.setText("$" + total);
    }
}

