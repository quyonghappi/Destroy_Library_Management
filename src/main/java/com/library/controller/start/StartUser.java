package com.library.controller.start;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.awt.*;

import static com.library.controller.start.LoadView.loadView;
import static com.library.controller.start.ShowView.showView;

public class StartUser {

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    private static Stage stage;
    private static Scene scene;
    private static Parent root;


    public void handleLoginButtonAction(ActionEvent actionEvent) {
        stage = (Stage) loginButton.getScene().getWindow();
        loadView(stage, "/fxml/Start/UserLogin.fxml", "Login", "/css/UserLogin.css");
        showView(stage, "/fxml/Start/UserLogin.fxml", "Login", "/css/UserLogin.css");
    }

    public void handleSignUpButtonAction(ActionEvent actionEvent) {
        stage = (Stage) registerButton.getScene().getWindow();
        loadView(stage, "/fxml/Start/Register.fxml", "Sign Up", "/css/register.css");
        showView(stage, "/fxml/Start/Register.fxml", "Login", "/css/register.css");
    }
}
