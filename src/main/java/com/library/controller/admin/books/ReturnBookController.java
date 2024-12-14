package com.library.controller.admin.books;

import com.library.controller.admin.dashboard.AdminDashboardController;
import com.library.controller.admin.members.MemInfoController;
import com.library.dao.BorrowingRecordDao;
import com.library.models.BorrowingRecord;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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
import static com.library.utils.LoadView.loadView;
import static com.library.utils.SceneSwitcher.*;

public class ReturnBookController implements Initializable {

    @FXML
    StackPane returnedBookRoot;

    @FXML
    private Label addBookButton;

    @FXML
    private Button lendButton;

    @FXML
    private ListView<BorrowingRecord> returnDetailContainer;

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
    private HBox lentNav;

    @FXML
    private ImageView filter;

    @FXML
    private Label returnedCountLabel;

    @FXML
    private TextField searchField1;

    @FXML
    private Button logOut;

    @FXML
    private Label countLabel;

    @FXML
    private  Label logout;

    private BorrowingRecordDao borrowingRecordDao=new BorrowingRecordDao();
    List<BorrowingRecord> brList=new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        brList=getBrList();
        countLabel.setText(String.valueOf(brList.size()));

        loadReturnList();
        allNav.setOnMouseClicked(event -> {
            String userFullName=memNameLabel.getText();
            BookInfoController controller = navigateToScene("/fxml/Admin/Books/BookInfo.fxml", allNav);
            if (controller != null) {
                controller.setUserFullName(userFullName);
            }
        });
        lentNav.setOnMouseClicked(event -> {
            String userFullName=memNameLabel.getText();
            LentBookController controller = navigateToScene("/fxml/Admin/Books/LentBook.fxml", lentNav);
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
        overviewContainer.setOnMouseClicked(event -> {
            String userFullName=memNameLabel.getText();
            AdminDashboardController controller= navigateToScene("/fxml/Admin//BookInfo.fxml", membersContainer);
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
        lendButton.setOnMouseClicked(event-> showLendBookScene(returnedBookRoot));
//        filter.setOnMouseClicked(event->showPopup(filter, event));
        addBookButton.setOnMouseClicked(event->showAddBookScene(returnedBookRoot));


        searchField1.textProperty().addListener((observable, oldValue, newValue) -> {
            searchReturn(newValue);
        });
    }

    private void loadReturnList() {
        Task<List<BorrowingRecord>> loadTask = new Task<>() {
            @Override
            protected List<BorrowingRecord> call() throws Exception {
                return brList;
            }
        };
        loadTask.setOnSucceeded(
                event-> {
                    returnDetailContainer.setCellFactory(param ->
                    {
                        ReturnBookCell returnBookCell=new ReturnBookCell();
                        return returnBookCell;
                    });
                    returnDetailContainer.getItems().setAll(loadTask.getValue());
                }
        );
        loadTask.setOnFailed(
                event-> {
                    System.out.println("failed to load return list"+loadTask.getException().getMessage());
                }
        );
        new Thread(loadTask).start();
    }

    private void searchReturn(String isbn) {
        if(isbn.trim().isEmpty()) {
            brList = borrowingRecordDao.getReturned();
            returnDetailContainer.getItems().setAll(brList);
        }

        brList = borrowingRecordDao.getReturnByISBN(isbn);
        returnDetailContainer.setCellFactory(param ->
        {
            return new ReturnBookCell();
        });
        returnDetailContainer.getItems().setAll(brList);
    }

    private List<BorrowingRecord> getBrList() {
        return borrowingRecordDao.getReturned();

    }

    public void setUserFullName(String userFullName) {
        memNameLabel.setText(userFullName);
    }


    public void setLogOut(MouseEvent mouseEvent) {
        logOut.setMouseTransparent(true);
        Stage stage = (Stage) logOut.getScene().getWindow();
        loadView(stage, "/fxml/Start/Role.fxml", "Sign Up", "/css/start/Role.css");
    }
}
