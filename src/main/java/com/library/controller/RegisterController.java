package com.library.controller;

import com.library.config.DatabaseConfig;
import com.library.dao.UserDao;
import com.library.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;


public class RegisterController {

    @FXML
    private TextField fullNameField;
    @FXML
    private TextField userNameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button signUpButton;

    @FXML
    protected Hyperlink loginLink;

    private UserDao userDao = new UserDao();

    @FXML
    void signUp(ActionEvent event) throws Exception {
        String fullName = fullNameField.getText();
        String username = userNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();


        if (!password.equals(confirmPassword)) {
            showAlert(AlertType.ERROR, "Error", "Passwords do not match");
            return;
        }

        if (isEmailExists(email)) {
            showAlert(AlertType.ERROR, "Error", "Email already exists");
            return;
        }

        User user = new User(fullName, username, email, password);

        if (userDao.findUserByName(username) != null) {
            showAlert(AlertType.ERROR,"Error", "Username already exists. Please choose a different username.");
            return;
        }
        try {
            userDao.add(user);
            showAlert(AlertType.INFORMATION,"Success", "User registered successfully!");
        } catch (Exception e) {
            showAlert(AlertType.INFORMATION,e.getMessage(), "Something went wrong");
        }

//        saveUserData(fullName, username, email, password, role);
        openLogin(event);
        clearFields();
    }

    @FXML
    void openLogin(ActionEvent event) {
        try {
            Stage stage = (Stage) loginLink.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/userLogin.fxml"));
            HBox root = (HBox) fxmlLoader.load();
            Scene scene = new Scene(root, 400, 600);

            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());
            stage.centerOnScreen();
            stage.setTitle("Login");
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isEmailExists(String email) {
        String checkEmailSQL = "SELECT COUNT(*) FROM Users WHERE email = ?";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(checkEmailSQL)) {

            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


//    private void saveUserData(String fullName, String username, String email, String password, String role) {
//        String insertUserSQL = "INSERT INTO Users (full_name, user_name, email, password_hash, user_role) VALUES (?, ?, ?, ?, ?)";
//
//        try (Connection connection = DatabaseConfig.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(insertUserSQL)) {
//
//            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
//            preparedStatement.setString(1, fullName);
//            preparedStatement.setString(2, username);
//            preparedStatement.setString(3, email);
//            preparedStatement.setString(4, hashedPassword);
//            preparedStatement.setString(5, role); // Save role to database
//
//            int rowsAffected = preparedStatement.executeUpdate();
//            if (rowsAffected > 0) {
//                System.out.println("User saved successfully.");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    private void clearFields() {
        userNameField.clear();
        passwordField.clear();
        fullNameField.clear();
        emailField.clear();
        confirmPasswordField.clear();
    }
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }



}

