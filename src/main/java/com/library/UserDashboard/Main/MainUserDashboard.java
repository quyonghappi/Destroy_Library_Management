package com.library.UserDashboard.Main;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;

public class MainUserDashboard extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/start.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 1466, 700);

        // Link the CSS file
        scene.getStylesheets().add(getClass().getResource("/css/styling.css").toExternalForm());

        primaryStage.setTitle("OnlyPipe ;D");
        primaryStage.setScene(scene);
        primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
