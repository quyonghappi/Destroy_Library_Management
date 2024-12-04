package com.library.controller.user;

import com.library.dao.DocumentDao;
import com.library.models.Author;
import com.library.models.Document;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static com.library.utils.LoadImage.loadImageLazy;
import static com.library.utils.SceneSwitcher.navigateToScene;
import static com.library.utils.SceneSwitcher.showBookDetails;

public class SearchBooksScreenController implements Initializable {

    @FXML
    private StackPane searchScreenRoot;

    @FXML
    private Label bestBookAuthor;

    @FXML
    private ImageView bestBookImage;

    @FXML
    private Label bestBookTitle;

    @FXML
    private Label category;

    @FXML
    private Label pages;

    @FXML
    private Label publisher;



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
    private Pagination pagination;

//    @FXML
//    private VBox searchResultsContainer;


    @FXML
    //private VBox searchResultsContainer; // StackPane for displaying search results or messages

    private FlowPane searchResultsContainer;

    private String username;
    private DocumentDao documentDao = new DocumentDao();
    private List<Document> filteredDocuments;
    private List<Document> allDocuments;

    private List<Document> recentlyAdded;

    public void setUsername(String username) {
        this.username = username;
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        //loadRecentAddedBooksAndRecommendation();

        loadAllBooks();
        searchField.textProperty().addListener(event -> onSearchBooks());

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

//        if (pagination != null) {
//            pagination.setPageFactory(pageIndex -> {
//                loadPageResults(pageIndex);
//                return searchResultsContainer;
//            });
//        }

    }

    private void loadAllBooks() {
        // Tải tất cả sách từ database
        Task<List<Document>> loadTask = new Task<>() {
            @Override
            protected List<Document> call() throws Exception {
                return documentDao.getAll();  // Giả sử có phương thức này trong DocumentDao
            }
        };

        loadTask.setOnSucceeded(event -> {
            filteredDocuments = loadTask.getValue();
            showBooks(filteredDocuments);  // Hiển thị tất cả sách
        });

        loadTask.setOnFailed(event -> {
            System.err.println("Failed to load books: " + loadTask.getException());
        });

        new Thread(loadTask).start();
    }

    private void showBooks(List<Document> books) {
        searchResultsContainer.getChildren().clear();  // Xóa các kết quả cũ

        for (Document document : books) {
            HBox bookCard = createBookCard(document);
            searchResultsContainer.getChildren().add(bookCard);
        }
        int pageCount = (int) Math.ceil(books.size() / 40.0);
        //pagination.setPageCount(pageCount);  // Cập nhật số trang cho pagination
    }



    private HBox createBookCard(Document document) {
        HBox bookCard = new HBox();
        bookCard.setSpacing(10);
        bookCard.setStyle("-fx-background-color: transparent; -fx-border-radius: 15; -fx-background-radius: 15;");
        bookCard.setPrefSize(300, 200);


        ImageView bookCover = new ImageView();
        bookCover.setFitWidth(100);
        bookCover.setFitHeight(150);
        if (!document.getImageLink().equals("N/A")) {
            loadImageLazy(document.getImageLink(), bookCover, bookCover.getFitWidth(), bookCover.getFitHeight());
        } else {
            bookCover.setImage(new Image("/ui/admindashboard/bookcover.png", true));
        }

        VBox bookInfo = new VBox();
        bookInfo.setSpacing(5);

        Label titleLabel = new Label(document.getTitle() != null ? document.getTitle() : "Unknown Title");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Author author = documentDao.getAuthor(document.getAuthorId());
        String authorName = (author != null) ? author.getName() : "Unknown Author";

        Label authorLabel = new Label(authorName);
        authorLabel.getStyleClass().add("book-author");

        Label pageLabel = new Label(document.getPage() > 0 ? document.getPage() + " pages" : "Page count not available");

        bookInfo.getChildren().addAll(titleLabel, authorLabel, pageLabel);

        bookInfo.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                showBookDetails(searchScreenRoot, username, document);
            }
        });

        bookCover.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                showBookDetails(searchScreenRoot, username, document);
            }
        });
        bookCard.getChildren().addAll(bookCover, bookInfo);

        return bookCard;
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
            loadAllBooks();
        }
    }

    public void performSearch(String query) {
        loadSearchResults(query);  // Gọi phương thức tìm kiếm và hiển thị kết quả
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



//    private void loadPageResults(int pageIndex) {
//        int startIndex = pageIndex * 40;
//        int endIndex = Math.min(startIndex + 40, filteredDocuments.size());
//
//        List<Document> pageBooks = filteredDocuments.subList(startIndex, endIndex);
//        showBooks(pageBooks);
//    }


    private void showSearchResults(String query) {
        if (!filteredDocuments.isEmpty()) {
            int column = 0;
            int row = 0;

            for (Document document : filteredDocuments) {
                // Create HBox for each book card
                HBox bookCard = new HBox();
                bookCard.setSpacing(10);
                bookCard.setStyle("-fx-background-color: transparent; -fx-border-radius: 15; -fx-background-radius: 15;");
                bookCard.setPrefSize(300, 220);


                // Book cover image
                ImageView bookCover = new ImageView();
                bookCover.setFitWidth(150);
                bookCover.setFitHeight(190);
                if (!document.getImageLink().equals("N/A")) {
                    loadImageLazy(document.getImageLink(), bookCover, bookCover.getFitWidth(), bookCover.getFitHeight());
                } else {
                    bookCover.setImage(new Image("/ui/admindashboard/bookcover.png", true));
                }

                VBox bookInfo = new VBox();
                bookInfo.setSpacing(5);

                Label titleLabel = new Label(document.getTitle() != null ? document.getTitle() : "Unknown Title");
                titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

                Author author = documentDao.getAuthor(document.getAuthorId());
                String authorName = (author != null) ? author.getName() : "Unknown Author";

                Label authorLabel = new Label(authorName);
                authorLabel.getStyleClass().add("book-author");

                Label pageLabel = new Label(document.getPage() > 0 ? document.getPage() + " pages" : "Page count not available");

                // Add book details to the VBox
                bookInfo.getChildren().addAll(titleLabel, authorLabel, pageLabel);
                bookCard.getChildren().addAll(bookCover, bookInfo);

                bookInfo.setOnMouseClicked(e -> {
                    if (e.getClickCount() == 2) {
                        showBookDetails(searchScreenRoot, username, document); // Open book details on double click
                    }
                });

                bookCover.setOnMouseClicked(e -> {
                    if (e.getClickCount() == 2) {
                        showBookDetails(searchScreenRoot, username, document); // Open book details on double click
                    }
                });

                searchResultsContainer.getChildren().add(bookCard);

                column++;
                if (column == 2) {
                    column = 0;
                    row++;
                }
            }
        } else {
            // If no results found, show a message
            Label noResultsLabel = new Label("No results found for \"" + query + "\".");
            searchResultsContainer.getChildren().add(noResultsLabel);
        }
    }


    public void setUserFullName(String userFullName) {
        memNameLabel.setText(userFullName);
    }

    public void setSearchQuery(String searchQuery) {

    }
}