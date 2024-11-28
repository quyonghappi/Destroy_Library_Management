package com.library.controller.start;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import static com.library.controller.start.LoadView.loadView;
import static com.library.controller.start.ShowView.showView;

public class StartUser {

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    private static Stage stage;

    public void handleLoginButtonAction(ActionEvent actionEvent) {
        stage = (Stage) loginButton.getScene().getWindow();
        loadView(stage, "/fxml/Start/UserLogin.fxml", "Login", "/css/start/UserLogin.css");
        showView(stage, "/fxml/Start/UserLogin.fxml", "Login", "/css/start/UserLogin.css");
    }

    public void handleSignUpButtonAction(ActionEvent actionEvent) {
        stage = (Stage) registerButton.getScene().getWindow();
        loadView(stage, "/fxml/Start/Register.fxml", "Sign Up", "/css/start/register.css");
        showView(stage, "/fxml/Start/Register.fxml", "Login", "/css/start/register.css");
    }
}
