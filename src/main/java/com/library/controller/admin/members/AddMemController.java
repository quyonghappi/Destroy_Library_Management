package com.library.controller.admin.members;

import com.library.dao.UserDao;
import com.library.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

public class AddMemController {
    @FXML
    private StackPane root;

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;

    @FXML
    private TextField emailField;

    @FXML
    private TextField fullnameField;

    @FXML
    private TextField usernameField;

    private UserDao userDao = new UserDao();

    @FXML
    private void addMem(ActionEvent event) {
        String email = emailField.getText();
        String fullName = fullnameField.getText();
        String username = usernameField.getText();

        if (validateInput(email, fullName, username)) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirm Add Member");
            confirmationAlert.setHeaderText("Confirm Member Details");
            confirmationAlert.setContentText("Are you sure you want to add this member: \n\n" +
                    "Email: " + email + "\n" +
                    "Full name: " + fullName + "\n" +
                    "username: " + username);
            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    User user = new User(fullName, username, email, username);
                    user.setUserRole("admin");
                    userDao.add(user);
                }
            });
        }
    }

    private boolean validateInput(String username, String email, String fullName) {
        if (username.isEmpty() || fullName.isEmpty() || email.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("Add member fail !");
            alert.setContentText("Please enter necessary information.");
            alert.showAndWait();
            return false;
        }
        return true;
    }
}
