package com.library.models;

public class Document {
    private String ISBN;
    private String title;
    private int categoryId;
    private int authorId;
    private int publisherId;
    private int publicationYear;
    //    private int totalCopies;
//    private int availableCopies;
    private int quantity = 1;
    private int page=1;
    private String description;
    private String location = "com/library";
    private String previewLink;
    private String imageLink;

    private Author author;
    private Publisher publisher;
    private Category category;

    public Document() {

    }
    //constructor w all field
    public Document(String ISBN, String title, int categoryId, int authorId, int publisherId,
                    int publicationYear, int quantity,
                    String description, String location) {
        this.ISBN = ISBN;
        this.title = title;
        this.categoryId = categoryId;
        this.authorId = authorId;
        this.publisherId = publisherId;
        this.publicationYear = publicationYear;
        this.quantity = quantity;
//        this.totalCopies = totalCopies;
//        this.availableCopies = availableCopies;
        this.description = description;
        this.location = location;



    }

    public String getISBN() {
        return ISBN;
    }
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public int getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    public int getAuthorId() {
        return authorId;
    }
    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }
    public int getAuthorName() {
        return authorId;
    }
    public void setAuthorName(int authorId) {
        this.authorId = authorId;
    }
    public int getPublisherId() {
        return publisherId;
    }
    public void setPublisherId(int publisherId) {
        this.publisherId = publisherId;
    }
    public int getPublicationYear() {
        return publicationYear;
    }
    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public void setPreviewLink(String previewLink) {
        this.previewLink = previewLink;
    }

    public String getPreviewLink() {
        return previewLink;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    @Override
    public String toString() {
        return "ISBN: " + ISBN + ", title: " + title + ", category " + categoryId;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
