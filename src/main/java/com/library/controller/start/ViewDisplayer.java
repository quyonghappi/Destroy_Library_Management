package com.library.controller.start;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.Objects;

public interface ViewDisplayer {

    /**
     * chuyen man hinh ko co hieu ung
     * @param stage stage cur
     * @param fxmlPath fxml
     * @param title title
     * @param stylesheets css
     */
    public void showView(Stage stage, String fxmlPath, String title, String stylesheets);

//
//    static void displayView(Stage stage, String fxmlPath, String title, String cssPath) {
//        try {
//            FXMLLoader fxmlLoader = new FXMLLoader(ViewDisplayer.class.getResource(fxmlPath));
//            StackPane root = fxmlLoader.load();
//            Scene scene = new Scene(root, 1200, 700);
//            scene.getStylesheets().add(Objects.requireNonNull(ViewDisplayer.class.getResource(cssPath)).toExternalForm());
//            stage.setScene(scene);
//            stage.setResizable(true);
//            stage.centerOnScreen();
//            stage.setTitle(title);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}