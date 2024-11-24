package com.library.controller.admin.books;

import com.library.dao.DocumentDao;
import com.library.dao.ReservationDao;
import com.library.models.Author;
import com.library.models.Document;
import com.library.models.Publisher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;

import static com.library.utils.LoadImage.loadImageLazy;

public class BookInfoCellController extends ListCell<Document> {
    @FXML
    private Label authorLabel;

    @FXML
    private ImageView bookImage;

    @FXML
    private Label bookNameLabel;

    @FXML
    private Label isbnLabel;

    @FXML
    private ImageView deleteImage;

    @FXML
    private ImageView editImage;

    @FXML
    private Label publisherLabel;

    @FXML
    private Label requestLabel;

    @FXML
    private Label locationLabel;

    @FXML
    private Label statusLabel;

    private ListView<Document> listView;
    private Document currentDocument;

    private ReservationDao reservationDao=new ReservationDao();
    private DocumentDao documentDao = new DocumentDao();
    private Publisher publisher=new Publisher();
    private Author author = new Author();

//    private static final Map<String, Image> imageCache = new HashMap<String, Image>();

    public void setListView(ListView<Document> listView) {
        this.listView = listView;
    }
    public void loadDocument(Document document) {
        if (document != null) {
            currentDocument = document;
            isbnLabel.setText(document.getISBN());
            bookNameLabel.setText(document.getTitle());
            locationLabel.setText(document.getLocation());
            publisher=documentDao.getPublisher(document.getPublisherId());
            author=documentDao.getAuthor(document.getAuthorId());
            if (publisher.getName() != null) {
                publisherLabel.setText(publisher.getName());
            } else publisherLabel.setText("N/A");
            authorLabel.setText(author.getName());
            //get num of request for this book
            requestLabel.setText(String.valueOf(reservationDao.getByISBN(document.getISBN())));

            // Load the image using lazy loading
            if (!document.getImageLink().equals("N/A")) {
                loadImageLazy(document.getImageLink(), bookImage, bookImage.getFitWidth(), bookImage.getFitHeight());
            }
        }
    }

    @FXML
    private void handleEditButtonAction(ActionEvent event) {
//        try {
//            documentDao.delete(documentDao.findByISBN(isbnLabel.getText());
//            BorrowingRecordDao borrowingRecordDao=new BorrowingRecordDao();
//            BorrowingRecord borrowingRecord=new BorrowingRecord(useridLabel.getText(), isbnLabel.getText(),now(),"borrowed");
//            borrowingRecordDao.add(borrowingRecord);
//            //remove current request from listview
//            lv.getItems().remove(current);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }

    }

    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        try {
            documentDao.delete(documentDao.get(isbnLabel.getText()));
            listView.getItems().remove(currentDocument);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
