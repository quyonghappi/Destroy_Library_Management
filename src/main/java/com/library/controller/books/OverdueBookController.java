package com.library.controller.books;

import com.library.controller.dashboard.AdminDashboardController;
import com.library.controller.dashboard.OverdueCell;
import com.library.controller.members.MemInfoController;
import com.library.dao.BorrowingRecordDao;
import com.library.dao.FineDao;
import com.library.models.Fine;
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
    private TextField searchField;

    @FXML
    private TextField searchField1;

    private BorrowingRecordDao borrowingRecordDao= new BorrowingRecordDao();
    private FineDao fineDao= new FineDao();
    List<Fine> fines=new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fines=getOverdueList();
        overdueDetailContainer.setCellFactory(param->
        {
            OverdueCell overdueCell = new OverdueCell();
            overdueCell.setListView(overdueDetailContainer);
            return overdueCell;
        });
        overdueDetailContainer.getItems().setAll(fines);

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
            AdminDashboardController controller= navigateToScene("/fxml/Admin//BookInfo.fxml", membersContainer);
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
        lendButton.setOnMouseClicked(event->showLendBookScene(overdueRoot));
    }

    private List<Fine> getOverdueList() {
        return fineDao.getAll();
    }

    public void setUserFullName(String userFullName) {
        memNameLabel.setText(userFullName);
    }
}

