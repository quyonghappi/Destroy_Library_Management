package com.library.controller.user;

import com.library.dao.DocumentDao;
import com.library.dao.FavouriteDao;
import com.library.models.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static com.library.utils.LoadImage.loadImageLazy;

public class FavouriteBooksCellController implements Initializable {
    @FXML
    private Label authorLabel;

    @FXML
    private ImageView bookImage;

    @FXML
    private Label bookNameLabel;

    @FXML
    private Label categoryLabel;

    @FXML
    private Label isbnLabel;

    @FXML
    private Label pageLabel;

    @FXML
    private ImageView favImage;

    private Favourite current;
    private DocumentDao documentDao=new DocumentDao();
    private FavouriteDao favouriteDao= FavouriteDao.getInstance();

    public void loadUserFavourite(Favourite r) throws SQLException {
        if (r != null) {
            current=r;
            Document doc = documentDao.get(r.getIsbn());
            Author author = documentDao.getAuthor(doc.getAuthorId());
            authorLabel.setText(author.getName());
            isbnLabel.setText("ISBN: " + doc.getISBN());
            bookNameLabel.setText(doc.getTitle());

            Category category = documentDao.getCategory(doc.getCategoryId());
            categoryLabel.setText("Category: "+category.getName());
            pageLabel.setText("Total pages: " + doc.getPage());


            if (!doc.getImageLink().equals("N/A")) {
                loadImageLazy(doc.getImageLink(), bookImage, bookImage.getFitWidth(), bookImage.getFitHeight());
            } else {
                bookImage.setImage(new Image("/ui/admindashboard/bookcover.png")); // Hình ảnh mặc định khi không có bìa
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        favImage.setOnMouseClicked(event -> {
            if (current != null) {
                favouriteDao.delete(current);
            }
        });
    }


}
