package com.library.UserDashboard.Controller;

import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BookDetailController {

    @FXML
    private ImageView bookCover;

    @FXML
    private Label bookTitle, bookAuthor, bookDescription, bookPages, bookPublishedDate, bookPublisher;

    /*
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
    */

    public void loadBookDetails(JsonObject book) {
        if (book != null && book.has("volumeInfo")) {
            JsonObject volumeInfo = book.getAsJsonObject("volumeInfo");

            // Kiểm tra từng trường và hiển thị thông tin
            bookTitle.setText(volumeInfo.has("title") ? volumeInfo.get("title").getAsString() : "Unknown Title");
            bookAuthor.setText(volumeInfo.has("authors")
                    ? String.join(", ", volumeInfo.getAsJsonArray("authors").toString().replaceAll("[\\[\\]\"]", ""))
                    : "Unknown Author");
            bookDescription.setText(volumeInfo.has("description") ? volumeInfo.get("description").getAsString() : "No description available.");
            bookPages.setText(volumeInfo.has("pageCount") ? volumeInfo.get("pageCount").getAsString() + " pages" : "Page count not available");
            bookPublishedDate.setText(volumeInfo.has("publishedDate") ? "Published Date: " + volumeInfo.get("publishedDate").getAsString() : "Published Date: N/A");
            bookPublisher.setText(volumeInfo.has("publisher") ? "Publisher: " + volumeInfo.get("publisher").getAsString() : "Publisher: Unknown");

            // Load ảnh bìa nếu có
            if (volumeInfo.has("imageLinks") && volumeInfo.getAsJsonObject("imageLinks").has("thumbnail")) {
                String imageUrl = volumeInfo.getAsJsonObject("imageLinks").get("thumbnail").getAsString();
                bookCover.setImage(new Image(imageUrl, true));
            } else {
                bookCover.setImage(null); // Xóa ảnh nếu không có link
            }
        } else {
            // Nếu không có thông tin, hiển thị thông báo mặc định
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
}
