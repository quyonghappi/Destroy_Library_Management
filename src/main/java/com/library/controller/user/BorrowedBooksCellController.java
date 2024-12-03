package com.library.controller.user;

import com.library.dao.DocumentDao;
import com.library.models.Author;
import com.library.models.BorrowingRecord;
import com.library.models.Category;
import com.library.models.Document;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;

import static com.library.utils.LoadImage.loadImageLazy;

public class BorrowedBooksCellController {

    @FXML
    private Label authorLabel;

    @FXML
    private Label pageLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private ImageView bookImage;

    @FXML
    private Label bookNameLabel;

    @FXML
    private Label brdateLabel;

    @FXML
    private Label categoryLabel;

    @FXML
    private Label duedateLabel;

    @FXML
    private Label isbnLabel;

    private ListView<BorrowingRecord> listView;
    private BorrowingRecord borrowedBook;
    private DocumentDao documentDao=new DocumentDao();

    public void setListView(ListView<BorrowingRecord> lv){
        this.listView=lv;
    }

    public void loadBorrowedBooks(BorrowingRecord br) throws SQLException {
        if (br != null) {
            borrowedBook = br;
            Document doc = documentDao.get(br.getISBN());
            Author author = documentDao.getAuthor(doc.getAuthorId());
            authorLabel.setText(author.getName());
            isbnLabel.setText(doc.getISBN());
            bookNameLabel.setText(doc.getTitle());
            Category category = documentDao.getCategory(doc.getCategoryId());
            categoryLabel.setText(category.getName());
            pageLabel.setText(String.valueOf(doc.getPage()));

            brdateLabel.setText(String.valueOf(br.getBorrowDate()));
            duedateLabel.setText(String.valueOf(br.getBorrowDate().plusDays(14)));
            //statusLabel.setText(br.getStatus());

            //Logic cho phat vi muon sach
            LocalDateTime bayh = LocalDateTime.now();
            LocalDateTime borrowDate = br.getBorrowDate();
            if(borrowDate.plusDays(30).isBefore(bayh)){
                statusLabel.setText("Lost");
            } else if (borrowDate.plusDays(14).isBefore(bayh)) {
                long daysLate = Duration.between(borrowDate.plusDays(14), bayh).toDays();
                statusLabel.setText("Late: " + daysLate + " ngày");
            } else {
                statusLabel.setText(br.getStatus());
            }

            if (!doc.getImageLink().equals("N/A")) {
                loadImageLazy(doc.getImageLink(), bookImage, bookImage.getFitWidth(), bookImage.getFitHeight());
            }
        }
    }
}
