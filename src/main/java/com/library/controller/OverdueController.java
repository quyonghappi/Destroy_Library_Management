package com.library.controller;

import com.library.dao.BorrowingRecordDao;
import com.library.dao.DocumentDao;
import com.library.dao.FineDao;
import com.library.dao.UserDao;
import com.library.models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.sql.SQLException;

import static java.time.LocalDateTime.now;

public class OverdueController {

    @FXML
    private ImageView bookImage;

    @FXML
    private Label bookNameLabel;

    @FXML
    private Label fineLabel;

    @FXML
    private Label recordIdLabel;

    @FXML
    private Label overdueLabel;

    @FXML
    private Label useridLabel;

    @FXML
    private Label usernameLabel;

    private DocumentDao documentDao = new DocumentDao();
    private UserDao userDao=new UserDao();
    private FineDao fineDao=new FineDao();
    private BorrowingRecordDao borrowingRecordDao=new BorrowingRecordDao();

    private ListView<Fine> lv;
    private Fine currentFine;

    public void setListView(ListView<Fine> lv) {
        this.lv = lv;
    }

    public void loadOverdueData(Fine fine) {
        try {
            int userId = fine.getUserId();

            int recordId = fine.getRecordId();

            User user = userDao.getUserById(userId);
            if (user != null) {
                useridLabel.setText(String.valueOf(user.getUserId()));
                usernameLabel.setText(user.getFullName());
            }
//            } else {
//                useridLabel.setText("N/A");
//                usernameLabel.setText("Unknown User");
//            }

            //load borrowing record
            BorrowingRecord borrowingRecord = borrowingRecordDao.getById(recordId);
            if (borrowingRecord != null) {
                Document document = documentDao.findByISBN(borrowingRecord.getIsbn());
                if (document != null) {
                    bookNameLabel.setText(document.getTitle());
                    fineLabel.setText(String.valueOf(fine.getFineAmount())); // Use fine's fine amount
                    recordIdLabel.setText("#" + String.valueOf(recordId));
                    overdueLabel.setText(String.valueOf(fineDao.daysOverdue(borrowingRecord))+ " days");

                    if (document.getImageLink() != null) {
                        if (!document.getImageLink().equals("N/A")) {
                            Image image = new Image(document.getImageLink());
                            bookImage.setImage(image);
                        }
                    }
                } else {
                    bookNameLabel.setText("N/A");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePaidButtonAction(ActionEvent event) {
        try {
            fineDao.deleteFine(fineDao.getFineDetailsWithRecordId(Integer.parseInt(recordIdLabel.getText())));
//            BorrowingRecord borrowingRecord = borrowingRecordDao.getById(currentFine.getRecordId());
            fineDao.changeFineStatus(Integer.parseInt(recordIdLabel.getText()));

            //remove current request from listview
            lv.getItems().remove(currentFine);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
