package com.mycompany.prodtool3;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ImageResizer {

    public Image resizeImage(String imagePath, double width, double height) {
        Image originalImage = new Image(imagePath);
        ImageView imageView = new ImageView(originalImage);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(true);

        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);

        WritableImage resizedImage = new WritableImage((int) width, (int) height);
        imageView.snapshot(params, resizedImage);

        return resizedImage;
    }
}
