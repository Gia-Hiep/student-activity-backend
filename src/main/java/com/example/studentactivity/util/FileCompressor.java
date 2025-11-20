package com.example.studentactivity.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class FileCompressor {

    private static final String UPLOAD_DIR = "uploads/";
    private static final float COMPRESSION_QUALITY = 0.7f; // 70% chất lượng → giảm dung lượng ~70%

    // Tạo thư mục uploads nếu chưa có
    public FileCompressor() {
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
        } catch (IOException e) {
            throw new RuntimeException("Không thể tạo thư mục uploads", e);
        }
    }

    public String saveAndCompress(MultipartFile file) {
        try {
            String originalName = file.getOriginalFilename();
            String extension = originalName != null && originalName.contains(".")
                    ? originalName.substring(originalName.lastIndexOf("."))
                    : ".jpg";

            String fileName = UUID.randomUUID() + extension;
            String filePath = UPLOAD_DIR + fileName;

            // Đọc ảnh
            BufferedImage image = ImageIO.read(file.getInputStream());

            if (image == null) {
                // Không phải ảnh → lưu nguyên bản (PDF, Word, v.v.)
                Files.copy(file.getInputStream(), Paths.get(filePath));
                return "/uploads/" + fileName;
            }

            // Nén ảnh
            File compressedFile = new File(filePath);
            try (FileOutputStream fos = new FileOutputStream(compressedFile);
                 ImageOutputStream ios = ImageIO.createImageOutputStream(fos)) {

                ImageWriter writer = ImageIO.getImageWritersByFormatName(extension.substring(1)).next();
                writer.setOutput(ios);

                ImageWriteParam param = writer.getDefaultWriteParam();
                if (param.canWriteCompressed()) {
                    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                    param.setCompressionQuality(COMPRESSION_QUALITY);
                }

                writer.write(null, new IIOImage(image, null, null), param);
                writer.dispose();
            }

            return "/uploads/" + fileName;

        } catch (Exception e) {
            throw new RuntimeException("Lỗi lưu và nén file: " + e.getMessage(), e);
        }
    }
}