package com.library.controller.user;

import com.library.dao.BorrowingRecordDao;
import com.library.dao.DocumentDao;
import com.library.dao.ReservationDao;
import com.library.models.BorrowingRecord;
import com.library.models.Document;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static com.library.utils.LoadImage.loadImageLazy;
import static com.library.utils.SceneSwitcher.navigateToScene;
import static com.library.utils.SceneSwitcher.showBookDetails;

public class HomeScreenController implements Initializable {

    @FXML
    private StackPane homeRoot;

    @FXML
    private Label bestBookAuthor;

    @FXML
    private ImageView bestBookImage;

    @FXML
    private Label bestBookTitle;

    @FXML
    private HBox brNav;

    @FXML
    private Label category;

    @FXML
    private HBox favNav;

    @FXML
    private HBox homeNav1;

    @FXML
    private HBox logout;

    @FXML
    private Label memNameLabel;

    @FXML
    private HBox myBooksContainer;

    @FXML
    private Label pages;

    @FXML
    private Label publisher;

    @FXML
    private GridPane recentlyAddedContainer;

    @FXML
    private HBox requestNav;

    @FXML
    private TextField searchField;

    @FXML
    private HBox searchNav;

    private String username;
    private List<BorrowingRecord> myBooks;
    private List<Document> recentlyAdded;
    private BorrowingRecordDao borrowingRecordDao = new BorrowingRecordDao();
    private DocumentDao documentDao = new DocumentDao();

