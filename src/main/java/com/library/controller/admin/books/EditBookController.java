package com.library.controller.admin.books;

import com.library.dao.DocumentDao;
import com.library.models.Document;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import static com.library.utils.LoadView.showAlert;

public class EditBookController {
    @FXML
    private TextField copiesQuantityField;

    @FXML
    private TextField locationField;

    private DocumentDao documentDao = DocumentDao.getInstance();
    private Document document;

    public void setDocument(Document document) {
        this.document = document;
    }
    @FXML
    private void editBook(ActionEvent event) {
        String location = document.getLocation();
        if (!locationField.getText().isEmpty() && !locationField.getText().equals(location)) {
            location = locationField.getText();
        }
        int newCopies;
        if(!copiesQuantityField.getText().isEmpty()) {
            newCopies = Integer.parseInt(copiesQuantityField.getText());
        } else {
            newCopies = 0;
        }

        if (newCopies <=0) {
            showAlert(Alert.AlertType.ERROR, "Validation number of copies", "please enter a valid number");
            return;
        }

        //confirm information of book before adding
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Edit Information");
        confirmationAlert.setHeaderText("Confirm Book Details");
        confirmationAlert.setContentText("Are you sure you want to change book location: \n\n" +
                "Location: " + location + "\n" +
                "New copies: " + newCopies);
        String finalLocation = location;
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                documentDao.editBook(document.getISBN(), finalLocation, newCopies);
            }
        });
    }
}
