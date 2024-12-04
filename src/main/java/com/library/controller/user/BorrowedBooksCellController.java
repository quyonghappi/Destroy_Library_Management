package com.library.controller.user;

import com.library.dao.DocumentDao;
import com.library.dao.FineDao;
import com.library.models.Author;
import com.library.models.BorrowingRecord;
import com.library.models.Category;
import com.library.models.Document;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.sql.SQLException;
import java.text.DecimalFormat;

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
    private Label fineAmountLabel;

    @FXML
    private Label isbnLabel;

    private ListView<BorrowingRecord> listView;
    private BorrowingRecord borrowedBook;
    private DocumentDao documentDao=new DocumentDao();
    private FineDao fineDao=new FineDao();

    public void setListView(ListView<BorrowingRecord> lv){
        this.listView=lv;
    }

    public void loadBorrowedBooks(BorrowingRecord br) throws SQLException {
        if (br != null) {
            borrowedBook = br;
            Document doc = documentDao.get(br.getISBN());
            Author author = documentDao.getAuthor(doc.getAuthorId());
            authorLabel.setText(author.getName());
            isbnLabel.setText("ISBN: " + doc.getISBN());
            bookNameLabel.setText(doc.getTitle());
            Category category = documentDao.getCategory(doc.getCategoryId());
            categoryLabel.setText("Category: " + category.getName());
            pageLabel.setText("Total pages: " +doc.getPage());

            brdateLabel.setText(String.valueOf(br.getBorrowDate()));
            duedateLabel.setText(String.valueOf(br.getBorrowDate().plusDays(14)));
            switch(br.getStatus()) {
                case "borrowed":
                    statusLabel.setText("Borrowed");
                    break;
                case "late":
                    statusLabel.setText("Late");
                    DecimalFormat decimalFormat = new DecimalFormat("#,###");
                    fineAmountLabel.setText("Fine: " + decimalFormat.format(fineDao.get(br.getRecordId()).getFineAmount()));
                    fineAmountLabel.setVisible(true);
                    break;
                case "lost":
                    statusLabel.setText("Lost");
                    DecimalFormat decimalFormat1 = new DecimalFormat("#,###");
                    fineAmountLabel.setText("Fine: " + decimalFormat1.format(fineDao.get(br.getRecordId()).getFineAmount()));
                    fineAmountLabel.setVisible(true);
                    break;
            }

            if (!doc.getImageLink().equals("N/A")) {
                loadImageLazy(doc.getImageLink(), bookImage, bookImage.getFitWidth(), bookImage.getFitHeight());
            } else {
                bookImage.setImage(new Image("/ui/admindashboard/bookcover.png")); // Hình ảnh mặc định khi không có bìa
            }
        }
    }
}
