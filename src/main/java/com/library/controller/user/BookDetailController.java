//package com.library.controller.user;
//
//import com.google.gson.JsonObject;
//import com.library.dao.DocumentDao;
//import com.library.models.Author;
//import com.library.models.Document;
//import javafx.fxml.FXML;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.geometry.Rectangle2D;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.Hyperlink;
//import javafx.scene.control.Label;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.GridPane;
//import javafx.scene.layout.StackPane;
//import javafx.stage.Modality;
//import javafx.stage.Screen;
//import javafx.stage.Stage;
//
//import java.awt.*;
//import java.io.IOException;
//import java.net.URI;
//
//public class BookDetailController {
//
//    @FXML
//    private ImageView bookCover;
//
//    @FXML
//    private Label bookTitle, bookAuthor, bookDescription, bookPages, bookPublishedDate, bookPublisher;
//
//    @FXML
//    private Hyperlink previewLink;
//
//
//
//
//
//
//
//    /*public void loadBookDetails(Document document) {
//        if (document != null) {
//            // Hiển thị tiêu đề sách
//            bookTitle.setText(document.getTitle() != null ? document.getTitle() : "Unknown Title");
//
//            // Hiển thị tên tác giả
//            DocumentDao documentDao = new DocumentDao();
//            Author author = documentDao.getAuthor(document.getAuthorId());
//            String authorName = (author != null) ? author.getName() : "Unknown Author";
//            bookAuthor.setText(authorName);
//
//            // Hiển thị mô tả sách
//            bookDescription.setText(document.getDescription() != null ? document.getDescription() : "No description available.");
//
//            // Hiển thị số trang
//            bookPages.setText(document.getPage() > 0 ? document.getPage() + " pages" : "Page count not available");
//
//            // Hiển thị năm xuất bản
//            bookPublishedDate.setText(document.getPublicationYear() > 0 ? "Published Date: " + document.getPublicationYear() : "Published Date: N/A");
//
//            // Hiển thị nhà xuất bản
//            bookPublisher.setText(document.getPublisher() != null ? "Publisher: " + document.getPublisher() : "Publisher: Unknown");
//
//            // Hiển thị ảnh bìa
//            if (document.getImageLink() != null && !document.getImageLink().isEmpty()) {
//                bookCover.setImage(new Image(document.getImageLink(), true));
//            } else {
//                bookCover.setImage(new Image("/ui/admindashboard/book1.png")); // Ảnh mặc định nếu không có
//            }
//
//            // Hiển thị liên kết preview (nếu có)
//            if (document.getPreviewLink() != null && !document.getPreviewLink().isEmpty()) {
//                Label previewLinkLabel = new Label("Preview Link: ");
//                Hyperlink previewLink = new Hyperlink(document.getPreviewLink());
//                previewLink.setOnAction(e -> {
//                    try {
//                        java.awt.Desktop.getDesktop().browse(new java.net.URI(document.getPreviewLink()));
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
//                });
//                // Gắn label và hyperlink vào giao diện
//            }
//        } else {
//            // Hiển thị thông báo mặc định nếu không có dữ liệu
//            bookTitle.setText("No details available.");
//            bookAuthor.setText("");
//            bookDescription.setText("");
//            bookPages.setText("");
//            bookPublishedDate.setText("");
//            bookPublisher.setText("");
//            bookCover.setImage(null); // Xóa ảnh
//        }
//    }
//
//     */
//
//    public void loadBookDetails(Document document) {
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
//    @FXML
//    private void onBackClicked() {
//        bookCover.getScene().getWindow().hide(); // Close the modal
//    }
//
//
////    public void showBookDetailsWindow() {
////        // Tạo cửa sổ mới (Stage)
////        Stage stage = new Stage();
////        stage.setTitle("Book Details");
////
////        // Cấu hình cho cửa sổ nhỏ
////        StackPane root = new StackPane();
////        root.getChildren().add(new Button("Book Information here"));
////
////        Scene scene = new Scene(root, 715, 590);  // Kích thước cửa sổ nhỏ
////        scene.getStylesheets().add(getClass().getResource("/css/styling.css").toExternalForm());
////
////        stage.setScene(scene);
////
////        // Đảm bảo cửa sổ này luôn ở trên cửa sổ chính
////        stage.initModality(Modality.APPLICATION_MODAL);  // Modal cửa sổ con
////
////        // Đặt cửa sổ ở giữa màn hình
////        stage.setX(400);  // Vị trí từ trái
////        stage.setY(200);  // Vị trí từ trên xuống
////
////        // Hiển thị cửa sổ nhỏ
////        stage.show();
////    }
//}

package com.library.controller.user;

import com.library.dao.DocumentDao;
import com.library.dao.ReservationDao;
import com.library.models.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebView;


import java.awt.Desktop;
import java.net.URI;
import java.time.LocalDateTime;

public class BookDetailController {

    @FXML
    private ImageView bookCover;

    @FXML
    private Label bookTitle, bookAuthor, bookPublishedDate, bookDescription;

    @FXML
    private Hyperlink previewLink;

    @FXML
    private Button addToFavoritesButton, requestButton, backButton;

