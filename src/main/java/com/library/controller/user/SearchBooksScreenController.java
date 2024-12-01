package com.library.controller.user;

import com.library.dao.DocumentDao;
import com.library.dao.FavouriteDao;
import com.library.models.Author;
import com.library.models.Document;
import com.library.utils.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

import static com.library.utils.LoadImage.loadImageLazy;
import static com.library.utils.SceneSwitcher.navigateToScene;

public class SearchBooksScreenController {

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
        DocumentDao documentDao = new DocumentDao();
        //List<Document> documents = documentDao.searchByTitle(query);

        // Lọc danh sách dựa trên từ khóa tìm kiếm
//                List<Document> filteredDocuments = documents.stream()
//                        .filter(doc -> doc.getTitle() != null && doc.getTitle().toLowerCase().contains(query.toLowerCase()))
//                        .collect(Collectors.toList());
        List<Document> filteredDocuments = documentDao.searchByTitle(query);

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

                viewDetailsButton.setOnAction(e -> showBookDetails(document));

                bookInfo.getChildren().addAll(titleLabel, authorLabel, pageLabel, viewDetailsButton);
                bookCard.getChildren().addAll(bookCover, bookInfo);

                searchResultsContainer.getChildren().add(bookCard);
            }
        } else {
            Label noResultsLabel = new Label("No results found for \"" + query + "\".");
            searchResultsContainer.getChildren().add(noResultsLabel);
        }
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
        if (!document.getImageLink().equals("N/A")) {
            loadImageLazy(document.getImageLink(), bookCover, bookCover.getFitWidth(), bookCover.getFitHeight());
        } else {
            bookCover.setImage(new Image("/ui/admindashboard/bookcover.png", true));
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
        if (!document.getPreviewLink().equals("There is no preview for this book.")) {
            previewLink = new Hyperlink("View Preview");
            previewLink.setOnAction(e -> {
                try {
                    //no no we will display that preview part in our app
                    // Mở liên kết preview trong trình duyệt
                    java.awt.Desktop.getDesktop().browse(java.net.URI.create(document.getPreviewLink()));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            previewLink.getStyleClass().add("book-preview-link");
        }
        else {
            previewLink = null;
        }

        // Tạo "Add to Favorites" label
        //this should be a button to handle action
        //hlinh styling cho dep nhu back button nhe
        Button addToFavorites = new Button("Add to Favorites");
        addToFavorites.getStyleClass().add("add-to-favorites");
        addToFavorites.setOnMouseClicked(e -> addToFavorites(document)); // Handle click event

        // Đặt các phần tử vào GridPane
        detailContainer.add(bookCover, 0, 0, 1, 6); // Ảnh bìa chiếm 6 dòng
        detailContainer.add(titleValue, 2, 0);
        detailContainer.add(authorValue, 2, 1);
        detailContainer.add(yearValue, 2, 3);
        detailContainer.add(descriptionValue, 2, 5);
        if (previewLink != null) {
            detailContainer.add(previewLink, 2, 7); // Thêm liên kết preview nếu có
        }
        detailContainer.add(addToFavorites, 2, 9); // Add to Favorites label
        detailContainer.add(backButton, 2, 9);

        // Tạo StackPane để căn giữa nội dung
        StackPane root = new StackPane(detailContainer);
        root.setAlignment(Pos.CENTER); // Căn giữa nội dung
        root.setPadding(new Insets(10)); // Padding nếu cần

        // Tạo Scene và thêm CSS
        Scene detailScene = new Scene(root, 715, 590);
        detailScene.getStylesheets().add(getClass().getResource("/css/user/styling.css").toExternalForm());

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

    public void setUserFullName(String userFullName) {
        memNameLabel.setText(userFullName);
    }

    private void addToFavorites(Document document) {
        FavouriteDao favouriteDao = new FavouriteDao();
        favouriteDao.add(document);
    }




}
