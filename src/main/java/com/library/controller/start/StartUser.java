package com.library.controller.start;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import static com.library.controller.start.LoadView.loadView;

public class StartUser {

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    private static Stage stage;

    public void handleLoginButtonAction(ActionEvent actionEvent) {
        stage = (Stage) loginButton.getScene().getWindow();
        loadView(stage, "/fxml/Start/UserLogin.fxml", "Destroy Library Management System", "/css/start/UserLogin.css");
    }

    public void handleSignUpButtonAction(ActionEvent actionEvent) {
        stage = (Stage) registerButton.getScene().getWindow();
        loadView(stage, "/fxml/Start/Register.fxml", "Destroy Library Management System", "/css/start/register.css");
    }
}
