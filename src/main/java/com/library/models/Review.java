package com.library.models;

import java.time.LocalDateTime;

public class Review {
    private int reviewId;
    private int userId;
    private String isbn;
    private double rating;
    private String comment;
    private LocalDateTime reviewDate;

    public Review() {

    }

    public Review(int reviewId, int userId, String isbn, double rating, String comment, LocalDateTime reviewDate) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.isbn = isbn;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
    }

    public Review(int userId, String isbn, double rating, String comment, LocalDateTime reviewDate) {
        this.userId = userId;
        this.isbn = isbn;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
    }


    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDateTime reviewDate) {
        this.reviewDate = reviewDate;
    }

}
