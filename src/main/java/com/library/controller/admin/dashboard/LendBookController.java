package com.library.controller.admin.dashboard;

import com.library.dao.BorrowingRecordDao;
import com.library.dao.DocumentDao;
import com.library.dao.UserDao;
import com.library.models.BorrowingRecord;
import com.library.models.Document;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;

import static com.library.utils.LoadView.showAlert;

public class LendBookController {

    @FXML
    private TextField bookDetailsField;

    @FXML
    private TextField usernameField;

    private BorrowingRecordDao borrowingRecordDao=new BorrowingRecordDao();

    @FXML
    private void addLentBook(ActionEvent event) {
        String username=usernameField.getText();
        String bookId = bookDetailsField.getText();

        if (username.isEmpty() || bookId.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill in all fields.");
            return;
        }

        try {

            if (UserDao.findUserByName(username) == null) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "User not found: " + username);
                return;
            }

            Document doc = DocumentDao.findByIsbn(bookId);
            if (doc == null) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Book not found: " + bookId);
                return;
            }

            if (doc.getQuantity() == 0) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Book is empty");
                return;
            }

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirm Lent Book");
            confirmationAlert.setHeaderText("Confirm Book Details");
            confirmationAlert.setContentText("Are you sure you want to lent the book: \n\n" +
                    "User Name: " + username + "\n" +
                    "BookDetails: " + bookId);
            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        String userId = String.valueOf(UserDao.findUserByName(username).getUserId());
                        LocalDateTime borrowDate=LocalDateTime.now();
                        borrowingRecordDao.add(new BorrowingRecord(userId,bookId.trim(),borrowDate, "borrowed"));
                        showAlert(Alert.AlertType.INFORMATION, "Lent Book", "Lent Book Successfully");
                        usernameField.clear();
                        bookDetailsField.clear();
                    } catch (Exception e) {
                        e.printStackTrace();
                        showAlert(Alert.AlertType.ERROR, "Error", "Failed to add book. Please try again.");
                    }
                }
            });

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
