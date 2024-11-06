package com.library.controller.start;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    @FXML
    private Label messageLabel;

    public static String role;

    // Enum for Role to avoid using hardcoded strings
    public enum Role {
        USER("/fxml/userLogin.fxml", "/fxml/register.fxml", "User"),
        ADMIN("/fxml/adminLogin.fxml", "/fxml/register.fxml", "Admin");

        private final String loginFxmlPath;
        private final String registerFxmlPath;
        private final String roleName;

        Role(String loginFxmlPath, String registerFxmlPath, String roleName) {
            this.loginFxmlPath = loginFxmlPath;
            this.registerFxmlPath = registerFxmlPath;
            this.roleName = roleName;
        }

        public String getLoginFxmlPath() {
            return loginFxmlPath;
        }

        public String getRegisterFxmlPath() {
            return registerFxmlPath;
        }

        public String getRoleName() {
            return roleName;
        }
    }

    // Method that gets called when the User button is pressed
    @FXML
    private void handleUserButtonAction(ActionEvent event) {
        loadRoleScene(Role.USER);
        role = "Reader";
    }

    // Method that gets called when the Admin button is pressed
    @FXML
    private void handleAdminButtonAction(ActionEvent event) {
        loadRoleScene(Role.ADMIN);
        role = "Admin";
    }

    /**
     * Utility method to load a new FXML scene based on the selected role.
     *
     * @param role The role enum that contains the FXML file path and role name.
     */
    private void loadRoleScene(Role role) {
        try {
            // Load the new scene for the given role
            FXMLLoader loader = new FXMLLoader(getClass().getResource(role.getLoginFxmlPath()));
            HBox root = loader.load();

            // Get the current stage and set the new scene
            Stage stage = (Stage) rootPane.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());
            stage.setScene(scene);
            stage.setMaximized(true);
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setWidth(screenBounds.getWidth());
            stage.setHeight(screenBounds.getHeight());
            stage.centerOnScreen();
            stage.setTitle(role.getRoleName() + " Login");
            stage.show();

            // Optionally, update message label
            messageLabel.setText(role.getRoleName() + " role selected. Please log in.");
        } catch (IOException e) {
            // Update the message label if there is an error loading the scene
            messageLabel.setText("Error loading " + role.getRoleName() + " scene.");
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
}

