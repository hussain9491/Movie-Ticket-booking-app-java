package com.example.cinebook.dao;

import com.example.cinebook.model.Movie;
import com.example.cinebook.util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {
    public List<Movie> searchMovies(String search, String genre) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM movies WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (search != null && !search.isBlank()) {
            sql.append(" AND LOWER(title) LIKE ?");
            params.add("%" + search.toLowerCase() + "%");
        }
        if (genre != null && !genre.isBlank() && !"All".equalsIgnoreCase(genre)) {
            sql.append(" AND genre = ?");
            params.add(genre);
        }
        sql.append(" ORDER BY show_time");

        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = statement.executeQuery()) {
                return mapList(rs);
            }
        }
    }

    public List<Movie> getAllMovies() throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM movies ORDER BY show_time");
             ResultSet rs = statement.executeQuery()) {
            return mapList(rs);
        }
    }

    public int countMovies() throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM movies");
             ResultSet rs = statement.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public boolean addMovie(Movie movie) throws SQLException {
        String sql = "INSERT INTO movies (title, genre, duration_min, show_time, price, poster_path) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, movie.getTitle());
            statement.setString(2, movie.getGenre());
            statement.setInt(3, movie.getDurationMin());
            statement.setTimestamp(4, Timestamp.valueOf(movie.getShowTime()));
            statement.setBigDecimal(5, movie.getPrice());
            statement.setString(6, movie.getPosterPath());
            return statement.executeUpdate() > 0;
        }
    }

    public boolean deleteMovie(int movieId) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM movies WHERE id = ?")) {
            statement.setInt(1, movieId);
            return statement.executeUpdate() > 0;
        }
    }

    private List<Movie> mapList(ResultSet rs) throws SQLException {
        List<Movie> movies = new ArrayList<>();
        while (rs.next()) {
            Movie movie = new Movie();
            movie.setId(rs.getInt("id"));
            movie.setTitle(rs.getString("title"));
            movie.setGenre(rs.getString("genre"));
            movie.setDurationMin(rs.getInt("duration_min"));
            Timestamp show = rs.getTimestamp("show_time");
            if (show != null) {
                movie.setShowTime(show.toLocalDateTime());
            }
            movie.setPrice(rs.getBigDecimal("price") == null ? BigDecimal.ZERO : rs.getBigDecimal("price"));
            movie.setPosterPath(rs.getString("poster_path"));
            movies.add(movie);
        }
        return movies;
    }
}

