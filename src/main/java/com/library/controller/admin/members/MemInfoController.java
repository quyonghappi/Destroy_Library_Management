package com.library.controller.admin.members;

import com.library.controller.admin.books.BookInfoController;
import com.library.controller.admin.dashboard.AdminDashboardController;
import com.library.dao.UserDao;
import com.library.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.library.utils.FilterPopup.showPopup;
import static com.library.utils.SceneSwitcher.navigateToScene;
import static com.library.utils.SceneSwitcher.showLendBookScene;

public class MemInfoController implements Initializable {

    @FXML
    StackPane memInfoRoot;

    @FXML
    private HBox aboutContainer;

    @FXML
    private Label addMem;

    @FXML
    private HBox booksContainer;

    @FXML
    private HBox helpContainer;

    @FXML
    private Button lendButton;

    @FXML
    private ListView<User> memDetailContainer;

    @FXML
    private Label memNameLabel;

    @FXML
    private HBox membersContainer;

    @FXML
    private HBox overviewContainer;

    @FXML
    private ImageView filter;

    @FXML
    private TextField searchField;

    @FXML
    private TextField searchField1;

    @FXML
    private HBox settingContainer;

    private UserDao userDao=new UserDao();
    List<User> userList=new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userList=getUserList();
        memDetailContainer.setCellFactory(param ->
        {
            MemInfoCell memInfoCell=new MemInfoCell();
            memInfoCell.setListView(memDetailContainer);
            return memInfoCell;
        });
        memDetailContainer.getItems().setAll(userList);

        booksContainer.setOnMouseClicked(event -> {
            String userFullName=memNameLabel.getText();
            BookInfoController controller= navigateToScene("/fxml/Admin/Books/BookInfo.fxml", booksContainer);
            if (controller != null) {
                controller.setUserFullName(userFullName);
            }
        });
        overviewContainer.setOnMouseClicked(event -> {
            String userFullName=memNameLabel.getText();
            AdminDashboardController controller= navigateToScene("/fxml/Admin/Dashboard/adminDashboard.fxml", overviewContainer);
            if (controller != null) {
                controller.setUserFullName(userFullName);
            }
        });
        addMem.setOnMouseClicked(event->showAddMemScene());
        lendButton.setOnAction(event -> showLendBookScene(memInfoRoot));
        filter.setOnMouseClicked(event->showPopup(filter, event));
    }

    public List<User> getUserList(){
        return userDao.getAll();
    }

    @FXML
    private void showAddMemScene() {
        try {
            Rectangle darkBackground = new Rectangle();
            darkBackground.setFill(Color.color(0, 0, 0, 0.7)); //black with 70% opacity
            darkBackground.widthProperty().bind(memInfoRoot.widthProperty());
            darkBackground.heightProperty().bind(memInfoRoot.heightProperty());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Admin/Member/AddMem.fxml"));
            StackPane addMemScene = loader.load();
            addMemScene.setStyle("-fx-background-color: #F8FCFF;");
            memInfoRoot.getChildren().addAll(darkBackground,addMemScene);

            addMemScene.lookup("#cancelButton").setOnMouseClicked(event -> {
                memInfoRoot.getChildren().removeAll(darkBackground, addMemScene);
            });
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("fail to load add mem scene");
        }
    }
    public void setUserFullName(String userFullName) {
        memNameLabel.setText(userFullName);
    }
}
