package com.example.cinebook.controller;

import com.example.cinebook.dao.UserDAO;
import com.example.cinebook.model.User;
import com.example.cinebook.util.AlertHelper;
import com.example.cinebook.util.SceneManager;
import com.example.cinebook.util.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.Optional;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    private void onLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        if (username.isBlank() || password.isBlank()) {
            statusLabel.setText("Username and password are required");
            return;
        }

        try {
            Optional<User> user = userDAO.authenticate(username, password);
            if (user.isEmpty()) {
                statusLabel.setText("Invalid credentials");
                return;
            }
            SessionManager.getInstance().login(user.get());
            if (SessionManager.getInstance().isAdmin()) {
                SceneManager.switchScene("/com/example/cinebook/view/admin.fxml", "CineBook - Admin", 1200, 760);
            } else {
                SceneManager.switchScene("/com/example/cinebook/view/home.fxml", "CineBook - Home", 1200, 760);
            }
        } catch (SQLException e) {
            AlertHelper.error("Login Error", "Unable to connect to database");
        }
    }

    @FXML
    private void onRegister() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        if (username.isBlank() || email.isBlank() || password.length() < 6) {
            statusLabel.setText("Enter username, email and 6+ char password");
            return;
        }
        try {
            boolean created = userDAO.register(username, email, password);
            statusLabel.setText(created ? "Account created. Login now." : "Registration failed");
        } catch (SQLException e) {
            statusLabel.setText("Registration failed (duplicate username/email)");
        }
    }
}

