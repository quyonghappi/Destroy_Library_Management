package com.library.controller;

import com.library.dao.BorrowingRecordDao;
import com.library.dao.DocumentDao;
import com.library.dao.ReservationDao;
import com.library.dao.UserDao;
import com.library.models.BorrowingRecord;
import com.library.models.Document;
import com.library.models.Reservation;
import com.library.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.sql.SQLException;

import static java.time.LocalDateTime.now;

public class RequestController {

    @FXML
    private Button approveButton;

    @FXML
    private ImageView bookImage;

    @FXML
    private Label bookNameLabel;

    @FXML
    private Button denyButton;

    @FXML
    private Label isbnLabel;

    @FXML
    private Label requestDateLabel;

    @FXML
    private Label requestIdLabel;

    @FXML
    private Label useridLabel;

    @FXML
    private Label usernameLabel;

    private ListView<Reservation> lv;
    private Reservation current;

    private DocumentDao documentDao = new DocumentDao();
    private UserDao userDao = new UserDao();
    private ReservationDao reservationDao = new ReservationDao();

    public void setListView(ListView<Reservation> lv) {
        this.lv = lv;
    }
    public void loadReservation(Reservation reservation) {
        try {
            current=reservation;
            int userId=reservation.getUserId();
            int reservationId=reservation.getReservationId();
            User user=userDao.getUserById(userId);
            if (user!=null) {
                useridLabel.setText(String.valueOf(user.getUserId()));
                usernameLabel.setText(user.getFullName());
            }

            //Reservation newReservation=reservationDao.getById(reservationId);
            Document doc = documentDao.findByISBN(reservation.getIsbn());
            if (doc!=null) {
                if (!doc.getImageLink().equals("N/A")) {
                    Image image = new Image(doc.getImageLink());
                    bookImage.setImage(image);
                }
                bookNameLabel.setText(doc.getTitle());
                isbnLabel.setText(reservation.getIsbn());
                requestDateLabel.setText(reservation.getReservationDate().toString());
                requestIdLabel.setText(String.valueOf(reservation.getReservationId()));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    private void handleApproveButtonAction(ActionEvent event) {
        try {
            reservationDao.delete(reservationDao.getById(Integer.parseInt(requestIdLabel.getText())));
            BorrowingRecordDao borrowingRecordDao=new BorrowingRecordDao();
            BorrowingRecord borrowingRecord=new BorrowingRecord(useridLabel.getText(), isbnLabel.getText(),now(),"borrowed");
            borrowingRecordDao.add(borrowingRecord);
            //remove current request from listview
            lv.getItems().remove(current);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    private void handleDenyButtonAction(ActionEvent event) {
        try {
            reservationDao.delete(reservationDao.getById(Integer.parseInt(requestIdLabel.getText())));
            lv.getItems().remove(current);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
