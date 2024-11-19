package com.library.controller.books;

import com.library.models.BorrowingRecord;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

import java.sql.SQLException;

public class LentBookCell extends ListCell<BorrowingRecord> {
    private HBox lentBox;
    private LentBookCellController lentBookCellController;

    public LentBookCell() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Admin/Books/LentInfoCell.fxml"));
            lentBox = loader.load();
            lentBookCellController = loader.getController();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setListView(ListView<BorrowingRecord> listView) {
        if(lentBox != null) {
            lentBookCellController.setListView(listView);
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
                lentBookCellController.loadLentBook(item);
                setGraphic(lentBox);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
