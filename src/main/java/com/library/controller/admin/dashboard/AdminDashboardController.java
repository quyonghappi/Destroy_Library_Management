package com.library.controller.admin.dashboard;

import com.library.controller.admin.books.BookInfoController;
import com.library.controller.admin.members.MemInfoController;
import com.library.dao.BorrowingRecordDao;
import com.library.dao.FineDao;
import com.library.dao.ReservationDao;
import com.library.dao.UserDao;
import com.library.models.Fine;
import com.library.models.Reservation;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

//import static com.library.utils.FilterPopup.showPopup;
import static com.library.utils.LoadView.loadView;
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
    private Label visitorLabel;

    @FXML
    private Button logOut;

    private BorrowingRecordDao borrowingRecordDao=new BorrowingRecordDao();
    private UserDao userDa0=new UserDao();
    private FineDao fineDao = new FineDao();
    private ReservationDao reservationDao=new ReservationDao();
    private List<Fine> fineList;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fineList= getFineList();
        borrowedLabel.setText(String.valueOf(borrowingRecordDao.getLent().size()));
        overdueBookLabel.setText(String.valueOf(fineList.size()));
        visitorLabel.setText(String.valueOf(userDa0.getAll().size()));

        loadFineList();
        loadReservationList();
        //add event on Container
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
//        filter.setOnMouseClicked(event->showPopup(filter, event));
    }

    private void loadFineList() {
        Task<List<Fine>> loadTask = new Task<>() {
            @Override
            protected List<Fine> call() {
                return fineList;
            }
        };

        loadTask.setOnSucceeded(event -> {
            refreshListView(loadTask.getValue());
        });
        loadTask.setOnFailed(event -> {
            System.out.println("fail to load fine info" + loadTask.getException());
        });
        new Thread(loadTask).start();
    }

    private void loadReservationList() {
        Task<List<Reservation>> loadTask = new Task<>() {
            @Override
            protected List<Reservation> call() throws Exception {
                return getReservationList();
            }
        };
        loadTask.setOnSucceeded(event -> {
            refreshListView1(loadTask.getValue());
        });
        loadTask.setOnFailed(event -> {
            System.out.println("fail to load request info" + loadTask.getException());
        });
        new Thread(loadTask).start();


    }


    private void refreshListView(List<Fine> fine) {
        overdueDetailContainer.setCellFactory(param-> {
            OverdueCell overdueCell=new OverdueCell();
            overdueCell.setListView(overdueDetailContainer);
            return overdueCell;
        });
        overdueDetailContainer.getItems().addAll(fineList);
    }

    private void refreshListView1(List<Reservation> reservations) {
        requestDetailContainer.setCellFactory(param ->
        {
            RequestCell requestCell = new RequestCell();
            requestCell.setListView(requestDetailContainer);
            requestCell.setParentController(this);
            return requestCell;
        });
        requestDetailContainer.getItems().addAll(reservations);
        sortListView();
    }

    private List<Reservation> getReservationList() {
        return reservationDao.getAll();
    }

    private List<Fine> getFineList() {
        return fineDao.getAll();
    }

    public void sortListView() {
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

    public void setLogOut(MouseEvent mouseEvent) {
        logOut.setMouseTransparent(true);
        Stage stage = (Stage) logOut.getScene().getWindow();
        loadView(stage, "/fxml/Start/Role.fxml", "Sign Up", "/css/start/Role.css");
    }
}
