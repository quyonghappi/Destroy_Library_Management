package com.library.controller.books;

import com.library.models.BorrowingRecord;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

import java.sql.SQLException;

public class ReturnBookCell extends ListCell<BorrowingRecord> {
    private HBox returnBox;
    private ReturnBookCellController returnBookCellController;

    public ReturnBookCell() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Admin/Books/LentInfoCell.fxml"));
            returnBox = loader.load();
            returnBookCellController = loader.getController();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setListView(ListView<BorrowingRecord> listView) {
        if(returnBox != null) {
            returnBookCellController.setListView(listView);
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
                returnBookCellController.loadReturnedBook(item);
                setGraphic(returnBox);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
