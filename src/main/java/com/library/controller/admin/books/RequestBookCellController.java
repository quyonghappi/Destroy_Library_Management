package com.library.controller.admin.books;

import com.library.dao.BorrowingRecordDao;
import com.library.dao.DocumentDao;
import com.library.dao.ReservationDao;
import com.library.dao.UserDao;
import com.library.models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.time.LocalDateTime;

import static com.library.utils.LoadImage.loadImageLazy;

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
    private Label availableQuantityLabel;

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
    private Document doc = new Document();

    public void setListView(ListView<Reservation> lv) {
        this.lv = lv;
    }

    public void setParentController(RequestBookController parentController) {
        this.parentController = parentController; // Set the parent controller
    }

    public void loadReservation(Reservation reservation) {
        current = reservation;
        int userId = reservation.getUserId();
        int reservationId = reservation.getReservationId();
        User user = userDao.get(userId);
        if (user != null) {
            userIdLabel.setText(String.valueOf(user.getUserId()));
            userLabel.setText(user.getFullName());
        }

        doc = documentDao.get(reservation.getIsbn());
            if (doc != null) {
                bookNameLabel.setText(doc.getTitle());
                isbnLabel.setText(reservation.getIsbn());
                dateLabel.setText(String.valueOf(reservation.getReservationDate()));
                requestIdLabel.setText(String.valueOf(reservation.getReservationId()));
                availableQuantityLabel.setText(String.valueOf(doc.getQuantity()));
                Author author=documentDao.getAuthor(doc.getAuthorId());
                if(author==null) authorLabel.setText("Unknown");
                else authorLabel.setText(author.getName());

                if (!doc.getImageLink().equals("N/A")) {
                    loadImageLazy(doc.getImageLink(), bookImage, bookImage.getFitWidth(), bookImage.getFitHeight());
                } else {
                    bookImage.setImage(new Image("/ui/admindashboard/bookcover.png")); // Hình ảnh mặc định khi không có bìa
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
        if (doc.getQuantity() > 0) {
            try {
                BorrowingRecordDao borrowingRecordDao = new BorrowingRecordDao();
                BorrowingRecord borrowingRecord = new BorrowingRecord(
                        userIdLabel.getText(),
                        isbnLabel.getText(),
                        LocalDateTime.now(),
                        "borrowed"
                );
                borrowingRecordDao.add(borrowingRecord);

                current.setStatus("fulfilled");
                reservationDao.updateStatus(current.getReservationId(), "fulfilled");

                updateButtonVisibility("fulfilled");
                lv.refresh();

                if (parentController != null) {
                    parentController.sortListView();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            String isbn = isbnLabel.getText();
            int quantity = doc.getQuantity();
            Alert warningAlert = new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Book Unavailable");
            warningAlert.setHeaderText("This book is currently unavailable.");
            warningAlert.setContentText("Details:\n\n" +
                    "ISBN: " + isbn + "\n" +
                    "Quantity: " + quantity);
            warningAlert.showAndWait();
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
