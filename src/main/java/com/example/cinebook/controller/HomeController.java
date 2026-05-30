package com.example.cinebook.controller;

import com.example.cinebook.dao.MovieDAO;
import com.example.cinebook.dao.SeatDAO;
import com.example.cinebook.model.Movie;
import com.example.cinebook.util.AlertHelper;
import com.example.cinebook.util.AppState;
import com.example.cinebook.util.SceneManager;
import com.example.cinebook.util.SessionManager;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HomeController {
    @FXML private Label welcomeLabel;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> genreFilter;
    @FXML private TilePane movieContainer;

    private final MovieDAO movieDAO = new MovieDAO();
    private final SeatDAO seatDAO = new SeatDAO();

    @FXML
    private void initialize() {
        welcomeLabel.setText("Welcome, " + SessionManager.getInstance().getCurrentUser().getUsername());
        genreFilter.getItems().setAll("All", "Action", "Comedy", "Drama", "Sci-Fi", "Animation", "Thriller");
        genreFilter.getSelectionModel().selectFirst();
        loadMovies();
    }

    @FXML private void onFilter() { loadMovies(); }
    @FXML private void onMyBookings() { SceneManager.switchScene("/com/example/cinebook/view/bookings.fxml", "CineBook - My Bookings", 1200, 760); }

    @FXML
    private void onAdmin() {
        if (!SessionManager.getInstance().isAdmin()) {
            AlertHelper.error("Access Denied", "Admin role required");
            return;
        }
        SceneManager.switchScene("/com/example/cinebook/view/admin.fxml", "CineBook - Admin", 1200, 760);
    }

    @FXML
    private void onLogout() {
        SessionManager.getInstance().logout();
        AppState.getInstance().clearBookingFlow();
        SceneManager.switchScene("/com/example/cinebook/view/login.fxml", "CineBook - Login", 980, 680);
    }

    private void loadMovies() {
        movieContainer.getChildren().clear();
        try {
            List<Movie> movies = movieDAO.searchMovies(searchField.getText(), genreFilter.getValue());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
            for (Movie movie : movies) {
                VBox card = new VBox(8);
                card.getStyleClass().add("movie-card");
                card.setPadding(new Insets(12));
                card.setPrefWidth(260);

                Label title = new Label(movie.getTitle());
                title.getStyleClass().add("movie-title");
                Label meta = new Label(movie.getGenre() + " | " + movie.getDurationMin() + " min");
                Label show = new Label("Showtime: " + formatter.format(movie.getShowTime()));
                Label price = new Label("Base Price: $" + movie.getPrice());
                Button book = new Button("Book Now");
                book.getStyleClass().add("accent-button");
                book.setOnAction(e -> {
                    try {
                        seatDAO.initializeSeatsIfMissing(movie.getId());
                        AppState.getInstance().setSelectedMovie(movie);
                        SceneManager.switchScene("/com/example/cinebook/view/seat.fxml", "CineBook - Seat Selection", 1200, 760);
                    } catch (SQLException ex) {
                        AlertHelper.error("Seat Error", "Unable to load seats");
                    }
                });
                card.getChildren().addAll(title, meta, show, price, book);
                movieContainer.getChildren().add(card);
            }
        } catch (SQLException e) {
            AlertHelper.error("Load Error", "Unable to load movies");
        }
    }
}

