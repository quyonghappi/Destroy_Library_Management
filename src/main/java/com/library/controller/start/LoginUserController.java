package com.library.controller.start;

import com.library.controller.user.HomeScreenController;
import com.library.utils.Check;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import static com.library.utils.Check.validateInput;
import static com.library.utils.LoadView.loadView;
import static com.library.utils.LoadView.showAlert;
import static com.library.dao.UserDao.findUserByName;

public class LoginUserController {

    @FXML
    public TextField userNameField;

    @FXML
    public PasswordField passwordField;

    @FXML
    private Hyperlink signUpLink;

    @FXML
    private Button loginButton;

    @FXML
    private HBox rootPane;

    @FXML
    private ImageView returnBack;

    /**
     * login User.
     */

    public UserDao userDao = new UserDao();
    private Parent root;
    private Scene scene;
    public Stage stage;

    @FXML
    void login(ActionEvent event) throws Exception {
        String username = userNameField.getText().trim();
        String password = passwordField.getText();

        User user = findUserByName(username);
        if (user == null) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Incorrect username.");
            return;
        }
        if (!user.getAccountStatus().equals("active")) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Your account has been locked.");
            return;
        }

        if (validateInput(username, password) && Check.isValidUsername(username)) {
            if (UserDao.authenticateUser(username, password)) {
                userDao.updateLastLogin(username);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/User/home_screen.fxml"));
                root = loader.load();
                HomeScreenController controller = loader.getController();
                controller.setUsername(username);
                controller.setUserFullName(getUserFullName());
                stage=(Stage)((Node)event.getSource()).getScene().getWindow();
                scene=new Scene(root);
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
        User user = findUserByName(userNameField.getText());
        assert user != null;
        return user.getFullName();
    }

    private void clearFields() {
        userNameField.clear();
        passwordField.clear();
    }

    @FXML
    private void openSignUp(ActionEvent event) {
        Stage stage = (Stage) signUpLink.getScene().getWindow();
        loadView(stage, "/fxml/Start/Register.fxml", "Sign Up", "/css/start/register.css");
    }

    public void setReturnback(MouseEvent mouseEvent) {
        returnBack.setMouseTransparent(true);
        Stage stage = (Stage) returnBack.getScene().getWindow();
        loadView(stage, "/fxml/Start/Role.fxml", "Sign Up", "/css/start/Role.css");
    }
}