package com.library.UserDashboard.Controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.library.GoogleBooks.GoogleBooksAPIClient;
import com.library.dao.*;
import com.library.models.Document;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.library.utils.LoadImage.loadImageLazy;

public class UserDashboardController {

        @FXML
        private Button libraryButton; // Preserved for opening BookView

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
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/book_viewer.fxml"));
                        Parent bookView = loader.load();

                        Stage stage = (Stage) libraryButton.getScene().getWindow();
                        Scene scene = new Scene(bookView, 1466, 700);
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

                                Label authorLabel = new Label("Author ID: " + document.getAuthorId());

                                Label pageLabel = new Label(document.getPage() > 0 ? document.getPage() + " pages" : "Page count not available");

                                Button viewDetailsButton = new Button("View Details");
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







        // Displays book details (You can customize this method to open a modal or a detailed view)
        /*
        private void showBookDetails(JsonObject book) {
                try {
                        System.out.println("showBookDetails invoked for book: " + book); // Debug message

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/book_detail.fxml"));
                        Parent root = loader.load();

                        // Pass book data to the detail controller
                        BookDetailController controller = loader.getController();
                        controller.loadBookDetails(book);

                        // Open the Book Detail Screen as a modal
                        Stage modalStage = new Stage();
                        Scene scene = new Scene(root);
                        scene.getStylesheets().add(getClass().getResource("/css/styling.css").toExternalForm());
                        modalStage.setScene(scene);
                        modalStage.setTitle("Book Details");
                        modalStage.initModality(Modality.APPLICATION_MODAL); // Make it modal
                        modalStage.showAndWait();
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

         */


        private void showBookDetails(Document document) {
                // Tạo cửa sổ chi tiết
                Stage detailStage = new Stage();
                detailStage.setTitle("Book Details");

                // Tạo VBox và áp dụng class CSS
                VBox detailContainer = new VBox();
                detailContainer.getStyleClass().add("detail-container");

                // Tạo các Label và áp dụng các class CSS tương ứng
                Label titleLabel = new Label("Title: " + (document.getTitle() != null ? document.getTitle() : "Unknown"));
                titleLabel.getStyleClass().add("book-title");

                Label authorLabel = new Label("Author ID: " + document.getAuthorId());
                authorLabel.getStyleClass().add("book-author");

                Label publisherLabel = new Label("Publisher ID: " + document.getPublisherId());
                publisherLabel.getStyleClass().add("book-publisher");

                Label yearLabel = new Label("Publication Year: " + document.getPublicationYear());
                yearLabel.getStyleClass().add("book-date");

                Label pageLabel = new Label("Pages: " + document.getPage());
                pageLabel.getStyleClass().add("book-pages");

                Label descriptionLabel = new Label("Description: " + (document.getDescription() != null ? document.getDescription() : "No description available"));
                descriptionLabel.getStyleClass().add("book-description");

                // Tạo nút "Back" và áp dụng class CSS
                Button backButton = new Button("Back");
                backButton.getStyleClass().add("back-button");
                backButton.setOnAction(e -> detailStage.close());

                // Thêm các phần tử vào VBox
                detailContainer.getChildren().addAll(titleLabel, authorLabel, publisherLabel, yearLabel, pageLabel, descriptionLabel, backButton);

                // Tạo và hiển thị Scene
                Scene detailScene = new Scene(detailContainer, 500, 400);
                detailScene.getStylesheets().add(getClass().getResource("/css/styling.css").toExternalForm()); // Thêm CSS vào Scene
                detailStage.setScene(detailScene);
                detailStage.show();
        }








}
