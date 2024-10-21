package com.library.controller;

import com.library.config.DatabaseConfig;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import org.mindrot.jbcrypt.BCrypt;

public class LoginController {

    @FXML
    private TextField userNameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    protected Hyperlink signUpLink;

    static final int SCENE_WIDTH = 400;
    static final int SCENE_HEIGHT = 600;

    @FXML
    void login(ActionEvent event) {
        String username = userNameField.getText().trim();
        String password = passwordField.getText();

        if (validateInput(username, password)) {
            if (isLoginValid(username, password)) {
                System.out.println("Login successful");
                // navigateToDashboard();  // Un-comment this when the dashboard is ready
            } else {
                showAlert(AlertType.ERROR, "Login Failed", "Incorrect username or password.");
            }
        }
    }

    @FXML
    void openSignUp(ActionEvent event) {
        loadScene("/fxml/Register.fxml", "Sign Up");
    }

    private boolean validateInput(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.ERROR, "Input Error", "Please enter both username and password.");
            return false;
        }
        return true;
    }

    private boolean isLoginValid(String username, String password) {
        final String query = "SELECT password_hash FROM Users WHERE user_name = ?";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String hashedPassword = resultSet.getString("password_hash");
                return checkPassword(password, hashedPassword);
            } else {
                showAlert(AlertType.ERROR, "Login Failed", "Username not found.");
            }
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error", "An error occurred while accessing the database.");
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }

    // Utility method to load new scenes
    private void loadScene(String fxmlPath, String title) {
        try {
            Stage stage = (Stage) signUpLink.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            StackPane root = fxmlLoader.load();
            Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setTitle(title);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Loading Error", "Unable to load the " + title + " page.");
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }


//    dashboard
//    private void navigateToDashboard() {
//        try {
//            Stage stage = (Stage) loginButton.getScene().getWindow();
//            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Dashboard.fxml"));
//            StackPane root = (StackPane) fxmlLoader.load();
//            Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
//            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/Dashboard.css")).toExternalForm());
//            stage.setScene(scene);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
