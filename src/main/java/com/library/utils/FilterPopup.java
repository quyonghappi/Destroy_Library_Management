package com.library.utils;

import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class FilterPopup {
    private static FilterPopup instance;
    private static final Popup popup = new Popup();
    private static final ListView<String> listView = new ListView<>();
    private static String selectedItem = null;

    private FilterPopup() {
//        popup = new Popup();
//        listView = new ListView<>();
        listView.getItems().addAll("Title", "ISBN", "Author", "Category");
        listView.setPrefSize(200, 100);
        listView.setStyle("-fx-border-radius: 5; " +
                "    -fx-font-size: 13px; " +
                "    -fx-border-color: transparent; " +
                "    -fx-border-width: 1px; " +
                "    -fx-font-family: \"Segoe UI\";");
        popup.getContent().add(listView);
        popup.setAutoHide(true); //hides when clicking outside
    }

    public static FilterPopup getInstance() {
        if (instance == null) {
            instance = new FilterPopup();
        }
        return instance;
    }

    public void show(double x, double y, Stage owner) {
        if (!popup.isShowing()) {
            //popup will be displayed relative to owner stage
            //popup.show(owner, x, y) method is part of the Popup class in javafx
            popup.show(owner, x, y);
        }
    }

    public void hide() {
        popup.hide();
    }

    public static void showPopup(ImageView filter, MouseEvent event) {
        //get imageview position in the scene
        double x = event.getSceneX();
        double y = event.getSceneY();
        Stage owner = (Stage) filter.getScene().getWindow();
        FilterPopup.getInstance().show(x+8, y+15, owner);
    }

    public static void setFilterSelectionListener(Consumer<String> listener) {
        listView.setOnMouseClicked(event -> {
            String selectedFilter = listView.getSelectionModel().getSelectedItem();
            if (selectedFilter != null && listener != null) {
                selectedItem = selectedFilter;
                listener.accept(selectedFilter);
            }
            popup.hide();
        });
    }

    public static  String getSelectedItem() {
        return selectedItem;
    }

}
