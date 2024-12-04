package com.library.controller.admin.books;

import com.library.controller.admin.dashboard.AdminDashboardController;
import com.library.controller.admin.dashboard.OverdueCell;
import com.library.controller.admin.members.MemInfoController;
import com.library.dao.BorrowingRecordDao;
import com.library.dao.FineDao;
import com.library.models.Fine;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

//import static com.library.utils.FilterPopup.showPopup;
import static com.library.controller.start.LoadView.loadView;
import static com.library.controller.start.ShowView.showView;
import static com.library.utils.SceneSwitcher.*;

public class OverdueBookController implements Initializable {

    @FXML
    StackPane overdueRoot;

    @FXML
    private Label addBookButton;

    @FXML
    private ListView<Fine> overdueDetailContainer;

    @FXML
    private Button lendButton;

    @FXML
    private HBox lentNav;

    @FXML
    private Label memNameLabel;

    @FXML
    private HBox membersContainer;

    @FXML
    private HBox overviewContainer;

    @FXML
    private HBox requestNav;

    @FXML
    private HBox returnNav;

    @FXML
    private HBox allNav;

    @FXML
    private ToggleButton paidButton;

    @FXML
    private ToggleButton unpaidButton;

    @FXML
    private ToggleButton allButton;

    @FXML
    private TextField searchField1;

    @FXML
    private Label countLabel;

    @FXML
    private Button logOut;

    private BorrowingRecordDao borrowingRecordDao= new BorrowingRecordDao();
    private FineDao fineDao= new FineDao();
    List<Fine> fines=new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fines = getOverdueList();
        countLabel.setText(String.valueOf(fines.size()));

        loadOverdueList();
        setupNavigation();

        lendButton.setOnMouseClicked(event -> showLendBookScene(overdueRoot));
//        filter.setOnMouseClicked(event -> showPopup(filter, event));
        addBookButton.setOnMouseClicked(event->showAddBookScene(overdueRoot));
        searchField1.textProperty().addListener((observable, oldValue, newValue) -> {
            searchOverDue(newValue);
        });
    }

    private void searchOverDue(String username) {
        if (username.isEmpty()) {
            fines = fineDao.getAll();
        } else {
            fines = fineDao.getFinesByUsername(username);
        }
        loadOverdueList();
    }

    private void loadOverdueList() {
        //use a background task for loading data
        Task<List<Fine>> loadTask = new Task<>() {
            @Override
            protected List<Fine> call() {
                return fines; // Database call
            }
        };

        //if task is success
        loadTask.setOnSucceeded(event -> {
            fines = loadTask.getValue();
            refreshListView(fines);
        });

        //if task was failed
        loadTask.setOnFailed(event -> {
            System.err.println("dailed to load overdue fines: " + loadTask.getException());
        });

        //run the task in a background thread
        new Thread(loadTask).start();
    }

    //refresh list view when toggle button was selected
    private void refreshListView(List<Fine> fines) {
        overdueDetailContainer.setCellFactory(param->
        {
            OverdueCell overdueCell = new OverdueCell();
            overdueCell.setListView(overdueDetailContainer);
            return overdueCell;
        });
        overdueDetailContainer.getItems().setAll(fines);
    }

    private void setupNavigation() {
        lentNav.setOnMouseClicked(event -> {
            String userFullName=memNameLabel.getText();
            LentBookController controller = navigateToScene("/fxml/Admin/Books/LentBook.fxml", lentNav);
            if (controller != null) {
                controller.setUserFullName(userFullName);
            }
        });
        allNav.setOnMouseClicked(event -> {
            String userFullName=memNameLabel.getText();
            BookInfoController controller = navigateToScene("/fxml/Admin/Books/BookInfo.fxml", allNav);
            if (controller != null) {
                controller.setUserFullName(userFullName);
            }
        });
        requestNav.setOnMouseClicked(event -> {
            String userFullName=memNameLabel.getText();
            RequestBookController controller=navigateToScene("/fxml/Admin/Books/RequestBook.fxml", requestNav);
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
    }

    @FXML
    private void unpaidFilter(ActionEvent event) {
        updateButtonStyles(unpaidButton,paidButton,allButton);
        applyFilter(() -> fineDao.getUnpaid());
    }

    @FXML
    private void allFilter(ActionEvent event) {
        updateButtonStyles(allButton,unpaidButton,paidButton);
        applyFilter(this::getOverdueList);
    }

    @FXML
    private void paidFilter(ActionEvent event) {
        updateButtonStyles(paidButton,allButton,unpaidButton);
        applyFilter(() -> fineDao.getPaid());
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
        Task<List<Fine>> filterTask = new Task<>() {
            @Override
            protected List<Fine> call() {
                return loader.load();
            }
        };

        filterTask.setOnSucceeded(event -> {
            List<Fine> filteredFines = filterTask.getValue();
            if (filteredFines.isEmpty()) {
                overdueDetailContainer.getItems().clear();
            } else {
                refreshListView(filteredFines);
            }
        });
        filterTask.setOnFailed(event -> {
            System.err.println("Failed to apply filter: " + filterTask.getException());
        });

        new Thread(filterTask).start();
    }

    public void setLogOut(MouseEvent mouseEvent) {
        logOut.setMouseTransparent(true);
        Stage stage = (Stage) logOut.getScene().getWindow();
        loadView(stage, "/fxml/Start/Role.fxml", "Sign Up", "/css/start/Role.css");
        showView(stage, "/fxml/Start/Role.fxml", "Login", "/css/start/Role.css");
    }

    @FunctionalInterface
    interface DataLoader {
        List<Fine> load();
    }

    private List<Fine> getOverdueList() {
        return fineDao.getAll();
    }

    public void setUserFullName(String userFullName) {
        memNameLabel.setText(userFullName);
    }
}
