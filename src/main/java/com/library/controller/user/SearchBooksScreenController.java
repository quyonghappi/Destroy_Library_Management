package com.library.controller.user;

import com.library.dao.DocumentDao;
import com.library.models.Author;
import com.library.models.Document;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import java.util.List;

import static com.library.utils.LoadImage.loadImageLazy;
import static com.library.utils.SceneSwitcher.navigateToScene;
import static com.library.utils.SceneSwitcher.showBookDetails;

public class SearchBooksScreenController {

    @FXML
    private StackPane searchScreenRoot;

    @FXML
    public HBox homeNav;
    @FXML
    public HBox searchNav1;
    @FXML
    public HBox brNav;
    @FXML
    public HBox favNav;
    @FXML
    public HBox requestNav;
    @FXML
    public TextField searchField;
    @FXML
    public HBox logout;

    @FXML
    public Label memNameLabel;
    @FXML
    private VBox searchResultsContainer;

    private String username;
    private DocumentDao documentDao = new DocumentDao();
    private List<Document> filteredDocuments;

    public void setUsername(String username) {
        this.username = username;
    }

    public void initialize() {
        searchField.setOnAction(event -> onSearchBooks());

        homeNav.setOnMouseClicked(event -> {
            String userFullName = memNameLabel.getText();
            HomeScreenController controller = navigateToScene("/fxml/User/home_screen.fxml", homeNav);
            if (controller != null) {
                controller.setUsername(username);
                controller.setUserFullName(userFullName);
            }
        });

        brNav.setOnMouseClicked(event -> {
            String userFullName = memNameLabel.getText();
            BorrowedBooksController controller = navigateToScene("/fxml/User/BorrowedBooks.fxml", brNav);
            if (controller != null) {
                controller.setUserFullName(userFullName);
                controller.setUsername(username);
            }
        });

        favNav.setOnMouseClicked(event -> {
            String userFullName = memNameLabel.getText();
            FavouriteController controller = navigateToScene("/fxml/User/FavouriteBooks.fxml", favNav);
            if (controller != null) {
                controller.setUserFullName(userFullName);
                controller.setUsername(username);
            }
        });

        requestNav.setOnMouseClicked(event -> {
            String userFullName = memNameLabel.getText();
            UserRequestController controller = navigateToScene("/fxml/User/user_request.fxml", requestNav);
            if (controller != null) {
                controller.setUsername(username);
                controller.setUserFullName(userFullName);
            }
        });

        logout.setOnMouseClicked(event->navigateToScene("/fxml/Start/Role.fxml", logout));
    }

    @FXML
    private void onSearchBooks() {
        String query = searchField.getText().trim();

        if (!query.isEmpty()) {
            loadSearchResults(query);
        } else {
            searchResultsContainer.getChildren().clear();
            Label noInputLabel = new Label("Please enter a search term.");
            searchResultsContainer.getChildren().add(noInputLabel);
        }
    }


    private void loadSearchResults(String query) {
        searchResultsContainer.getChildren().clear();

        Task<List<Document>> loadTask = new Task<>() {
            @Override
            protected List<Document> call() throws Exception {
                return documentDao.searchByTitle(query);
            }
        };

        loadTask.setOnSucceeded(event -> {
            filteredDocuments = loadTask.getValue();
            showSearchResults(query);
        });

        loadTask.setOnFailed(event -> {
            System.err.println("fail to load reservation list: " + loadTask.getException());
        });

        new Thread(loadTask).start();
    }

    private void showSearchResults(String query) {
        if (!filteredDocuments.isEmpty()) {
            for (Document document : filteredDocuments) {
                HBox bookCard = new HBox();
                bookCard.setSpacing(10);
                bookCard.setStyle("-fx-background-color: #95b1ee; -fx-border-radius: 15; -fx-background-radius: 15;");

                // Ảnh bìa sách
                ImageView bookCover = new ImageView();
                bookCover.setFitWidth(100);
                bookCover.setFitHeight(150);
                if (!document.getImageLink().equals("N/A")) {
                    loadImageLazy(document.getImageLink(), bookCover, bookCover.getFitWidth(), bookCover.getFitHeight());
                } else {
                    bookCover.setImage(new Image("/ui/admindashboard/bookcover.png", true));
                }

                // Thông tin sách
                VBox bookInfo = new VBox();
                bookInfo.setSpacing(5);

                Label titleLabel = new Label(document.getTitle() != null ? document.getTitle() : "Unknown Title");
                titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

                //Label authorLabel = new Label("Author ID: " + document.getAuthorId());
                //DocumentDao documentDao = new DocumentDao();
                Author author = documentDao.getAuthor(document.getAuthorId());
                String authorName = (author != null) ? author.getName() : "Unknown Author";

                Label authorLabel = new Label(authorName);
                authorLabel.getStyleClass().add("book-author");

                Label pageLabel = new Label(document.getPage() > 0 ? document.getPage() + " pages" : "Page count not available");

                Button viewDetailsButton = new Button("View Details");
                viewDetailsButton.getStyleClass().add("back-button");

                viewDetailsButton.setOnAction(e -> showBookDetails(searchScreenRoot, username, document));

                bookInfo.getChildren().addAll(titleLabel, authorLabel, pageLabel, viewDetailsButton);
                bookCard.getChildren().addAll(bookCover, bookInfo);

                searchResultsContainer.getChildren().add(bookCard);
            }
        } else {
            Label noResultsLabel = new Label("No results found for \"" + query + "\".");
            searchResultsContainer.getChildren().add(noResultsLabel);
        }
    }

    public void setUserFullName(String userFullName) {
        memNameLabel.setText(userFullName);
    }

}
