package com.library.controller.user;

import com.library.dao.DocumentDao;
import com.library.dao.ReservationDao;
import com.library.models.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static com.library.utils.LoadImage.loadImageLazy;

public class UserRequestCellController implements Initializable {
    @FXML
    private Label authorLabel;

    @FXML
    private ImageView bookImage;

    @FXML
    private Label bookNameLabel;

    @FXML
    private Label reqdateLabel;

    @FXML
    private Label categoryLabel;

    @FXML
    private Label isbnLabel;

    @FXML
    private Label pageLabel;

    @FXML
    private ImageView deleteImage;

    @FXML
    private Label statusLabel;

    private ListView<Reservation> listView;
    private Reservation current;
    private DocumentDao documentDao=new DocumentDao();
    private ReservationDao reservationDao = new ReservationDao();

    public void setListView(ListView<Reservation> lv){
        this.listView=lv;
    }

    public void loadUserRequest(Reservation r) throws SQLException {
        if (r != null) {
            current=r;
            Document doc = documentDao.get(r.getIsbn());
            Author author = documentDao.getAuthor(doc.getAuthorId());
            authorLabel.setText(author.getName());
            isbnLabel.setText(doc.getISBN());
            bookNameLabel.setText(doc.getTitle());

            Category category = documentDao.getCategory(doc.getCategoryId());
            categoryLabel.setText(category.getName());
            pageLabel.setText(String.valueOf(doc.getPage()));

            reqdateLabel.setText(String.valueOf(r.getReservationDate()));
            switch (r.getStatus()) {
                case "active":
                    statusLabel.setVisible(false);
                    break;
                case "fulfilled":
                    statusLabel.setText("Fulfilled");
                    ((VBox) deleteImage.getParent()).getChildren().remove(deleteImage);
                    statusLabel.setVisible(true);
                    break;
                case "cancelled":
                    statusLabel.setText("Cancelled");
                    ((VBox) deleteImage.getParent()).getChildren().remove(deleteImage);
                    break;
            }


            if (!doc.getImageLink().equals("N/A")) {
                loadImageLazy(doc.getImageLink(), bookImage, bookImage.getFitWidth(), bookImage.getFitHeight());
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        deleteImage.setOnMouseClicked(event -> {
            if (current != null && listView != null) {
                reservationDao.delete(current);
                listView.getItems().remove(current);
            }
        });
    }

}
