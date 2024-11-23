package com.library.controller.start;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class AppController extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        // Sử dụng RoleController để hiển thị màn hình chọn vai trò
        RoleController roleController = new RoleController();
        roleController.showRoleView(primaryStage);
    }

    private void configurePrimaryStage(String title, Scene scene) {
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}