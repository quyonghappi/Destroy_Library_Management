package com.library.controller.admin.dashboard;
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
import javafx.scene.image.ImageView;

import java.sql.SQLException;

import static com.library.utils.LoadImage.loadImageLazy;
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
    private Label statusLabel;

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
    private ReservationDao reservationDao = ReservationDao.getInstance();
    private AdminDashboardController parentController;

    public void setParentController(AdminDashboardController parentController) {
        this.parentController = parentController; // Set the parent controller
    }

    public void setListView(ListView<Reservation> lv) {
        this.lv = lv;
    }
    public void loadReservation(Reservation reservation) {
        current = reservation;
        int userId = reservation.getUserId();
        int reservationId = reservation.getReservationId();
        User user = userDao.get(userId);
        if (user != null) {
            useridLabel.setText(String.valueOf(user.getUserId()));
            usernameLabel.setText(user.getFullName());
        }

        Document doc = documentDao.get(reservation.getIsbn());
        if (doc != null) {
            // Set other information
            bookNameLabel.setText(doc.getTitle());
            isbnLabel.setText(reservation.getIsbn());
            requestDateLabel.setText(reservation.getReservationDate().toString());
            requestIdLabel.setText(String.valueOf(reservation.getReservationId()));

            // Load the image using lazy loading
            if (!doc.getImageLink().equals("N/A")) {
                loadImageLazy(doc.getImageLink(), bookImage, 50, 60);
            }
        }
        updateButtonVisibility(reservation.getStatus());
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
            BorrowingRecord borrowingRecord = new BorrowingRecord(useridLabel.getText(), isbnLabel.getText(), now(), "borrowed");
            borrowingRecordDao.add(borrowingRecord);
            current.setStatus("fulfilled");
            reservationDao.updateStatus(current.getReservationId(), "fulfilled");
            updateButtonVisibility("fulfilled");
            lv.refresh();
            if (parentController != null) {
                parentController.sortListView1();
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
