package com.library.controller.user;

import com.library.dao.DocumentDao;

import com.library.dao.FavouriteDao;
import com.library.dao.ReservationDao;
import com.library.models.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebView;
import java.time.LocalDateTime;

import static com.library.dao.UserDao.findUserByName;
import static com.library.utils.LoadImage.loadImageLazy;

public class BookDetailController {

    private String username;
    private boolean isFavourite = false;
    private boolean isReserved = false;

    @FXML
    private Button addFavButton;

    @FXML
    private ImageView addFavImage;

    @FXML
    private Button backButton;

    @FXML
    private Label bookAuthor;

    @FXML
    private ImageView bookCover;

    @FXML
    private Label bookDescription;

    @FXML
    private Label bookPublishedDate;

    @FXML
    private Label bookTitle;

    @FXML
    private Label categoryLabel;

    @FXML
    private Label isbnLabel;

    @FXML
    private Label pageLabel;

    @FXML
    private WebView previewWebView;

    @FXML
    private Label publisherLabel;

    @FXML
    private Button requestButton;

    private ReservationDao reservationDao = new ReservationDao();
    private FavouriteDao favDao = new FavouriteDao();


    public void setUsername(String username) {
        this.username = username;
    }

    public void setCurrentDocument(Document doc) {
        loadBookDetails(doc);
    }

    /**
     * Load book details into the view components.
     *
     * @param document The Document object containing the book's details.
     */
    private void loadBookDetails(Document document) {
        // Set book title
        bookTitle.setText(document.getTitle());
        pageLabel.setText("Pages: " + document.getPage());
        isbnLabel.setText("ISBN: " + document.getISBN());


        // Set author name
        DocumentDao documentDao = new DocumentDao();
        Author author = documentDao.getAuthor(document.getAuthorId());
        String authorName = author.getName();
        bookAuthor.setText("Author: " + authorName);
        Publisher publisher = documentDao.getPublisher(document.getPublisherId());
        publisherLabel.setText("Publisher: " + publisher.getName());
        Category category = documentDao.getCategory(document.getCategoryId());
        categoryLabel.setText("Category: " + category.getName());

        // Set publication year
        bookPublishedDate.setText("Published date: " + document.getPublicationYear());


        // Set description
        bookDescription.setText(document.getDescription());

        // Set book cover image
        if (!document.getImageLink().equals("N/A")) {
            loadImageLazy(document.getImageLink(), bookCover, bookCover.getFitWidth(), bookCover.getFitHeight());
        } else {
            bookCover.setImage(new Image("/ui/admindashboard/bookcover.png")); // Default image
        }

        // Set preview link
        if (!document.getPreviewLink().equals("No preview available.")) {
            previewWebView.setVisible(true);
            previewWebView.getEngine().load(document.getPreviewLink() + "&output=embed");
        } else {
            previewWebView.getEngine().loadContent("No preview available.");
        }


        String isbn = isbnLabel.getText().substring(6).trim();
        User user = findUserByName(username);

        if (user != null) {
            isReserved = reservationDao.reservationExists(isbn, user.getUserId());
            if (isReserved) {
                updateRequestButtonStyle(requestButton, isReserved);

            }
            isFavourite = favDao.favExists(isbn, user.getUserId());
            System.out.println(isFavourite);
            if (isFavourite) {
                updateFavButtonStyle(addFavImage, isFavourite);
            }
        }
    }


    @FXML
    private void onRequestClicked() {
        Reservation reservation = new Reservation();
        User user = findUserByName(username);
        assert user != null;
        if(!isReserved) {
            reservationDao.add(new Reservation(user.getUserId(), isbnLabel.getText().substring(6).trim(), LocalDateTime.now(), "active"));
            isReserved = true;
            updateRequestButtonStyle(requestButton, isReserved);
        }
        else {
            reservationDao.delete(isbnLabel.getText().substring(6).trim(), user.getUserId());
            isReserved = false;
            updateRequestButtonStyle(requestButton, isReserved);
        }
    }

    @FXML
    private void addFav() {
        User user = findUserByName(username);
        assert user != null;
        if (!isFavourite) {
            favDao.add(new Favourite(user.getUserId(), isbnLabel.getText().substring(6).trim()));
            isFavourite = true;
            updateFavButtonStyle(addFavImage, isFavourite);
        } else {
            favDao.delete(isbnLabel.getText().substring(6).trim(), user.getUserId());
            isFavourite = false;
            updateFavButtonStyle(addFavImage, isFavourite);
        }

    }

    private void updateRequestButtonStyle(Button button, boolean checked) {
        if (!checked) {
            requestButton.setText("Request Book");
            requestButton.setStyle("-fx-background-color: #364c84");
        } else {
            requestButton.setText("Cancel Request");
            requestButton.setStyle("-fx-background-color: #e7f1a8");
        }
    }

    private void updateFavButtonStyle(ImageView imageView, boolean checked) {
        if (!checked) {
            addFavImage.setImage(new Image("/ui/user/heart-add-line.png"));
        } else {
            addFavImage.setImage(new Image("/ui/user/heart-add-fill.png"));
        }
    }
}
