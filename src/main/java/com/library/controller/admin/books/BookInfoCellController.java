package com.library.controller.admin.books;

import com.library.dao.DocumentDao;
import com.library.dao.ReservationDao;
import com.library.models.Author;
import com.library.models.Document;
import com.library.models.Publisher;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static com.library.utils.LoadImage.loadImageLazy;

public class BookInfoCellController extends ListCell<Document> implements Initializable {
    @FXML
    private Label authorLabel;

    @FXML
    private ImageView bookImage;

    @FXML
    private Label bookNameLabel;

    @FXML
    private Label isbnLabel;

    @FXML
    private Label availableQuantityLabel;

    @FXML
    private ImageView deleteImage;

    @FXML
    private ImageView editImage;

    @FXML
    private Label publisherLabel;

    @FXML
    private Label requestLabel;

    @FXML
    private Label locationLabel;


    private ListView<Document> listView;
    private Document currentDocument;

    private ReservationDao reservationDao=new ReservationDao();
    private DocumentDao documentDao = new DocumentDao();
    private Publisher publisher=new Publisher();
    private Author author = new Author();

//    private static final Map<String, Image> imageCache = new HashMap<String, Image>();

    public void setListView(ListView<Document> listView) {
        this.listView = listView;
    }

    public void loadDocument(Document document) {
        if (document != null) {
            currentDocument = document;
            isbnLabel.setText(document.getISBN());
            availableQuantityLabel.setText(String.valueOf(document.getQuantity()));
            bookNameLabel.setText(document.getTitle());
            locationLabel.setText(document.getLocation());
            publisher=documentDao.getPublisher(document.getPublisherId());
            author=documentDao.getAuthor(document.getAuthorId());
            if (publisher.getName() != null) {
                publisherLabel.setText(publisher.getName());
            } else publisherLabel.setText("N/A");
            authorLabel.setText(author.getName());
            //get num of request for this book
            requestLabel.setText(String.valueOf(reservationDao.getByISBN(document.getISBN())));

            // Load the image using lazy loading
            if (!document.getImageLink().equals("N/A")) {
                loadImageLazy(document.getImageLink(), bookImage, bookImage.getFitWidth(), bookImage.getFitHeight());
            } else {
                bookImage.setImage(new Image("/ui/admindashboard/bookcover.png")); // Hình ảnh mặc định khi không có bìa
            }
        }
    }


    private void handleEditButtonAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Admin/Books/EditBookInfo.fxml"));
            StackPane root = loader.load();
//            Rectangle darkBackground = new Rectangle();
//            darkBackground.setFill(Color.color(0, 0, 0, 0.7)); //black with 70% opacity
//            darkBackground.widthProperty().bind(root.widthProperty());
//            darkBackground.heightProperty().bind(root.heightProperty());

            EditBookController controller = loader.getController();
            controller.setDocument(currentDocument);
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            scene.lookup("#cancelButton").setOnMouseClicked(event -> {
                stage.close();
            });
            stage.setScene(scene);
            stage.setTitle("Edit Book");
            stage.centerOnScreen();
            //set the new stage as modal
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(editImage.getScene().getWindow()); //set owner to the parent stage

            stage.showAndWait();
        } catch (IOException e) {
            System.out.println("fxml path is not valid");
        }
    }

    private void handleDeleteButtonAction() {
        try {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirm Delete Information");
            confirmationAlert.setHeaderText("Confirm Book Details");
            confirmationAlert.setContentText("Are you sure you want to delete book: \n\n" +
                    "Title: " + bookNameLabel.getText() + "\n" +
                    "ISBN: " + isbnLabel.getText());
            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        documentDao.delete(documentDao.get(isbnLabel.getText()));
                    } catch (SQLException e) {
                        throw new RuntimeException("Can not delete book: " + isbnLabel.getText());
                    }
                    listView.getItems().remove(currentDocument);
                }
            });
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        editImage.setOnMouseClicked(mouseEvent -> handleEditButtonAction());
        deleteImage.setOnMouseClicked(mouseEvent -> handleDeleteButtonAction());
    }
}
