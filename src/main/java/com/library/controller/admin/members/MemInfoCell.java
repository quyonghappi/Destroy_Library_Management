package com.library.controller.admin.members;
import com.library.models.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

public class MemInfoCell extends ListCell<User> {
    private HBox memInfoBox;
    private MemInfoCellController memInfoCellController;

    public MemInfoCell() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Admin/Member/memInfoCell.fxml"));
            memInfoBox = loader.load();
            memInfoCellController = loader.getController();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(User item, boolean empty) {
        super.updateItem(item, empty);
        if(empty || item == null) {
            setText(null);
            setGraphic(null);
        }
        else {
            memInfoCellController.loadUser(item);
            setGraphic(memInfoBox);
        }
    }

}
