package com.library.controller.books;

import com.library.controller.dashboard.OverdueCell;
import com.library.dao.BorrowingRecordDao;
import com.library.dao.FineDao;
import com.library.models.Fine;
import com.library.utils.SceneSwitcher;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class OverdueBookController implements Initializable {

    @FXML
    private Label addBookButton;

    @FXML
    private ListView<Fine> overdueDetailContainer;

    @FXML
    private HBox booksContainer1;

    @FXML
    private HBox helpContainer;

    @FXML
    private Button lendButton;

    @FXML
    private HBox lentNav;

    @FXML
    private Label memNameLabel;

    @FXML
    private HBox membersContainer;

    @FXML
    private HBox overdueNav;

    @FXML
    private HBox overviewContainer;

    @FXML
    private HBox requestNav;

    @FXML
    private HBox returnNav;

    @FXML
    private HBox allNav;

    @FXML
    private TextField searchField;

    @FXML
    private TextField searchField1;

    @FXML
    private HBox settingContainer;

    private BorrowingRecordDao borrowingRecordDao= new BorrowingRecordDao();
    private FineDao fineDao= new FineDao();
    List<Fine> fines=new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fines=getOverdueList();
        overdueDetailContainer.setCellFactory(param->
        {
            OverdueCell overdueCell = new OverdueCell();
            overdueCell.setListView(overdueDetailContainer);
            return overdueCell;
        });
        overdueDetailContainer.getItems().setAll(fines);
        lentNav.setOnMouseClicked(event ->
        {
            System.out.println("lentNav clicked");
            navigateToLentBookInfo();
        });
        allNav.setOnMouseClicked(event -> navigateToBookInfo());
        requestNav.setOnMouseClicked(event -> navigateToRequestBookInfo());
        returnNav.setOnMouseClicked(event -> navigateToReturnBookInfo());
    }

    private List<Fine> getOverdueList() {
        return fineDao.getAll();
    }

    @FXML
    public void navigateToLentBookInfo() {
        SceneSwitcher.loadSceneAsync("/fxml/Admin/Books/LentBook.fxml").thenAccept(scene -> {
            if (scene != null) {
                Platform.runLater(() -> {
                    Stage stage = (Stage) lentNav.getScene().getWindow();
                    SceneSwitcher.switchScene(stage, scene);
                });
            } else {
                System.out.println("Failed to load LentBook scene.");
            }
        });
    }

    @FXML
    public void navigateToReturnBookInfo() {
        SceneSwitcher.loadSceneAsync("/fxml/Admin/Books/ReturnedBook.fxml").thenAccept(scene -> {
            if (scene != null) {
                Platform.runLater(() -> {
                    Stage stage = (Stage) returnNav.getScene().getWindow();
                    SceneSwitcher.switchScene(stage, scene);
                });
            } else {
                System.out.println("Failed to load ReturnBook scene.");
            }
        });

    }

    @FXML
    public void navigateToBookInfo() {
        SceneSwitcher.loadSceneAsync("/fxml/Admin/Books/BookInfo.fxml").thenAccept(scene -> {
            if (scene != null) {
                Platform.runLater(() -> {
                    Stage stage = (Stage) allNav.getScene().getWindow();
                    SceneSwitcher.switchScene(stage, scene);
                });
            } else {
                System.out.println("Failed to load BookInfo scene.");
            }
        });
    }

    @FXML
    public void navigateToRequestBookInfo() {
        SceneSwitcher.loadSceneAsync("/fxml/Admin/Books/RequestBook.fxml").thenAccept(scene -> {
            if (scene != null) {
                Platform.runLater(() -> {
                    Stage stage = (Stage) requestNav.getScene().getWindow();
                    SceneSwitcher.switchScene(stage, scene);
                });
            } else {
                System.out.println("Failed to load LentBook scene.");
            }
        });
    }

}

