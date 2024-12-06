package com.library.controller.admin.books;

import com.library.models.Reservation;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

public class RequestBookCell extends ListCell<Reservation> {
    private HBox requestBox;
    private RequestBookCellController controller;

    public RequestBookCell() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Admin/Books/RequestBookCell.fxml"));
            requestBox = loader.load();
            controller = loader.getController();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(Reservation item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        }
        else {
            controller.loadReservation(item);
            setGraphic(requestBox);
        }
    }
}
