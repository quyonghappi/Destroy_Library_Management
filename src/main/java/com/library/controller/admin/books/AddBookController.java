package com.library.controller.admin.books;

import com.library.api.GoogleBooksAPIClient;
import com.library.dao.DocumentDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import static com.library.controller.start.LoadView.showAlert;

public class AddBookController {

    @FXML
    private TextField bookQuantityField;

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;

    @FXML
    private TextField isbnField;

    @FXML
    private void addBook(ActionEvent event) {
        String isbn = isbnField.getText();
        String bookQuantity = bookQuantityField.getText();
        if (isbn.isEmpty() || bookQuantity.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill in all fields.");
            return;
        }

        //handle input exception when quantity is <= 0
        int quantity;
        try {
            quantity = Integer.parseInt(bookQuantity);

            //isbn 13
            if (isbn.length() != 13) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "please enter a valid ISBN number");
                return;
            }

            if (!DocumentDao.searchByIsbn(isbn).isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Book already exists.");
                return;
            }
            if (quantity <= 0) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Quantity must be a positive number.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Quantity must be a valid number.");
            return;
        }

        //confirm information of book before adding
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Add Book");
        confirmationAlert.setHeaderText("Confirm Book Details");
        confirmationAlert.setContentText("Are you sure you want to add the book: \n\n" +
                "ISBN: " + isbn + "\n" +
                "Quantity: " + quantity);
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                GoogleBooksAPIClient client = new GoogleBooksAPIClient();
                try {
                    // document not exists
                    if (!client.isBookAvailable(isbn)) {
                        showAlert(Alert.AlertType.ERROR, "Validation Error", "Book is not available");
                        return;
                    }
                    client.getBookData(isbn, quantity);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Book added successfully.");
                    BookInfoCellController.updateBookCount();

                    //clear input
                    isbnField.clear();
                    bookQuantityField.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to add book. Please try again.");
                }
            }
        });
    }
}
