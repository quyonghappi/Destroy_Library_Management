package com.library.controller.admin.books;

import com.library.dao.DocumentDao;
import com.library.dao.UserDao;
import com.library.models.Author;
import com.library.models.BorrowingRecord;
import com.library.models.Document;
import com.library.models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;

import java.sql.SQLException;

import static com.library.utils.LoadImage.loadImageLazy;

public class ReturnBookCellController {

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
    private Label returndateLabel;

    @FXML
    private Label userIdLabel;

    @FXML
    private Label userLabel;

    private ListView<BorrowingRecord> listView;
    private BorrowingRecord currentBr;
    private DocumentDao documentDao = new DocumentDao();
    private UserDao userDao = new UserDao();

    public void setListView(ListView<BorrowingRecord> listView) {
        this.listView = listView;
    }

    public void loadReturnedBook(BorrowingRecord br) throws SQLException {
        if (br != null) {
            currentBr = br;
            Document doc = documentDao.get(br.getISBN());
            Author author = documentDao.getAuthor(doc.getAuthorId());
            authorLabel.setText(author.getName());
            isbnLabel.setText(doc.getISBN());
            bookNameLabel.setText(doc.getTitle());

            User user = userDao.get(br.getUserId());
            userIdLabel.setText(String.valueOf(user.getUserId()));
            userLabel.setText(user.getFullName());

            brdateLabel.setText(String.valueOf(br.getBorrowDate()));
            duedateLabel.setText(String.valueOf(br.getBorrowDate().plusDays(14)));
            returndateLabel.setText(String.valueOf(br.getReturnDate()));

            if (!doc.getImageLink().equals("N/A")) {
                loadImageLazy(doc.getImageLink(), bookImage, bookImage.getFitWidth(), bookImage.getFitHeight());
            }
        }
    }
}
