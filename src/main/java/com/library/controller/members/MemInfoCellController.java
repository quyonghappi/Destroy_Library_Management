package com.library.controller.members;

import com.library.dao.FineDao;
import com.library.dao.UserDao;
import com.library.models.Fine;
import com.library.models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;

import java.math.BigDecimal;
import java.util.List;

public class MemInfoCellController {
    @FXML
    private ImageView userImage;

    @FXML
    private ImageView editImage;

    @FXML
    private ImageView deleteImage;

    @FXML
    private Label emailLabel;

    @FXML
    private Label fineLabel;

    @FXML
    private Label fullnameLabel;

    @FXML
    private Label idLabel;

    @FXML
    private Label lastLoginLabel;

    private ListView<User> listView;
    private User currentUser;
    private UserDao userDao = new UserDao();
    private FineDao fineDao=new FineDao();

    public void initialize() {
        editImage.setOnMouseClicked(event->handleEditButtonAction());
        deleteImage.setOnMouseClicked(event->handleDeleteButtonAction());
    }
    public void setListView(ListView<User> listView) {
        this.listView = listView;
    }

    public void loadUser(User user) {
        currentUser = user;
        fullnameLabel.setText(currentUser.getFullName());
        emailLabel.setText(currentUser.getEmail());
        idLabel.setText("#"+currentUser.getUserId());
        lastLoginLabel.setText(String.valueOf(currentUser.getLastLoginDate()));
        List<Fine> fineList=fineDao.getFinesByUserId(user.getUserId());
        BigDecimal amount=new BigDecimal(0);
        for (Fine fine:fineList) {
            amount = amount.add(fine.getFineAmount());
        }
        fineLabel.setText(String.valueOf(amount));


    }

    @FXML
    private void handleDeleteButtonAction() {
        try {
            userDao.delete(userDao.get(idLabel.getText()));
            listView.getItems().remove(currentUser);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleEditButtonAction() {
//        try {
//            documentDao.delete(documentDao.findByISBN(isbnLabel.getText());
//            BorrowingRecordDao borrowingRecordDao=new BorrowingRecordDao();
//            BorrowingRecord borrowingRecord=new BorrowingRecord(useridLabel.getText(), isbnLabel.getText(),now(),"borrowed");
//            borrowingRecordDao.add(borrowingRecord);
//            //remove current request from listview
//            lv.getItems().remove(current);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }

    }

}
