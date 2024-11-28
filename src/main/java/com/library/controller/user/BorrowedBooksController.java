package com.library.controller.user;

import com.library.dao.BorrowingRecordDao;
import com.library.models.BorrowingRecord;
import com.library.utils.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BorrowedBooksController implements Initializable {

    @FXML
    private ListView<BorrowingRecord> borrowListContainer;

    @FXML
    private HBox brNav1;

    @FXML
    private HBox favNav;

    @FXML
    private HBox homeNav;

    @FXML
    private Button logoutButton;

    @FXML
    private Label memNameLabel;

    @FXML
    private HBox requestNav;

    @FXML
    private TextField searchField;

    @FXML
    private HBox searchNav;

    //khi switch scene thi controller will call setUsername method
    private String username;
    private BorrowingRecordDao borrowingRecordDao = new BorrowingRecordDao();
    private List<BorrowingRecord> borrowedBooks = new ArrayList<>();

    public void setUsername(String username) {
        this.username = username;
        System.out.println("Username set: " + username);

        borrowedBooks=borrowingRecordDao.getByUserName(username);
        //System.out.println(borrowedBooks.toString());
        borrowListContainer.setCellFactory(param ->
        {
            BorrowedBooksCell borrowedBooksCell=new BorrowedBooksCell();
            borrowedBooksCell.setListView(borrowListContainer);
            return borrowedBooksCell;
        });
        borrowListContainer.getItems().setAll(borrowedBooks);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //truong hop minh nhan vao mot cell thi navigate to book detail cua cell do
        borrowListContainer.setOnMouseClicked(event -> {
            BorrowingRecord selectedRecord = borrowListContainer.getSelectionModel().getSelectedItem();
            if (selectedRecord != null) {
                BookDetailController controller = SceneSwitcher.navigateToScene("/fxml/User/book_detail.fxml", borrowListContainer);
                if (controller != null) {
                    //tu hoan thanh o phan book detail
                    //controller.setBookDetails(selectedRecord);
                }
            }
        });
    }

    public void setUserFullName(String userFullName) {
        memNameLabel.setText(userFullName);
    }

    @FXML
    public void logout(ActionEvent event) {
        SceneSwitcher.navigateToScene("/fxml/Start/Role.fxml", logoutButton);
    }
}
