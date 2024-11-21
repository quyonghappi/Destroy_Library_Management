package com.library.controller.books;

import com.library.controller.dashboard.AdminDashboardController;
import com.library.controller.dashboard.RequestCell;
import com.library.controller.members.MemInfoController;
import com.library.dao.ReservationDao;
import com.library.models.Reservation;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.library.utils.SceneSwitcher.navigateToScene;
import static com.library.utils.SceneSwitcher.showLendBookScene;

public class RequestBookController implements Initializable {

    @FXML
    StackPane requestRoot;
    @FXML
    private HBox aboutContainer;

    @FXML
    private Label addBookButton;

    @FXML
    private HBox allNav;

    @FXML
    private ListView<Reservation> requestDetailContainer;

    @FXML
    private Button lendButton;

    @FXML
    private HBox lentNav;

    @FXML
    private Label memNameLabel;

    @FXML
    private HBox membersContainer;

    @FXML
    private HBox overdueNav;

    @FXML
    private HBox overviewContainer;

    @FXML
    private HBox returnNav;

    @FXML
    private TextField searchField;

    @FXML
    private TextField searchField1;

    private ReservationDao reservationDao=new ReservationDao();
    List<Reservation> reservations=new ArrayList<Reservation>();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reservations=getReservations();
        requestDetailContainer.setCellFactory(param->
        {
            RequestBookCell requestBookCell = new RequestBookCell();
            requestBookCell.setListView(requestDetailContainer);
            requestBookCell.setParentController(this);
            return requestBookCell;
        });
        requestDetailContainer.getItems().setAll(reservations);
        sortListView();

        allNav.setOnMouseClicked(event -> {
            String userFullName=memNameLabel.getText();
            BookInfoController controller = navigateToScene("/fxml/Admin/Books/BookInfo.fxml", allNav);
            if (controller != null) {
                controller.setUserFullName(userFullName);
            }
        });
        returnNav.setOnMouseClicked(event -> {
            String userFullName=memNameLabel.getText();
            ReturnBookController controller=navigateToScene("/fxml/Admin/Books/ReturnedBook.fxml", returnNav);
            if (controller != null) {
                controller.setUserFullName(userFullName);
            }
        });
        lentNav.setOnMouseClicked(event -> {
            String userFullName=memNameLabel.getText();
            LentBookController controller = navigateToScene("/fxml/Admin/Books/LentBook.fxml", lentNav);
            if (controller != null) {
                controller.setUserFullName(userFullName);
            }
        });
        overdueNav.setOnMouseClicked(event -> {
            String userFullName=memNameLabel.getText();
            OverdueBookController controller=navigateToScene("/fxml/Admin/Books/OverdueBook.fxml", overdueNav);
            if (controller != null) {
                controller.setUserFullName(userFullName);
            }
        });
        overviewContainer.setOnMouseClicked(event -> {
            String userFullName=memNameLabel.getText();
            AdminDashboardController controller= navigateToScene("/fxml/Admin/Dashboard/adminDashboard.fxml", overviewContainer);
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
        lendButton.setOnMouseClicked(event->showLendBookScene(requestRoot));
    }


    private List<Reservation> getReservations(){
        return reservationDao.getReservations();
    }

    void sortListView() {
        requestDetailContainer.getItems().sort((r1, r2) -> {
            if ("active".equals(r1.getStatus()) && !"active".equals(r2.getStatus())) {
                return -1;
            } else if (!"active".equals(r1.getStatus()) && "active".equals(r2.getStatus())) {
                return 1;
            }
            return 0;
        });
    }

    public void setUserFullName(String userFullName) {
        memNameLabel.setText(userFullName);
    }
}
