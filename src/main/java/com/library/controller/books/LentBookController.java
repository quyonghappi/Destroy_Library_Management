package com.library.controller.books;

import com.library.dao.BorrowingRecordDao;
import com.library.models.BorrowingRecord;
import com.library.utils.SceneSwitcher;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
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

public class LentBookController implements Initializable {

    @FXML
    private Label addBookButton;

    @FXML
    private Label allCountLabel;

    @FXML
    private HBox booksContainer1;

    @FXML
    private Button lendButton;

    @FXML
    private Label lentCountLabel;

    @FXML
    private ListView<BorrowingRecord> lentDetailContainer;

    @FXML
    private HBox allNav;

    @FXML
    private Label memNameLabel;

    @FXML
    private HBox membersContainer;

    @FXML
    private Label overdueCountLabel;

    @FXML
    private HBox overdueNav;

    @FXML
    private HBox overviewContainer;

    @FXML
    private Label requestCountLabel;

    @FXML
    private HBox requestNav;

    @FXML
    private HBox returnNav;

    @FXML
    private Label returnedCountLabel;

    @FXML
    private TextField searchField;

    @FXML
    private TextField searchField1;

    private BorrowingRecordDao borrowingRecordDao=new BorrowingRecordDao();
    List<BorrowingRecord> brList=new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        brList=getBrList();
        lentDetailContainer.setCellFactory(param ->
        {
            LentBookCell lentBookCell=new LentBookCell();
            lentBookCell.setListView(lentDetailContainer);
            return lentBookCell;
        });
        lentDetailContainer.getItems().setAll(brList);

        allNav.setOnMouseClicked(event -> navigateToBookInfo());
        overdueNav.setOnMouseClicked(event -> navigateToOverdueBookInfo());
        requestNav.setOnMouseClicked(event -> navigateToRequestBookInfo());
        returnNav.setOnMouseClicked(event -> navigateToReturnBookInfo());
    }

    private List<BorrowingRecord> getBrList() {
        return borrowingRecordDao.getAll();

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
    public void navigateToOverdueBookInfo() {
        SceneSwitcher.loadSceneAsync("/fxml/Admin/Books/OverdueBook.fxml").thenAccept(scene -> {
            if (scene != null) {
                Platform.runLater(() -> {
                    Stage stage = (Stage) overdueNav.getScene().getWindow();
                    SceneSwitcher.switchScene(stage, scene);
                });
            } else {
                System.out.println("Failed to load overdueBook scene.");
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
                System.out.println("Failed to load requestBook scene.");
            }
        });
    }
}
