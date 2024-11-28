package com.library.controller.start;

import com.library.controller.admin.dashboard.AdminDashboardController;
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
import javafx.stage.Screen;
import javafx.stage.Stage;

import static com.library.controller.start.Check.validateInput;
import static com.library.controller.start.ShowView.showAlert;

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

    //good good :3
    //to tach thanh login cua admin -> chuyen sang man hinh admin
    @FXML
    void login(ActionEvent event) throws Exception {
        String username = userNameField.getText().trim();
        String password = passwordField.getText();

        if (validateInput(username, password)) {
            if (userDao.authenticateAdmin(username,password)) {
                userDao.updateLastLogin(username);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Admin/Dashboard/adminDashboard.fxml"));
                root = loader.load();
                AdminDashboardController controller = loader.getController();
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
                showAlert(AlertType.ERROR, "Login Failed", "Incorrect username or password.");
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
}
