package com.library.UserDashboard.Controller;

import com.library.dao.*;
import com.library.models.Author;
import com.library.models.Document;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

// THIS IS A SEARCH SCREEN PAY ATTENTION

public class SearchScreenController {

        @FXML
        private Button libraryButton; // Preserved for opening BookView


        @FXML
        private Button homeButton;

        @FXML
        private TextField searchField; // Search input field for book queries

        @FXML
        private VBox searchResultsContainer; // Container for displaying search results dynamically

        @FXML
        public void initialize() {
                // Add event handler for the library button (existing feature)
                libraryButton.setOnAction(event -> openBookView());

                // Add listener for the search input field
                searchField.setOnAction(event -> onSearchBooks());
        }

        // Opens BookView (existing feature)
        private void openBookView() {
                try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/User/book_viewer.fxml"));
                        Parent bookView = loader.load();

                        Stage stage = (Stage) libraryButton.getScene().getWindow();
                        Scene scene = new Scene(bookView, 1466, 750);
                        scene.getStylesheets().add(getClass().getResource("/css/styling.css").toExternalForm());

                        stage.setScene(scene);
                        stage.show();
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        private void openHomeScreen() {
                try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/User/home_user_dashboard.fxml"));
                        Parent bookView = loader.load();

                        Stage stage = (Stage) homeButton.getScene().getWindow();
                        Scene scene = new Scene(bookView, 1466, 750);
                        scene.getStylesheets().add(getClass().getResource("/css/styling.css").toExternalForm());

                        stage.setScene(scene);
                        stage.show();
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        // Handles book search based on user input
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

        // Loads search results dynamically into the container
        /*
        private void loadSearchResults(String query) {
                searchResultsContainer.getChildren().clear(); // Clear previous results
                JsonArray books = GoogleBooksAPIClient.getBooks(query); // Fetch books from API

                if (books != null && books.size() > 0) {
                        for (int i = 0; i < books.size(); i++) {
                                JsonObject book = books.get(i).getAsJsonObject().getAsJsonObject("volumeInfo");

                                HBox bookCard = new HBox();
                                bookCard.setSpacing(10);
                                bookCard.setStyle("-fx-padding: 10; -fx-border-color: #ddd; -fx-background-color: #fff; -fx-border-radius: 5; -fx-background-radius: 5;");

                                // Book Cover
                                ImageView bookCover = new ImageView();
                                bookCover.setFitWidth(100);
                                bookCover.setFitHeight(150);
                                if (book.has("imageLinks")) {
                                        String imageUrl = book.getAsJsonObject("imageLinks").get("thumbnail").getAsString();
                                        bookCover.setImage(new Image(imageUrl, true));
                                }

                                // Book Information
                                VBox bookInfo = new VBox();
                                bookInfo.setSpacing(5);

                                Label titleLabel = new Label(book.has("title") ? book.get("title").getAsString() : "Unknown Title");
                                titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

                                Label authorLabel = new Label(book.has("authors") ? String.join(", ", book.getAsJsonArray("authors").toString()) : "Unknown Author");
                                Label pageLabel = new Label(book.has("pageCount") ? book.get("pageCount").getAsString() + " pages" : "Page count not available");

                                Button viewDetailsButton = new Button("View Details");
                                viewDetailsButton.setOnAction(e -> showBookDetails(book));

                                bookInfo.getChildren().addAll(titleLabel, authorLabel, pageLabel, viewDetailsButton);
                                bookCard.getChildren().addAll(bookCover, bookInfo);

                                searchResultsContainer.getChildren().add(bookCard);
                        }
                } else {
                        Label noResultsLabel = new Label("No results found for \"" + query + "\".");
                        searchResultsContainer.getChildren().add(noResultsLabel);
                }
        }

        */
        /*ver 2 - load from db*/

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
                                bookCard.setStyle("-fx-padding: 10; -fx-border-color: #ddd; -fx-background-color: #fff; -fx-border-radius: 5; -fx-background-radius: 5;");

                                // Ảnh bìa sách
                                ImageView bookCover = new ImageView();
                                bookCover.setFitWidth(100);
                                bookCover.setFitHeight(150);
                                if (document.getImageLink() != null && !document.getImageLink().isEmpty()) {
                                        bookCover.setImage(new Image(document.getImageLink(), true));
                                        //aspect ratio of the image will be preserved ì true


                                       // loadImageLazy(document.getImageLink(), bookCover);
                                } else {
                                        bookCover.setImage(new Image("/ui/admindashboard/book1.png", true));
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







        // Displays book details (You can customize this method to open a modal or a detailed view
        /*
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

                // Đặt các phần tử vào GridPane
                detailContainer.add(bookCover, 0, 0, 1, 6); // Ảnh bìa chiếm 6 dòng
                detailContainer.add(titleValue, 2, 0);
                detailContainer.add(authorValue, 2, 1);
                detailContainer.add(yearValue, 2, 3);
                detailContainer.add(descriptionValue, 2, 5);
                detailContainer.add(backButton, 2, 6);

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

         */



// showbook details with preview link

//        private void showBookDetails(Document document) {
//                // Tạo cửa sổ chi tiết
//                Stage detailStage = new Stage();
//                detailStage.setTitle("Book Details");
//
//                // Tạo GridPane cho bố cục
//                GridPane detailContainer = new GridPane();
//                detailContainer.getStyleClass().add("detail-container");
//                detailContainer.setHgap(10);
//                detailContainer.setVgap(10);
//                detailContainer.setPadding(new Insets(20));
//
//                // Tạo ImageView để hiển thị bìa sách
//                ImageView bookCover = new ImageView();
//                bookCover.setFitWidth(150);
//                bookCover.setFitHeight(200);
//                bookCover.setPreserveRatio(true);
//                if (document.getImageLink() != null && !document.getImageLink().isEmpty()) {
//                        bookCover.setImage(new Image(document.getImageLink(), true));
//                } else {
//                        bookCover.setImage(new Image("/ui/admindashboard/book1.png")); // Ảnh mặc định nếu không có
//                }
//
//                // Tạo các nhãn hiển thị thông tin
//                Label titleValue = new Label(document.getTitle() != null ? document.getTitle() : "Unknown");
//                titleValue.getStyleClass().add("book-title");
//                titleValue.setWrapText(true); // Enable wrapping
//
//                // Lấy tên tác giả từ DocumentDao
//                DocumentDao documentDao = new DocumentDao();
//                Author author = documentDao.getAuthor(document.getAuthorId());
//                String authorName = (author != null) ? author.getName() : "Unknown Author";
//
//                Label authorValue = new Label(authorName);
//                authorValue.getStyleClass().add("book-author");
//
//                Label yearValue = new Label(String.valueOf(document.getPublicationYear()));
//                yearValue.getStyleClass().add("book-date");
//
//                Label descriptionValue = new Label(document.getDescription() != null ? document.getDescription() : "No description available");
//                descriptionValue.getStyleClass().add("book-description");
//                descriptionValue.setWrapText(true);
//
//                // Tạo nút Back
//                Button backButton = new Button("Back");
//                backButton.getStyleClass().add("back-button");
//                backButton.setOnAction(e -> detailStage.close());
//
//                // Tạo Hyperlink cho liên kết sách preview nếu có
//                Hyperlink previewLink = null;
//                if (document.getPreviewLink() != null && !document.getPreviewLink().isEmpty()) {
//                        previewLink = new Hyperlink("View Preview");
//                        previewLink.setOnAction(e -> {
//                                try {
//                                        // Mở liên kết preview trong trình duyệt
//                                        java.awt.Desktop.getDesktop().browse(java.net.URI.create(document.getPreviewLink()));
//                                } catch (IOException ex) {
//                                        ex.printStackTrace();
//                                }
//                        });
//                        previewLink.getStyleClass().add("book-preview-link");
//                }
//
//                // Đặt các phần tử vào GridPane
//                detailContainer.add(bookCover, 0, 0, 1, 6); // Ảnh bìa chiếm 6 dòng
//                detailContainer.add(titleValue, 2, 0);
//                detailContainer.add(authorValue, 2, 1);
//                detailContainer.add(yearValue, 2, 3);
//                detailContainer.add(descriptionValue, 2, 5);
//                if (previewLink != null) {
//                        detailContainer.add(previewLink, 2, 7); // Thêm liên kết preview nếu có
//                }
//                detailContainer.add(backButton, 2, 8);
//
//                // Tạo StackPane để căn giữa nội dung
//                StackPane root = new StackPane(detailContainer);
//                root.setAlignment(Pos.CENTER); // Căn giữa nội dung
//                root.setPadding(new Insets(10)); // Padding nếu cần
//
//                // Tạo Scene và thêm CSS
//                Scene detailScene = new Scene(root, 715, 590);
//                detailScene.getStylesheets().add(getClass().getResource("/css/styling.css").toExternalForm());
//
//                // Set Scene cho Stage
//                detailStage.setScene(detailScene);
//
//                // Đảm bảo Stage hiển thị ở giữa màn hình
//                detailStage.setOnShown(event -> {
//                        // Tính toán kích thước màn hình
//                        javafx.geometry.Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
//
//                        // Đặt vị trí chính giữa
//                        detailStage.setX((screenBounds.getWidth() - detailStage.getWidth()) / 2);
//                        detailStage.setY((screenBounds.getHeight() - detailStage.getHeight()) / 2);
//                });
//
//                // Hiển thị cửa sổ
//                detailStage.show();
//        }

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

        // Handle Add to Favorites functionality
//        private void addToFavorites(Document document) {
//                FavoritesDao favoritesDao = new FavoritesDao();
//
//                // Check if the book already exists in the favorites
//                if (!favoritesDao.isFavorite(document.getISBN())) {
//                        favoritesDao.addFavorite(document); // Add book to favorites
//                        System.out.println("Book added to favorites!");
//
//                        // Optionally, show a confirmation message or refresh the favorite screen
//                        // Here you can trigger an event to update the FavoriteBooks screen.
//                } else {
//                        System.out.println("This book is already in favorites.");
//                }
//        }

}
