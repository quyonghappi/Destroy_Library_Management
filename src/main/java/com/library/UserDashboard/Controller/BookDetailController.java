package com.library.UserDashboard.Controller;

import com.google.gson.JsonObject;
import com.library.dao.DocumentDao;
import com.library.models.Author;
import com.library.models.Document;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BookDetailController {

    @FXML
    private ImageView bookCover;

    @FXML
    private Label bookTitle, bookAuthor, bookDescription, bookPages, bookPublishedDate, bookPublisher;

    @FXML
    private Hyperlink previewLink;


    public void loadBookDetails(JsonObject book) {
        if (book != null) {
            JsonObject volumeInfo = book.has("volumeInfo") ? book.getAsJsonObject("volumeInfo") : null;

            if (volumeInfo != null) {
                bookTitle.setText(volumeInfo.has("title") ? volumeInfo.get("title").getAsString() : "Unknown Title");
                bookAuthor.setText(volumeInfo.has("authors") ? String.join(", ", volumeInfo.getAsJsonArray("authors").toString().replaceAll("[\\[\\]\"]", "")) : "Unknown Author");
                bookDescription.setText(volumeInfo.has("description") ? volumeInfo.get("description").getAsString() : "No description available.");
                bookPages.setText(volumeInfo.has("pageCount") ? volumeInfo.get("pageCount").getAsString() + " pages" : "Page count not available");
                bookPublishedDate.setText(volumeInfo.has("publishedDate") ? "Published Date: " + volumeInfo.get("publishedDate").getAsString() : "Published Date: N/A");
                bookPublisher.setText(volumeInfo.has("publisher") ? "Publisher: " + volumeInfo.get("publisher").getAsString() : "Publisher: Unknown");

                if (volumeInfo.has("imageLinks") && volumeInfo.getAsJsonObject("imageLinks").has("thumbnail")) {
                    String imageUrl = volumeInfo.getAsJsonObject("imageLinks").get("thumbnail").getAsString();
                    bookCover.setImage(new Image(imageUrl, true));
                }
            } else {
                bookTitle.setText("No details available.");
                bookAuthor.setText("");
                bookDescription.setText("");
                bookPages.setText("");
                bookPublishedDate.setText("");
                bookPublisher.setText("");
                bookCover.setImage(null); // Clear image if no details available
            }
        } else {
            System.out.println("Book object is null!"); // Debugging
        }
    }




    public void loadBookDetails(Document document) {
        if (document != null) {
            // Hiển thị tiêu đề sách
            bookTitle.setText(document.getTitle() != null ? document.getTitle() : "Unknown Title");

            // Hiển thị tên tác giả
            DocumentDao documentDao = new DocumentDao();
            Author author = documentDao.getAuthor(document.getAuthorId());
            String authorName = (author != null) ? author.getName() : "Unknown Author";
            bookAuthor.setText(authorName);

            // Hiển thị mô tả sách
            bookDescription.setText(document.getDescription() != null ? document.getDescription() : "No description available.");

            // Hiển thị số trang
            bookPages.setText(document.getPage() > 0 ? document.getPage() + " pages" : "Page count not available");

            // Hiển thị năm xuất bản
            bookPublishedDate.setText(document.getPublicationYear() > 0 ? "Published Date: " + document.getPublicationYear() : "Published Date: N/A");

            // Hiển thị nhà xuất bản
            bookPublisher.setText(document.getPublisher() != null ? "Publisher: " + document.getPublisher() : "Publisher: Unknown");

            // Hiển thị ảnh bìa
            if (document.getImageLink() != null && !document.getImageLink().isEmpty()) {
                bookCover.setImage(new Image(document.getImageLink(), true));
            } else {
                bookCover.setImage(new Image("/ui/admindashboard/book1.png")); // Ảnh mặc định nếu không có
            }

            // Hiển thị liên kết preview (nếu có)
            if (document.getPreviewLink() != null && !document.getPreviewLink().isEmpty()) {
                Label previewLinkLabel = new Label("Preview Link: ");
                Hyperlink previewLink = new Hyperlink(document.getPreviewLink());
                previewLink.setOnAction(e -> {
                    try {
                        java.awt.Desktop.getDesktop().browse(new java.net.URI(document.getPreviewLink()));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
                // Gắn label và hyperlink vào giao diện
            }
        } else {
            // Hiển thị thông báo mặc định nếu không có dữ liệu
            bookTitle.setText("No details available.");
            bookAuthor.setText("");
            bookDescription.setText("");
            bookPages.setText("");
            bookPublishedDate.setText("");
            bookPublisher.setText("");
            bookCover.setImage(null); // Xóa ảnh
        }
    }





    @FXML
    private void onBackClicked() {
        bookCover.getScene().getWindow().hide(); // Close the modal
    }


    public void showBookDetailsWindow() {
        // Tạo cửa sổ mới (Stage)
        Stage stage = new Stage();
        stage.setTitle("Book Details");

        // Cấu hình cho cửa sổ nhỏ
        StackPane root = new StackPane();
        root.getChildren().add(new Button("Book Information here"));

        Scene scene = new Scene(root, 715, 590);  // Kích thước cửa sổ nhỏ
        scene.getStylesheets().add(getClass().getResource("/css/styling.css").toExternalForm());

        stage.setScene(scene);

        // Đảm bảo cửa sổ này luôn ở trên cửa sổ chính
        stage.initModality(Modality.APPLICATION_MODAL);  // Modal cửa sổ con

        // Đặt cửa sổ ở giữa màn hình
        stage.setX(400);  // Vị trí từ trái
        stage.setY(200);  // Vị trí từ trên xuống

        // Hiển thị cửa sổ nhỏ
        stage.show();
    }
}
