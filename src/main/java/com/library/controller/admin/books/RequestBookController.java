//package com.library.controller.books;
//
//import com.library.controller.admin.dashboard.AdminDashboardController;
//import com.library.controller.admin.dashboard.RequestCell;
//import com.library.controller.admin.members.MemInfoController;
//import com.library.dao.ReservationDao;
//import com.library.models.Reservation;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.control.*;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.StackPane;
//
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.ResourceBundle;
//
//import static com.library.utils.FilterPopup.showPopup;
//import static com.library.utils.SceneSwitcher.navigateToScene;
//import static com.library.utils.SceneSwitcher.showLendBookScene;
//
//public class RequestBookController implements Initializable {
//
//    @FXML
//    StackPane requestRoot;
//    @FXML
//    private HBox aboutContainer;
//
//    @FXML
//    private Label addBookButton;
//
//    @FXML
//    private HBox allNav;
//
//    @FXML
//    private ListView<Reservation> requestDetailContainer;
//
//    @FXML
//    private Button lendButton;
//
//    @FXML
//    private HBox lentNav;
//
//    @FXML
//    private Label memNameLabel;
//
//    @FXML
//    private HBox membersContainer;
//
//    @FXML
//    private HBox overdueNav;
//
//    @FXML
//    private HBox overviewContainer;
//
//    @FXML
//    private HBox returnNav;
//
//    @FXML
//    private ImageView filter;
//
//    @FXML
//    private ToggleButton allButton;
//
//    @FXML
//    private ToggleButton fulfilledButton;
//
//    @FXML
//    private ToggleButton cancelledButton;
//
//    @FXML
//    private TextField searchField;
//
//    @FXML
//    private TextField searchField1;
//
//    private ReservationDao reservationDao=new ReservationDao();
//    List<Reservation> reservations=new ArrayList<Reservation>();
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        reservations=getReservations();
//        requestDetailContainer.setCellFactory(param->
//        {
//            RequestBookCell requestBookCell = new RequestBookCell();
//            requestBookCell.setListView(requestDetailContainer);
//            requestBookCell.setParentController(this);
//            return requestBookCell;
//        });
//        requestDetailContainer.getItems().setAll(reservations);
//        sortListView();
//
//        allNav.setOnMouseClicked(event -> {
//            String userFullName=memNameLabel.getText();
//            BookInfoController controller = navigateToScene("/fxml/admin/Books/BookInfo.fxml", allNav);
//            if (controller != null) {
//                controller.setUserFullName(userFullName);
//            }
//        });
//        returnNav.setOnMouseClicked(event -> {
//            String userFullName=memNameLabel.getText();
//            ReturnBookController controller=navigateToScene("/fxml/admin/Books/ReturnedBook.fxml", returnNav);
//            if (controller != null) {
//                controller.setUserFullName(userFullName);
//            }
//        });
//        lentNav.setOnMouseClicked(event -> {
//            String userFullName=memNameLabel.getText();
//            LentBookController controller = navigateToScene("/fxml/admin/Books/LentBook.fxml", lentNav);
//            if (controller != null) {
//                controller.setUserFullName(userFullName);
//            }
//        });
//        overdueNav.setOnMouseClicked(event -> {
//            String userFullName=memNameLabel.getText();
//            OverdueBookController controller=navigateToScene("/fxml/admin/Books/OverdueBook.fxml", overdueNav);
//            if (controller != null) {
//                controller.setUserFullName(userFullName);
//            }
//        });
//        overviewContainer.setOnMouseClicked(event -> {
//            String userFullName=memNameLabel.getText();
//            AdminDashboardController controller= navigateToScene("/fxml/admin/Dashboard/adminDashboard.fxml", overviewContainer);
//            if (controller != null) {
//                controller.setUserFullName(userFullName);
//            }
//        });
//        membersContainer.setOnMouseClicked(event-> {
//            String userFullName=memNameLabel.getText();
//            MemInfoController controller= navigateToScene("/fxml/admin/Member/MemInfo.fxml", membersContainer);
//            if (controller != null) {
//                controller.setUserFullName(userFullName);
//            }
//        });
//        lendButton.setOnMouseClicked(event->showLendBookScene(requestRoot));
//        filter.setOnMouseClicked(event->showPopup(filter, event));
//    }
//
//
//    private List<Reservation> getReservations(){
//        return reservationDao.getReservations();
//    }
//
//    private List<Reservation> getFulfilledList() {
//        return reservationDao.getFulfilledReservations();
//    }
//
//    private List<Reservation> getCancelledList() {
//        return reservationDao.getCancelledReservations();
//    }
//
//    void sortListView() {
//        requestDetailContainer.getItems().sort((r1, r2) -> {
//            if ("active".equals(r1.getStatus()) && !"active".equals(r2.getStatus())) {
//                return -1;
//            } else if (!"active".equals(r1.getStatus()) && "active".equals(r2.getStatus())) {
//                return 1;
//            }
//            return 0;
//        });
//    }
//
//    public void setUserFullName(String userFullName) {
//        memNameLabel.setText(userFullName);
//    }
//
//    @FXML
//    private void fulfilledFilter(ActionEvent event) {
//        reservations=getFulfilledList();
//        requestDetailContainer.setCellFactory(param->
//        {
//            RequestBookCell requestBookCell = new RequestBookCell();
//            requestBookCell.setListView(requestDetailContainer);
//            requestBookCell.setParentController(this);
//            return requestBookCell;
//        });
//        requestDetailContainer.getItems().setAll(reservations);
//    }
//
//    @FXML
//    private void cancelledFilter(ActionEvent event) {
//        reservations=getCancelledList();
//        requestDetailContainer.setCellFactory(param->
//        {
//            RequestBookCell requestBookCell = new RequestBookCell();
//            requestBookCell.setListView(requestDetailContainer);
//            requestBookCell.setParentController(this);
//            return requestBookCell;
//        });
//        requestDetailContainer.getItems().setAll(reservations);
//    }
//
//    @FXML
//    private void showAllRequest(ActionEvent event) {
//        reservations=getReservations();
//        requestDetailContainer.setCellFactory(param->
//        {
//            RequestBookCell requestBookCell = new RequestBookCell();
//            requestBookCell.setListView(requestDetailContainer);
//            requestBookCell.setParentController(this);
//            return requestBookCell;
//        });
//        requestDetailContainer.getItems().setAll(reservations);
//        sortListView();
//    }
//}


