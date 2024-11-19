package com.library.controller.start;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.Objects;

public interface LoadView {

    //  Fade In
    public static void fadeInTransition(Node node) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), node);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    // Fade Out
    public static void fadeOutTransition(Node node) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), node);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.play();
    }

    // Slide Left
    public static void slideLeftTransition(Node node) {
        TranslateTransition slide = new TranslateTransition(Duration.millis(500), node);
        slide.setFromX(node.getTranslateX());
        slide.setToX(-node.getScene().getWidth());
        slide.play();
    }

    //  Slide Right
    public static void slideRightTransition(Node node) {
        TranslateTransition slide = new TranslateTransition(Duration.millis(500), node);
        slide.setFromX(node.getTranslateX());
        slide.setToX(node.getScene().getWidth());
        slide.play();
    }

    public static void displayViewWithAnimation(Stage stage, String fxmlPath, String title, String cssPath, Node currentRoot) {
        try {
            // Áp dụng hiệu ứng fade out cho màn hình hiện tại
//            fadeOutTransition(currentRoot);

            // Sau khi hiệu ứng fade-out hoàn tất, chuyển màn hình
            FXMLLoader fxmlLoader = new FXMLLoader(LoadView.class.getResource(fxmlPath));
            StackPane newRoot = fxmlLoader.load();
            Scene scene = new Scene(newRoot, 1200, 700);
            scene.getStylesheets().add(Objects.requireNonNull(LoadView.class.getResource(cssPath)).toExternalForm());

            // Đặt cảnh mới vào stage
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setTitle(title);

            // Áp dụng hiệu ứng fade-in cho màn hình mới
            fadeInTransition(newRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // bao loi khi ko chuyen duoc
     static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }


}
