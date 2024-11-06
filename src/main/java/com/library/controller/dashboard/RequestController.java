package com.library.controller.dashboard;
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
    //private static final Map<String, Image> imageCache = new HashMap<>();

    public void setListView(ListView<Reservation> lv) {
        this.lv = lv;
    }
    public void loadReservation(Reservation reservation) {
        try {
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
                    loadImageLazy(doc.getImageLink(), bookImage);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleApproveButtonAction(ActionEvent event) {
        try {
            //reservationDao.delete(reservationDao.getById(Integer.parseInt(requestIdLabel.getText())));
            BorrowingRecordDao borrowingRecordDao=new BorrowingRecordDao();
            //System.out.println(isbnLabel.getText());
            BorrowingRecord borrowingRecord=new BorrowingRecord(useridLabel.getText(), isbnLabel.getText(),now(),"borrowed");
            borrowingRecordDao.add(borrowingRecord);

            current.setStatus("Fulfilled");
            lv.refresh();

            sortListView();
//            approveButton.setVisible(false);
//            denyButton.setVisible(false);
//            statusLabel.setText("Fulfilled");
//            statusLabel.setVisible(true);
            //remove current request from listview
            //lv.getItems().remove(current);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    private void handleDenyButtonAction(ActionEvent event) {
        try {
            //reservationDao.delete(reservationDao.getById(Integer.parseInt(requestIdLabel.getText())));
//            approveButton.setVisible(false);
//            denyButton.setVisible(false);
//            statusLabel.setText("Canceled");
//            statusLabel.setVisible(true);
            //lv.getItems().remove(current);
            current.setStatus("Cancelled");
            lv.refresh();
            sortListView();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void sortListView() {
        lv.getItems().sort((r1, r2) -> {
            //if r1 pending and r2 != pending return -1 then r1 will appear first
            if ("active".equals(r1.getStatus()) && !"Pending".equals(r2.getStatus())) {
                return -1;
            }
            else if(!"active".equals(r1.getStatus()) && "Pending".equals(r2.getStatus())) {
                return 1;
            } else return 0;
        });
    }

}
