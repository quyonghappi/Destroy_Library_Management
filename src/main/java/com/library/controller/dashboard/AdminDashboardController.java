package com.library.controller.dashboard;

import com.library.controller.books.BookInfoController;
import com.library.controller.members.MemInfoController;
import com.library.dao.BorrowingRecordDao;
import com.library.dao.FineDao;
import com.library.dao.ReservationDao;
import com.library.dao.UserDao;
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
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static com.library.utils.SceneSwitcher.navigateToScene;
import static com.library.utils.SceneSwitcher.showLendBookScene;

public class AdminDashboardController implements Initializable {

    @FXML
    StackPane dashboardRoot;

    @FXML
    private HBox aboutContainer;

    @FXML
    private HBox booksContainer;

    @FXML
    private Label borrowedLabel;

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
    private HBox overdueNav, requestNav;

    @FXML
    private ListView<Reservation> requestDetailContainer;

    @FXML
    private HBox searchContainer;

    @FXML
    private TextField searchField;

    @FXML
    private Label visitorLabel;

    private BorrowingRecordDao borrowingRecordDao=new BorrowingRecordDao();
    private UserDao userDa0=new UserDao();
    private FineDao fineDao = new FineDao();
    private ReservationDao reservationDao=new ReservationDao();
    private List<Fine> fineList;
    private List<Reservation> reservationList;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fineList=getFineList();
        reservationList=getReservationList();
        borrowedLabel.setText(String.valueOf(borrowingRecordDao.getLent().size()));
        overdueBookLabel.setText(String.valueOf(borrowingRecordDao.getLate().size()));
        visitorLabel.setText(String.valueOf(userDa0.getAll().size()));

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

        //add event on Container to switch scene
        booksContainer.setOnMouseClicked(event -> {
            String userFullName=memNameLabel.getText();
            BookInfoController controller= navigateToScene("/fxml/Admin/Books/BookInfo.fxml", booksContainer);
            if (controller != null) {
                controller.setUserFullName(userFullName);
            }
        });
        membersContainer.setOnMouseClicked(event-> {
            String userFullName=memNameLabel.getText();
            MemInfoController controller= navigateToScene("/fxml/Admin/Member/MemInfo.fxml", membersContainer);
            if (controller != null) {
                controller.setUserFullName(userFullName);
            }
        });
        overdueNav.setOnMouseClicked(event -> navigateToScene("/fxml/Admin/Books/OverdueBook.fxml", overdueNav));
        requestNav.setOnMouseClicked(event -> navigateToScene("/fxml/Admin/Books/RequestBook.fxml", requestNav));
        lendButton.setOnMouseClicked(event->showLendBookScene(dashboardRoot));
    }

    private List<Reservation> getReservationList() {
        return reservationDao.getReservations();
    }

    private List<Fine> getFineList() {
//        List<BorrowingRecord> lateList=borrowingRecordDao.getLate();
//        for (int i=0; i<lateList.size(); i++) {
//            fineDao.addFine(lateList.get(i));
//        }
        return fineDao.getAll();
    }

    public void setUserFullName(String userFullName) {
        memNameLabel.setText(userFullName);
    }

}
