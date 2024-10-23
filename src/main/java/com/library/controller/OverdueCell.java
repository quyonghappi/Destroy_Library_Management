package com.library.controller;

import com.library.controller.OverdueController;
import com.library.models.Fine;
import com.library.models.Reservation;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

public class OverdueCell extends ListCell<Fine> {
    private HBox overdueCellBox;
    private OverdueController overdueController;


    public OverdueCell() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Overdue.fxml"));
            overdueCellBox = loader.load();
            overdueController = loader.getController();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setListView(ListView<Fine> listView) {
        if(overdueController!=null) {
            overdueController.setListView(listView);
        }
    }


    @Override
    protected void updateItem(Fine fine, boolean empty) {
        super.updateItem(fine, empty);

        if (empty || fine == null) {
            setText(null);
            setGraphic(null);
        } else {
            overdueController.loadOverdueData(fine);
            setGraphic(overdueCellBox); // Set custom graphic
        }
    }
}
