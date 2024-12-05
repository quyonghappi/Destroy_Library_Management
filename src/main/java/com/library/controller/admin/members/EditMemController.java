package com.library.controller.admin.members;

import com.library.utils.Check;
import com.library.dao.UserDao;
import com.library.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import static com.library.utils.LoadView.showAlert;

public class EditMemController {

    @FXML
    private TextField userName;

    @FXML
    private TextField currentPassword;

    @FXML
    private TextField newPassword;

    @FXML
    private TextField confirmPassword;

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    private User user = new User();

    public void setUser(User user) {
        this.user = user;
    }

    public void editMem(ActionEvent actionEvent) throws Exception {
        String userNameInput = this.userName.getText();
        String currentPasswordInput = this.currentPassword.getText();
        String newPasswordInput = this.newPassword.getText();
        String confirmPasswordInput = this.confirmPassword.getText();


        if (userNameInput.isEmpty() || currentPasswordInput.isEmpty() || newPasswordInput.isEmpty() || confirmPasswordInput.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter all fields.");
            return;
        }

        // Validate input
        if (!Check.validateInput(userNameInput, currentPasswordInput)) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter both username and password.");
            return;
        }

        if (!Check.isValidUsername(userNameInput)) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter a valid username.");
            return;
        }

        if (!UserDao.authenticateAdmin(userNameInput, currentPasswordInput)) {
            showAlert(Alert.AlertType.ERROR, "Authentication Error", "Invalid current password.");
            return;
        }

        if (!newPasswordInput.equals(confirmPasswordInput)) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Passwords do not match.");
            return;
        }

        // Confirmation dialog
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Edit Information");
        confirmationAlert.setHeaderText("Confirm Member Details");
        confirmationAlert.setContentText("Are you sure you want to update the user's information?\n\n"
                + "User Name: " + userNameInput + "\n"
                + "New Password: (hidden)");

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    user = UserDao.findUserByName(userNameInput);
                    user.setPassword(newPasswordInput);
                    UserDao.updatePassword(user); // Assuming updateUser takes the `User` object as a parameter.
                    showAlert(Alert.AlertType.INFORMATION, "Success", "User information updated successfully.");
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update user information.");
                    e.printStackTrace();
                }
            }
        });

    }
}
