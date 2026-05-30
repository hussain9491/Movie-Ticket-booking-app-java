package com.example.cinebook.util;

import com.example.cinebook.model.Movie;
import com.example.cinebook.model.Seat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class AppState {
    private static final AppState INSTANCE = new AppState();

    private Movie selectedMovie;
    private final List<Seat> selectedSeats = new ArrayList<>();

    private AppState() {
    }

    public static AppState getInstance() {
        return INSTANCE;
    }

    public Movie getSelectedMovie() {
        return selectedMovie;
    }

    public void setSelectedMovie(Movie selectedMovie) {
        this.selectedMovie = selectedMovie;
    }

    public List<Seat> getSelectedSeats() {
        return Collections.unmodifiableList(selectedSeats);
    }

    public void setSelectedSeats(List<Seat> seats) {
        selectedSeats.clear();
        selectedSeats.addAll(seats);
    }

    public void clearBookingFlow() {
        selectedMovie = null;
        selectedSeats.clear();
    }
}

