package com.library.controller.user;

import com.library.controller.Observer;
import com.library.dao.DocumentDao;
import com.library.dao.ReservationDao;
import com.library.models.Reservation;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static com.library.utils.SceneSwitcher.navigateToScene;
import static com.library.utils.SceneSwitcher.showBookDetails;

public class UserRequestController implements Initializable, Observer {
    @FXML
    private StackPane userRequestRoot;

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

    //private boolean observerAdded = false;
    private String username;
    private ReservationDao reservationDao;
    private DocumentDao documentDao = new DocumentDao();

    public void setUsername(String username) {
        this.username = username;
        //System.out.println("Username set: " + username);
        loadUserRequestList();

    }

    private void loadUserRequestList() {
        Task<List<Reservation>> loadTask = new Task<>() {
            @Override
            protected List<Reservation> call() {
                return reservationDao.getByUserName(username);
            }
        };
        loadTask.setOnSucceeded(event -> {
            requestListContainer.setCellFactory(param ->
            {
                UserRequestCell cell = new UserRequestCell();
                return cell;
            });
            requestListContainer.getItems().setAll(loadTask.getValue());
        });
        loadTask.setOnFailed(event -> {
            System.out.println("fail to load user" + username +"favorite books" + loadTask.getException());
        });
        new Thread(loadTask).start();

    }

    @Override
    public void update() {
        loadUserRequestList();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reservationDao = ReservationDao.getInstance();
        reservationDao.addObserver(this);

        requestListContainer.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Reservation selectedReservation = requestListContainer.getSelectionModel().getSelectedItem();
                if (selectedReservation != null) {
                    showBookDetails(userRequestRoot,username,documentDao.get(selectedReservation.getIsbn()));
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
            String userFullName= memNameLabel.getText();

            SearchBooksScreenController searchController = navigateToScene("/fxml/User/search_books_screen.fxml", reqSearchField);

            if (searchController != null) {
                searchController.performSearch(query);
                searchController.setUsername(username);
                searchController.setUserFullName(userFullName);
            }
        }
    }
}
