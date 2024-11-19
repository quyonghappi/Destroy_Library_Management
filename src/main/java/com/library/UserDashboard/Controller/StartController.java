package com.library.UserDashboard.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

public class StartController {
    @FXML
    private Button guestButton;

    @FXML
    private Button librarianButton;

    @FXML
    public void initialize() {
        // Set actions for Guest and Librarian buttons to open the login screen
        guestButton.setOnAction(event -> openLoginScreen());
        librarianButton.setOnAction(event -> openLoginScreen());
    }

    private void openLoginScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            AnchorPane loginPane = fxmlLoader.load();
            Stage stage = (Stage) guestButton.getScene().getWindow();
            stage.setScene(new Scene(loginPane));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}