package com.library.controller.start;

import com.library.config.DatabaseConfig;
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

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.stage.Screen;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import static com.library.controller.start.LoadView.loadView;
import static com.library.controller.start.LoadView.showAlert;
import static com.library.controller.start.ShowView.showView;
import static com.library.controller.start.check.checkPassword;
import static com.library.controller.start.check.validateInput;

public class LoginAdminController {

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

    // to tach thanh login cua admin -> chuyen sang man hinh admin
    @FXML
    void login(ActionEvent event) throws Exception {
        String username = userNameField.getText().trim();
        String password = passwordField.getText();

//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/book_viewer.fxml"));
//            Parent bookView = loader.load();
//
//            Stage stage = (Stage) libraryButton.getScene().getWindow();
//            Scene scene = new Scene(bookView, 1466, 700);
//            scene.getStylesheets().add(getClass().getResource("/css/styling.css").toExternalForm());
//
//            stage.setScene(scene);
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        if (validateInput(username, password) && check.isValidUsername(username)) {
            if (userDao.authenticateAdmin(username,password)) {
                stage=(Stage)((Node)event.getSource()).getScene().getWindow();
                loadView(stage,"/fxml/Admin/Dashboard/adminDashboard.fxml", "Admin Dashboard", "/css/AdminDashboardStyling.css");
                showView(stage,"/fxml/Admin/Dashboard/adminDashboard.fxml", "Admin Dashboard", "/css/AdminDashboardStyling.css");
            } else if (!check.isValidUsername(username)) {
                showAlert(AlertType.ERROR, "Login Failed", "Incorrect username or password.");
                clearFields();
            } else if (check.isValidUsername(username)) {
                showAlert(AlertType.ERROR, "Login Failed", "username is already taken.");
            }
        }
    }


    public String getUserFullName() throws Exception {
        User user=userDao.findUserByName(userNameField.getText());
        return user.getFullName();
    }


    private boolean validateInput(String adminName, String password) {
        if (adminName.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.ERROR, "Input Error", "Please enter both username and password.");
            return false;
        }
        return true;
    }

    /**
     * check password.
     * @param password p
     * @param hashedPassword hp
     * @return check
     */
    private boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
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
}
