package com.library.UserDashboard.Run;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MDash extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            // Load FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/User/search_screen.fxml"));
            Parent root = loader.load();

            // Create scene with root node
            Scene scene = new Scene(root, 1466, 750);

            // Add CSS styling
            scene.getStylesheets().add(getClass().getResource("/css/styling.css").toExternalForm());

            // Configure primary stage
            primaryStage.setTitle("OnlyPipe ;D");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading FXML or initializing primary stage.");
        }
    }

    public static void main(String[] args) {
        launch(args); // Launch JavaFX application
    }
}
