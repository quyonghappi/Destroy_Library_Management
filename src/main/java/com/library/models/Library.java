package com.library.models;

import javax.print.Doc;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Library {
    private List<Document> documents;
    private List<User> users;
    private List<BorrowingRecord> borrowingRecords;

    public Library() {
        documents = new ArrayList<>();
        users = new ArrayList<>();
        borrowingRecords = new ArrayList<>();
    }

    // getter and setter
    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public List<User> getUsersReads() {
        return users;
    }

    public void setUsersReads(List<User> usersReads) {
        this.users = usersReads;
    }

    public void borrowDocument(int userId, String documentTitle) {
        User borrowedUser = null;
        for (User user : users) {
            if (user.getUserId() == userId) {
                borrowedUser = user;
                break;
            }
        }
        if (borrowedUser == null) {
            System.out.println("User not found");
            return;
        }

        Document documentToBorrow = null;
        for(Document document : documents) {
            if(document.getTitle().equals(documentTitle)) {
                documentToBorrow = document;
                break;
            }
        }

        if(documentToBorrow == null) {
            System.out.println("Document not found");
            return;
        }

        if(documentToBorrow.getQuantity()<=0) {
            System.out.println("Document is not available for borrowing");
            return;
        }

        documentToBorrow.setQuantity(documentToBorrow.getQuantity()-1);
        borrowingRecords.add(new BorrowingRecord(1,  borrowedUser.getUserId(), documentToBorrow.getISBN(), LocalDateTime.now(), null, "borrowed"));
        System.out.println(userId + " borrowed " + documentTitle + " successfully!");
    }

    public void returnDocument(int userReadId, String ISBN) {
        User borrowedUser = null;
        for(User userRead : users) {
            if(userRead.getUserId() == userReadId) {
                borrowedUser = userRead;
                break;
            }
        }
        if(borrowedUser == null) {
            System.out.println("User not found");
            return;
        }

        BorrowingRecord documentToReturn = null;
        for(BorrowingRecord borrowingRecord : borrowingRecords) {
            if(borrowingRecord.getIsbn().equals(ISBN)) {
                documentToReturn = borrowingRecord;
                borrowingRecords.remove(borrowingRecord);
                for(Document document : documents) {
                    if (document.getISBN().equals(documentToReturn.getIsbn())) {
                        document.setQuantity(document.getQuantity()-1);
                    }
                }
                System.out.println(userReadId + " returned " + ISBN + " successfully!");
                return;
            }
        }
        System.out.println("Document not found!");
    }

    public void updateDocument(String documentIsbn, int newQuantity) {
        for (Document document : documents) {
            if (document.getISBN().equals(documentIsbn)) {
                document.setQuantity(newQuantity);
                System.out.println("Document " + documentIsbn + " updated successfully!");
                return;
            }
        }
        System.out.println("Document not found");
    }

    public void findDocument(String documentIsbn) {
        for (Document document : documents) {
            if (document.getISBN().equals(documentIsbn)) {
                System.out.println("Document " + documentIsbn + " found successfully!");
                return;
            }
        }
        System.out.println("Document not found");
    }

    public void displayDocuments() {
        if (documents.isEmpty()) {
            System.out.println("No documents available.");
            return;
        }

        for (Document document : documents) {
            System.out.print(document.toString());
        }
    }

    public void displayUserReads() {
        if (users.isEmpty()) {
            System.out.println("No users available.");
            return;
        }
        for (User user : users) {
            System.out.println(user.toString());
        }
    }

    public void addDocument(Document document) {
        documents.add(document);
        System.out.println("Document added successfully.");
    }

    public void removeDocument(String documentIsbn) {
        for (Document document : documents) {
            if (document.getISBN().equals(documentIsbn)) {
                documents.remove(document);
                System.out.println("Document removed successfully.");
                return;
            }
        }
        System.out.println("Document not found");
    }

    public void addUserRead(User userRead) {
        users.add(userRead);
        System.out.println("User added successfully.");
    }

}
