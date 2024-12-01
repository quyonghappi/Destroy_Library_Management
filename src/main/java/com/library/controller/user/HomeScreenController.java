package com.library.controller.user;

import com.library.dao.BorrowingRecordDao;
import com.library.dao.DocumentDao;
import com.library.models.Author;
import com.library.models.BorrowingRecord;
import com.library.models.Document;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static com.library.utils.SceneSwitcher.navigateToScene;

public class HomeScreenController implements Initializable {

    @FXML
    private Label bestBookAuthor;

    @FXML
    private ImageView bestBookImage;

    @FXML
    private Label bestBookTitle;

    @FXML
    private HBox brNav;

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
    private void loadBestBook() {
//        DocumentDao documentDao = new DocumentDao();
//        List<Document> filteredDocuments = documentDao.searchByTitle("fiction");
//
//        if (!filteredDocuments.isEmpty()) {
//
//            Random random = new Random();
//            int randomIndex = random.nextInt(filteredDocuments.size());
//
//            Document document = filteredDocuments.get(randomIndex);
//
//            // Create the book card
//            HBox bookCard = new HBox();
//            bookCard.setSpacing(10);
//            bookCard.setStyle("-fx-padding: 10; -fx-border-color: #ddd; -fx-background-color: #fff; -fx-border-radius: 5; -fx-background-radius: 5;");
//            bookCard.setOnMouseClicked(e -> showBookDetails(document)); // Open book details on click
//
//            // Book Cover Image
//            ImageView bookCover = new ImageView();
//            bookCover.setFitWidth(100);
//            bookCover.setFitHeight(150);
//
//            if (document.getImageLink() != null && !document.getImageLink().isEmpty()) {
//                bookCover.setImage(new Image(document.getImageLink(), true)); // Load the image
//            } else {
//                bookCover.setImage(new Image("/ui/admindashboard/book1.png", true)); // Default image
//            }
//
//            // Book Information
//            VBox bookInfo = new VBox();
//            bookInfo.setSpacing(5);
//
//            Label titleLabel = new Label(document.getTitle() != null ? document.getTitle() : "Unknown Title");
//            titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
//
//            // Author Information
//            Author author = documentDao.getAuthor(document.getAuthorId());
//            String authorName = (author != null) ? author.getName() : "Unknown Author";
//            Label authorLabel = new Label(authorName);
//            authorLabel.getStyleClass().add("book-author");
//
//            Label pageLabel = new Label(document.getPage() > 0 ? document.getPage() + " pages" : "Page count not available");
//
//            // Add Book Info to the card
//            bookInfo.getChildren().addAll(titleLabel, authorLabel, pageLabel);
//
//            // Add Cover and Info to the Book Card
//            bookCard.getChildren().addAll(bookCover, bookInfo);

//
//        }
    }


