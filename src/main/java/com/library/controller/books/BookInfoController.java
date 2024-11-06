package com.library.controller.books;

import com.library.dao.BorrowingRecordDao;
import com.library.dao.DocumentDao;
import com.library.models.BorrowingRecord;
import com.library.models.Document;
import com.library.utils.SceneSwitcher;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BookInfoController implements Initializable {

    @FXML
    private HBox aboutContainer;

    @FXML
    private Label addBookButton;

    @FXML
    private ListView<Document> bookDetailContainer;

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

    private Scene lentBookInfoScene;
    private Scene returnBookInfoScene;
    private Scene requestBookInfoScene;
    private Scene overdueBookInfoScene;

    private DocumentDao documentDao=new DocumentDao();
    private BorrowingRecordDao borrowingRecordDao=new BorrowingRecordDao();
    List<Document> documentList=new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        documentList=getDocumentList();
        bookDetailContainer.setCellFactory(param ->
        {
            BookInfoCell bookInfoCell=new BookInfoCell();
            bookInfoCell.setListView(bookDetailContainer);
            return bookInfoCell;
        });
        bookDetailContainer.getItems().setAll(documentList);

        lentNav.setOnMouseClicked(event ->
        {
            System.out.println("lentNav clicked");
            navigateToLentBookInfo();
        });
        overdueNav.setOnMouseClicked(event -> navigateToOverdueBookInfo());
        requestNav.setOnMouseClicked(event -> navigateToRequestBookInfo());
        returnNav.setOnMouseClicked(event -> navigateToReturnBookInfo());
    }

    private List<Document> getDocumentList() {
        return documentDao.getAll();
    }

    //get list of lent documents and navigate to matching scene
    private List<Document> getLentDocumentList() throws SQLException {
        List<Document> lentList=new ArrayList<>();
        List<BorrowingRecord> lentRecord= borrowingRecordDao.getLent();
        for (BorrowingRecord i: lentRecord) {
            Document document=documentDao.get(i.getISBN());
            lentList.add(document);
        }
        return lentList;
    }

//    private void loadLentBookInfoSceneAsync() {
//        new Thread(() -> {
//            try {
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LentBook.fxml"));
//                Parent bookInfoRoot = loader.load();
//                lentBookInfoScene = new Scene(bookInfoRoot);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }).start();
//    }
//
//    private void loadReturnBookInfoSceneAsync() {
//        new Thread(() -> {
//            try {
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ReturnedBook.fxml"));
//                Parent bookInfoRoot = loader.load();
//                returnBookInfoScene = new Scene(bookInfoRoot);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }).start();
//    }
//
//    private void loadOverdueBookInfoSceneAsync() {
//        new Thread(() -> {
//            try {
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/OverdueBook.fxml"));
//                Parent bookInfoRoot = loader.load();
//                overdueBookInfoScene = new Scene(bookInfoRoot);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }).start();
//    }
//
//    private void loadRequestBookInfoSceneAsync() {
//        new Thread(() -> {
//            try {
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RequestBook.fxml"));
//                Parent bookInfoRoot = loader.load();
//                requestBookInfoScene = new Scene(bookInfoRoot);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }).start();
//    }
//
    @FXML
    public void navigateToLentBookInfo() {
        SceneSwitcher.loadSceneAsync("/fxml/Admin/Books/LentBook.fxml").thenAccept(scene -> {
            if (scene != null) {
                Platform.runLater(() -> {
                    Stage stage = (Stage) lentNav.getScene().getWindow();
                    SceneSwitcher.switchScene(stage, scene);
                });
            } else {
                System.out.println("Failed to load LentBook scene.");
            }
        });
        // Delay full-screen mode to improve performance
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
    public void navigateToRequestBookInfo() {
        SceneSwitcher.loadSceneAsync("/fxml/Admin/Books/RequestBook.fxml").thenAccept(scene -> {
            if (scene != null) {
                Platform.runLater(() -> {
                    Stage stage = (Stage) requestNav.getScene().getWindow();
                    SceneSwitcher.switchScene(stage, scene);
                });
            } else {
                System.out.println("Failed to load LentBook scene.");
            }
        });
    }

}
