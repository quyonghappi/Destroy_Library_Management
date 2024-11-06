package com.library.controller.dashboard;

import com.library.dao.BorrowingRecordDao;
import com.library.dao.FineDao;
import com.library.dao.ReservationDao;
import com.library.dao.UserDao;
import com.library.models.Fine;
import com.library.models.Reservation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
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

//    private Parent root;
//    private Scene scene;
//    private Stage stage;

    private Scene bookInfoScene;

    private BorrowingRecordDao borrowingRecordDao=new BorrowingRecordDao();
    private UserDao userDa0=new UserDao();
    private FineDao fineDao = new FineDao();
    private ReservationDao reservationDao=new ReservationDao();
    private List<Fine> fineList;
    private List<Reservation> reservationList;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadBookInfoSceneAsync();
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

        //add event on booksContainer
        booksContainer.setOnMouseClicked(event -> navigateToBookInfo());
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

//    @FXML
//    public void navigateToBookInfo() throws IOException {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/BookInfo.fxml"));
//        root = loader.load();
//        BookInfoController controller = loader.getController();
//        stage=(Stage)booksContainer.getScene().getWindow();
//        scene=new Scene(root);
//        stage.setScene(scene);
//        //stage.setMaximized(true);
//        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
//        stage.setWidth(screenBounds.getWidth());
//        stage.setHeight(screenBounds.getHeight());
//        stage.centerOnScreen();
////        Platform.runLater(() -> {
////            stage.setMaximized(true);
////            stage.centerOnScreen();
////        });
//        stage.setFullScreen(true);
//        stage.show();
//    }

    private void loadBookInfoSceneAsync() {
        new Thread(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Admin/Books/BookInfo.fxml"));
                Parent bookInfoRoot = loader.load();
                bookInfoScene = new Scene(bookInfoRoot);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    public void navigateToBookInfo() {
        if (bookInfoScene == null) {
            System.out.println("please wait...");
            return;
        }

        Stage stage = (Stage) booksContainer.getScene().getWindow();
        stage.setScene(bookInfoScene);
        stage.setMaximized(true);
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setWidth(screenBounds.getWidth());
        stage.setHeight(screenBounds.getHeight());
        stage.centerOnScreen();
        // Delay full-screen mode to improve performance
        //Platform.runLater(() -> stage.setFullScreen(true));
    }
}
