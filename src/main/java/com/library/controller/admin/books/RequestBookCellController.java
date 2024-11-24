package com.library.controller.admin.books;

import com.library.dao.BorrowingRecordDao;
import com.library.dao.DocumentDao;
import com.library.dao.ReservationDao;
import com.library.dao.UserDao;
import com.library.models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;

import java.sql.SQLException;

import static com.library.utils.LoadImage.loadImageLazy;
import static java.time.LocalDateTime.now;

public class RequestBookCellController {
    @FXML
    private Button approveButton;

    @FXML
    private Label authorLabel;

    @FXML
    private ImageView bookImage;

    @FXML
    private Label bookNameLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Button denyButton;

    @FXML
    private Label isbnLabel;

    @FXML
    private Label requestIdLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Label userIdLabel;

    @FXML
    private Label userLabel;

    private ListView<Reservation> lv;
    private Reservation current;
    private RequestBookController parentController;

    private DocumentDao documentDao = new DocumentDao();
    private UserDao userDao = new UserDao();
    private ReservationDao reservationDao = new ReservationDao();

    public void setListView(ListView<Reservation> lv) {
        this.lv = lv;
    }

    public void setParentController(RequestBookController parentController) {
        this.parentController = parentController; // Set the parent controller
    }

    public void loadReservation(Reservation reservation) {
        try {
            current = reservation;
            int userId = reservation.getUserId();
            int reservationId = reservation.getReservationId();
            User user = userDao.get(userId);
            if (user != null) {
                userIdLabel.setText(String.valueOf(user.getUserId()));
                userLabel.setText(user.getFullName());
            }

            Document doc = documentDao.get(reservation.getIsbn());
            if (doc != null) {
                bookNameLabel.setText(doc.getTitle());
                isbnLabel.setText(reservation.getIsbn());
                dateLabel.setText(String.valueOf(reservation.getReservationDate()));
                requestIdLabel.setText(String.valueOf(reservation.getReservationId()));
                Author author=documentDao.getAuthor(doc.getAuthorId());
                if(author==null) authorLabel.setText("Unknown");
                else authorLabel.setText(author.getName());

                if (!doc.getImageLink().equals("N/A")) {
                    loadImageLazy(doc.getImageLink(), bookImage, bookImage.getFitWidth(), bookImage.getFitHeight());
                }
            }

            updateButtonVisibility(reservation.getStatus());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateButtonVisibility(String status) {
        switch (status) {
            case "active":
                approveButton.setVisible(true);
                denyButton.setVisible(true);
                statusLabel.setVisible(false);
                statusLabel.setPrefSize(0,7);
                break;
            case "fulfilled":
                approveButton.setVisible(false);
                denyButton.setVisible(false);
                statusLabel.setText("Fulfilled");
                statusLabel.setPrefSize(110,30);
                statusLabel.setVisible(true);
                break;
            case "cancelled":
                approveButton.setVisible(false);
                denyButton.setVisible(false);
                statusLabel.setText("Cancelled");
                statusLabel.setPrefSize(110,30);
                statusLabel.setVisible(true);
                break;
        }
    }
    @FXML
    private void handleApproveButtonAction(ActionEvent event) {
        try {
            BorrowingRecordDao borrowingRecordDao = new BorrowingRecordDao();
            BorrowingRecord borrowingRecord = new BorrowingRecord(userIdLabel.getText(), isbnLabel.getText(), now(), "borrowed");
            borrowingRecordDao.add(borrowingRecord);
            current.setStatus("fulfilled");
            reservationDao.updateStatus(current.getReservationId(), "fulfilled");
            updateButtonVisibility("fulfilled");
            lv.refresh();
            if (parentController != null) {
                parentController.sortListView();
            }
        } catch (Exception e) {
            e.printStackTrace(); //debug
        }
    }

    @FXML
    private void handleDenyButtonAction(ActionEvent event) {
        try {
            //reservationDao.delete(reservationDao.getById(Integer.parseInt(requestIdLabel.getText())));
            //lv.getItems().remove(current);
            current.setStatus("cancelled");
            System.out.println(current.getReservationId());
            reservationDao.updateStatus(current.getReservationId(), "cancelled");
            updateButtonVisibility("cancelled");
           // lv.getItems().remove(current);
            lv.refresh();
            if (parentController != null) {
                parentController.sortListView();
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
