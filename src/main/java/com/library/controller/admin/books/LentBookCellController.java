package com.library.controller.admin.books;

import com.library.dao.BorrowingRecordDao;
import com.library.dao.DocumentDao;
import com.library.dao.UserDao;
import com.library.models.Author;
import com.library.models.BorrowingRecord;
import com.library.models.Document;
import com.library.models.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import static com.library.utils.LoadImage.loadImageLazy;

public class LentBookCellController implements Initializable {
    @FXML
    private Label authorLabel;

    @FXML
    private ImageView bookImage;

    @FXML
    private Label bookNameLabel;

    @FXML
    private Label brdateLabel;

    @FXML
    private Label duedateLabel;

    @FXML
    private Label isbnLabel;

    @FXML
    private ImageView lostImage;

    @FXML
    private ImageView returnImage;

    @FXML
    private Label statusLabel;

    @FXML
    private Label userIdLabel;

    @FXML
    private Label userLabel;

    private ListView<BorrowingRecord> listView;
    private BorrowingRecord currentBr;

    private BorrowingRecordDao borrowingRecordDao = new BorrowingRecordDao();
    private DocumentDao documentDao = new DocumentDao();
    private UserDao userDao = new UserDao();

    public void setListView(ListView<BorrowingRecord> listView) {
        this.listView = listView;
    }

    public void loadLentBook(BorrowingRecord br) throws SQLException {
        if (br!=null) {
            currentBr = br;
            Document doc=documentDao.get(br.getISBN());
            Author author=documentDao.getAuthor(doc.getAuthorId());
            authorLabel.setText(author.getName());
            isbnLabel.setText(doc.getISBN());
            bookNameLabel.setText(doc.getTitle());
            statusLabel.setText(br.getStatus());
            User user= userDao.get(br.getUserId());
            userIdLabel.setText(String.valueOf(user.getUserId()));
            userLabel.setText(user.getFullName());

            brdateLabel.setText(String.valueOf(br.getBorrowDate()));
            duedateLabel.setText(String.valueOf(br.getBorrowDate().plusDays(14)));

            if (!doc.getImageLink().equals("N/A")) {
                loadImageLazy(doc.getImageLink(), bookImage, bookImage.getFitWidth(), bookImage.getFitHeight());
            } else {
                bookImage.setImage(new Image("/ui/admindashboard/bookcover.png")); // Hình ảnh mặc định khi không có bìa
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lostImage.setOnMouseClicked(mouseEvent -> handleLostAction());
        returnImage.setOnMouseClicked(mouseEvent -> handleReturnAction());
    }

    private void handleLostAction() {
        try {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirm Borrowing Record Information");
            confirmationAlert.setHeaderText("Confirm Book Details");
            confirmationAlert.setContentText("This book copy was lost, wasn't it ? \n\n" +
                    "Title: " + bookNameLabel.getText() + "\n" +
                    "ISBN: " + isbnLabel.getText());
            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    currentBr.setStatus("lost");
                    borrowingRecordDao.update(currentBr);
                    //khong can add fine dao vi
                    //fine list se tu dong cap nhat khi minh call fine dao getAll
                    listView.getItems().remove(currentBr);
                }
            });
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void handleReturnAction() {
        try {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirm Delete Information");
            confirmationAlert.setHeaderText("Confirm Book Details");
            confirmationAlert.setContentText("This book was return by user " + userLabel.getText()+ ", wasn't it ? \n\n" +
                    "Title: " + bookNameLabel.getText() + "\n" +
                    "ISBN: " + isbnLabel.getText());
            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    //update status and return date
                    currentBr.setStatus("returned");
                    currentBr.setReturnDate(LocalDateTime.now());
                    borrowingRecordDao.update(currentBr);
                    listView.getItems().remove(currentBr);
                }
            });
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
