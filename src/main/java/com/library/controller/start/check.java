package com.library.controller.start;

import javafx.scene.control.Alert;
import org.mindrot.jbcrypt.BCrypt;

import static com.library.controller.start.LoadView.showAlert;

public interface check {

    static boolean validateInput(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter both username and password.");
            return false;
        }
        return true;
    }

    static boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }

}
