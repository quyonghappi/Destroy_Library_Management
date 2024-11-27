package com.library.controller.user;

import com.library.dao.ReservationDao;
import com.library.models.Reservation;
import com.library.utils.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UserRequestController implements Initializable {
    @FXML
    private HBox brNav;

    @FXML
    private HBox favNav;

    @FXML
    private HBox homeNav;

    @FXML
    private Button logoutButton;

    @FXML
    private Label memNameLabel;

    @FXML
    private ListView<Reservation> requestListContainer;

    @FXML
    private TextField searchField;

    @FXML
    private HBox searchNav;

    private String username;
    private ReservationDao reservationDao = new ReservationDao();
    private List<Reservation> requestList;

    public void setUsername(String username) {
        this.username = username;
        System.out.println("Username set: " + username);

        requestList=reservationDao.getByUserName(username);
        requestListContainer.setCellFactory(param ->
        {
            UserRequestCell userRequestCell = new UserRequestCell();
            userRequestCell.setListView(requestListContainer);
            return userRequestCell;

        });
        requestListContainer.getItems().setAll(requestList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setUserFullName(String userFullName) {
        memNameLabel.setText(userFullName);
    }

    @FXML
    public void logout(ActionEvent event) {
        SceneSwitcher.navigateToScene("/fxml/Start/Role.fxml", logoutButton);
    }
}