    private void showBookDetails(Document document) {
        // Tạo cửa sổ chi tiết
        Stage detailStage = new Stage();
        detailStage.setTitle("Book Details");

        // Tạo GridPane cho bố cục
        GridPane detailContainer = new GridPane();
        detailContainer.getStyleClass().add("detail-container");
        detailContainer.setHgap(10);
        detailContainer.setVgap(10);
        detailContainer.setPadding(new Insets(20));

        // Tạo ImageView để hiển thị bìa sách
        ImageView bookCover = new ImageView();
        bookCover.setFitWidth(150);
        bookCover.setFitHeight(200);
        bookCover.setPreserveRatio(true);
        if (document.getImageLink() != null && !document.getImageLink().isEmpty()) {
            bookCover.setImage(new Image(document.getImageLink(), true));
        } else {
            bookCover.setImage(new Image("/ui/admindashboard/book1.png")); // Ảnh mặc định nếu không có
        }

        // Tạo các nhãn hiển thị thông tin
        Label titleValue = new Label(document.getTitle() != null ? document.getTitle() : "Unknown");
        titleValue.getStyleClass().add("book-title");
        titleValue.setWrapText(true); // Enable wrapping

        // Lấy tên tác giả từ DocumentDao
        DocumentDao documentDao = new DocumentDao();
        Author author = documentDao.getAuthor(document.getAuthorId());
        String authorName = (author != null) ? author.getName() : "Unknown Author";

        Label authorValue = new Label(authorName);
        authorValue.getStyleClass().add("book-author");

        Label yearValue = new Label(String.valueOf(document.getPublicationYear()));
        yearValue.getStyleClass().add("book-date");

        Label descriptionValue = new Label(document.getDescription() != null ? document.getDescription() : "No description available");
        descriptionValue.getStyleClass().add("book-description");
        descriptionValue.setWrapText(true);

        // Tạo nút Back
        Button backButton = new Button("Back");
        backButton.getStyleClass().add("back-button");
        backButton.setOnAction(e -> detailStage.close());

        // Tạo Hyperlink cho liên kết sách preview nếu có
        Hyperlink previewLink = null;
        if (document.getPreviewLink() != null && !document.getPreviewLink().isEmpty()) {
            previewLink = new Hyperlink("View Preview");
            previewLink.setOnAction(e -> {
                try {
                    // Mở liên kết preview trong trình duyệt
                    java.awt.Desktop.getDesktop().browse(java.net.URI.create(document.getPreviewLink()));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            previewLink.getStyleClass().add("book-preview-link");
        }

        // Tạo "Add to Favorites" label
        Label addToFavoritesLabel = new Label("Add to Favorites");
        addToFavoritesLabel.getStyleClass().add("add-to-favorites");
        //addToFavoritesLabel.setOnMouseClicked(e -> addToFavorites(document)); // Handle click event

        // Đặt các phần tử vào GridPane
        detailContainer.add(bookCover, 0, 0, 1, 6); // Ảnh bìa chiếm 6 dòng
        detailContainer.add(titleValue, 2, 0);
        detailContainer.add(authorValue, 2, 1);
        detailContainer.add(yearValue, 2, 3);
        detailContainer.add(descriptionValue, 2, 5);
        if (previewLink != null) {
            detailContainer.add(previewLink, 2, 7); // Thêm liên kết preview nếu có
        }
        detailContainer.add(addToFavoritesLabel, 2, 8); // Add to Favorites label
        detailContainer.add(backButton, 2, 9);

        // Tạo StackPane để căn giữa nội dung
        StackPane root = new StackPane(detailContainer);
        root.setAlignment(Pos.CENTER); // Căn giữa nội dung
        root.setPadding(new Insets(10)); // Padding nếu cần

        // Tạo Scene và thêm CSS
        Scene detailScene = new Scene(root, 715, 590);
        detailScene.getStylesheets().add(getClass().getResource("/css/styling.css").toExternalForm());

        // Set Scene cho Stage
        detailStage.setScene(detailScene);

        // Đảm bảo Stage hiển thị ở giữa màn hình
        detailStage.setOnShown(event -> {
            // Tính toán kích thước màn hình
            javafx.geometry.Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

            // Đặt vị trí chính giữa
            detailStage.setX((screenBounds.getWidth() - detailStage.getWidth()) / 2);
            detailStage.setY((screenBounds.getHeight() - detailStage.getHeight()) / 2);
        });

        // Hiển thị cửa sổ
        detailStage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadRecentAddedBooks();

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
            UserRequestController controller = navigateToScene("/fxml/User/FavouriteBooks.fxml", favNav);
            if (controller != null) {
                controller.setUsername(username);
                controller.setUserFullName(userFullName);
            }
        });

        logout.setOnMouseClicked(event->navigateToScene("/fxml/Start/Role.fxml", logout));
    }

    private void loadMyBooks() {
        myBooks= borrowingRecordDao.getByUserName(username);
        try {
            for (BorrowingRecord myBook : myBooks) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/book_card.fxml"));
                VBox bookInfo = loader.load();
                BookCardController controller = loader.getController();
                controller.loadBookInfo(documentDao.get(myBook.getISBN()));
                myBooksContainer.getChildren().add(bookInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void loadRecentAddedBooks() {
        recentlyAdded=documentDao.getRecentAddedBooks();

        int column =0;
        int row=1;
        try {
            for (Document doc : recentlyAdded) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/book_card.fxml"));
                VBox bookInfo = loader.load();
                BookCardController controller = loader.getController();
                controller.loadBookInfo(doc);
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
}
