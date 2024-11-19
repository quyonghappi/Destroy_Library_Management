package com.library.UserDashboard.Controller;

import javafx.fxml.FXML;

//package com.library.UserDashboard.Controller;
//
//import com.library.UserDashboard.model.Book;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.fxml.Initializable;
//import javafx.scene.control.Label;
//import javafx.scene.layout.HBox;
//
//import javax.smartcardio.Card;
//import java.io.IOException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.ResourceBundle;
//
//public class UserDashboardController { // implements Initializable {
//    @FXML
//    private HBox cardLayout;
//    private List<Book> continueReading;
//
//    @FXML
//    private Label iconHome;
//
//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//        continueReading = new ArrayList<>(continueReading());
//
//        try {
//            for (int i = 0; i < continueReading.size(); i++) {
//                FXMLLoader fxmlLoader = new FXMLLoader();
//                fxmlLoader.setLocation(getClass().getResource("/fxml/card.fxml"));
//
//                HBox cardBox = fxmlLoader.load();
//                CardController cardController = fxmlLoader.getController();
//                cardController.setData(continueReading.get(i));
//                cardLayout.getChildren().add(cardBox);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private List<Book> continueReading(){
//        List<Book> ls = new ArrayList<>();
//        Book book = new Book();
//
//        //Adventures of Tom Sawyer
//        book.setName("Adventures of\nTom Sawyer");
//        book.setAuthor("Mark Twain");
//        book.setImageSrc("/images/Adventure_of_Tom_Sawyer_cover.png");
//        ls.add(book);
//
//        //The old man and the sea
//        book = new Book();
//        book.setName("The Old man\nand the Sea");
//        book.setAuthor("Ernest Hemingway");
//        book.setImageSrc("/images/The_Old_Man_and_Sea_cover.png");
//        ls.add(book);
//
//        // Sherlock Holmes;
//        book = new Book();
//        book.setName("The rivals of\nSherlock Holmes");
//        book.setAuthor("Arthur Conan Doyle");
//        book.setImageSrc("/images/the_rivals_of_Sherlock_Holmes_cover.png");
//        ls.add(book);
//
//        // The time machine
//        book = new Book();
//        book.setName("The Time Machine");
//        book.setAuthor("H. G. Wells");
//        book.setImageSrc("/images/the_time_machine_cover.png");
//        ls.add(book);
//
//
//        return ls;
//    }
//}

//package com.library.UserDashboard.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class UserDashboardController {
        @FXML
        public void initialize() {
            // Initialize dashboard content here

        }
}
