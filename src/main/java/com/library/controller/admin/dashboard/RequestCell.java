package com.library.controller.admin.dashboard;

import com.library.models.Reservation;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

public class RequestCell extends ListCell<Reservation> {
    private HBox requestCellBox;
    private RequestController requestController;

    public RequestCell() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Admin/Dashboard/Request.fxml"));
            requestCellBox = loader.load();
            requestController = loader.getController();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setParentController(AdminDashboardController parentController) {
        if (requestController != null) {
            requestController.setParentController(parentController);
        }
    }

    @Override
    protected void updateItem(Reservation reservation, boolean empty) {
        super.updateItem(reservation, empty);

        if (empty || reservation == null) {
            setText(null);
            setGraphic(null);
        } else {
            requestController.loadReservation(reservation);
            setGraphic(requestCellBox); //set custom graphic
        }
    }
}
