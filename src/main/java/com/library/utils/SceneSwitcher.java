package com.library.utils;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneSwitcher {
    public static void switchScene(Stage stage, Scene newScene) {
        if (newScene == null) {
            System.out.println("pls wait");
            return;
        }

        stage.setScene(newScene);
        stage.setMaximized(true);
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setWidth(screenBounds.getWidth());
        stage.setHeight(screenBounds.getHeight());
        stage.centerOnScreen();
    }

    public static <T> T navigateToScene(String path, Node container) {
        try {
            Stage stage = (Stage) container.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource(path));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            switchScene(stage, scene);
            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load scene: " + path);
            return null;
        }

    }

    //navigate to lend book scene by lend book button in sidebar
    public static void showLendBookScene(StackPane root) {
        try {
            Rectangle darkBackground = new Rectangle();
            darkBackground.setFill(Color.color(0, 0, 0, 0.7)); //black with 70% opacity
            darkBackground.widthProperty().bind(root.widthProperty());
            darkBackground.heightProperty().bind(root.heightProperty());
            FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource("/fxml/Admin/Dashboard/LendBook.fxml"));
            StackPane lendBookScene = loader.load();
            lendBookScene.setStyle("-fx-background-color: #F8FCFF");
            root.getChildren().addAll(darkBackground,lendBookScene);

            lendBookScene.lookup("#cancelButton").setOnMouseClicked(event -> {
                root.getChildren().removeAll(darkBackground, lendBookScene);
            });
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("fail to load lend book scene");
        }
    }

    /*navigate to add book scene by clicking on
    add book label in all scenes about books
     */
    public static void showAddBookScene(StackPane root) {
        //create dark layer overlay previous scene to emphasize new scene
        try {
            Rectangle darkBackground = new Rectangle();
            darkBackground.setFill(Color.color(0, 0, 0, 0.7)); //black with 70% opacity
            darkBackground.widthProperty().bind(root.widthProperty());
            darkBackground.heightProperty().bind(root.heightProperty());
            FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource("/fxml/Admin/Books/AddBook.fxml"));
            StackPane addBookScene = loader.load();
            addBookScene.setStyle("-fx-background-color: #F8FCFF");
            root.getChildren().addAll(darkBackground, addBookScene);
            addBookScene.lookup("#cancelButton").setOnMouseClicked(event -> {
                root.getChildren().removeAll(darkBackground, addBookScene);
            });
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("fail to load add book scene");
        }
    }
}