package com.library.controller.admin.books;

import com.library.controller.admin.dashboard.AdminDashboardController;
import com.library.controller.admin.members.MemInfoController;
import com.library.dao.ReservationDao;
import com.library.models.Reservation;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

//import static com.library.utils.FilterPopup.showPopup;
import static com.library.utils.SceneSwitcher.*;

public class RequestBookController implements Initializable {

    @FXML
    StackPane requestBookRoot;
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
    private ImageView filter;

    @FXML
    private ToggleButton allButton;

    @FXML
    private ToggleButton fulfilledButton;

    @FXML
    private ToggleButton cancelledButton;

    @FXML
    private TextField searchField;

    @FXML
    private TextField searchField1;

    private ReservationDao reservationDao=new ReservationDao();
    List<Reservation> reservations=new ArrayList<Reservation>();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadReservationList();

        //reservations=getReservations();

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
        lendButton.setOnMouseClicked(event->showLendBookScene(requestBookRoot));
//        filter.setOnMouseClicked(event->showPopup(filter, event));
        addBookButton.setOnMouseClicked(event->showAddBookScene(requestBookRoot));
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

    private void loadReservationList() {
        Task<List<Reservation>> loadTask=new Task<>() {
            @Override
            protected List<Reservation> call() throws Exception {
                return getReservations();
            }
        };

        //handle task completion
        loadTask.setOnSucceeded(event-> {
            reservations=loadTask.getValue();
            refreshListView(reservations);
        });

        loadTask.setOnFailed(event -> {
            System.err.println("fail to load reservation list: " + loadTask.getException());
        });

        new Thread(loadTask).start();
    }

    //refresh list view when toggle button was selected
    private void refreshListView(List<Reservation> reservations) {
        requestDetailContainer.setCellFactory(param->
        {
            RequestBookCell requestBookCell = new RequestBookCell();
            requestBookCell.setListView(requestDetailContainer);
            requestBookCell.setParentController(this);
            return requestBookCell;
        });
        requestDetailContainer.getItems().setAll(reservations);
        sortListView();
    }

    public void setUserFullName(String userFullName) {
        memNameLabel.setText(userFullName);
    }

    @FXML
    private void fulfilledFilter(ActionEvent event) {
        updateButtonStyles(fulfilledButton,allButton,cancelledButton);
        applyFilter(()->reservationDao.getFulfilledReservations());
    }

    @FXML
    private void cancelledFilter(ActionEvent event) {
        updateButtonStyles(cancelledButton, allButton, fulfilledButton);
       applyFilter(()->reservationDao.getCancelledReservations());
    }

    @FXML
    private void showAllRequest(ActionEvent event) {
        updateButtonStyles(allButton, fulfilledButton, cancelledButton);
        applyFilter(()->reservationDao.getReservations());
    }

    private void updateButtonStyles(ToggleButton selectedButton, ToggleButton other1, ToggleButton other2) {
        selectedButton.getStyleClass().removeAll("chosen-button", "bar-button");
        other1.getStyleClass().removeAll("chosen-button", "bar-button");
        other2.getStyleClass().removeAll("chosen-button", "bar-button");

        selectedButton.getStyleClass().add("chosen-button");
        other1.getStyleClass().add("bar-button");
        other2.getStyleClass().add("bar-button");
    }

    private void applyFilter(DataLoader loader) {
        Task<List<Reservation>> applyTask=new Task<>() {
            @Override
            protected List<Reservation> call() {
                return loader.load();
            }
        };

        applyTask.setOnSucceeded(event-> {
            if (!applyTask.getValue().isEmpty()) {
                refreshListView(applyTask.getValue());
            }
            else {
                requestDetailContainer.getItems().clear();
            }
        });
        applyTask.setOnFailed(event -> {
            System.err.println("fail to load reservation list: " + applyTask.getException());
        });
        new Thread(applyTask).start();
    }

    @FunctionalInterface
    interface DataLoader {
        List<Reservation> load();
    }
}
