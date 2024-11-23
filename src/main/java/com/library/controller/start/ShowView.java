package com.library.controller.start;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public interface ShowView {

    static void showView(Stage stage, String fxmlPath, String title, String stylesheets) {
        try {
            // Tải FXML
            FXMLLoader fxmlLoader = new FXMLLoader(ShowView.class.getResource(fxmlPath));
            Parent root = fxmlLoader.load();

            // Tạo Scene mới và thiết lập stylesheet nếu có
            Scene scene = new Scene(root);
            if (stylesheets != null && !stylesheets.isEmpty()) {
                scene.getStylesheets().add(stylesheets);
            }

            // Thiết lập Scene và Stage
            stage.setTitle(title);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to show the view: " + e.getMessage());
        }
    }

    static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

}
