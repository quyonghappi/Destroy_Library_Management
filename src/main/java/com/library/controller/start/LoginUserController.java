package com.library.controller.start;

import com.library.config.DatabaseConfig;
import com.library.controller.dashboard.AdminDashboardController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import com.library.dao.UserDao;
import com.library.models.User;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static com.library.controller.start.LoadView.displayViewWithAnimation;
import static com.library.controller.start.LoadView.showAlert;

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

        if (validateInput(username, password)) {
            if (userDao.authenticateUser(username, password)) {
//                hlinh them user de lien ket nha
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/User/Dashboard/userDashboard.fxml"));
//                root = loader.load();

//                UserDashboardController controller = loader.getController();
//                controller.setUserFullName(userDao.getUserFullName(username));

                AdminDashboardController controller = loader.getController();
                controller.setUserFullName(getUserFullName());
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.setMaximized(true);

                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                stage.setWidth(screenBounds.getWidth());
                stage.setHeight(screenBounds.getHeight());
                stage.centerOnScreen();
                stage.show();
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
        displayViewWithAnimation(stage, "/fxml/Start/Register.fxml", "Sign Up", "/css/register.css", rootPane);
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