    public void setUsername(String username) {
        this.username=username;
        loadMyBooks();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        recentlyAdded = documentDao.getRecentAddedBooks();
        loadRecentAddedBooks();
        loadMyBooks();
        loadBestBook();

        searchNav.setOnMouseClicked(event -> {
            String userFullName= memNameLabel.getText();
            SearchBooksScreenController controller = navigateToScene("/fxml/User/search_books_screen.fxml", searchNav);
            if (controller != null) {
                controller.setUsername(username);
                controller.setUserFullName(userFullName);
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

        requestNav.setOnMouseClicked(event -> {
            String userFullName= memNameLabel.getText();
            UserRequestController controller = navigateToScene("/fxml/User/user_request.fxml", requestNav);
            if (controller != null) {
                controller.setUsername(username);
                controller.setUserFullName(userFullName);
            }
        });

        favNav.setOnMouseClicked(event -> {
            String userFullName= memNameLabel.getText();
            FavouriteController controller = navigateToScene("/fxml/User/FavouriteBooks.fxml", favNav);
            if (controller != null) {
                controller.setUsername(username);
                controller.setUserFullName(userFullName);
            }
        });

        logout.setOnMouseClicked(event->navigateToScene("/fxml/Start/Role.fxml", logout));
    }

    private void loadMyBooks() {
        Task<List<BorrowingRecord>> loadTask = new Task<>() {
            @Override
            protected List<BorrowingRecord> call() throws Exception {
                myBooks = borrowingRecordDao.getByUserName(username);
                return myBooks;
            }
        };
        loadTask.setOnSucceeded(event -> {
            try {
                for (int i = 0; i < myBooks.size(); i++) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/book_card.fxml"));
                    VBox bookInfo = loader.load();
                    BookCardController controller = loader.getController();
                    Document document = documentDao.get(myBooks.get(i).getISBN());
                    controller.loadBookInfo(documentDao.get(myBooks.get(i).getISBN()));

                    bookInfo.setOnMouseClicked(event1 -> {
                        if (event1.getClickCount() == 2) {
                            showBookDetails(homeRoot, username, document);
                        }
                    });

                    FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), bookInfo);
                    fadeIn.setFromValue(0);
                    fadeIn.setToValue(1);
                    TranslateTransition slideIn = new TranslateTransition(Duration.seconds(0.5), bookInfo);
                    slideIn.setFromX(-300);
                    slideIn.setToX(0);
                    SequentialTransition sequentialTransition = new SequentialTransition();
                    sequentialTransition.getChildren().addAll(fadeIn, slideIn);
                    sequentialTransition.play();
                    myBooksContainer.getChildren().add(bookInfo);
                    sequentialTransition.setDelay(Duration.seconds(i * 0.1)); // Delay
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        loadTask.setOnFailed(event -> {
            System.out.println("failed to load my books");
        });
        new Thread(loadTask).start();
    }

    private void loadRecentAddedBooks() {
        Task<List<Document>> loadTask = new Task<>() {
            @Override
            protected List<Document> call() throws Exception {
                return recentlyAdded;
            }
        };
        loadTask.setOnSucceeded(event -> {
            int column =0;
            int row=1;
            try {
                for (int i=0; i<recentlyAdded.size(); i++) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/book_card.fxml"));
                    VBox bookInfo = loader.load();
                    BookCardController controller = loader.getController();
                    controller.loadBookInfo(recentlyAdded.get(i));
                    Document document = recentlyAdded.get(i);

                    bookInfo.setOnMouseClicked(event1 -> {
                        if (event1.getClickCount() == 2) {
                            showBookDetails(homeRoot, username, document);
                        }
                    });

                    // (pop-up effect)
                    TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), bookInfo);
                    translateTransition.setFromY(100); // Start off the screen (Y-axis)
                    translateTransition.setToY(0);    // End at normal position
                    translateTransition.setInterpolator(Interpolator.EASE_OUT); // Make the movement smoother
                    // (fade-in effect)
                    FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), bookInfo);
                    fadeIn.setFromValue(0);  // bắt đàu = no visibility
                    fadeIn.setToValue(1);    // kthuc = full visibility
                    SequentialTransition sequentialTransition = new SequentialTransition(translateTransition, fadeIn);
                    sequentialTransition.setDelay(Duration.seconds(0.1 * i)); // delay
                    sequentialTransition.play();

                    if (column == 9) {
                        column = 0;
                        ++row;
                    }

                    recentlyAddedContainer.add(bookInfo, column++, row);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        new Thread(loadTask).start();
    }

    private void loadBestBook() {
        Task<List<Document>> loadTask = new Task<>() {
            @Override
            protected List<Document> call() throws Exception {
                return recentlyAdded;
            }
        };
        loadTask.setOnSucceeded(event1 -> {
            Map<String, Integer> bookRequestCounts = new HashMap<>();

            for (Document doc : recentlyAdded) {
                ReservationDao reservationDao = ReservationDao.getInstance();
                int requestCount = reservationDao.getByISBN(doc.getISBN());
                bookRequestCounts.put(doc.getISBN(), requestCount);
            }

            String bestBookIsbn = null;
            int maxRequests = 0;

            for (Map.Entry<String, Integer> entry : bookRequestCounts.entrySet()) {
                if (entry.getValue() > maxRequests) {
                    maxRequests = entry.getValue();
                    bestBookIsbn = entry.getKey();
                }
            }

            if (bestBookIsbn != null) {
                Document bestBook = documentDao.get(bestBookIsbn);

                bestBookAuthor.setText(documentDao.getAuthor(bestBook.getAuthorId()).getName());
                bestBookTitle.setText(bestBook.getTitle());
                pages.setText("Pages: " + bestBook.getPage());
                publisher.setText("Publisher: " + documentDao.getPublisher(bestBook.getPublisherId()).getName());
                category.setText("Genre: " + documentDao.getCategory(bestBook.getCategoryId()).getName());

                bestBookImage.setOnMouseClicked(event -> showBookDetails(homeRoot, username, bestBook));

                if (!bestBook.getImageLink().equals("N/A")) {
                    loadImageLazy(bestBook.getImageLink(), bestBookImage, bestBookImage.getFitWidth(), bestBookImage.getFitHeight());
                }
            } else {
                Random random = new Random();
                int bestBookIndex= random.nextInt(54);
                Document doc=recentlyAdded.get(bestBookIndex);
                bestBookAuthor.setText(documentDao.getAuthor(doc.getAuthorId()).getName());
                bestBookTitle.setText(doc.getTitle());
                pages.setText("Pages: " +doc.getPage());
                publisher.setText("Publisher: "+(documentDao.getPublisher(doc.getPublisherId()).getName()));
                category.setText("Genre: "+documentDao.getCategory(doc.getCategoryId()).getName());
                bestBookImage.setOnMouseClicked(event -> showBookDetails(homeRoot, username, doc));

                if (!doc.getImageLink().equals("N/A")) {
                    loadImageLazy(doc.getImageLink(),bestBookImage, bestBookImage.getFitWidth(), bestBookImage.getFitHeight());
                }
            }
        });
        new Thread(loadTask).start();
    }




    public void setUserFullName(String userFullName) {
        memNameLabel.setText(userFullName);
    }

    @FXML
    private TextField homeSearchField;

    @FXML
    private void onSearchHome() {
        String query = homeSearchField.getText().trim();

        if (!query.isEmpty()) {
            String userFullName= memNameLabel.getText();
            SearchBooksScreenController searchController = navigateToScene("/fxml/User/search_books_screen.fxml", homeSearchField);

            if (searchController != null) {

                searchController.performSearch(query);
                searchController.setUsername(username);
                searchController.setUserFullName(userFullName);
            }
        }
    }
}
