package com.library.controller.admin.books;

import com.library.controller.admin.dashboard.AdminDashboardController;
import com.library.controller.admin.members.MemInfoController;
import com.library.dao.BorrowingRecordDao;
import com.library.models.BorrowingRecord;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

//import static com.library.utils.FilterPopup.showPopup;
import static com.library.utils.SceneSwitcher.*;

public class LentBookController implements Initializable {

    @FXML
    StackPane lentBookRoot;

    @FXML
    private Label addBookButton;

    @FXML
    private Label allCountLabel;

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
    private ImageView filter;

    @FXML
    private Label returnedCountLabel;

    @FXML
    private TextField searchField;

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
//        filter.setOnMouseClicked(event->showPopup(filter, event));
        addBookButton.setOnMouseClicked(event->showAddBookScene(lentBookRoot));
    }

    private List<BorrowingRecord> getBrList() {
        return borrowingRecordDao.getAll();
    }

    public void setUserFullName(String userFullName) {
        memNameLabel.setText(userFullName);
    }

}
