package com.library.controller.user.Run;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.IOException;

public class MDash extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            // Load FXML file for the initial scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/User/home_screen.fxml"));
            Parent root = loader.load();

            // Create the scene with the root node loaded from FXML
            Scene scene = new Scene(root, 1466, 750);

            // Add the external CSS stylesheet to style the UI
            scene.getStylesheets().add(getClass().getResource("/css/start/styling.css").toExternalForm());

            // Set the scene and stage properties
            primaryStage.setTitle("OnlyPipe ;D");  // Set the window title
            //primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/icon.png")));  // Optional: Add an icon
            primaryStage.setScene(scene);

            // Show the stage (window)
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading FXML or initializing primary stage.");
            showErrorAndExit(primaryStage);
        }
    }

    private void showErrorAndExit(Stage primaryStage) {
        // Optionally, show an error message or a pop-up before closing
        System.err.println("Critical error: Could not load the application. Exiting...");
        primaryStage.close();  // Close the primary stage if loading fails
    }

    public static void main(String[] args) {
        launch(args);  // Launch the JavaFX application
    }
}
