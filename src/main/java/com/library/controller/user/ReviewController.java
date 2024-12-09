//package com.library.controller.user;
//
//import com.library.dao.ReviewDao;
//import com.library.models.Review;
//import com.library.models.User;
//import com.library.dao.UserDao;
//import javafx.fxml.FXML;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Alert.AlertType;
//import javafx.scene.control.Button;
//import javafx.scene.control.Slider;
//import javafx.scene.control.TextArea;
//import javafx.scene.control.TextField;
//import javafx.scene.control.ListView;
//import javafx.event.ActionEvent;
//import java.time.LocalDateTime;
//
//public class ReviewController {
//
//    @FXML
//    private TextField isbnField;
//    @FXML
//    private TextArea commentField;
//    @FXML
//    private Slider ratingSlider;
//    @FXML
//    private Button submitReviewButton;
//    @FXML
//    private ListView<Review> reviewListView;  // Thêm ListView để hiển thị review
//
//    private ReviewDao reviewDao = new ReviewDao();
//    private UserDao userDao = new UserDao();
//
//    private String username;
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    @FXML
//    private void handleSubmitReview(ActionEvent event) {
//        String isbn = isbnField.getText().trim();
//        String comment = commentField.getText().trim();
//        double rating = ratingSlider.getValue();
//
//        if (isbn.isEmpty() || comment.isEmpty()) {
//            showAlert("Error", "ISBN and comment cannot be empty.", AlertType.ERROR);
//            return;
//        }
//
//        User currentUser = userDao.findUserByName(username);
//        if (currentUser == null) {
//            showAlert("Error", "User not found.", AlertType.ERROR);
//            return;
//        }
//
//        Review review = new Review(currentUser.getUserId(), isbn, rating, comment, LocalDateTime.now());
//
//        try {
//            reviewDao.add(review);
//            reviewListView.getItems().add(review);
//            showAlert("Success", "Review added successfully!", AlertType.INFORMATION);
//        } catch (Exception e) {
//            showAlert("Error", "Failed to add review. Please try again later.", AlertType.ERROR);
//            e.printStackTrace();
//        }
//
//        isbnField.clear();
//        commentField.clear();
//        ratingSlider.setValue(3);
//    }
//
//    private void showAlert(String title, String message, AlertType type) {
//        Alert alert = new Alert(type);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//}
