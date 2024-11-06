package com.library.utils;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Map;

public class LoadImage {
    private static final Map<String, Image> imageCache = new HashMap<String, Image>();
    public static void loadImageLazy(String imageUrl, ImageView imageView) {
        imageView.setFitWidth(50);
        imageView.setFitHeight(60);
        imageView.setPreserveRatio(false); //disable aspect ratio to fill exact size
        //check if image is already cached
        if (imageCache.containsKey(imageUrl)) {
            imageView.setImage(imageCache.get(imageUrl));
            return;
        }
        Task<Image> loadImageTask = new Task<>() {
            @Override
            protected Image call() throws Exception {
                return new Image(imageUrl, 50, 60, false, true);
            }
        };
        //preserveRatio = false, smooth = true

        loadImageTask.setOnSucceeded(event -> {
            Image image = loadImageTask.getValue();
            imageCache.put(imageUrl, image);  //cache the image
            Platform.runLater(() -> imageView.setImage(image));
        });

        loadImageTask.setOnFailed(event -> {
            System.out.println("Failed to load image from URL: " + imageUrl);

        });

        new Thread(loadImageTask).start();  //run the task on a background thread
    }

}
