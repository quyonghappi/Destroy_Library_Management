package com.library.controller.user;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class FavouriteController implements Initializable {
    @FXML
    private Label memNameLabel;

    private String username;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserFullName(String userFullName) {
        memNameLabel.setText(userFullName);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
