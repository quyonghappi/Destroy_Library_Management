package com.library.controller.dashboard;

import com.library.dao.BorrowingRecordDao;
import com.library.dao.UserDao;
import com.library.models.BorrowingRecord;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class LendBookController {

    @FXML
    private TextField bookDetailsField;

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;

    @FXML
    private TextField usernameField;

    private UserDao userDao=new UserDao();
    private BorrowingRecordDao borrowingRecordDao=new BorrowingRecordDao();

    @FXML
    private void addLentBook(ActionEvent event) {
        String username=usernameField.getText();
        String[] isbn=bookDetailsField.getText().split(",");
        for (int i=0;i<isbn.length;i++) {
            try {
                String userId = String.valueOf(userDao.findUserByName(username).getUserId());
                LocalDateTime borrowDate=LocalDateTime.now();
                borrowingRecordDao.add(new BorrowingRecord(userId,isbn[i].trim(),borrowDate, "borrowed"));
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("cannot find user "+username);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
