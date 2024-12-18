package com.library.controller.user;

import com.library.dao.BorrowingRecordDao;
import com.library.dao.DocumentDao;
import com.library.dao.FineDao;
import com.library.models.BorrowingRecord;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import static com.library.utils.SceneSwitcher.navigateToScene;
import static com.library.utils.SceneSwitcher.showBookDetails;

public class BorrowedBooksController implements Initializable {

    @FXML
    private StackPane borrowedBooksRoot;

    @FXML
    private ListView<BorrowingRecord> borrowListContainer;

    @FXML
    private HBox favNav;

    @FXML
    private HBox homeNav;

    @FXML
    private HBox logout;

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
    private DocumentDao documentDao =DocumentDao.getInstance();

    public void setUsername(String username) {
        this.username = username;
        //System.out.println("Username set: " + username);
        loadBorrowedList();

    }

    private void loadBorrowedList() {
        Task<List<BorrowingRecord>> loadTask = new Task<>() {
            @Override
            protected List<BorrowingRecord> call() {
                List<BorrowingRecord> myBookList = borrowingRecordDao.getByUserName(username);
                FineDao fineDao = FineDao.getInstance();
                for (BorrowingRecord book : myBookList) {
                    fineDao.checkAndAddFine(book);
                }
                return myBookList;
            }
        };
        loadTask.setOnSucceeded(event -> {
            borrowListContainer.setCellFactory(param ->
            {
                return new BorrowedBooksCell();

            });
            borrowListContainer.getItems().setAll(loadTask.getValue());
        });
        loadTask.setOnFailed(event -> {
            System.out.println("fail to load user " + username +" borrowed books" + loadTask.getException());
        });
        new Thread(loadTask).start();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logout.setOnMouseClicked(event -> navigateToScene("/fxml/Start/Role.fxml", logout));
        //truong hop minh nhan vao mot cell thi navigate to book detail cua cell do
        borrowListContainer.setOnMouseClicked(event -> {

            if (event.getClickCount() == 2) {
                BorrowingRecord selectedItem = borrowListContainer.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    showBookDetails(borrowedBooksRoot,username,documentDao.get(selectedItem.getISBN()));
                }
            }
        });

        homeNav.setOnMouseClicked(event -> {
            String userFullName= memNameLabel.getText();
            HomeScreenController controller = navigateToScene("/fxml/User/home_screen.fxml", homeNav);
            if (controller != null) {
                controller.setUsername(username);
                controller.setUserFullName(userFullName);
            }
        });

        searchNav.setOnMouseClicked(event -> {
            String userFullName= memNameLabel.getText();
            SearchBooksScreenController controller = navigateToScene("/fxml/User/search_books_screen.fxml", searchNav);
            if (controller != null) {
                controller.setUserFullName(userFullName);
                controller.setUsername(username);
            }
        });

        favNav.setOnMouseClicked(event -> {
            String userFullName= memNameLabel.getText();
            FavouriteController controller = navigateToScene("/fxml/User/FavouriteBooks.fxml", favNav);
            if (controller != null) {
                controller.setUserFullName(userFullName);
                controller.setUsername(username);
            }
        });

        requestNav.setOnMouseClicked(event -> {
            String userFullName= memNameLabel.getText();
            UserRequestController controller = navigateToScene("/fxml/User/user_request.fxml", requestNav);
            if (controller != null) {
                controller.setUsername(username);
                controller.setUserFullName(userFullName);
            }
        });
    }

    public void setUserFullName(String userFullName) {
        memNameLabel.setText(userFullName);
    }


    @FXML
    private TextField brSearchField;

    @FXML
    private void onSearchBorrow() {
        String query = brSearchField.getText().trim();

        if (!query.isEmpty()) {
            String userFullName= memNameLabel.getText();
            SearchBooksScreenController searchController = navigateToScene("/fxml/User/search_books_screen.fxml", brSearchField);

            if (searchController != null) {
                searchController.performSearch(query);
                searchController.setUsername(username);
                searchController.setUserFullName(userFullName);
            }
        }
    }
}
