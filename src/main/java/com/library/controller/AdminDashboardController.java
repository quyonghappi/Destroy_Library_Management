package com.library.controller;


import com.library.dao.BorrowingRecordDao;
import com.library.dao.FineDao;
import com.library.dao.ReservationDao;
import com.library.models.BorrowingRecord;
import com.library.models.Fine;
import com.library.models.Reservation;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {


    @FXML
    private HBox aboutContainer;

    @FXML
    private HBox booksContainer;

    @FXML
    private Label borrowedLabel;

    @FXML
    private HBox helpContainer;

    @FXML
    private Button lendButton;

    @FXML
    private Label memNameLabel;

    @FXML
    private HBox membersContainer;

    @FXML
    private Label overdueBookLabel;

    @FXML
    private ListView<Fine> overdueDetailContainer;

    @FXML
    private ImageView overdueNav;

    @FXML
    private HBox overviewContainer;

    @FXML
    private ListView<Reservation> requestDetailContainer;

    @FXML
    private ImageView requestNav;

    @FXML
    private HBox searchContainer;

    @FXML
    private TextField searchField;

    @FXML
    private HBox settingContainer;

    @FXML
    private HBox totalBorrowedContainer;

    @FXML
    private HBox totalOverdueContainer;

    @FXML
    private HBox totalVisitContainer;

    @FXML
    private Label visitorLabel;

    private BorrowingRecordDao borrowingRecordDao=new BorrowingRecordDao();
    private FineDao fineDao = new FineDao();
    private ReservationDao reservationDao=new ReservationDao();
    private List<Fine> fineList;
    private List<Reservation> reservationList;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fineList=getFineList();
        reservationList=getReservationList();

        requestDetailContainer.setCellFactory(param ->
        {
            RequestCell requestCell = new RequestCell();
            requestCell.setListView(requestDetailContainer);
            return requestCell;
        });
        requestDetailContainer.getItems().addAll(reservationList);

        overdueDetailContainer.setCellFactory(param-> {
            OverdueCell overdueCell=new OverdueCell();
            overdueCell.setListView(overdueDetailContainer);
            return overdueCell;
        });
        overdueDetailContainer.getItems().addAll(fineList);
    }

    private List<Reservation> getReservationList() {
        return reservationDao.getReservations();
    }
    private List<Fine> getFineList() {
        List<BorrowingRecord> lateList=borrowingRecordDao.getLate();
        for (int i=0; i<lateList.size(); i++) {
            fineDao.addFine(lateList.get(i));
        }
        return fineDao.getFines();
    }
}
