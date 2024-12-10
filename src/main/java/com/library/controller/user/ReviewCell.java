package com.library.controller.user;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.ListCell;
import com.library.models.Review;
import com.library.dao.UserDao;

public class ReviewCell extends ListCell<Review> {

    private UserDao userDao = new UserDao();
    private String username;
    //private ReviewController reviewController;

    @Override
    protected void updateItem(Review review, boolean empty) {
        super.updateItem(review, empty);

        setText(null);
        setGraphic(null);

        if (review != null && !empty) {
            VBox reviewBox = new VBox();
            reviewBox.setSpacing(5);
            reviewBox.setStyle("-fx-padding: 10px; -fx-background-color: #f0f0f0; -fx-border-color: #ddd;");
            Label usernameLabel = new Label(username);
            usernameLabel.setStyle("-fx-font-weight: bold;");

            //Label ratingLabel = new Label("Rating: " + Math.round((review.getRating()*100)/100));
            double rating = review.getRating();
            rating = Math.round(rating * 2.0) / 2.0;  // Round-> 0.5
            Label ratingLabel = new Label("Rating: " + rating);

            Label commentLabel = new Label(review.getComment());

            reviewBox.getChildren().addAll(usernameLabel, ratingLabel, commentLabel);

            setGraphic(reviewBox);
        }
    }


    private String getUsernameByReview(Review review) {
        String username = "Unknown User";

        if (review != null) {
            try {
                // what does this mean
                username = userDao.findUserByName(review.getIsbn()).getUsername();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return username;
    }

    public void setUsername(String username) {
       this.username = username;
    }
}
