package com.library.controller.user;

import com.library.controller.Observer;
import com.library.dao.DocumentDao;
import com.library.dao.ReservationDao;
import com.library.models.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

    private Reservation current;
    private DocumentDao documentDao= DocumentDao.getInstance();
    private ReservationDao reservationDao = ReservationDao.getInstance();

    public void loadUserRequest(Reservation r) throws SQLException {
        if (r != null) {
            current = r;
            Document doc = documentDao.get(r.getIsbn());
            Author author = documentDao.getAuthor(doc.getAuthorId());
            authorLabel.setText(author.getName());
            isbnLabel.setText(doc.getISBN());
            bookNameLabel.setText(doc.getTitle());

            Category category = documentDao.getCategory(doc.getCategoryId());
            categoryLabel.setText("Category: " + category.getName());
            pageLabel.setText("Total pages: " + doc.getPage());

            reqdateLabel.setText(String.valueOf(r.getReservationDate()));
            updateVisibility(r);
            if (!doc.getImageLink().equals("N/A")) {
                loadImageLazy(doc.getImageLink(), bookImage, bookImage.getFitWidth(), bookImage.getFitHeight());
            } else {
                bookImage.setImage(new Image("/ui/admindashboard/bookcover.png")); // Hình ảnh mặc định khi không có bìa
            }
        }
    }

        private void updateVisibility(Reservation r) {
            switch (r.getStatus()) {
                case "active":
                    statusLabel.setVisible(false);
                    deleteImage.setVisible(true);
                    break;
                case "fulfilled":
                    statusLabel.setText("Fulfilled");
                    deleteImage.setVisible(false);
                    statusLabel.setVisible(true);
                    break;
                case "cancelled":
                    statusLabel.setText("Cancelled");
                    deleteImage.setVisible(false);
                    statusLabel.setVisible(true);
                    break;
            }


        }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        deleteImage.setOnMouseClicked(event -> {
            if (current != null) {
                reservationDao.delete(current);
            }
        });
    }

}
