package com.library.controller.start;

import javafx.scene.control.Alert;
import org.mindrot.jbcrypt.BCrypt;

import java.util.regex.Pattern;

import static com.library.controller.start.ShowView.showAlert;
//import static com.library.controller.start.ViewLoader.showAlert;

public interface check {

    /**
     * check email
     * @param email email
     */
    static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@(gmail\\.com|[a-zA-Z0-9.-]+\\.edu.vn)$";

    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false; // Trả về false nếu email rỗng hoặc null
        }
        return Pattern.matches(EMAIL_REGEX, email);
    }

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
