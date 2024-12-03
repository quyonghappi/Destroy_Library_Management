package com.library.controller.admin.books;
import com.library.controller.admin.dashboard.AdminDashboardController;
import com.library.controller.admin.members.MemInfoController;
import com.library.dao.DocumentDao;
import com.library.models.Document;
import com.library.utils.FilterPopup;
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
import java.util.List;
import java.util.ResourceBundle;

//import static com.library.utils.FilterPopup.showPopup;
import static com.library.controller.start.LoadView.loadView;
import static com.library.controller.start.ShowView.showView;
import static com.library.utils.FilterPopup.*;
import static com.library.utils.SceneSwitcher.*;

public class BookInfoController implements Initializable {

    @FXML
    StackPane bookInfoRoot;

    @FXML
    private HBox aboutContainer;

    @FXML
    private Label addBookButton;

    @FXML
    private ListView<Document> bookDetailContainer;

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
    private ImageView filter;

    @FXML
    private ToggleButton allButton;

    @FXML
    private ToggleButton availableButton;

    @FXML
    private ToggleButton lostButton;

    @FXML
    private TextField searchField1;

    @FXML
    private Button logOut;

    private DocumentDao documentDao=new DocumentDao();
//    List<Document> documentList=new ArrayList<>();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        loadBookList();
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

        lendButton.setOnAction(event -> showLendBookScene(bookInfoRoot));
        filter.setOnMouseClicked(event->showPopup(filter, event));
        addBookButton.setOnMouseClicked(event->showAddBookScene(bookInfoRoot));

        FilterPopup.setFilterSelectionListener(selectedFilter -> {
            SearchBookInfo.handleSearch(bookDetailContainer, searchField1.getText(), FilterPopup.getSelectedItem());
            System.out.println( FilterPopup.getSelectedItem());
        });

        searchField1.textProperty().addListener((observable, oldValue, newValue) -> {
            SearchBookInfo.handleSearch(bookDetailContainer, newValue, FilterPopup.getSelectedItem());
        });

    }

    private void loadBookList() {
        Task<List<Document>> loadTask = new Task<>() {
            @Override
            protected List<Document> call() {
                return documentDao.getAll();
            }
        };

        loadTask.setOnSucceeded(event -> {
            SearchBookInfo.refreshListView(bookDetailContainer, loadTask.getValue());
        });
        loadTask.setOnFailed(event -> {
            System.out.println("fail to load book info" + loadTask.getException());
        });
        new Thread(loadTask).start();
    }

    private void applyFilter(DataLoader loader) {
        Task<List<Document>> applyTask = new Task<>() {
            @Override
            protected List<Document> call() {
                return loader.load();
            }
        };
        applyTask.setOnSucceeded(event -> {
            List<Document> filteredDoc = applyTask.getValue();
            if (filteredDoc.isEmpty()) {
                bookDetailContainer.getItems().clear();
            } else {
                SearchBookInfo.refreshListView(bookDetailContainer, filteredDoc);
            }
        });
        applyTask.setOnFailed(event -> {
            System.out.println("fail to load book info" + applyTask.getException());
        });
        new Thread(applyTask).start();
    }

    @FXML
    private void allFilter(ActionEvent event) {
        updateButtonStyles(allButton,availableButton,lostButton);
        applyFilter(()-> documentDao.getAll());
    }

    @FXML
    private void availableFilter(ActionEvent e) {
        updateButtonStyles(availableButton,allButton,lostButton);
        applyFilter(()->documentDao.getAvailableList());
    }

    @FXML
    private void lostFilter(ActionEvent e) {
        updateButtonStyles(lostButton,allButton,availableButton);
        applyFilter(()->documentDao.getLostList());
    }

    private void updateButtonStyles(ToggleButton selectedButton, ToggleButton other1, ToggleButton other2) {
        selectedButton.getStyleClass().removeAll("chosen-button", "bar-button");
        other1.getStyleClass().removeAll("chosen-button", "bar-button");
        other2.getStyleClass().removeAll("chosen-button", "bar-button");

        selectedButton.getStyleClass().add("chosen-button");
        other1.getStyleClass().add("bar-button");
        other2.getStyleClass().add("bar-button");
    }

    public void setLogout(MouseEvent mouseEvent) {
        logOut.setMouseTransparent(true);
        Stage stage = (Stage) logOut.getScene().getWindow();
        loadView(stage, "/fxml/Start/Role.fxml", "Sign Up", "/css/start/Role.css");
        showView(stage, "/fxml/Start/Role.fxml", "Login", "/css/start/Role.css");
    }

    @FunctionalInterface
    interface DataLoader {
        List<Document> load();
    }

    public void setUserFullName(String userFullName) {
        memNameLabel.setText(userFullName);
    }

    public TextField getSearchField1() {
        return searchField1;
    }

    public void setSearchField1(TextField searchField1) {
        this.searchField1 = searchField1;
    }

}
