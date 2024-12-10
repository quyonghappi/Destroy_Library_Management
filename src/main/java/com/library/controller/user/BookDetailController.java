package com.library.controller.user;

import com.library.dao.*;

import com.library.models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import java.time.LocalDateTime;
import java.util.List;

import static com.library.QrCode.BookQR.createQRCode;
import static com.library.dao.UserDao.findUserByName;
import static com.library.utils.LoadImage.loadImageLazy;
import static com.library.utils.LoadView.showAlert;
import static javafx.scene.layout.StackPane.setAlignment;


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

    @FXML
    private ImageView qrCode;

    @FXML
    private VBox reviewsVBox;

    private ReservationDao reservationDao = ReservationDao.getInstance();
    private FavouriteDao favDao = FavouriteDao.getInstance();
    private Document doc = new Document();
    private BorrowingRecordDao recordDao = new BorrowingRecordDao();

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
        // document hien dang load
        doc = document;
        loadQrCode();

        // Set book title
        bookTitle.setText(document.getTitle());
        pageLabel.setText("Total pages: " + document.getPage());
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
            Reservation re = reservationDao.reservationExists(isbn, user.getUserId());
            //System.out.println(re.getStatus());
            if (re != null && re.getStatus().equals("active")) {
                isReserved = true;
            } else {
                isReserved = false;
            }
            if (isReserved) {
                updateRequestButtonStyle(requestButton, isReserved);
            }
            isFavourite = favDao.favExists(isbn, user.getUserId());
            if (isFavourite) {
                updateFavButtonStyle(addFavImage, isFavourite);
            }
            loadBookReviews(document.getISBN());

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

    public void loadQrCode() {
        String previewLink = doc.getPreviewLink();
        ImageView imageView = createQRCode(previewLink);
        qrCode.setImage(imageView.getImage());
    }

    /**
     * Load review o day ne
     */
    @FXML
    private ListView<Review> reviewsListView;

    private ObservableList<Review> reviews = FXCollections.observableArrayList();

    public void loadBookReviews(String isbn) {
        Task<List<Review>> loadTask = new Task<>() {
            @Override
            protected List<Review> call() {
                return new ReviewDao().getByIsbn(isbn);  // Load reviews by ISBN
            }
        };

        loadTask.setOnSucceeded(event -> {
            if (reviewsListView.getCellFactory() == null) {
                reviewsListView.setCellFactory(param -> {
                    return new ReviewCell();
                });
            }

            reviews.clear();
            reviews.addAll(loadTask.getValue());

            reviewsListView.setItems(reviews);
        });

        loadTask.setOnFailed(event -> {
            System.out.println("Failed to load reviews for ISBN " + isbn + ": " + loadTask.getException());
        });

        new Thread(loadTask).start();
    }



    @FXML
    private AnchorPane reviewModal;

    @FXML
    private Slider ratingSlider;

    @FXML
    private TextArea commentField;


    @FXML
    private void openReviewModal() {
        BorrowingRecordDao borrowingRecordDao = new BorrowingRecordDao();
        List<BorrowingRecord> userBooksList = borrowingRecordDao.getByUserName(username);
        for (BorrowingRecord borrowingRecord : userBooksList) {
            //means that user nay da muon cuon sach nay
            if (borrowingRecord.getISBN().equals(doc.getISBN())) {
                reviewModal.setVisible(true);
                return;
            }
        }
        showAlert(Alert.AlertType.ERROR, "You cannot submit a review.", "You haven't borrowed the book yet! Please borrow the book first.");

    }

    @FXML
    private void submitReview() {
        double rating = ratingSlider.getValue();
        String comment = commentField.getText();


        User user = findUserByName(username);
        if (user != null) {
            String isbn = doc.getISBN();
            LocalDateTime reviewDate = LocalDateTime.now();
            ReviewDao reviewDao = new ReviewDao();
            reviewDao.add(new Review(user.getUserId(), isbn, rating, comment, reviewDate));

            loadBookReviews(isbn);

            closeReviewModal();
            commentField.clear();
        } else {
            System.out.println("Cannot find user with user name " + username);
        }
    }


    @FXML
    private void closeReviewModal() {
        reviewModal.setVisible(false);
    }
}




