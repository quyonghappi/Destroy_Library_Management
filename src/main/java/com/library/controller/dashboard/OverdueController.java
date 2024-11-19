package com.library.controller.dashboard;

import com.library.dao.BorrowingRecordDao;
import com.library.dao.DocumentDao;
import com.library.dao.FineDao;
import com.library.dao.UserDao;
import com.library.models.BorrowingRecord;
import com.library.models.Document;
import com.library.models.Fine;
import com.library.models.User;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

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
    private static final Map<String, Image> imageCache = new HashMap<>();

    private ListView<Fine> lv;
    private Fine currentFine;

    public void setListView(ListView<Fine> lv) {
        this.lv = lv;
    }

    public void loadOverdueData(Fine fine) {
        try {
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
                    fineLabel.setText(String.valueOf(fine.getFineAmount())); //use fine's fine amount
                    recordIdLabel.setText(String.valueOf(recordId));
                    overdueLabel.setText(String.valueOf(fineDao.daysOverdue(borrowingRecord))+ " days");

                    if (!document.getImageLink().equals("N/A")) {
                        loadImageLazy(document.getImageLink(), bookImage);
                    }
                } else {
                    bookNameLabel.setText("N/A");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadImageLazy(String imageUrl, ImageView imageView) {
        //check if image is already cached
        if (imageCache.containsKey(imageUrl)) {
            imageView.setImage(imageCache.get(imageUrl));
            return;
        }
        Task<Image> loadImageTask = new Task<>() {
            @Override
            protected Image call() throws Exception {
                return new Image(imageUrl, 45, 55, true, true);
            }
        };

        loadImageTask.setOnSucceeded(event -> {
            Image image = loadImageTask.getValue();
            imageCache.put(imageUrl, image);  //cache the image
            Platform.runLater(() -> imageView.setImage(image));
        });

        loadImageTask.setOnFailed(event -> {
            System.out.println("Failed to load image from URL: " + imageUrl);

        });

        new Thread(loadImageTask).start();  //run the task on a background thread
    }

    @FXML
    private void handlePaidButtonAction(ActionEvent event) {
        try {
            fineDao.delete(fineDao.get(Integer.parseInt(recordIdLabel.getText())));
            fineDao.changeFineStatus(Integer.parseInt(recordIdLabel.getText()));

            //remove current request from listview
            lv.getItems().remove(currentFine);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
