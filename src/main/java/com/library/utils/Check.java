package com.library.utils;

import javafx.scene.control.Alert;
import java.util.regex.Pattern;
import static com.library.utils.LoadView.showAlert;

public class Check {


    static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@(gmail\\.com|[a-zA-Z0-9.-]+\\.edu.vn)$"; // gamil sv hoac gmail thuong
    static final String FULLNAME_REGEX = "^[A-Z][a-zA-Z]*(?: [A-Z][a-zA-Z]*){0,4}$"; // full name gom chu hoa dau va cach
    static final String USERNAME_REGEX = "^[a-zA-Z0-9_-]+$"; // username ko dau ko cach


    /**
     * Check email
     * @param email email
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false; // Trả về false nếu email rỗng hoặc null
        }
        return Pattern.matches(EMAIL_REGEX, email);
    }

    /**
     * Check fullName
     * @param fullName ten nguoi dung
     * @return true or false
     */
    public static boolean isValidFullName(String fullName) {
        if(fullName == null || fullName.isEmpty()) {
            return false;
        }
        return Pattern.matches(FULLNAME_REGEX, fullName);
    }

    /**
     * Check userName
     * @param username user
     * @return
     */
    public static boolean isValidUsername(String username) {
        if(username == null || username.isEmpty()) {
            return false;
        }
        return Pattern.matches(USERNAME_REGEX, username);
    }


    public static boolean validateInput(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter both username and password.");
            return false;
        }
        return true;
    }
}
