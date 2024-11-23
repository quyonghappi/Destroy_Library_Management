package com.library.controller.start;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public interface LoadView {

    /**
     * hiệu ứng (chưa áp dụng đươc)
     *
     * @param stage        Stage hiện tại, nơi màn hình mới sẽ được hiển thị.
     * @param fxmlPath     Đường dẫn FXML của màn hình cần tải.
     * @param title        Tiêu đề cửa sổ.
     * @param stylesheets  Đường dẫn đến stylesheet (nếu có).
     */
    static void loadView(Stage stage, String fxmlPath, String title, String stylesheets) {
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

    /**
     * Áp dụng các hiệu ứng chuyển cảnh cho root Node.
     *
     * @param root          Root Node của màn hình.
     * @param transitionType Loại hiệu ứng (Fade, Zoom, Slide).
     * @param rootPane      RootPane chứa các thành phần cần trượt (như HBox, ảnh).
     */
    private static void applyTransition(Parent root, String transitionType, Parent rootPane) {
        switch (transitionType.toLowerCase()) {
            case "fade":
                FadeTransition fadeIn = new FadeTransition(Duration.millis(500), root);
                fadeIn.setFromValue(0);
                fadeIn.setToValue(1);
                fadeIn.setCycleCount(1);
                fadeIn.setAutoReverse(false);
                fadeIn.play();
                break;
            case "slideleft":
                slideTransition(rootPane, 0, -500);  // Di chuyển ra ngoài màn hình từ bên trái
                break;
            case "slideright":
                slideTransition(rootPane, 0, 500);  // Di chuyển ra ngoài màn hình từ bên phải
                break;
            default:
                FadeTransition defaultFade = new FadeTransition(Duration.millis(500), root);
                defaultFade.setFromValue(0);
                defaultFade.setToValue(1);
                defaultFade.setCycleCount(1);
                defaultFade.setAutoReverse(false);
                defaultFade.play();
        }
    }

    /**
     * Hiệu ứng trượt (slide) cho HBox và ảnh.
     *
     * @param rootPane  Root Node chứa HBox và ảnh.
     * @param fromX Khoảng cách bắt đầu.
     * @param toX   Khoảng cách kết thúc (di chuyển sang trái hoặc phải).
     */
    private static void slideTransition(Parent rootPane, double fromX, double toX) {
        TranslateTransition slide = new TranslateTransition(Duration.millis(500), rootPane);
        slide.setFromX(fromX); // Vị trí ban đầu
        slide.setToX(toX);     // Vị trí kết thúc
        slide.setCycleCount(1);
        slide.setAutoReverse(false);
        slide.play();
    }

    /**
     * Hiển thị thông báo lỗi dưới dạng Alert.
     *
     * @param alertType Loại của thông báo (Error, Information, Warning...)
     * @param title     Tiêu đề của thông báo.
     * @param message   Nội dung của thông báo.
     */
    static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