    /*
    public void loadBookDetails(Document document) {
        if (document != null) {
            bookTitle.setText(document.getTitle() != null ? document.getTitle() : "Unknown Title");

            DocumentDao documentDao = new DocumentDao();
            Author author = documentDao.getAuthor(document.getAuthorId());
            String authorName = (author != null) ? author.getName() : "Unknown Author";
            bookAuthor.setText("Author: " + authorName);

            bookPublishedDate.setText(document.getPublicationYear() > 0
                    ? "Published: " + document.getPublicationYear()
                    : "Published: N/A");

            bookDescription.setText(document.getDescription() != null
                    ? document.getDescription()
                    : "No description available.");

            if (document.getImageLink() != null && !document.getImageLink().isEmpty()) {
                bookCover.setImage(new Image(document.getImageLink(), true));
            } else {
                bookCover.setImage(new Image("/ui/admindashboard/book1.png")); // Default image
            }

            if (document.getPreviewLink() != null && !document.getPreviewLink().isEmpty()) {
                previewLink.setText("View Preview");
                previewLink.setOnAction(e -> {
                    try {
                        Desktop.getDesktop().browse(new URI(document.getPreviewLink()));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
                previewLink.setVisible(true);
            } else {
                previewLink.setVisible(false);
            }
        } else {
            bookTitle.setText("No details available.");
            bookAuthor.setText("");
            bookPublishedDate.setText("");
            bookDescription.setText("");
            bookCover.setImage(null);
            previewLink.setVisible(false);
        }
    }

    */

    @FXML
    private WebView previewWebView;  // WebView for previewing the book

    /**
     * Load book details into the view components.
     *
     * @param document The Document object containing the book's details.
     */
    public void loadBookDetails(Document document) {
        if (document != null) {
            // Set book title
            bookTitle.setText(document.getTitle() != null ? document.getTitle() : "Unknown Title");

            // Set author name
            DocumentDao documentDao = new DocumentDao();
            Author author = documentDao.getAuthor(document.getAuthorId());
            String authorName = (author != null) ? author.getName() : "Unknown Author";
            bookAuthor.setText("Author: " + authorName);

            // Set publication year
            bookPublishedDate.setText(document.getPublicationYear() > 0
                    ? "Published: " + document.getPublicationYear()
                    : "Published: N/A");

            // Set description
            bookDescription.setText(document.getDescription() != null
                    ? document.getDescription()
                    : "No description available.");

            // Set book cover image
            if (document.getImageLink() != null && !document.getImageLink().isEmpty()) {
                bookCover.setImage(new Image(document.getImageLink(), true));
            } else {
                bookCover.setImage(new Image("/ui/admindashboard/book1.png")); // Default image
            }

            // Set preview link
            if (document.getPreviewLink() != null && !document.getPreviewLink().isEmpty()) {
                previewLink.setText("View Preview");
                previewLink.setOnAction(e -> {
                    try {
                        Desktop.getDesktop().browse(new URI(document.getPreviewLink()));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
                previewLink.setVisible(true);

                // Also load the preview into the WebView if available
                previewWebView.setVisible(true);
                previewWebView.getEngine().load(document.getPreviewLink());
            } else {
                previewLink.setVisible(false);
                previewWebView.setVisible(false);
            }
        } else {
            // Default values when no document is loaded
            bookTitle.setText("No details available.");
            bookAuthor.setText("");
            bookPublishedDate.setText("");
            bookDescription.setText("");
            bookCover.setImage(null);
            previewLink.setVisible(false);
            previewWebView.setVisible(false);
        }
    }

    @FXML
    private void onBackClicked() {
        backButton.getScene().getWindow().hide(); // Close the current window
    }

    @FXML
    private void onAddToFavoritesClicked() {
        System.out.println("Book added to favorites!");
    }

//    @FXML
//    private void onRequestClicked(Document document) {
//        requestButton.setOnAction(event -> onRequestClicked(document));//        User currentUser = getCurrentUser();
////        if (currentUser != null && document != null) {
////            // Create a new reservation instance
////            Reservation reservation = new Reservation();
////            reservation.setUserId(currentUser.getUserId());
////            reservation.setIsbn(document.getISBN());
////            reservation.setReservationDate(LocalDateTime.now());
////            reservation.setStatus("active");
////
//            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//            alert.setTitle("Request Book");
//            alert.setHeaderText("Do you want to request this book?");
//
//            String bookDetails = "Title: " + document.getTitle() + "\n" +
//                    "Author: " + document.getAuthor() + "\n" +
//                    "Published: " + document.getPublicationYear() + "\n" +
//                    "Description: " + document.getDescription();
//
//            TextArea textArea = new TextArea(bookDetails);
//            textArea.setWrapText(true);
//            textArea.setEditable(false);
//
//            alert.getDialogPane().setContent(textArea);
//
//            ButtonType requestButton = new ButtonType("Request now");
//            ButtonType cancelButton = new ButtonType("Cancel");
//            alert.getButtonTypes().setAll(requestButton, cancelButton);
//
////            alert.showAndWait().ifPresent(response -> {
////                if (response == requestButton) {
////                    ReservationDao reservationDao = new ReservationDao();
////                    reservationDao.add(reservation);
////                    System.out.println("Request for book sent!");
////                } else {
////                    System.out.println("Request cancelled.");
////                }
////            });
////        } else {
////            System.out.println("No user or book details available.");
////        }
//    }

    @FXML
    private void onRequestClicked() {
        System.out.println("Book requested!");
    }



}
