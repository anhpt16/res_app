package com.anhpt.res_app.common.utils;

import com.anhpt.res_app.common.exception.FileDeleteException;
import com.anhpt.res_app.common.exception.FileInvalidException;
import com.anhpt.res_app.common.exception.FileUploadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.info.MultimediaInfo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Slf4j
@Component
public class FileMeta {

    // Lấy kích thước ảnh
    public Dimension getImageDimension(MultipartFile file) {
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image != null) {
                int width = image.getWidth();
                int height = image.getHeight();
                return new Dimension(width, height);
            } else {
                log.warn("Ảnh không hợp lệ: {}", file.getOriginalFilename());
                throw new FileInvalidException("Ảnh không hợp lệ");
            }
        } catch (IOException e) {
            log.warn("Không thể đọc ảnh {}: {}", file.getOriginalFilename(), e.getMessage());
            throw new FileInvalidException("Không thể đọc ảnh");
        }
    }

    // TODO: Hàm lấy thời lượng Video

    // Lấy phần mở rộng của tệp
    public String getExtension(String filename) {
        if (filename == null) return "";
        int dot = filename.lastIndexOf('.');
        return (dot == -1) ? "" : filename.substring(dot + 1).toLowerCase();
    }

    // Xóa tệp vật lý
    public void deletePhysicalFile(String fileName, String uploadDir) {
        if (fileName == null || fileName.trim().isEmpty()) {
            log.error("Tên file không hợp lệ");
            throw new FileDeleteException("Lỗi khi xóa file");
        }

        File file = new File(uploadDir, fileName);
        if (!file.exists()) {
            log.error("File không tồn tại: {}", file.getAbsolutePath());
            throw new FileDeleteException("Lỗi khi xóa file");
        }

        try {
            if (!file.delete()) {
                log.error("Không thể xóa file: {}", file.getAbsolutePath());
                throw new FileDeleteException("Không thể xóa file vật lý");
            }
            log.info("Đã xóa file thành công: {}", file.getAbsolutePath());
        } catch (SecurityException e) {
            log.error("Lỗi quyền truy cập khi xóa file: {}", e.getMessage());
            throw new FileDeleteException("Không có quyền xóa file");
        } catch (Exception e) {
            log.error("Lỗi không xác định khi xóa file: {}", e.getMessage());
            throw new FileDeleteException("Lỗi khi xóa file");
        }
    }
}
