package com.library.controller.user;
import com.library.controller.Observer;
import com.library.dao.DocumentDao;
import com.library.dao.FavouriteDao;
import com.library.models.Favourite;
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

public class FavouriteController implements Initializable, Observer {
    @FXML
    private StackPane favRoot;
    @FXML
    private HBox brNav;

    @FXML
    private HBox favNav1;

    @FXML
    private ListView<Favourite> favoriteListContainer;

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

    private String username;
    private FavouriteDao favouriteDao = FavouriteDao.getInstance();
    private DocumentDao documentDao = DocumentDao.getInstance();

    public void setUsername(String username) {
        this.username = username;
        loadFavList();
        
    }

    public void setUserFullName(String userFullName) {
        memNameLabel.setText(userFullName);
    }
    
    private void loadFavList() {
        Task<List<Favourite>> loadTask = new Task<>() {
            @Override
            protected List<Favourite> call() {
                return favouriteDao.getByUserName(username);
            }
        };

        loadTask.setOnSucceeded(event -> {
            favoriteListContainer.setCellFactory(param->
            {
                FavouriteBooksCell favouriteBooksCell = new FavouriteBooksCell();
                return favouriteBooksCell;
            });
            favoriteListContainer.getItems().setAll(loadTask.getValue());
        });
        loadTask.setOnFailed(event -> {
            System.out.println("fail to load user" + username +"favorite books" + loadTask.getException());
        });
        new Thread(loadTask).start();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        favouriteDao.addObserver(this);
        favoriteListContainer.setOnMouseClicked(event -> {

            if (event.getClickCount() == 2) {
                Favourite selectedItem = favoriteListContainer.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    showBookDetails(favRoot,username,documentDao.get(selectedItem.getIsbn()));
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

        requestNav.setOnMouseClicked(event -> {
            String userFullName= memNameLabel.getText();
            UserRequestController controller = navigateToScene("/fxml/User/user_request.fxml", requestNav);
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

    @FXML
    private TextField favSearchField;

    @FXML
    private void onSearchFavorite() {
        String query = favSearchField.getText().trim();

        if (!query.isEmpty()) {
            String userFullName= memNameLabel.getText();

            SearchBooksScreenController searchController = navigateToScene("/fxml/User/search_books_screen.fxml", favSearchField);

            if (searchController != null) {
                searchController.performSearch(query);
                searchController.setUsername(username);
                searchController.setUserFullName(userFullName);
            }
        }
    }

    @Override
    public void update() {
        loadFavList();
    }
}
