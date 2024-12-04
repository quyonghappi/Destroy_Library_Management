package com.library.controller.user;

import com.library.dao.BorrowingRecordDao;
import com.library.dao.DocumentDao;
import com.library.models.BorrowingRecord;
import com.library.models.Document;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
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
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

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

        loadRecentAddedBooksAndRecommendation();

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
        myBooks = borrowingRecordDao.getByUserName(username);
        try {
            for (int i = 0; i < myBooks.size(); i++) {
                BorrowingRecord myBook = myBooks.get(i);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/book_card.fxml"));
                VBox bookInfo = loader.load();
                BookCardController controller = loader.getController();
                Document document = documentDao.get(recentlyAdded.get(i).getISBN());
                controller.loadBookInfo(documentDao.get(recentlyAdded.get(i).getISBN()));

                bookInfo.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2) {
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
    }

    private void loadRecentAddedBooksAndRecommendation() {
        recentlyAdded=documentDao.getRecentAddedBooks();
        Random random = new Random();
        int bestBookIndex= random.nextInt(55);
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

        int column =0;
        int row=1;
        try {
            for (int i=0; i<recentlyAdded.size()-1; i++) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/book_card.fxml"));
                VBox bookInfo = loader.load();
                BookCardController controller = loader.getController();
                controller.loadBookInfo(recentlyAdded.get(i));
                Document document = recentlyAdded.get(i);

                bookInfo.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2) {
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
            SearchBooksScreenController searchController = navigateToScene("/fxml/User/search_books_screen.fxml", homeSearchField);

            if (searchController != null) {
                searchController.performSearch(query);
            }
        }
    }
}
