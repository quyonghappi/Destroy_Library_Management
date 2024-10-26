package com.library.controller;

import com.library.dao.*;
import com.library.models.BorrowingRecord;
import com.library.models.Document;
import com.library.models.Fine;
import com.library.models.Reservation;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BookInfoController implements Initializable {

    @FXML
    private HBox aboutContainer;

    @FXML
    private ListView<Document> bookDetailContainer;

    @FXML
    private HBox booksContainer;

    @FXML
    private HBox helpContainer;

    @FXML
    private Button lendButton;

    @FXML
    private Label memNameLabel;

    @FXML
    private HBox membersContainer;

    @FXML
    private HBox overviewContainer;

    @FXML
    private TextField searchField;

    @FXML
    private TextField searchField1;

    @FXML
    private TextField searchField11;

    @FXML
    private HBox settingContainer;

    private Parent root;
    private Scene scene;
    private Stage stage;

    private DocumentDao documentDao=new DocumentDao();
    List<Document> documentList=new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        documentList=getDocumentList();
        bookDetailContainer.setCellFactory(param ->
        {
            BookInfoCell bookInfoCell=new BookInfoCell();
            bookInfoCell.setListView(bookDetailContainer);
            return bookInfoCell;
        });
        bookDetailContainer.getItems().setAll(documentList);

    }

    private List<Document> getDocumentList() {
        return documentDao.getDocuments();
    }


}
