package com.library.controller.members;

import com.library.dao.UserDao;
import com.library.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
            userDao.add(new User(email, fullName, username, username));
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
