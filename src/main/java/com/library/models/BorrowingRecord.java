package com.library.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.lang.Integer.parseInt;

public class BorrowingRecord {
    private int recordId;
    private int userId;
    private String isbn;
    private LocalDateTime borrowDate;
    private LocalDateTime returnDate;
    private String status="borrowed";

    public BorrowingRecord() {}

    public BorrowingRecord(int recordId, int userId, String isbn, LocalDateTime localDate, LocalDateTime localDate1, String status) {
        this.recordId = recordId;
        this.userId = userId;
        this.isbn = isbn;
        borrowDate = localDate;
        returnDate = localDate1;
        this.status = status;
    }

    public BorrowingRecord(String userId, String isbn, LocalDateTime borrowDate, String status) {
        this.userId = parseInt(userId);
        this.isbn = isbn;
        this.borrowDate = borrowDate;
        this.status = status;
    }

    public int getRecordId() {
        return recordId;
    }

    public String getISBN() {
        return isbn;
    }

    public void setISBN(String isbn) {
        this.isbn = isbn;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDateTime borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
