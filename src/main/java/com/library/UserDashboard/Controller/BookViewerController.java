package com.library.UserDashboard.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class BookViewerController {

    @FXML
    private Button homeButton;


    @FXML
    public void initialize() {
        // Add event handler for library button
        homeButton.setOnAction(event -> openDashBoard());

    }

    private void openDashBoard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user_dashboard.fxml"));
            Parent bookView = loader.load();

            Stage stage = (Stage) homeButton.getScene().getWindow();
            Scene scene = new Scene(bookView, 1466, 750);
            scene.getStylesheets().add(getClass().getResource("/css/styling.css").toExternalForm());

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
