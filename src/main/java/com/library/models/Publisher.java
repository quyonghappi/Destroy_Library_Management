package com.library.models;

public class Publisher {
    private String publisherName;
    private int publisherId;

    public void setName(String name) {
        this.publisherName = name;

    }

    public String getName() {
        return this.publisherName;
    }

    public void setPublisherId(int id) {
        this.publisherId = id;
    }

    public int getPublisherId() {
        return this.publisherId;
    }
}
