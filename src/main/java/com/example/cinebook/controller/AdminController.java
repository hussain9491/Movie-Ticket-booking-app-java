package com.example.cinebook.controller;

import com.example.cinebook.dao.BookingDAO;
import com.example.cinebook.dao.MovieDAO;
import com.example.cinebook.dao.UserDAO;
import com.example.cinebook.model.Booking;
import com.example.cinebook.model.Movie;
import com.example.cinebook.model.User;
import com.example.cinebook.util.AlertHelper;
import com.example.cinebook.util.SceneManager;
import com.example.cinebook.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class AdminController {
    @FXML private Label moviesCountLabel;
    @FXML private Label usersCountLabel;
    @FXML private Label todayBookingsLabel;
    @FXML private Label todayRevenueLabel;

    @FXML private TableView<Movie> moviesTable;
    @FXML private TableColumn<Movie, Integer> movieIdColumn;
    @FXML private TableColumn<Movie, String> movieTitleColumn;
    @FXML private TableColumn<Movie, String> movieGenreColumn;

    @FXML private TextField titleField;
    @FXML private TextField genreField;
    @FXML private TextField durationField;
    @FXML private TextField priceField;

    @FXML private TableView<User> usersTable;
    @FXML private TableColumn<User, Integer> userIdColumn;
    @FXML private TableColumn<User, String> usernameColumn;
    @FXML private TableColumn<User, String> roleColumn;
    @FXML private TableColumn<User, Boolean> activeColumn;

    @FXML private TableView<Booking> bookingsTable;
    @FXML private TableColumn<Booking, String> bookingCodeColumn;
    @FXML private TableColumn<Booking, String> bookingMovieColumn;
    @FXML private TableColumn<Booking, String> bookingSeatColumn;
    @FXML private TableColumn<Booking, String> bookingStatusColumn;

    private final MovieDAO movieDAO = new MovieDAO();
    private final UserDAO userDAO = new UserDAO();
    private final BookingDAO bookingDAO = new BookingDAO();

    @FXML
    private void initialize() {
        if (SessionManager.getInstance().getCurrentUser() == null) {
            return;
        }

        if (!SessionManager.getInstance().isAdmin()) {
            if (SceneManager.hasStage()) {
                SceneManager.switchScene("/com/example/cinebook/view/home.fxml", "CineBook - Home", 1200, 760);
            }
            return;
        }

        movieIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        movieTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        movieGenreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));

        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        activeColumn.setCellValueFactory(new PropertyValueFactory<>("active"));

        bookingCodeColumn.setCellValueFactory(new PropertyValueFactory<>("bookingCode"));
        bookingMovieColumn.setCellValueFactory(new PropertyValueFactory<>("movieTitle"));
        bookingSeatColumn.setCellValueFactory(new PropertyValueFactory<>("seatNumber"));
        bookingStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        refresh();
    }

    @FXML
    private void onAddMovie() {
        try {
            Movie movie = new Movie();
            movie.setTitle(titleField.getText().trim());
            movie.setGenre(genreField.getText().trim());
            movie.setDurationMin(Integer.parseInt(durationField.getText().trim()));
            movie.setPrice(new BigDecimal(priceField.getText().trim()));
            movie.setShowTime(LocalDateTime.now().plusDays(1));
            movie.setPosterPath("");
            movieDAO.addMovie(movie);
            titleField.clear();
            genreField.clear();
            durationField.clear();
            priceField.clear();
            refresh();
        } catch (Exception e) {
            AlertHelper.error("Add Movie", "Check values and try again");
        }
    }

    @FXML
    private void onDeleteMovie() {
        Movie selected = moviesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.error("No Movie", "Select a movie row");
            return;
        }
        try {
            movieDAO.deleteMovie(selected.getId());
            refresh();
        } catch (SQLException e) {
            AlertHelper.error("Delete Failed", "Movie may have linked bookings");
        }
    }

    @FXML
    private void onToggleUserActive() {
        User selected = usersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.error("No User", "Select a user row");
            return;
        }
        try {
            userDAO.toggleActive(selected.getId(), !selected.isActive());
            refresh();
        } catch (SQLException e) {
            AlertHelper.error("Update Failed", "Unable to update user state");
        }
    }

    @FXML private void onGoHome() { SceneManager.switchScene("/com/example/cinebook/view/home.fxml", "CineBook - Home", 1200, 760); }
    @FXML private void onLogout() { SessionManager.getInstance().logout(); SceneManager.switchScene("/com/example/cinebook/view/login.fxml", "CineBook - Login", 980, 680); }

    private void refresh() {
        try {
            moviesCountLabel.setText(String.valueOf(movieDAO.countMovies()));
            usersCountLabel.setText(String.valueOf(userDAO.countUsers()));
            todayBookingsLabel.setText(String.valueOf(bookingDAO.countTodayBookings()));
            todayRevenueLabel.setText("$" + bookingDAO.todayRevenue());

            moviesTable.setItems(FXCollections.observableArrayList(movieDAO.getAllMovies()));
            usersTable.setItems(FXCollections.observableArrayList(userDAO.getAllUsers()));
            bookingsTable.setItems(FXCollections.observableArrayList(bookingDAO.getAllBookings()));
        } catch (SQLException e) {
            AlertHelper.error("Admin Load", "Unable to load admin data");
        }
    }
}

