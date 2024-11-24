package com.library.utils;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Map;

public class LoadImage {
    private static final Map<String, Image> imageCache = new HashMap<String, Image>();

    public static void loadImageLazy(String imageUrl, ImageView imageView, double w, double h) {
        imageView.setPreserveRatio(false);
        imageView.setSmooth(true);

        //kiểm tra cache
        if (imageCache.containsKey(imageUrl)) {
            imageView.setImage(imageCache.get(imageUrl));
            return;
        }

        Task<Image> loadImageTask = new Task<>() {
            @Override
            protected Image call() throws Exception {
                //Tải ảnh ở kích thước lớn hơn (2x hoặc 3x kích thước mong muốn)
                // //then giảm kích thước trong ImageView giúp ảnh trông sắc nét hơn
                double scaledWidth = w * 2;
                double scaledHeight = h * 2;
                return new Image(imageUrl, scaledWidth, scaledHeight, false, true); // smooth = true
            }
        };

        loadImageTask.setOnSucceeded(event -> {
            Image image = loadImageTask.getValue();
            imageCache.put(imageUrl, image); // Cache ảnh
            Platform.runLater(() -> {
                imageView.setImage(image);
                imageView.setFitWidth(w); // Giảm kích thước trong ImageView
                imageView.setFitHeight(h);
            });
        });

        loadImageTask.setOnFailed(event -> {
            System.err.println("Failed to load image from URL: " + imageUrl);
            Platform.runLater(() -> imageView.setImage(new Image("file:placeholder.png")));
        });

        new Thread(loadImageTask).start();
    }


}
