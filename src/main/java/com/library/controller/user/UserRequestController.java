package com.library.controller.user;

import com.library.dao.ReservationDao;
import com.library.models.Reservation;
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
import java.util.List;
import java.util.ResourceBundle;

import static com.library.utils.SceneSwitcher.navigateToScene;

public class UserRequestController implements Initializable {
    @FXML
    private HBox brNav;

    @FXML
    private HBox favNav;

    @FXML
    private HBox homeNav;

    @FXML
    private HBox logout;

    @FXML
    private Label memNameLabel;

    @FXML
    private ListView<Reservation> requestListContainer;

    @FXML
    private TextField searchField;

    @FXML
    private HBox searchNav;

    private String username;
    private ReservationDao reservationDao = new ReservationDao();
    private List<Reservation> requestList;

    public void setUsername(String username) {
        this.username = username;
        //System.out.println("Username set: " + username);

        requestList=reservationDao.getByUserName(username);
        requestListContainer.setCellFactory(param ->
        {
            UserRequestCell userRequestCell = new UserRequestCell();
            userRequestCell.setListView(requestListContainer);
            return userRequestCell;

        });
        requestListContainer.getItems().setAll(requestList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

        brNav.setOnMouseClicked(event -> {
            String userFullName= memNameLabel.getText();
            BorrowedBooksController controller = navigateToScene("/fxml/User/BorrowedBooks.fxml", brNav);
            if (controller != null) {
                controller.setUsername(username);
                controller.setUserFullName(userFullName);
            }
        });

        logout.setOnMouseClicked(event -> navigateToScene("/fxml/Start/Role.fxml", logout));
    }

    public void setUserFullName(String userFullName) {
        memNameLabel.setText(userFullName);
    }

    @FXML
    private TextField reqSearchField;

    @FXML
    private void onSearchRequest() {
        String query = reqSearchField.getText().trim();

        if (!query.isEmpty()) {
            SearchBooksScreenController searchController = navigateToScene("/fxml/User/search_books_screen.fxml", reqSearchField);

            if (searchController != null) {
                searchController.performSearch(query);
            }
        }
    }
}
