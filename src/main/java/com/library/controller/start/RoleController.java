package com.library.controller.start;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class RoleController {

    @FXML
    private HBox rootPane;

    @FXML
    private Button userButton;

    @FXML
    private Button adminButton;

    public static String role;

    // Enum to define roles with associated FXML paths for login screens
    public enum Role {
        USER("/fxml/Start/userLogin.fxml", "User"),
        ADMIN("/fxml/Start/adminLogin.fxml", "Admin");

        private final String loginFxmlPath;
        private final String roleName;

        Role(String loginFxmlPath, String roleName) {
            this.loginFxmlPath = loginFxmlPath;
            this.roleName = roleName;
        }

        public String getLoginFxmlPath() {
            return loginFxmlPath;
        }

        public String getRoleName() {
            return roleName;
        }
    }

    /**
     * Method called when the User button is pressed
     */
    @FXML
    private void handleUserButtonAction(ActionEvent event) {
        chooseRole(Role.USER);
    }

    /**
     * Method called when the Admin button is pressed
     */
    @FXML
    private void handleAdminButtonAction(ActionEvent event) {
        chooseRole(Role.ADMIN);
    }

    /**
     * This method sets the role based on the button clicked
     * and loads the appropriate login screen.
     *
     * @param selectedRole The selected role (User or Admin)
     */
    private void chooseRole(Role selectedRole) {
        role = selectedRole.getRoleName(); // Set the role name globally for other parts of the app
        loadRoleScene(selectedRole); // Load the login scene based on selected role
    }

    /**
     * Utility method to load a new FXML scene based on the selected role.
     *
     * @param role The role enum that contains the FXML file path and role name.
     */
    private void loadRoleScene(Role role) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(role.getLoginFxmlPath()));
            HBox root = loader.load();

            // Get the current stage and set the new scene
            Stage stage = (Stage) rootPane.getScene().getWindow();
            Scene scene = new Scene(root);

            // add css
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/UserLogin.css")).toExternalForm());
            stage.setScene(scene);
            stage.setMaximized(true);
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setWidth(screenBounds.getWidth());
            stage.setHeight(screenBounds.getHeight());
            stage.centerOnScreen();
            stage.setTitle(role.getRoleName() + " Login");
            stage.show();
        } catch (IOException e) {
            showErrorDialog("Error", "Failed to load " + role.getRoleName() + " login screen.");
            e.printStackTrace();
        }
    }

    /**
     * Show an error dialog to notify the user in case of errors.
     *
     * @param title   Title of the error dialog.
     * @param message Message content of the error dialog.
     */
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void showRoleView(Stage primaryStage) {
        try {

//            displayViewWithAnimation()
            FXMLLoader loader = new FXMLLoader(RoleController.class.getResource("/fxml/Start/Role.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/Role.css")).toExternalForm());
            primaryStage.setTitle("Library Management");
            primaryStage.setScene(scene);
            primaryStage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
