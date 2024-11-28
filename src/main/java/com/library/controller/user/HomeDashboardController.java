//package com.library.controller.user;
//
//
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//import com.library.api.GoogleBooksAPIClient;
//import com.library.dao.DocumentDao;
//import com.library.models.Author;
//import com.library.models.Document;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.geometry.Rectangle2D;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.ScrollPane;
//import javafx.scene.control.TextField;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.*;
//import javafx.scene.text.Text;
//import javafx.stage.Modality;
//import javafx.stage.Screen;
//import javafx.stage.Stage;
//
//import java.awt.*;
//import java.io.IOException;
//import java.net.URI;
//import java.util.List;
//import java.util.Random;
//
//public class HomeDashboardController {
//
//    @FXML
//    private TextField searchField;
//
//    @FXML
//    private ScrollPane myBooksScrollPane;
//
//    @FXML
//    private HBox myBooksContainer;
//
//    @FXML
//    private ImageView bestBookImage;
//
//    @FXML
//    private Label bestBookTitle;
//
//    @FXML
//    private Label bestBookAuthor;
//
//    @FXML
//    private Text bestBookDescription;
//
//    @FXML
//    private ScrollPane popularBooksScrollPane;
//
//    @FXML
//    private TilePane popularBooksGrid;
//
//
//    @FXML
//    private Button searchScreenButton;
//
//    @FXML
//    private Button libraryButton;
//
//    @FXML
//    private VBox searchResultsContainer;
//
//
//
//    private void openDashBoard() {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/User/search_screen.fxml"));
//            Parent bookView = loader.load();
//
//            Stage stage = (Stage) searchScreenButton.getScene().getWindow();
//            Scene scene = new Scene(bookView, 1466, 750);
//            scene.getStylesheets().add(getClass().getResource("/css/start/styling.css").toExternalForm());
//
//            stage.setScene(scene);
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // Opens BookView (existing feature)
//    private void openBookView() {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/User/book_viewer.fxml"));
//            Parent bookView = loader.load();
//
//            Stage stage = (Stage) libraryButton.getScene().getWindow();
//            Scene scene = new Scene(bookView, 1466, 750);
//            scene.getStylesheets().add(getClass().getResource("/css/styling.css").toExternalForm());
//
//            stage.setScene(scene);
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public void initialize() {
//        // Populate the "My Books" horizontal scroll bar
////          loadMyBooks();
////
////        // Load a random book as "Best Book"
//        loadBestBook();
////
////        // Populate the "Popular Books" grid
//        loadPopularBooks();
//
//        searchScreenButton.setOnAction(event -> {
//            System.out.println("Search button clicked!");
//            openDashBoard();
//        });
//
//        libraryButton.setOnAction(event -> openBookView());
//
//
//        // LỖI SWITCH SCENE BETWEEN HOME< FAV < SEARCH
//        // LỖI SWITCH SCENE BETWEEN HOME< FAV < SEARCH
//        // LỖI SWITCH SCENE BETWEEN HOME< FAV < SEARCH
//        // LỖI SWITCH SCENE BETWEEN HOME< FAV < SEARCH
//        // LỖI SWITCH SCENE BETWEEN HOME< FAV < SEARCH
//
//
//    }
//
//
//    @FXML
//    private void onSearchBooks() {
//        String query = searchField.getText().trim();
//
//        if (!query.isEmpty()) {
//            loadSearchResults(query);
//        } else {
//            searchResultsContainer.getChildren().clear();
//            Label noInputLabel = new Label("Please enter a search term.");
//            searchResultsContainer.getChildren().add(noInputLabel);
//        }
//    }
//
//    private void loadSearchResults(String query) {
//        searchResultsContainer.getChildren().clear();
//        DocumentDao documentDao = new DocumentDao();
//        //List<Document> documents = documentDao.searchByTitle(query);
//
//        // Lọc danh sách dựa trên từ khóa tìm kiếm
////                List<Document> filteredDocuments = documents.stream()
////                        .filter(doc -> doc.getTitle() != null && doc.getTitle().toLowerCase().contains(query.toLowerCase()))
////                        .collect(Collectors.toList());
//        List<Document> filteredDocuments = documentDao.searchByTitle(query);
//
//        if (!filteredDocuments.isEmpty()) {
//            for (Document document : filteredDocuments) {
//                HBox bookCard = new HBox();
//                bookCard.setSpacing(10);
//                bookCard.setStyle("-fx-padding: 10; -fx-border-color: #ddd; -fx-background-color: #fff; -fx-border-radius: 5; -fx-background-radius: 5;");
//
//                // Ảnh bìa sách
//                ImageView bookCover = new ImageView();
//                bookCover.setFitWidth(100);
//                bookCover.setFitHeight(150);
//                if (document.getImageLink() != null && !document.getImageLink().isEmpty()) {
//                    bookCover.setImage(new Image(document.getImageLink(), true));
//                    //aspect ratio of the image will be preserved ì true
//
//
//                    // loadImageLazy(document.getImageLink(), bookCover);
//                } else {
//                    bookCover.setImage(new Image("/ui/admindashboard/book1.png", true));
//                }
//
//                // Thông tin sách
//                VBox bookInfo = new VBox();
//                bookInfo.setSpacing(5);
//
//                Label titleLabel = new Label(document.getTitle() != null ? document.getTitle() : "Unknown Title");
//                titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
//
//                //Label authorLabel = new Label("Author ID: " + document.getAuthorId());
//                //DocumentDao documentDao = new DocumentDao();
//                Author author = documentDao.getAuthor(document.getAuthorId());
//                String authorName = (author != null) ? author.getName() : "Unknown Author";
//
//                Label authorLabel = new Label(authorName);
//                authorLabel.getStyleClass().add("book-author");
//
//                Label pageLabel = new Label(document.getPage() > 0 ? document.getPage() + " pages" : "Page count not available");
//
//                Button viewDetailsButton = new Button("View Details");
//                viewDetailsButton.getStyleClass().add("back-button");
//
//                viewDetailsButton.setOnAction(e -> showBookDetails(document));
//
//                bookInfo.getChildren().addAll(titleLabel, authorLabel, pageLabel, viewDetailsButton);
//                bookCard.getChildren().addAll(bookCover, bookInfo);
//
//                searchResultsContainer.getChildren().add(bookCard);
//            }
//        } else {
//            Label noResultsLabel = new Label("No results found for \"" + query + "\".");
//            searchResultsContainer.getChildren().add(noResultsLabel);
//        }
//    }
//
///*
//    private void loadMyBooks() {
//        searchResultsContainer.getChildren().clear(); // Clear previous results
//
//        // Fetch books from the database
//        DocumentDao documentDao = new DocumentDao();
//        List<Document> myBooks = documentDao.getAll();
//
//        if (myBooks != null && !myBooks.isEmpty()) {
//            for (Document book : myBooks) {
//                // Create a new HBox for each book cover
//                HBox bookCard = new HBox();
//                bookCard.setSpacing(10);
//
//                // Book Cover Image
//                ImageView bookCover = new ImageView();
//                bookCover.setFitWidth(100);   // Set width of the image
//                bookCover.setFitHeight(150);  // Set height of the image
//                if (book.getImageLink() != null && !book.getImageLink().isEmpty()) {
//                    bookCover.setImage(new Image(book.getImageLink(), true));  // Use the image from the database
//                } else {
//                    bookCover.setImage(new Image("/ui/admindashboard/book1.png", true));  // Default image if no image link is available
//                }
//
//                // When the user clicks the cover, you can show details (Optional)
//                bookCover.setOnMouseClicked(e -> showBookDetails(book));  // Show book details when clicked
//
//                // Add the book cover to the book card
//                bookCard.getChildren().add(bookCover);
//
//                // Add the book card (only cover) to the container
//                searchResultsContainer.getChildren().add(bookCard);
//            }
//        } else {
//            Label noResultsLabel = new Label("No books found in your library.");
//            searchResultsContainer.getChildren().add(noResultsLabel);
//        }
//    }
//
//*/
//
//
///*
//    private void loadMyBooks() {
//        myBooksContainer.getChildren().clear(); // Clear current books
//
//        // Fetch books from database
//        DocumentDao documentDao = new DocumentDao();
//        List<Document> myBooks = documentDao.searchByTitle("harry"); // Method to fetch books
//
//        if (!myBooks.isEmpty()) {
//            for (Document book : myBooks) {
//                ImageView bookThumbnail = new ImageView();
//                bookThumbnail.setFitWidth(100);
//                bookThumbnail.setFitHeight(140);
//
//                if (book.getImageLink() != null && !book.getImageLink().isEmpty()) {
//                    bookThumbnail.setImage(new javafx.scene.image.Image(book.getImageLink(), true));
//                } else {
//                    bookThumbnail.setImage(new javafx.scene.image.Image("/ui/admindashboard/book1.png", true));
//                }
//
//                // Add click event to view book details
//                bookThumbnail.setOnMouseClicked(e -> showBookDetails(book));
//
//                myBooksContainer.getChildren().add(bookThumbnail);
//            }
//        } else {
//            Label noBooksLabel = new Label("No books found in your library.");
//            myBooksContainer.getChildren().add(noBooksLabel);
//        }
//    }
//*/
//
//    /*
//    private void loadBestBook() {
//        // Example data for the "Best Book"
//        JsonArray books = GoogleBooksAPIClient.getBooks("bestsellers"); // Tìm theo title
//
//
//            bestBookImage.setImage(new javafx.scene.image.Image("/images/Dune_cover.jpg"));
//            bestBookTitle.setText("DUNE");
//            bestBookAuthor.setText("Herbert Franklin");
//            bestBookDescription.setText("This is an example description for the best book.");
//
//    }
//    */
//
//
//    private void loadBestBook() {
//        // Fetch books from the Google Books API using the "bestsellers" query
//        JsonArray books = GoogleBooksAPIClient.getBooks("bestsellers"); // This gets the bestsellers
//
//
//        if (books != null && books.size() > 0) {
//            // Create a Random object to pick a random book from the list
//            Random random = new Random();
//            int randomIndex = random.nextInt(books.size());  // Select a random index within the list size
//
//            // Get the random book from the list
//            JsonObject bestBook = books.get(randomIndex).getAsJsonObject().getAsJsonObject("volumeInfo");
//
//            // Set the book image (Cover)
//            if (bestBook.has("imageLinks")) {
//                String imageUrl = bestBook.getAsJsonObject("imageLinks").get("thumbnail").getAsString();
//                bestBookImage.setImage(new Image(imageUrl));
//            } else {
//                bestBookImage.setImage(new Image("/images/default_cover.jpg")); // Fallback if no cover
//            }
//
//            // Set the book title
//            String title = bestBook.has("title") ? bestBook.get("title").getAsString() : "Unknown Title";
//            bestBookTitle.setText(title);
//
//            // Set the book author
//            String author = bestBook.has("authors") ? String.join(", ", bestBook.getAsJsonArray("authors").toString()) : "Unknown Author";
//            bestBookAuthor.setText(author);
//
//            // Set the book description
//            String description = bestBook.has("description") ? bestBook.get("description").getAsString() : "No description available";
//            if (description.length() > 450) {
//                description = description.substring(0, 450) + "...";  // Trim and append ellipsis
//            }
//
//            bestBookDescription.setText(description);
//        } else {
//            // Handle the case where no books were found
//            bestBookTitle.setText("No best book found.");
//            bestBookAuthor.setText("");
//            bestBookDescription.setText("");
//            bestBookImage.setImage(new Image("/images/default_cover.jpg"));
//        }
//    }
//
//
//    /*
//
//    private void loadPopularBooks() {
//        popularBooksGrid.getChildren().clear(); // Clear current books
//
//        // Fetch popular books from database
//        DocumentDao documentDao = new DocumentDao();
//        List<Document> popularBooks = documentDao.searchByTitle("harry"); // Method to fetch books
//
//        if (!popularBooks.isEmpty()) {
//            for (Document book : popularBooks) {
//                ImageView bookThumbnail = new ImageView();
//                bookThumbnail.setFitWidth(100);
//                bookThumbnail.setFitHeight(140);
//
//                if (book.getImageLink() != null && !book.getImageLink().isEmpty()) {
//                    bookThumbnail.setImage(new javafx.scene.image.Image(book.getImageLink(), true));
//                } else {
//                    bookThumbnail.setImage(new javafx.scene.image.Image("/ui/admindashboard/book1.png", true));
//                }
//
//                // Add click event to view book details
//                bookThumbnail.setOnMouseClicked(e -> showBookDetails(book));
//
//                popularBooksGrid.getChildren().add(bookThumbnail);
//            }
//        } else {
//            Label noPopularBooksLabel = new Label("No popular books available.");
//            popularBooksGrid.getChildren().add(noPopularBooksLabel);
//        }
//    } */
//
//
//
//    //Load popular works (chưa chỉnh layout)
//
//    //private VBox booksContainer; // The VBox will contain the book cards
//
//    /*
//    // Inside your initialize or other relevant method:
//    private void loadPopularBooks() {
//        JsonArray books = GoogleBooksAPIClient.getBooks("bestsellers");  // Fetch books from API
//
//        booksContainer = new VBox();
//        booksContainer.setSpacing(10); // Spacing between book cards
//        popularBooksScrollPane.setContent(booksContainer); // Set the VBox as the ScrollPane content
//
//        if (books != null && books.size() > 0) {
//            booksContainer.getChildren().clear(); // Clear any previous results
//
//            for (int i = 0; i < books.size(); i++) {
//                JsonObject book = books.get(i).getAsJsonObject().getAsJsonObject("volumeInfo");
//
//                HBox bookCard = new HBox();
//                bookCard.setSpacing(10);
//                bookCard.setStyle("-fx-padding: 10; -fx-border-color: #ddd; -fx-background-color: #fff; -fx-border-radius: 5; -fx-background-radius: 5;");
//
//                // Book Cover
//                ImageView bookCover = new ImageView();
//                bookCover.setFitWidth(100);
//                bookCover.setFitHeight(150);
//                if (book.has("imageLinks")) {
//                    String imageUrl = book.getAsJsonObject("imageLinks").get("thumbnail").getAsString();
//                    bookCover.setImage(new Image(imageUrl, true));
//                }
//
//                // Book Information
//                VBox bookInfo = new VBox();
//                bookInfo.setSpacing(5);
//
//                Label titleLabel = new Label(book.has("title") ? book.get("title").getAsString() : "Unknown Title");
//                titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
//
//                Label authorLabel = new Label(book.has("authors") ? String.join(", ", book.getAsJsonArray("authors").toString()) : "Unknown Author");
//                Label pageLabel = new Label(book.has("pageCount") ? book.get("pageCount").getAsString() + " pages" : "Page count not available");
//
//                Button viewDetailsButton = new Button("View Details");
//                viewDetailsButton.setOnAction(e -> showBookDetailsOnline(book));
//
//                bookInfo.getChildren().addAll(titleLabel, authorLabel, pageLabel, viewDetailsButton);
//                bookCard.getChildren().addAll(bookCover, bookInfo);
//
//                // Add the book card to the container
//                booksContainer.getChildren().add(bookCard);
//            }
//        } else {
//            Label noResultsLabel = new Label("No results found.");
//            booksContainer.getChildren().add(noResultsLabel);
//        }
//    }
//
//
//     */
//
////    private FlowPane booksContainer;
////    private void loadPopularBooks() {
////        //JsonArray books = GoogleBooksAPIClient.getBooks("bestsellers"); // Tìm theo title
////        JsonArray books = GoogleBooksAPIClient.getBooks("subject:science");  // Tim theo category
////
////        booksContainer = new FlowPane();
////        booksContainer.setHgap(10); // Horizontal gap between book cards
////        booksContainer.setVgap(10); // Vertical gap between book cards
////        booksContainer.setPrefWrapLength(600); // Set the width to wrap after 5 items (adjust as needed)
////        popularBooksScrollPane.setContent(booksContainer); // Set the FlowPane as the ScrollPane content
////
////        if (books != null && books.size() > 0) {
////            booksContainer.getChildren().clear();
////
////            for (int i = 0; i < books.size(); i++) {
////                JsonObject book = books.get(i).getAsJsonObject().getAsJsonObject("volumeInfo");
////
////                HBox bookCard = new HBox();
////                bookCard.setSpacing(10);
////                bookCard.setStyle("-fx-padding: 10; -fx-border-color: #ddd; -fx-background-color: #fff; -fx-border-radius: 5; -fx-background-radius: 5;");
////                bookCard.setPrefWidth(250); // chỉnh size của box books
////
////                // Book Cover
////                ImageView bookCover = new ImageView();
////                bookCover.setFitWidth(100);
////                bookCover.setFitHeight(150);
////                if (book.has("imageLinks")) {
////                    String imageUrl = book.getAsJsonObject("imageLinks").get("thumbnail").getAsString();
////                    bookCover.setImage(new Image(imageUrl, true));
////                }
////
////                // Book Information
////                VBox bookInfo = new VBox();
////                bookInfo.setSpacing(5);
////
////                Label titleLabel = new Label(book.has("title") ? book.get("title").getAsString() : "Unknown Title");
////                titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
////
////                Label authorLabel = new Label(book.has("authors") ? String.join(", ", book.getAsJsonArray("authors").toString()) : "Unknown Author");
////                Label pageLabel = new Label(book.has("pageCount") ? book.get("pageCount").getAsString() + " pages" : "Page count not available");
////
////                Button viewDetailsButton = new Button("View Details");
////                viewDetailsButton.setOnAction(e -> showBookDetailsOnline(book));
////
////                bookInfo.getChildren().addAll(titleLabel, authorLabel, pageLabel, viewDetailsButton);
////
////                // To fill space between book cover and book info
////                Region spacer = new Region();
////                HBox.setHgrow(spacer, Priority.ALWAYS); // Makes spacer flexible and pushes content to the right
////
////                // Add cover and spacer to the card
////                bookCard.getChildren().addAll(bookCover, spacer, bookInfo);
////
////                // Add the book card to the container
////                booksContainer.getChildren().add(bookCard);
////            }
////        } else {
////            Label noResultsLabel = new Label("No results found.");
////            booksContainer.getChildren().add(noResultsLabel);
////        }
////    }
//
//    private FlowPane booksContainer;
//
//    private void loadPopularBooks() {
//        // Lấy dữ liệu sách từ Google Books API
//        JsonArray books = GoogleBooksAPIClient.getBooks("subject:science");
//
//        booksContainer = new FlowPane();
//        booksContainer.setHgap(10); // Khoảng cách ngang giữa các card sách
//        booksContainer.setVgap(10); // Khoảng cách dọc giữa các card sách
//        booksContainer.setPrefWrapLength(600); // Điều chỉnh chiều rộng để các card tự động xuống dòng
//        popularBooksScrollPane.setContent(booksContainer); // Đặt FlowPane vào ScrollPane
//
//        if (books != null && books.size() > 0) {
//            booksContainer.getChildren().clear();
//
//            for (int i = 0; i < books.size(); i++) {
//                JsonObject book = books.get(i).getAsJsonObject().getAsJsonObject("volumeInfo");
//
//                // Tạo layout cho mỗi sách
//                VBox bookCard = new VBox();
//                bookCard.setSpacing(10);
//                bookCard.setStyle("-fx-padding: 10; -fx-border-color: #ddd; -fx-background-color: #fff; -fx-border-radius: 10; -fx-background-radius: 10;");
//                bookCard.setPrefWidth(250);
//
//                // Bìa sách
//                ImageView bookCover = new ImageView();
//                bookCover.setFitWidth(150);
//                bookCover.setFitHeight(200);
//                if (book.has("imageLinks")) {
//                    String imageUrl = book.getAsJsonObject("imageLinks").get("thumbnail").getAsString();
//                    bookCover.setImage(new Image(imageUrl, true));
//                }
//
//                // Thông tin sách (dưới bìa sách)
//                VBox bookInfo = new VBox();
//                bookInfo.setSpacing(5);
//
//                Label titleLabel = new Label(book.has("title") ? book.get("title").getAsString() : "Unknown Title");
//                titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
//
//                Label authorLabel = new Label(book.has("authors") ? String.join(", ", book.getAsJsonArray("authors").toString()) : "Unknown Author");
//                Label pageLabel = new Label(book.has("pageCount") ? book.get("pageCount").getAsString() + " pages" : "Page count not available");
//
//                // Thêm thông tin vào VBox
//                bookInfo.getChildren().addAll(titleLabel, authorLabel, pageLabel);
//
//                // Xử lý click vào bìa sách để mở modal hiển thị chi tiết
//                bookCover.setOnMouseClicked(e -> showBookDetailsModal(book));
//
//                // Thêm bìa sách và thông tin vào layout card sách
//                bookCard.getChildren().addAll(bookCover, bookInfo);
//
//                // Thêm card sách vào FlowPane
//                booksContainer.getChildren().add(bookCard);
//            }
//        } else {
//            Label noResultsLabel = new Label("No results found.");
//            booksContainer.getChildren().add(noResultsLabel);
//        }
//    }
//
//    // Hàm để hiển thị modal khi click vào bìa sách
//    private void showBookDetailsModal(JsonObject book) {
//        // Tạo một cửa sổ mới (Modal) để hiển thị chi tiết sách
//        String title = book.has("title") ? book.get("title").getAsString() : "Unknown Title";
//        String description = book.has("description") ? book.get("description").getAsString() : "No description available.";
//        String authors = book.has("authors") ? String.join(", ", book.getAsJsonArray("authors").toString()) : "Unknown Author";
//        String publisher = book.has("publisher") ? book.get("publisher").getAsString() : "Unknown Publisher";
//        String publishedDate = book.has("publishedDate") ? book.get("publishedDate").getAsString() : "Unknown Date";
//
//        // Tạo một Stage mới cho Modal
//        Stage modalStage = new Stage();
//        modalStage.initModality(Modality.APPLICATION_MODAL); // Chỉ cho phép thao tác trong modal này
//        modalStage.setTitle(title); // Tiêu đề cửa sổ modal là tên sách
//
//        // Layout của modal
//        VBox modalLayout = new VBox(10);
//        modalLayout.setPadding(new Insets(20));
//        modalLayout.setStyle("-fx-background-color: white; -fx-border-radius: 10; -fx-background-radius: 10;");
//
//        // Hiển thị chi tiết sách trong modal
//        Label titleLabel = new Label("Title: " + title);
//        Label authorLabel = new Label("Authors: " + authors);
//        Label publisherLabel = new Label("Publisher: " + publisher);
//        Label publishedDateLabel = new Label("Published Date: " + publishedDate);
//        Text descriptionText = new Text(description);
//
//        modalLayout.getChildren().addAll(titleLabel, authorLabel, publisherLabel, publishedDateLabel, descriptionText);
//
//        // Scene và Stage
//        Scene modalScene = new Scene(modalLayout);
//        modalStage.setScene(modalScene);
//        modalStage.show();
//    }
//
//
//
//
//
//
//
//
//
//
//    private void showBookDetails(Document document) {
//        // Tạo cửa sổ chi tiết
//        Stage detailStage = new Stage();
//        detailStage.setTitle("Book Details");
//
//        // Tạo GridPane cho bố cục
//        GridPane detailContainer = new GridPane();
//        detailContainer.getStyleClass().add("detail-container");
//        detailContainer.setHgap(10);
//        detailContainer.setVgap(10);
//        detailContainer.setPadding(new Insets(20));
//
//        // Tạo ImageView để hiển thị bìa sách
//        ImageView bookCover = new ImageView();
//        bookCover.setFitWidth(150);
//        bookCover.setFitHeight(200);
//        bookCover.setPreserveRatio(true);
//        if (document.getImageLink() != null && !document.getImageLink().isEmpty()) {
//            bookCover.setImage(new Image(document.getImageLink(), true));
//        } else {
//            bookCover.setImage(new Image("/ui/admindashboard/book1.png")); // Ảnh mặc định nếu không có
//        }
//
//        // Tạo các nhãn hiển thị thông tin
//        Label titleValue = new Label(document.getTitle() != null ? document.getTitle() : "Unknown");
//        titleValue.getStyleClass().add("book-title");
//        titleValue.setWrapText(true); // Enable wrapping
//
//        // Lấy tên tác giả từ DocumentDao
//        DocumentDao documentDao = new DocumentDao();
//        Author author = documentDao.getAuthor(document.getAuthorId());
//        String authorName = (author != null) ? author.getName() : "Unknown Author";
//
//        Label authorValue = new Label(authorName);
//        authorValue.getStyleClass().add("book-author");
//
//        Label yearValue = new Label(String.valueOf(document.getPublicationYear()));
//        yearValue.getStyleClass().add("book-date");
//
//        Label descriptionValue = new Label(document.getDescription() != null ? document.getDescription() : "No description available");
//        descriptionValue.getStyleClass().add("book-description");
//        descriptionValue.setWrapText(true);
//
//        // Tạo nút Back
//        Button backButton = new Button("Back");
//        backButton.getStyleClass().add("back-button");
//        backButton.setOnAction(e -> detailStage.close());
//
//        // Tạo Hyperlink cho liên kết sách preview nếu có
//        Hyperlink previewLink = null;
//        if (document.getPreviewLink() != null && !document.getPreviewLink().isEmpty()) {
//            previewLink = new Hyperlink("View Preview");
//            previewLink.setOnAction(e -> {
//                try {
//                    // Mở liên kết preview trong trình duyệt
//                    Desktop.getDesktop().browse(URI.create(document.getPreviewLink()));
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
//            });
//            previewLink.getStyleClass().add("book-preview-link");
//        }
//
//        // Tạo "Add to Favorites" label
//        Label addToFavoritesLabel = new Label("Add to Favorites");
//        addToFavoritesLabel.getStyleClass().add("add-to-favorites");
//        //addToFavoritesLabel.setOnMouseClicked(e -> addToFavorites(document)); // Handle click event
//
//        // Đặt các phần tử vào GridPane
//        detailContainer.add(bookCover, 0, 0, 1, 6); // Ảnh bìa chiếm 6 dòng
//        detailContainer.add(titleValue, 2, 0);
//        detailContainer.add(authorValue, 2, 1);
//        detailContainer.add(yearValue, 2, 3);
//        detailContainer.add(descriptionValue, 2, 5);
//        if (previewLink != null) {
//            detailContainer.add(previewLink, 2, 7); // Thêm liên kết preview nếu có
//        }
//        detailContainer.add(addToFavoritesLabel, 2, 8); // Add to Favorites label
//        detailContainer.add(backButton, 2, 9);
//
//        // Tạo StackPane để căn giữa nội dung
//        StackPane root = new StackPane(detailContainer);
//        root.setAlignment(Pos.CENTER); // Căn giữa nội dung
//        root.setPadding(new Insets(10)); // Padding nếu cần
//
//        // Tạo Scene và thêm CSS
//        Scene detailScene = new Scene(root, 715, 590);
//        detailScene.getStylesheets().add(getClass().getResource("/css/styling.css").toExternalForm());
//
//        // Set Scene cho Stage
//        detailStage.setScene(detailScene);
//
//        // Đảm bảo Stage hiển thị ở giữa màn hình
//        detailStage.setOnShown(event -> {
//            // Tính toán kích thước màn hình
//            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
//
//            // Đặt vị trí chính giữa
//            detailStage.setX((screenBounds.getWidth() - detailStage.getWidth()) / 2);
//            detailStage.setY((screenBounds.getHeight() - detailStage.getHeight()) / 2);
//        });
//
//        // Hiển thị cửa sổ
//        detailStage.show();
//    }
//
//
//
//}
//
//
