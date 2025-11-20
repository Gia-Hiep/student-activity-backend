package com.example.studentactivity.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class FileUploadUtil {

    @Value("${file.upload-dir:./uploads}")
    private String uploadDir;

    // Đảm bảo thư mục tồn tại
    private void ensureDirectoryExists() {
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * Lưu file + tự động nén nếu là ảnh (JPEG/PNG/WEBP)
     * Trả về URL để lưu vào DB
     */
    public String saveFile(MultipartFile file) throws IOException {
        ensureDirectoryExists();

        String originalName = file.getOriginalFilename();
        String extension = "";
        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf(".")).toLowerCase();
        }

        String fileName = UUID.randomUUID() + extension;
        Path filePath = Paths.get(uploadDir, fileName);

        if (isImage(file)) {
            // Nén ảnh chất lượng cao (70-80%)
            byte[] compressed = compressImage(file, 0.8f);
            Files.write(filePath, compressed);
        } else {
            // File khác (PDF, DOC...) → lưu nguyên bản
            Files.write(filePath, file.getBytes());
        }

        // Trả về URL truy cập (dùng với static resource)
        return "/uploads/" + fileName;
    }

    private boolean isImage(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    private byte[] compressImage(MultipartFile file, float quality) throws IOException {
        BufferedImage image = ImageIO.read(file.getInputStream());

        // Resize nếu quá lớn (> 1920px)
        if (image.getWidth() > 1920) {
            double scale = 1920.0 / image.getWidth();
            int newWidth = 1920;
            int newHeight = (int) (image.getHeight() * scale);
            java.awt.Image scaled = image.getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH);
            image = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            image.getGraphics().drawImage(scaled, 0, 0, null);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageOutputStream ios = ImageIO.createImageOutputStream(baos);

        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(quality); // 0.8 = 80% chất lượng

        writer.setOutput(ios);
        writer.write(null, new IIOImage(image, null, null), param);

        writer.dispose();
        ios.close();
        return baos.toByteArray();
    }
}