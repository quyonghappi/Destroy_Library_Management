package com.library.controller.books;

import com.library.controller.dashboard.RequestCell;
import com.library.dao.ReservationDao;
import com.library.models.Reservation;
import com.library.utils.SceneSwitcher;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RequestBookController implements Initializable {
    @FXML
    private HBox aboutContainer;

    @FXML
    private Label addBookButton;

    @FXML
    private HBox allNav;

    @FXML
    private ListView<Reservation> requestDetailContainer;

    @FXML
    private HBox booksContainer1;

    @FXML
    private HBox helpContainer;

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
    private HBox requestNav;

    @FXML
    private HBox returnNav;

    @FXML
    private TextField searchField;

    @FXML
    private TextField searchField1;

    @FXML
    private HBox settingContainer;

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

        allNav.setOnMouseClicked(event -> navigateToBookInfo());
        overdueNav.setOnMouseClicked(event -> navigateToOverdueBookInfo());
        lentNav.setOnMouseClicked(event -> navigateToLentBookInfo());
        returnNav.setOnMouseClicked(event -> navigateToReturnBookInfo());
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

    @FXML
    public void navigateToBookInfo() {
        SceneSwitcher.loadSceneAsync("/fxml/Admin/Books/BookInfo.fxml").thenAccept(scene -> {
            if (scene != null) {
                Platform.runLater(() -> {
                    Stage stage = (Stage) allNav.getScene().getWindow();
                    SceneSwitcher.switchScene(stage, scene);
                });
            } else {
                System.out.println("Failed to load LentBook scene.");
            }
        });
        //Platform.runLater(() -> stage.setFullScreen(true));
    }

    @FXML
    public void navigateToReturnBookInfo() {
        SceneSwitcher.loadSceneAsync("/fxml/Admin/Books/ReturnedBook.fxml").thenAccept(scene -> {
            if (scene != null) {
                Platform.runLater(() -> {
                    Stage stage = (Stage) returnNav.getScene().getWindow();
                    SceneSwitcher.switchScene(stage, scene);
                });
            } else {
                System.out.println("Failed to load ReturnBook scene.");
            }
        });

    }

    @FXML
    public void navigateToOverdueBookInfo() {
        SceneSwitcher.loadSceneAsync("/fxml/Admin/Books/OverdueBook.fxml").thenAccept(scene -> {
            if (scene != null) {
                Platform.runLater(() -> {
                    Stage stage = (Stage) overdueNav.getScene().getWindow();
                    SceneSwitcher.switchScene(stage, scene);
                });
            } else {
                System.out.println("Failed to load overdueBook scene.");
            }
        });
    }

    @FXML
    public void navigateToLentBookInfo() {
        SceneSwitcher.loadSceneAsync("/fxml/Admin/Books/LentBook.fxml").thenAccept(scene -> {
            if (scene != null) {
                Platform.runLater(() -> {
                    Stage stage = (Stage) requestNav.getScene().getWindow();
                    SceneSwitcher.switchScene(stage, scene);
                });
            } else {
                System.out.println("Failed to load requestBook scene.");
            }
        });
    }
}
