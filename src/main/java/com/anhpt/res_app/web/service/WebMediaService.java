package com.anhpt.res_app.web.service;

import com.anhpt.res_app.common.entity.Media;
import com.anhpt.res_app.common.exception.MediaNotFoundException;
import com.anhpt.res_app.common.repository.MediaRepository;
import com.anhpt.res_app.common.utils.FileMeta;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebMediaService {
    @Value("${file.upload-dir}")
    private String uploadDir;

    private final MediaRepository mediaRepository;

    public ResponseEntity<Resource> loadMedia(String fileName) {
        Optional<Media> optionalMedia = mediaRepository.findByFileName(fileName);

        // Kiểm tra (fileName) tồn tại trong database
        if (optionalMedia.isEmpty()) {
            log.warn("Không tìm thấy file trong Database: {}", fileName);
            throw new MediaNotFoundException("Không tìm thấy file");
        }

        // Kiểm tra (fileName) tồn tại trong thư mục upload
        File file = new File(uploadDir, fileName);
        if (!file.exists() || !file.isFile()) {
            log.warn("Không tìm thấy file trong thư mục Upload: {}", fileName);
            throw new MediaNotFoundException("Không tìm thấy file");
        }

        try {
            Resource resource = new UrlResource(file.toURI());

            if (resource.isReadable()) {
                return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(optionalMedia.get().getMimeType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + optionalMedia.get().getFileName() + "\"")
                    .header(HttpHeaders.CACHE_CONTROL, "max-age=86400") // Cache for 1 year
                    .body(resource);
            } else {
                log.warn("Không thể đọc file: {}", optionalMedia.get().getFileName());
                return ResponseEntity.badRequest().build();
            }
        } catch (IOException ex) {
            log.error("Lỗi xảy ra trong quá trình đọc file: {}", optionalMedia.get().getFileName(), ex);
            return ResponseEntity.badRequest().build();
        }
    }
}
