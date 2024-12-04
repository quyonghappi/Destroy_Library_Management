package com.library.controller.start;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import static com.library.controller.start.LoadView.loadView;
import static com.library.controller.start.ShowView.showView;

public class RoleController {

    @FXML
    private HBox rootPane;

    @FXML
    private Button userButton;

    @FXML
    private Button adminButton;

    public static String role;
    private Stage stage;

    /**
     * Method called when the User button is pressed
     */
    @FXML
    private void handleUserButtonAction(ActionEvent event) {
//        chooseRole(Role.USER);
        role = "reader";
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        loadView(stage, "/fxml/Start/StartUser.fxml", "Login", "/css/start/StartUser.css");
        showView(stage, "/fxml/Start/StartUser.fxml", "Login", "/css/start/StartUser.css");
    }

    /**
     * Method called when the Admin button is pressed
     */
    @FXML
    private void handleAdminButtonAction(ActionEvent event) {
//        chooseRole(Role.ADMIN);
        role = "admin";
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        loadView(stage, "/fxml/Start/adminLogin.fxml", "Login", "/css/start/adminLogin.css");
        showView(stage, "/fxml/Start/adminLogin.fxml", "Login", "/css/start/adminLogin.css");
    }

    @FXML
    public void showRoleView(Stage primaryStage) {
        try {

//            displayViewWithAnimation()
            FXMLLoader loader = new FXMLLoader(RoleController.class.getResource("/fxml/Start/Role.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/start/Role.css")).toExternalForm());
            primaryStage.setTitle("Library Management");
            primaryStage.setScene(scene);
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            primaryStage.setWidth(screenBounds.getWidth());
            primaryStage.setHeight(screenBounds.getHeight());
            primaryStage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
