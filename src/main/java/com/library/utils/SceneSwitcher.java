package com.library.utils;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

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

    public static CompletableFuture<Scene> loadSceneAsync(String fxmlPath) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource(fxmlPath));
                Parent root = loader.load();
                //System.out.println("Loaded scene: " + fxmlPath); //debug
                return new Scene(root);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to load scene: " + fxmlPath); //debug
                return null;
            }
        });
    }

}
