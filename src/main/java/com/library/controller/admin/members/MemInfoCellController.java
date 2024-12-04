package com.library.controller.admin.members;

import com.library.dao.FineDao;
import com.library.dao.UserDao;
import com.library.models.Fine;
import com.library.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

public class MemInfoCellController {
    @FXML
    private ImageView userImage;

    @FXML
    private ImageView editImage;

    @FXML
    private ImageView lockImage;

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
        lockImage.setOnMouseClicked(event->handleLockButtonAction());
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
        DecimalFormat format = new DecimalFormat("#,###");
        fineLabel.setText(format.format(amount));


    }

    @FXML
    private void handleLockButtonAction() {
        try {
            String Id = idLabel.getText().substring(1);

            User user = UserDao.findUserById(Id);
            if(user.getAccountStatus().equals("active")) {
                user.setAccountStatus("suspended");

                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Confirm Lock Member");
                confirmationAlert.setHeaderText("Confirm Member Details");
                confirmationAlert.setContentText("Are you sure you want to Lock Member: \n\n" +
                        "UserName: " + fullnameLabel.getText() + "\n" +
                        "Id: " + idLabel.getText());
                confirmationAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        UserDao.updateAccount(user);
                    }
                });
            } else {
                user.setAccountStatus("active");
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Confirm Open Member");
                confirmationAlert.setHeaderText("Confirm Member Details");
                confirmationAlert.setContentText("Are you sure you want to open Member: \n\n" +
                        "UserName: " + fullnameLabel.getText() + "\n" +
                        "Id: " + idLabel.getText());
                confirmationAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        UserDao.updateAccount(user);
                    }
                });
            }







        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleEditButtonAction() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Admin/Member/EditMemInfo.fxml"));
            StackPane root = loader.load();

            EditMemController controller = loader.getController();
            controller.setUser(currentUser);

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            scene.lookup("#cancelButton").setOnMouseClicked(event -> {
                stage.close();
            });
            stage.setScene(scene);
            stage.setTitle("Edit Member");
            stage.centerOnScreen();
            //set the new stage as modal
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(editImage.getScene().getWindow()); //set owner to the parent stage

            stage.showAndWait();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
