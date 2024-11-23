package com.library.UserDashboard.model;

public class Book {

    private String title;
    private String authors;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String description;

    public Book(String title, String authors, String description) {
        this.title = title;
        this.authors = authors;
        this.description = description;
    }

    // Getters
    public String getTitle() { return title; }
    public String getAuthors() { return authors; }
    public String getDescription() { return description; }
}