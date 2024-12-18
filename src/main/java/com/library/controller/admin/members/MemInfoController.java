package com.library.controller.admin.members;
import com.library.controller.admin.books.BookInfoController;
import com.library.controller.admin.dashboard.AdminDashboardController;
import com.library.dao.UserDao;
import com.library.models.User;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.library.utils.LoadView.loadView;
import static com.library.dao.UserDao.searchByEmail;
import static com.library.utils.SceneSwitcher.navigateToScene;
import static com.library.utils.SceneSwitcher.showLendBookScene;

public class MemInfoController implements Initializable {

    @FXML
    StackPane memInfoRoot;

    @FXML
    private Label addMem;

    @FXML
    private HBox booksContainer;

    @FXML
    private Button lendButton;

    @FXML
    private ListView<User> memDetailContainer;

    @FXML
    private Label memNameLabel;

    @FXML
    private Label countLabel;

    @FXML
    private ToggleButton allButton;

    @FXML
    private ToggleButton fineButton;

    @FXML
    private HBox overviewContainer;

    @FXML
    private ImageView filter;

    @FXML
    private TextField searchField1;

    @FXML
    private Button logOut;

    private UserDao userDao=new UserDao();
    private List<User> userList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userList = userDao.getAll();
        countLabel.setText(String.valueOf(userList.size()));
        loadMemList();
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
        //filter.setOnMouseClicked(event->showPopup(filter, event));

        searchField1.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.trim().isEmpty()) {
                loadMemList();
            } else {
                handleMemSearch(memDetailContainer, newValue.trim());
            }
        });
    }

    private void loadMemList() {
        Task<List<User>> loadTask = new Task<>() {
            @Override
            protected List<User> call() {
                return userList;
            }
        };

        loadTask.setOnSucceeded(event -> {
            refreshListView(loadTask.getValue());
        });
        loadTask.setOnFailed(event -> {
            System.out.println("fail to load mem info" + loadTask.getException());
        });
        new Thread(loadTask).start();
    }

    private void refreshListView(List<User> users) {
        memDetailContainer.setCellFactory(param->
        {
            return new MemInfoCell();
        });
        memDetailContainer.getItems().setAll(users);
    }

    private void applyFilter(DataLoader loader) {
        Task<List<User>> applyTask = new Task<>() {
            @Override
            protected List<User> call() {
                return loader.load();
            }
        };
        applyTask.setOnSucceeded(event -> {
            List<User> filteredDoc = applyTask.getValue();
            if (filteredDoc.isEmpty()) {
                memDetailContainer.getItems().clear();
            } else {
                refreshListView(filteredDoc);
            }
        });
        applyTask.setOnFailed(event -> {
            System.out.println("fail to load book info" + applyTask.getException());
        });
        new Thread(applyTask).start();
    }

    @FXML
    private void allFilter(ActionEvent event) {
        updateButtonStyles(allButton,fineButton);
        applyFilter(()->userDao.getAll());
    }

    @FXML
    private void fineFilter(ActionEvent event) {
        updateButtonStyles(fineButton, allButton);
        applyFilter(()->userDao.getUserHasFine());
    }

    private void updateButtonStyles(ToggleButton selectedButton, ToggleButton other) {
        selectedButton.getStyleClass().removeAll("chosen-button", "bar-button");
        other.getStyleClass().removeAll("chosen-button", "bar-button");

        selectedButton.getStyleClass().add("chosen-button");
        other.getStyleClass().add("bar-button");
    }

    public void setlogOut(MouseEvent mouseEvent) {
        logOut.setMouseTransparent(true);
        Stage stage = (Stage) logOut.getScene().getWindow();
        loadView(stage, "/fxml/Start/Role.fxml", "Sign Up", "/css/start/Role.css");
    }

    @FunctionalInterface
    interface DataLoader {
        List<User> load();
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

    void handleMemSearch(ListView<User> memDetailContainer, String email) {
        Task<List<User>> searchTask = new Task<>() {

            @Override
            protected List<User> call() throws SQLException {
                return searchByEmail(email);
            }
        };

        searchTask.setOnSucceeded(event -> {
            List<User> searchResult = searchTask.getValue();
            if (!searchResult.isEmpty()) {
                refreshListView(searchResult);
            } else {
                memDetailContainer.getItems().clear();
            }
        });

        searchTask.setOnFailed(event -> {
            System.out.println("fail to load users with this email: "+ email + searchTask.getException());
        });
        new Thread(searchTask).start();
    }


    public void setUserFullName(String userFullName) {
        memNameLabel.setText(userFullName);
    }
}
