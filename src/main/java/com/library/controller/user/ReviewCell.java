package com.library.controller.user;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.ListCell;
import com.library.models.Review;
import com.library.dao.UserDao;

public class ReviewCell extends ListCell<Review> {

    private UserDao userDao = new UserDao();

    @Override
    protected void updateItem(Review review, boolean empty) {
        super.updateItem(review, empty);

        setText(null);
        setGraphic(null);

        if (review != null && !empty) {
            VBox reviewBox = new VBox();
            reviewBox.setSpacing(5);
            reviewBox.setStyle("-fx-padding: 10px; -fx-background-color: #f0f0f0; -fx-border-color: #ddd;");

            String username = getUsernameByReview(review);

            Label usernameLabel = new Label(username);
            usernameLabel.setStyle("-fx-font-weight: bold;");

            Label ratingLabel = new Label("Rating: " + review.getRating());
            Label commentLabel = new Label("Comment: " + review.getComment());

            reviewBox.getChildren().addAll(usernameLabel, ratingLabel, commentLabel);

            setGraphic(reviewBox);
        }
    }


    private String getUsernameByReview(Review review) {
        String username = "Unknown User";

        if (review != null) {
            try {
                username = userDao.findUserByName(review.getIsbn()).getUsername();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return username;
    }
}
