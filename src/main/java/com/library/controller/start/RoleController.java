package com.library.controller.start;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import static com.library.utils.LoadView.loadView;

public class RoleController {

    @FXML
    private HBox rootPane;

    @FXML
    private Button userButton;

    @FXML
    private Button adminButton;

    public static String role;
    private Stage stage;


    @FXML
    private void handleUserButtonAction(ActionEvent event) {
        role = "reader";
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        loadView(stage, "/fxml/Start/StartUser.fxml", "Destroy Library Management System", "/css/start/StartUser.css");
    }


    @FXML
    private void handleAdminButtonAction(ActionEvent event) {
        role = "admin";
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        loadView(stage, "/fxml/Start/adminLogin.fxml", "Destroy Library Management System", "/css/start/adminLogin.css");
    }

    @FXML
    public void showRoleView(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(RoleController.class.getResource("/fxml/Start/Role.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/start/Role.css")).toExternalForm());
            primaryStage.setTitle("Destroy Library Management System");
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            primaryStage.setWidth(screenBounds.getWidth());
            primaryStage.setHeight(screenBounds.getHeight());
            primaryStage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
