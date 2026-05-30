package com.example.cinebook.dao;

import com.example.cinebook.model.Seat;
import com.example.cinebook.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SeatDAO {
    private static final int ROW_COUNT = 5;
    private static final int COL_COUNT = 12;

    public void initializeSeatsIfMissing(int movieId) throws SQLException {
        if (count(movieId) > 0) {
            return;
        }
        String sql = "INSERT INTO seats (movie_id, seat_number, row_label, is_booked, seat_type) VALUES (?, ?, ?, 0, ?)";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int row = 0; row < ROW_COUNT; row++) {
                String rowLabel = String.valueOf((char) ('A' + row));
                for (int col = 1; col <= COL_COUNT; col++) {
                    statement.setInt(1, movieId);
                    statement.setString(2, rowLabel + col);
                    statement.setString(3, rowLabel);
                    statement.setString(4, (row == 0 || col > 10) ? "vip" : "standard");
                    statement.addBatch();
                }
            }
            statement.executeBatch();
        }
    }

    public List<Seat> getSeatsByMovie(int movieId) throws SQLException {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT * FROM seats WHERE movie_id = ? ORDER BY row_label, seat_number";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, movieId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Seat seat = new Seat();
                    seat.setId(rs.getInt("id"));
                    seat.setMovieId(rs.getInt("movie_id"));
                    seat.setSeatNumber(rs.getString("seat_number"));
                    seat.setBooked(rs.getBoolean("is_booked"));
                    seat.setSeatType(rs.getString("seat_type"));
                    seats.add(seat);
                }
            }
        }
        return seats;
    }

    private int count(int movieId) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM seats WHERE movie_id = ?")) {
            statement.setInt(1, movieId);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }
}

