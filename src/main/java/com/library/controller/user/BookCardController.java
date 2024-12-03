package com.library.controller.user;

import com.library.dao.DocumentDao;
import com.library.models.Document;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

import static com.library.utils.LoadImage.loadImageLazy;

public class BookCardController implements Initializable {
    @FXML
    private Label authorLabel;

    @FXML
    private ImageView bookImage;

    @FXML
    private Label titleLabel;

    private DocumentDao documentDao = new DocumentDao();

    public void loadBookInfo(Document doc) {
        authorLabel.setText(documentDao.getAuthor(doc.getAuthorId()).getName());
        titleLabel.setText(doc.getTitle());
        if (!doc.getImageLink().equals("N/A")) {
            loadImageLazy(doc.getImageLink(), bookImage, bookImage.getFitWidth(), bookImage.getFitHeight());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //bookImage.setOnMouseClicked(event -> )
    }
}
