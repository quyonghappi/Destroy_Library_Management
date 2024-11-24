package com.library.controller.admin.books;

import com.library.models.Document;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

public class BookInfoCell extends ListCell<Document> {
    private HBox bookInfoBox;
    private BookInfoCellController bookInfoCellController;

    public BookInfoCell() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Admin/Books/bookInfoCell.fxml"));
            bookInfoBox = loader.load();
            bookInfoCellController = loader.getController();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setListView(ListView<Document> listView) {
        if(bookInfoBox != null) {
            bookInfoCellController.setListView(listView);
        }
    }

    @Override
    protected void updateItem(Document item, boolean empty) {
        super.updateItem(item, empty);
        if(empty || item == null) {
            setText(null);
            setGraphic(null);
        }
        else {
            bookInfoCellController.loadDocument(item);
            setGraphic(bookInfoBox);
        }
    }
}
