package com.library.controller.start;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import com.library.dao.UserDao;
import com.library.models.User;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import static com.library.controller.start.LoadView.loadView;
import static com.library.controller.start.ShowView.showAlert;
import static com.library.controller.start.ShowView.showView;

public class LoginUserController {

    @FXML
    private TextField userNameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private CheckBox rememberMeCheckBox;

    @FXML
    private Hyperlink forgotPasswordLink;

    @FXML
    private Hyperlink signUpLink;

    @FXML
    private Button loginButton;

    @FXML
    private HBox rootPane;

    /**
     * login User.
     */

    private final UserDao userDao = new UserDao();
    private Parent root;
    private Scene scene;
    private Stage stage;

    @FXML
    void login(ActionEvent event) throws Exception {
        String username = userNameField.getText().trim();
        String password = passwordField.getText();

        if (validateInput(username, password) && check.isValidUsername(username)) {
            if (userDao.authenticateUser(username, password)) {
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                loadView(stage, "/fxml/User/user_dashboard.fxml", "User Dashboard", "");
                showView(stage, "/fxml/User/user_dashboard.fxml", "User Dashboard", "");


            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Incorrect username or password.");
                clearFields();
            }
        }
    }

    public String getUserFullName() throws Exception {
        User user=userDao.findUserByName(userNameField.getText());
        return user.getFullName();
    }

    private void clearFields() {
        userNameField.clear();
        passwordField.clear();
    }

    /**
     * Xử lý sự kiện khi nhấn vào liên kết 'Sign Up'
     */
    @FXML
    private void openSignUp(ActionEvent event) {
        Stage stage = (Stage) signUpLink.getScene().getWindow();
        loadView(stage, "/fxml/Start/StartUser.fxml", "Sign Up", "/css/register.css");
        showView(stage, "/fxml/Start/StartUser.fxml", "Login", "/css/register.css");
    }

    /**
     * Xử lý kiểm tra thông tin nhập
     */
    private boolean validateInput(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter both username and password.");
            return false;
        }
        return true;
    }

    /**
     * check password
     */
    private boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
