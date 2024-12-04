package com.library.controller.admin.dashboard;

import com.library.controller.admin.books.OverdueBookController;
import com.library.controller.admin.books.RequestBookController;
import com.library.dao.BorrowingRecordDao;
import com.library.dao.DocumentDao;
import com.library.dao.FineDao;
import com.library.dao.UserDao;
import com.library.models.BorrowingRecord;
import com.library.models.Document;
import com.library.models.Fine;
import com.library.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.text.DecimalFormat;

import static com.library.utils.LoadImage.loadImageLazy;

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

    @FXML
    private Button paidButton;

    @FXML
    private Label statusLabel;

    private DocumentDao documentDao = new DocumentDao();
    private UserDao userDao=new UserDao();
    private FineDao fineDao=new FineDao();
    private BorrowingRecordDao borrowingRecordDao=new BorrowingRecordDao();
    private OverdueBookController parentController;
    private ListView<Fine> lv;
    private Fine currentFine;

    public void setListView(ListView<Fine> lv) {
        this.lv = lv;
    }

    public void setParentController(OverdueBookController parentController) {
        this.parentController = parentController; // Set the parent controller
    }

    public void loadOverdueData(Fine fine) {
        currentFine=fine;
        int userId = fine.getUserId();
        int recordId = fine.getRecordId();


        User user = userDao.get(userId);
        if (user != null) {
            useridLabel.setText(String.valueOf(user.getUserId()));
            usernameLabel.setText(user.getFullName());
        }
//            } else {
//                useridLabel.setText("N/A");
//                usernameLabel.setText("Unknown User");
//            }

            //load borrowing record
        BorrowingRecord borrowingRecord = borrowingRecordDao.get(recordId);
        if (borrowingRecord != null) {
            Document document = documentDao.get(borrowingRecord.getISBN());
            if (document != null) {
                bookNameLabel.setText(document.getTitle());
                DecimalFormat decimalFormat = new DecimalFormat("#,###");
                fineLabel.setText(decimalFormat.format(fine.getFineAmount()));
                    //fineLabel.setText(String.valueOf(fine.getFineAmount())); //use fine's fine amount
                recordIdLabel.setText(String.valueOf(recordId));
                overdueLabel.setText(fineDao.daysOverdue(borrowingRecord) + " days");
                if (!document.getImageLink().equals("N/A")) {
                    loadImageLazy(document.getImageLink(), bookImage, 50, 60);
                }
            } else {
                bookNameLabel.setText("N/A");
            }
        }
        updateButtonVisibility(fine.getStatus());
    }

    private void updateButtonVisibility(String status) {
        if(status.equals("PAID")) {
            statusLabel.setText("PAID");
            statusLabel.setVisible(true);
            paidButton.setVisible(false);
        } else {
            statusLabel.setVisible(false);
            paidButton.setVisible(true);
        }
    }

    @FXML
    private void handlePaidButtonAction() {
        try {
            //fineDao.delete(fineDao.get(Integer.parseInt(recordIdLabel.getText())));
            fineDao.changeFineStatus(Integer.parseInt(recordIdLabel.getText()));
            currentFine.setStatus("PAID");
            updateButtonVisibility("PAID");
            lv.refresh();
            if (parentController != null) {
                parentController.sortListView();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
