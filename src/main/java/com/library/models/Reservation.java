package com.library.models;

import java.time.LocalDateTime;

public class Reservation {
    private int reservationId;
    private int userId;
    private String isbn;
    private LocalDateTime reservationDate;
    private String status="active";

    public Reservation() {

    }
    public Reservation(int userId, String isbn, LocalDateTime date, String status) {
        this.userId = userId;
        this.isbn = isbn;
        this.reservationDate = date;
        this.status = status;
    }

    public Reservation(int reservationId, int userId, String isbn, LocalDateTime date, String status) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.isbn = isbn;
        this.reservationDate = date;
        this.status = status;
    }


    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
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

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDateTime reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String toString() {
        return reservationId + " " + userId + " " + isbn + " " + reservationDate + " " + status;
    }

}
