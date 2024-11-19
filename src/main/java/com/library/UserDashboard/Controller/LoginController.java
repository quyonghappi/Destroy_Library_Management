package com.library.UserDashboard.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signUpButton;

    @FXML
    public void initialize() {
        // Set action for Sign Up button
        signUpButton.setOnAction(event -> openDashboardScreen());
    }

    private void openDashboardScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/user_dashboard.fxml"));
            AnchorPane dashboardPane = fxmlLoader.load();
            Stage stage = (Stage) signUpButton.getScene().getWindow();
            stage.setScene(new Scene(dashboardPane));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
