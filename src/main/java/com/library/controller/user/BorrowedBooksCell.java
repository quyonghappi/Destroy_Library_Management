package com.library.controller.user;

import com.library.models.BorrowingRecord;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class BorrowedBooksCell extends ListCell<BorrowingRecord> {
    private HBox borrowedBooksBox;
    private BorrowedBooksCellController borrowedBooksCellController;

    public BorrowedBooksCell() {
        try {
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/fxml/User/BorrowedBooksCell.fxml"));
            borrowedBooksBox = loader.load();
            borrowedBooksCellController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
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
