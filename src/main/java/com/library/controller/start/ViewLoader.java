package com.library.controller.start;

import javafx.stage.Stage;

public interface ViewLoader {

    /**
     * chuyen man hinh co hieu ung.
     * @param stage stage cur
     * @param fxmlPath path fxml
     * @param title title
     * @param stylesheets css
     */
    public void loadView(Stage stage, String fxmlPath, String title, String css, String stylesheets);
}


//
//import javafx.animation.FadeTransition;
//import javafx.animation.TranslateTransition;
//import javafx.scene.Node;
//import javafx.scene.Scene;
//import javafx.scene.control.Alert;
//import javafx.scene.layout.StackPane;
//import javafx.stage.Stage;
//import javafx.util.Duration;
//import javafx.fxml.FXMLLoader;
//import java.io.IOException;
//import java.util.Objects;
//
//public interface ViewLoader {
//
//    //  Fade In
//    public static void fadeInTransition(Node node) {
//        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), node);
//        fadeIn.setFromValue(0.0);
//        fadeIn.setToValue(1.0);
//        fadeIn.play();
//    }
//
//    public static void loadViewWithSlide(Node currentView, Node nextView) {
//        // Đặt node tiếp theo ngoài màn hình bên trái
//        nextView.setTranslateX(-currentView.getScene().getWidth());
//        nextView.setOpacity(0); // Làm cho view tiếp theo trong suốt lúc đầu
//
//        // Thực hiện hiệu ứng trượt view hiện tại
//        TranslateTransition slideOut = new TranslateTransition(Duration.millis(500), currentView);
//        slideOut.setToX(currentView.getScene().getWidth());
//        slideOut.setOnFinished(event -> {
//            // Khi view hiện tại đã trượt hết, ẩn nó và hiển thị view tiếp theo
//            currentView.setOpacity(0);
//        });
//
//        // Thực hiện hiệu ứng trượt view tiếp theo vào
//        TranslateTransition slideIn = new TranslateTransition(Duration.millis(500), nextView);
//        slideIn.setToX(0);  // Trượt view tiếp theo vào từ bên trái
//        slideIn.setFromX(-currentView.getScene().getWidth()); // Đặt nó ngoài màn hình bên trái
//        slideIn.setOnFinished(event -> {
//            // Khi view tiếp theo đã trượt vào hoàn tất, làm cho nó rõ ràng
//            nextView.setOpacity(1);
//        });
//
//        // Bắt đầu các hiệu ứng
//        slideOut.play();
//        slideIn.play();
//    }
//
//
//    // Fade Out
//    public static void fadeOutTransition(Node node) {
//        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), node);
//        fadeOut.setFromValue(1.0);
//        fadeOut.setToValue(0.0);
//        fadeOut.play();
//    }
//
//    // Slide Left
//    public static void slideLeftTransition(Node node) {
//        TranslateTransition slide = new TranslateTransition(Duration.millis(500), node);
//        slide.setFromX(node.getTranslateX());
//        slide.setToX(-node.getScene().getWidth());
//        slide.play();
//    }
//
//    //  Slide Right
//    public static void slideRightTransition(Node node) {
//        TranslateTransition slide = new TranslateTransition(Duration.millis(500), node);
//        slide.setFromX(node.getTranslateX());
//        slide.setToX(node.getScene().getWidth());
//        slide.play();
//    }
//
//    public static void displayViewWithAnimation(Stage stage, String fxmlPath, String title, String cssPath, Node currentRoot) {
//        try {
//            // Áp dụng hiệu ứng fade out cho màn hình hiện tại
//            fadeOutTransition(currentRoot);
//
//            // Sau khi hiệu ứng fade-out hoàn tất, chuyển màn hình
//            FXMLLoader fxmlLoader = new FXMLLoader(ViewLoader.class.getResource(fxmlPath));
//            StackPane newRoot = fxmlLoader.load();
//            Scene scene = new Scene(newRoot, 1200, 700);
//            scene.getStylesheets().add(Objects.requireNonNull(ViewLoader.class.getResource(cssPath)).toExternalForm());
//
//            // Đặt cảnh mới vào stage
//            stage.setScene(scene);
//            stage.centerOnScreen();
//            stage.setTitle(title);
//
//            // Áp dụng hiệu ứng fade-in cho màn hình mới
//            fadeInTransition(newRoot);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // bao loi khi ko chuyen duoc
//     static void showAlert(Alert.AlertType alertType, String title, String message) {
//        Alert alert = new Alert(alertType);
//        alert.setTitle(title);
//        alert.setContentText(message);
//        alert.show();
//    }
//
//
//}
