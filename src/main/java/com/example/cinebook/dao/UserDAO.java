package com.example.cinebook.dao;

import com.example.cinebook.model.User;
import com.example.cinebook.util.DBConnection;
import com.example.cinebook.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO {
    public Optional<User> authenticate(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND active = 1";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next() && PasswordUtil.verifyPassword(password, rs.getString("password"))) {
                    return Optional.of(map(rs));
                }
                return Optional.empty();
            }
        }
    }

    public boolean register(String username, String email, String rawPassword) throws SQLException {
        String sql = "INSERT INTO users (username, password, email, role, active) VALUES (?, ?, ?, 'user', 1)";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, PasswordUtil.hashPassword(rawPassword));
            statement.setString(3, email);
            return statement.executeUpdate() > 0;
        }
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users ORDER BY id DESC");
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                users.add(map(rs));
            }
        }
        return users;
    }

    public int countUsers() throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM users");
             ResultSet rs = statement.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public boolean toggleActive(int userId, boolean active) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE users SET active = ? WHERE id = ?")) {
            statement.setBoolean(1, active);
            statement.setInt(2, userId);
            return statement.executeUpdate() > 0;
        }
    }

    private User map(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPasswordHash(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setRole(rs.getString("role"));
        user.setActive(rs.getBoolean("active"));
        return user;
    }
}

