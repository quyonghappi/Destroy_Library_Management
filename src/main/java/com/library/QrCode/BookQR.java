package com.library.QrCode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BookQR {
    private static final int QR_WIDTH = 200;
    private static final int QR_HEIGHT = 200;

    // Tạo QR code từ text
    public static BufferedImage generateQRCodeImage(String text) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    // Tạo ImageView từ QR code
    public static ImageView createQRCode(String text) {
        try {
            BufferedImage bufferedImage = generateQRCodeImage(text);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "PNG", outputStream);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            javafx.scene.image.Image fxImage = new javafx.scene.image.Image(inputStream);

            ImageView imageView = new ImageView(fxImage);
            imageView.setFitWidth(QR_WIDTH);
            imageView.setFitHeight(QR_HEIGHT);
            return imageView;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
