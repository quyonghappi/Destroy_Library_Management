package com.library.models;

public class Author {
    private int authorId;
    private String name;


    public Author() {}
    public int getAuthorId() {
        return authorId;
    }
    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Author [authorId=" + authorId + ", name=" + name + "]";
    }
}
