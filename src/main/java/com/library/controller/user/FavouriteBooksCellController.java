package com.library.controller.user;

import com.library.dao.DocumentDao;
import com.library.dao.FavouriteDao;
import com.library.models.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
    private Label reqdateLabel;

    @FXML
    private Label categoryLabel;

    @FXML
    private Label isbnLabel;

    @FXML
    private Label pageLabel;

    @FXML
    private ImageView favImage;

    private ListView<Favourite> listView;
    private Favourite current;
    private DocumentDao documentDao=new DocumentDao();
    private FavouriteDao favouriteDao=new FavouriteDao();

    public void setListView(ListView<Favourite> lv){
        this.listView=lv;
    }

    public void loadUserFavourite(Favourite r) throws SQLException {
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


            if (!doc.getImageLink().equals("N/A")) {
                loadImageLazy(doc.getImageLink(), bookImage, bookImage.getFitWidth(), bookImage.getFitHeight());
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        favImage.setOnMouseClicked(event -> {
            if (current != null && listView != null) {
                favouriteDao.delete(current);
                listView.getItems().remove(current);
            }
        });
    }

}