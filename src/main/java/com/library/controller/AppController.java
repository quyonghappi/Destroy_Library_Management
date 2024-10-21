package com.library.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class AppController extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showRoleView();
    }

    public void showRoleView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Role.fxml"));
            HBox root = loader.load();
            Scene scene = new Scene(root, 400,600);

            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());

            primaryStage.setTitle("Library Management - Login");
            primaryStage.setScene(scene);
            primaryStage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public void showLoginView() {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/userLogin.fxml"));
//            StackPane root = loader.load();
//            Scene scene = new Scene(root, 400, 600);
//
//            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());
//
//            primaryStage.setTitle("Library Management - Login");
//            primaryStage.setScene(scene);
//            primaryStage.show();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void showRegisterView() {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Register.fxml"));
//            StackPane root = loader.load();
//            Scene scene = new Scene(root, 400, 600);
//
//            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());
//
//            primaryStage.setTitle("Library Management - Login");
//            primaryStage.setScene(scene);
//            primaryStage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] args) {
        launch(args);
    }
}

