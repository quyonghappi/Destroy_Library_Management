//package com.library.UserDashboard.Controller;
//
//import com.library.utils.SceneSwitcher;
//import javafx.scene.Node;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//
//import java.util.concurrent.CompletableFuture;
//
//public abstract class BaseController {
//
//    // Chuyển đổi đến bất kỳ màn hình nào
//    protected void switchTo(String fxmlPath, Node node) {
//        CompletableFuture<Scene> sceneFuture = SceneSwitcher.loadSceneAsync(fxmlPath);
//        sceneFuture.thenAccept(scene -> {
//            // Lấy Stage từ Node hiện tại
//            Stage currentStage = (Stage) node.getScene().getWindow();
//            SceneSwitcher.switchScene(currentStage, scene);
//        });
//    }
//}
