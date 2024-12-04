package com.library.controller.admin.dashboard;

import com.library.controller.admin.books.OverdueBookController;
import com.library.controller.admin.books.RequestBookController;
import com.library.models.Fine;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

public class OverdueCell extends ListCell<Fine> {
    private HBox overdueCellBox;
    private OverdueController overdueController;


    public OverdueCell() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Admin/Dashboard/Overdue.fxml"));
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

    public void setParentController(OverdueBookController parentController) {
        if (overdueController != null) {
            overdueController.setParentController(parentController);
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
