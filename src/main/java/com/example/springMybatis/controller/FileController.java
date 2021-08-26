package com.example.springMybatis.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Hashtable;

@Controller
@RequestMapping("/file")
public class FileController {

    @RequestMapping("/qrcode")
    @ResponseBody
    @CrossOrigin("*")
    public String getQrcode(@RequestParam("data")String data) throws Exception {
        BufferedImage image = generateQRCodeImage(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] bytes = baos.toByteArray();


        return Base64.getEncoder().encodeToString(bytes);
    }

    public static BufferedImage generateQRCodeImage(String barcodeText) throws Exception {

        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix =
                barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 200, 200,hints);

        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
}
