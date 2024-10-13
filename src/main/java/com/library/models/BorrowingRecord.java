package com.library.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BorrowingRecord {
    private int recordId;
    private int userId;
    private String isbn;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private String status="borrowed";

    public BorrowingRecord(int recordId, int userId, String isbn, LocalDate localDate, LocalDate localDate1, String status) {
        this.recordId = recordId;
        this.userId = userId;
        this.isbn = isbn;
        borrowDate = localDate;
        returnDate = localDate1;
        this.status = status;
    }

    public int getRecordId() {
        return recordId;
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
    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public LocalDate getBorrowDate() {
        return borrowDate;
    }
    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }
    public LocalDate getReturnDate() {
        return returnDate;
    }
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

}
