package com.library.controller.user;

import com.library.models.BorrowingRecord;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.sql.SQLException;

public class BorrowedBooksCell extends ListCell<BorrowingRecord> {
    private HBox borrowedBooksBox;
    private BorrrowedBooksCellController borrowedBooksCellController;

    public BorrowedBooksCell() {
        try {
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/fxml/User/BorrowedBooksCell.fxml"));
            borrowedBooksBox = loader.load();
            borrowedBooksCellController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setListView(ListView<BorrowingRecord> lv) {
        if (borrowedBooksBox != null) {
            borrowedBooksCellController.setListView(lv);
        }
    }

    @Override
    protected void updateItem(BorrowingRecord item, boolean empty) {
        super.updateItem(item, empty);
        if(empty || item == null) {
            setText(null);
            setGraphic(null);
        }
        else {
            try {
                borrowedBooksCellController.loadBorrowedBooks(item);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            setGraphic(borrowedBooksBox);
        }
    }

}