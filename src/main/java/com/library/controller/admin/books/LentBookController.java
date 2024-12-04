package com.library.controller.admin.books;

import com.library.controller.admin.dashboard.AdminDashboardController;
import com.library.controller.admin.members.MemInfoController;
import com.library.dao.BorrowingRecordDao;
import com.library.models.BorrowingRecord;
import com.library.utils.Filter1;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

//import static com.library.utils.FilterPopup.showPopup;
import static com.library.controller.start.LoadView.loadView;
import static com.library.controller.start.LoadView.showAlert;
import static com.library.controller.start.ShowView.showView;
import static com.library.utils.SceneSwitcher.*;

public class LentBookController implements Initializable {

    @FXML
    StackPane lentBookRoot;

    @FXML
    private Label addBookButton;

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
    private HBox overdueNav;

    @FXML
    private HBox overviewContainer;

    @FXML
    private HBox requestNav;

    @FXML
    private HBox returnNav;

    @FXML
    private  TextField searchField1;

    @FXML
    private Button logOut;

    @FXML
    private Label countLabel;

    private BorrowingRecordDao borrowingRecordDao=new BorrowingRecordDao();
    List<BorrowingRecord> brList=new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateBorrowingCount();
        brList=getBrList();
        lentDetailContainer.setCellFactory(param ->
        {
            LentBookCell lentBookCell=new LentBookCell();
            lentBookCell.setListView(lentDetailContainer);
            return lentBookCell;
        });
        lentDetailContainer.getItems().setAll(brList);

        allNav.setOnMouseClicked(event -> {
            String userFullName=memNameLabel.getText();
            BookInfoController controller = navigateToScene("/fxml/Admin/Books/BookInfo.fxml", allNav);
            if (controller != null) {
                controller.setUserFullName(userFullName);
            }
        });
        overdueNav.setOnMouseClicked(event -> {
            String userFullName=memNameLabel.getText();
            OverdueBookController controller=navigateToScene("/fxml/Admin/Books/OverdueBook.fxml", overdueNav);
            if (controller != null) {
                controller.setUserFullName(userFullName);
            }
        });
        requestNav.setOnMouseClicked(event -> {
            String userFullName=memNameLabel.getText();
            RequestBookController controller=navigateToScene("/fxml/Admin/Books/RequestBook.fxml", requestNav);
            if (controller != null) {
                controller.setUserFullName(userFullName);
            }
        });
        returnNav.setOnMouseClicked(event -> {
            String userFullName=memNameLabel.getText();
            ReturnBookController controller=navigateToScene("/fxml/Admin/Books/ReturnedBook.fxml", returnNav);
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
        membersContainer.setOnMouseClicked(event-> {
            String userFullName=memNameLabel.getText();
            MemInfoController controller= navigateToScene("/fxml/Admin/Member/MemInfo.fxml", membersContainer);
            if (controller != null) {
                controller.setUserFullName(userFullName);
            }
        });
        lendButton.setOnMouseClicked(event->showLendBookScene(lentBookRoot));
//        filter.setOnMouseClicked(event-> Filter1.showPopup(filter, event));
        addBookButton.setOnMouseClicked(event->showAddBookScene(lentBookRoot));


        searchField1.textProperty().addListener((observable, oldValue, newValue) -> {
            searchBorrowing(newValue);
        });

    }

    public void updateBorrowingCount() {
        List<BorrowingRecord> bookList = borrowingRecordDao.getLent();
        countLabel.setText(String.valueOf(bookList.size()));
    }

    private List<BorrowingRecord> getBrList() {
        return borrowingRecordDao.getLent();
    }

    public void setUserFullName(String userFullName) {
        memNameLabel.setText(userFullName);
    }


    public void searchBorrowing(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            brList = borrowingRecordDao.getLent();
        } else {
            brList = borrowingRecordDao.searchByIsbn(isbn.trim());
        }
        lentDetailContainer.setCellFactory(param ->
        {
            LentBookCell lentBookCell=new LentBookCell();
            lentBookCell.setListView(lentDetailContainer);
            return lentBookCell;
        });
        lentDetailContainer.getItems().setAll(brList);

    }
    public void setLogOut(MouseEvent mouseEvent) {
        logOut.setMouseTransparent(true);
        Stage stage = (Stage) logOut.getScene().getWindow();
        loadView(stage, "/fxml/Start/Role.fxml", "Sign Up", "/css/start/Role.css");
        showView(stage, "/fxml/Start/Role.fxml", "Login", "/css/start/Role.css");
    }
}
