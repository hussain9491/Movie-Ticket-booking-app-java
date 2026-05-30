package com.example.cinebook.dao;

import com.example.cinebook.model.Booking;
import com.example.cinebook.model.Movie;
import com.example.cinebook.model.Seat;
import com.example.cinebook.model.User;
import com.example.cinebook.util.BookingCodeGenerator;
import com.example.cinebook.util.DBConnection;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {
    public String createBooking(User user, Movie movie, List<Seat> seats, String paymentRef) throws SQLException {
        String bookingCode = BookingCodeGenerator.nextCode();
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try {
                String lockSql = "SELECT is_booked FROM seats WHERE id = ? FOR UPDATE";
                String insertSql = "INSERT INTO bookings (booking_code, user_id, movie_id, seat_id, total_price, status, payment_ref) VALUES (?, ?, ?, ?, ?, 'confirmed', ?)";
                String seatSql = "UPDATE seats SET is_booked = 1 WHERE id = ?";

                try (PreparedStatement lock = connection.prepareStatement(lockSql);
                     PreparedStatement insert = connection.prepareStatement(insertSql);
                     PreparedStatement seatUpdate = connection.prepareStatement(seatSql)) {

                    for (Seat seat : seats) {
                        lock.setInt(1, seat.getId());
                        try (ResultSet rs = lock.executeQuery()) {
                            if (!rs.next() || rs.getBoolean("is_booked")) {
                                throw new SQLException("Seat unavailable: " + seat.getSeatNumber());
                            }
                        }

                        insert.setString(1, bookingCode);
                        insert.setInt(2, user.getId());
                        insert.setInt(3, movie.getId());
                        insert.setInt(4, seat.getId());
                        insert.setBigDecimal(5, movie.getPrice().multiply(seat.multiplier()).setScale(2, RoundingMode.HALF_UP));
                        insert.setString(6, paymentRef);
                        insert.addBatch();

                        seatUpdate.setInt(1, seat.getId());
                        seatUpdate.addBatch();
                    }

                    insert.executeBatch();
                    seatUpdate.executeBatch();
                }

                connection.commit();
                return bookingCode;
            } catch (Exception e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public List<Booking> getBookingsByUser(int userId) throws SQLException {
        String sql = "SELECT b.*, m.title movie_title, s.seat_number FROM bookings b " +
                "JOIN movies m ON m.id = b.movie_id JOIN seats s ON s.id = b.seat_id " +
                "WHERE b.user_id = ? ORDER BY b.booking_date DESC";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                return mapBookings(rs);
            }
        }
    }

    public List<Booking> getAllBookings() throws SQLException {
        String sql = "SELECT b.*, m.title movie_title, s.seat_number FROM bookings b " +
                "JOIN movies m ON m.id = b.movie_id JOIN seats s ON s.id = b.seat_id ORDER BY b.booking_date DESC";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {
            return mapBookings(rs);
        }
    }

    public boolean cancelBooking(int bookingId, int userId, boolean admin) throws SQLException {
        String getSql = "SELECT seat_id, status FROM bookings WHERE id = ?" + (admin ? "" : " AND user_id = ?");
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement get = connection.prepareStatement(getSql)) {
                get.setInt(1, bookingId);
                if (!admin) {
                    get.setInt(2, userId);
                }

                int seatId;
                String status;
                try (ResultSet rs = get.executeQuery()) {
                    if (!rs.next()) {
                        connection.rollback();
                        return false;
                    }
                    seatId = rs.getInt("seat_id");
                    status = rs.getString("status");
                }

                if ("cancelled".equalsIgnoreCase(status)) {
                    connection.rollback();
                    return false;
                }

                try (PreparedStatement booking = connection.prepareStatement("UPDATE bookings SET status='cancelled' WHERE id = ?");
                     PreparedStatement seat = connection.prepareStatement("UPDATE seats SET is_booked = 0 WHERE id = ?")) {
                    booking.setInt(1, bookingId);
                    seat.setInt(1, seatId);
                    boolean changed = booking.executeUpdate() > 0;
                    seat.executeUpdate();
                    connection.commit();
                    return changed;
                }
            } catch (Exception e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public int countTodayBookings() throws SQLException {
        String sql = "SELECT COUNT(*) FROM bookings WHERE DATE(booking_date)=CURRENT_DATE AND status='confirmed'";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public BigDecimal todayRevenue() throws SQLException {
        String sql = "SELECT COALESCE(SUM(total_price),0) FROM bookings WHERE DATE(booking_date)=CURRENT_DATE AND status='confirmed'";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {
            return rs.next() ? rs.getBigDecimal(1) : BigDecimal.ZERO;
        }
    }

    private List<Booking> mapBookings(ResultSet rs) throws SQLException {
        List<Booking> list = new ArrayList<>();
        while (rs.next()) {
            Booking booking = new Booking();
            booking.setId(rs.getInt("id"));
            booking.setBookingCode(rs.getString("booking_code"));
            booking.setMovieTitle(rs.getString("movie_title"));
            booking.setSeatNumber(rs.getString("seat_number"));
            booking.setTotalPrice(rs.getBigDecimal("total_price"));
            booking.setStatus(rs.getString("status"));
            booking.setPaymentRef(rs.getString("payment_ref"));
            list.add(booking);
        }
        return list;
    }
}

