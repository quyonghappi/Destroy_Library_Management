package com.library.controller.user;

import com.library.models.Favourite;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.sql.SQLException;

public class FavouriteBooksCell extends ListCell<Favourite> {
    private HBox favBox;
    private FavouriteBooksCellController controller;

    public FavouriteBooksCell() {
        try {
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/fxml/User/FavouriteBookCell.fxml"));
            favBox = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(Favourite item, boolean empty) {
        super.updateItem(item, empty);
        if(empty || item == null) {
            setText(null);
            setGraphic(null);
        }
        else {
            try {
                controller.loadUserFavourite(item);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            setGraphic(favBox);
        }
    }
}
