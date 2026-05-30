package com.example.cinebook.controller;

import com.example.cinebook.dao.BookingDAO;
import com.example.cinebook.model.Booking;
import com.example.cinebook.util.AlertHelper;
import com.example.cinebook.util.SceneManager;
import com.example.cinebook.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.sql.SQLException;

public class BookingsController {
    @FXML private TableView<Booking> bookingTable;
    @FXML private TableColumn<Booking, String> codeColumn;
    @FXML private TableColumn<Booking, String> movieColumn;
    @FXML private TableColumn<Booking, String> seatColumn;
    @FXML private TableColumn<Booking, String> statusColumn;
    @FXML private TableColumn<Booking, String> paymentColumn;
    @FXML private TableColumn<Booking, BigDecimal> totalColumn;

    private final BookingDAO bookingDAO = new BookingDAO();

    @FXML
    private void initialize() {
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("bookingCode"));
        movieColumn.setCellValueFactory(new PropertyValueFactory<>("movieTitle"));
        seatColumn.setCellValueFactory(new PropertyValueFactory<>("seatNumber"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        paymentColumn.setCellValueFactory(new PropertyValueFactory<>("paymentRef"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        load();
    }

    @FXML
    private void onCancelSelected() {
        Booking selected = bookingTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.error("No Selection", "Select a booking row");
            return;
        }
        if (!AlertHelper.confirm("Confirm", "Cancel booking " + selected.getBookingCode() + "?")) {
            return;
        }
        try {
            boolean ok = bookingDAO.cancelBooking(selected.getId(), SessionManager.getInstance().getCurrentUser().getId(), SessionManager.getInstance().isAdmin());
            if (!ok) {
                AlertHelper.error("Cancel Failed", "Booking cannot be cancelled");
            }
            load();
        } catch (SQLException e) {
            AlertHelper.error("DB Error", "Unable to cancel booking");
        }
    }

    @FXML private void onBackHome() { SceneManager.switchScene("/com/example/cinebook/view/home.fxml", "CineBook - Home", 1200, 760); }
    @FXML private void onLogout() { SessionManager.getInstance().logout(); SceneManager.switchScene("/com/example/cinebook/view/login.fxml", "CineBook - Login", 980, 680); }

    private void load() {
        try {
            if (SessionManager.getInstance().getCurrentUser() == null) {
                bookingTable.setItems(FXCollections.observableArrayList());
                return;
            }

            bookingTable.setItems(FXCollections.observableArrayList(
                    bookingDAO.getBookingsByUser(SessionManager.getInstance().getCurrentUser().getId())
            ));
        } catch (SQLException e) {
            AlertHelper.error("Load Error", "Unable to fetch bookings");
        }
    }
}

