package com.library.controller.start;

import com.library.controller.dashboard.AdminDashboardController;
import com.library.dao.UserDao;
import com.library.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoginController {

    @FXML
    private TextField userNameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    protected Hyperlink signUpLink;

    private final UserDao userDao = new UserDao();
    private Parent root;
    private Scene scene;
    private Stage stage;
    static final int SCENE_WIDTH = 400;
    static final int SCENE_HEIGHT = 600;

    @FXML
    void login(ActionEvent event) throws Exception {
        String username = userNameField.getText().trim();
        String password = passwordField.getText();

        if (validateInput(username, password)) {
            if (userDao.authenticate(username,password)) {
                //showAlert(AlertType.INFORMATION, "Success", "Login successful!");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Admin/Dashboard/adminDashboard.fxml"));
                root = loader.load();
                AdminDashboardController controller = loader.getController();
                controller.setUserFullName(getUserFullName());
                stage=(Stage)((Node)event.getSource()).getScene().getWindow();
                scene=new Scene(root);
                stage.setScene(scene);
                stage.setMaximized(true);
                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                stage.setWidth(screenBounds.getWidth());
                stage.setHeight(screenBounds.getHeight());
                stage.centerOnScreen();
                stage.show();
            } else {
                showAlert(AlertType.ERROR, "Login Failed", "Incorrect username or password.");
                clearFields();
            }
        }
    }

    @FXML
    void openSignUp(ActionEvent event) {
        loadScene("/fxml/Register.fxml", "Sign Up");
    }

    private boolean validateInput(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.ERROR, "Input Error", "Please enter both username and password.");
            return false;
        }
        return true;
    }

    //already have similar method in userDao

    // Utility method to load new scenes
    private void loadScene(String fxmlPath, String title) {
        try {
            Stage stage = (Stage) signUpLink.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            StackPane root = fxmlLoader.load();
            Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.centerOnScreen();
            stage.setTitle(title);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Loading Error", "Unable to load the " + title + " page.");
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    private void clearFields() {
        userNameField.clear();
        passwordField.clear();
    }


//    dashboard
//    private void navigateToDashboard() {
//        try {
//            Stage stage = (Stage) loginButton.getScene().getWindow();
//            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Dashboard.fxml"));
//            StackPane root = (StackPane) fxmlLoader.load();
//            Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
//            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/Dashboard.css")).toExternalForm());
//            stage.setScene(scene);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    public String getUserFullName() throws Exception {
        User user=userDao.findUserByName(userNameField.getText());
        return user.getFullName();
    }
}