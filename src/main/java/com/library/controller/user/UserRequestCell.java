package com.library.controller.user;

import com.library.models.Reservation;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.sql.SQLException;

public class UserRequestCell extends ListCell<Reservation> {
    private HBox requestBox;
    private UserRequestCellController controller;

    public UserRequestCell() {
        try {
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/fxml/User/user_request_cell.fxml"));
            requestBox = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(Reservation item, boolean empty) {
        super.updateItem(item, empty);
        if(empty || item == null) {
            setText(null);
            setGraphic(null);
        }
        else {
            try {
                controller.loadUserRequest(item);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            setGraphic(requestBox);
        }
    }
}
