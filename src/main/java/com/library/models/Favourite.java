package com.library.models;

import java.time.LocalDateTime;

public class Favourite {
    private int favouriteId;
    private int userId;
    private String isbn;

    public Favourite() {

    }
    public Favourite(int favouriteId, int userId, String isbn) {
        this.favouriteId = favouriteId;
        this.userId = userId;
        this.isbn = isbn;
    }

    public Favourite(int userId, String isbn) {
        this.favouriteId = favouriteId;
        this.userId = userId;
        this.isbn = isbn;
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

    public int getFavouriteId() {
        return favouriteId;
    }

    public void setFavouriteId(int favouriteId) {
        this.favouriteId = favouriteId;
    }



}
