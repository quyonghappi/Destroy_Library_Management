package com.library.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class  LoadView {

    /**
     * hiệu ứng (chưa áp dụng đươc)
     *
     * @param stage        Stage hiện tại, nơi màn hình mới sẽ được hiển thị.
     * @param fxmlPath     Đường dẫn FXML của màn hình cần tải.
     * @param title        Tiêu đề cửa sổ.
     * @param stylesheets  Đường dẫn đến stylesheet (nếu có).
     */
    public static void loadView(Stage stage, String fxmlPath, String title, String stylesheets) {
        try {
            // Tải FXML
            FXMLLoader fxmlLoader = new FXMLLoader(LoadView.class.getResource(fxmlPath));
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root);
            if (stylesheets != null && !stylesheets.isEmpty()) {
                scene.getStylesheets().add(stylesheets);
            }

            stage.setTitle(title);
            stage.setScene(scene);

            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the view: " + e.getMessage());
        }
    }

    public static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
