package com.anhpt.res_app.admin.service;

import com.anhpt.res_app.admin.dto.AdminMediaMapper;
import com.anhpt.res_app.admin.dto.request.media.MediaSearchRequest;
import com.anhpt.res_app.admin.dto.request.media.MediaUpdateRequest;
import com.anhpt.res_app.admin.dto.request.media.MediaUploadRequest;
import com.anhpt.res_app.admin.dto.response.media.MediaResponse;
import com.anhpt.res_app.admin.dto.response.media.MediaShortResponse;
import com.anhpt.res_app.admin.filter.AdminMediaFilter;
import com.anhpt.res_app.common.dto.response.PageResponse;
import com.anhpt.res_app.common.entity.Media;
import com.anhpt.res_app.common.entity.User;
import com.anhpt.res_app.common.exception.MediaException;
import com.anhpt.res_app.common.exception.ResourceNotFoundException;
import com.anhpt.res_app.common.exception.file.FileDeleteException;
import com.anhpt.res_app.common.exception.file.FileInvalidException;
import com.anhpt.res_app.common.exception.file.FileUploadException;
import com.anhpt.res_app.common.repository.MediaRepository;
import com.anhpt.res_app.common.utils.FileMeta;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminMediaService {
    @Value("${file.upload-dir}")
    private String uploadDir;
    private final FileMeta fileMeta;
    private final MediaRepository mediaRepository;
    private final AdminMediaMapper adminMediaMapper;
    private final AdminMediaFilter adminMediaFilter;

    public MediaResponse uploadFile(MediaUploadRequest request) {
        File destFile = null;
        String extension = fileMeta.getExtension(request.getFile().getOriginalFilename());
        String fileName = UUID.randomUUID().toString().replace("-", "") + "." + extension;

        // 1. Kiểm tra thư mục
        File uploadDirFile = new File(uploadDir);
        if (uploadDir == null || uploadDir.trim().isEmpty()) {
            log.error("Thư mục lưu file không tồn tại: {}", uploadDir);
            throw new FileUploadException("Thư mục upload không tồn tại");
        }
        if (!uploadDirFile.exists()) {
            if (!uploadDirFile.mkdirs()) {
                log.error("Không thể tạo thư mục upload: {}", uploadDir);
                throw new FileUploadException("Không thể tạo thư mục upload");
            }
        }

        // 2. Lấy metadata (mime type, kích thước, thời lượng...)
        String originName = request.getName() + "." + extension;
        String mimeType = request.getFile().getContentType();
        Long fileSize = request.getFile().getSize();

        Integer width = null;
        Integer height = null;
        Long duration = null;

        try {
            if (mimeType != null && mimeType.startsWith("image/")) {
                Dimension dimension = fileMeta.getImageDimension(request.getFile());
                width = dimension.width;
                height = dimension.height;
            } else if (mimeType != null && mimeType.startsWith("video/")) {
                // TODO: Xử lý để lấy duration
            } else {
                throw new FileInvalidException("File không hợp lệ");
            }
        } catch (Exception e) {
            log.warn("Lỗi xử lý metadata file: {}", e.getMessage());
            throw new FileInvalidException("Không thể phân tích file");
        }


        // 3. Ghi file vào hệ thống
        try {
            destFile = new File(uploadDirFile, fileName);
            request.getFile().transferTo(destFile);
        } catch (IOException e) {
            log.error("Lỗi ghi file: {}", e.getMessage(), e);
            if (destFile.exists()) destFile.delete();
            throw new FileUploadException("Không thể ghi file lên hệ thống");
        }

        // 4. Lưu DB
        User user = new User();
        user.setId(1L);

        Media media = new Media();
        media.setUser(user);
        media.setFileName(fileName);
        media.setOriginName(originName);
        media.setMimeType(mimeType);
        media.setFileSize(fileSize);
        media.setWidth(width);
        media.setHeight(height);
        media.setDuration(duration);
        media.setDescription(request.getDescription());
        media.setCreatedAt(LocalDateTime.now());
        media.setUpdatedAt(LocalDateTime.now());

        try {
            media = mediaRepository.save(media);
        } catch (Exception e) {
            log.error("Lỗi lưu thông tin vào database: {}", e.getMessage(), e);
            if (destFile.exists()) destFile.delete();
            throw new FileUploadException("Lưu thông tin file thất bại");
        }

        // 5. Trả về response
        return adminMediaMapper.toMediaResponse(media);
    }

    public MediaResponse getMediaById(Long id) {
        // Kiểm tra mediaId tồn tại
        // TODO: Kiểm tra (userId-mediaId) tồn tại
        Media media = mediaRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Không tìm thấy media với id: {}", id);
                throw new ResourceNotFoundException("Không tìm thấy media");
            });
        // Loại bỏ extension nếu có
        String originName = media.getOriginName();
        if (originName != null && originName.contains(".")) {
            originName = originName.substring(0, originName.lastIndexOf('.'));
        }
        media.setOriginName(originName);

        return adminMediaMapper.toMediaResponse(media);
    }

    public MediaResponse updateMediaById(Long id, MediaUpdateRequest request) {
        // TODO: Kiểm tra (mediaId-userId) tồn tại
        // 2. Tìm và validate media
        Media media = mediaRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Không tìm thấy media với id: {}", id);
                return new ResourceNotFoundException("Không tìm thấy media");
            });

        // 3. Cập nhật thông tin
        try {
            // Lấy extension từ tên file gốc
            String extension = fileMeta.getExtension(media.getOriginName());
            String newOriginName = request.getName() + "." + extension;

            // Cập nhật thông tin
            media.setOriginName(newOriginName);
            media.setDescription(request.getDescription());
            media.setUpdatedAt(LocalDateTime.now());

            // Lưu vào database
            media = mediaRepository.save(media);
            log.info("Đã cập nhật media thành công: id={}, newName={}",
                media.getId(), newOriginName);

            // 4. Trả về response
            return adminMediaMapper.toMediaResponse(media);

        } catch (Exception e) {
            log.error("Lỗi khi cập nhật media: {}", e.getMessage());
            throw new FileUploadException("Không thể cập nhật thông tin media");
        }
    }

    public void deleteMediaById(Long id) {
        // 2. Tìm và validate media
        Media media = mediaRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Không tìm thấy media với id: {}", id);
                return new ResourceNotFoundException("Không tìm thấy media");
            });

        // 3. Xóa file vật lý trước
        fileMeta.deletePhysicalFile(media.getFileName(), uploadDir);

        // 4. Xóa record trong database
        try {
            mediaRepository.delete(media);
            log.info("Đã xóa media thành công: id={}, fileName={}",
                media.getId(), media.getFileName());
        } catch (Exception e) {
            log.error("Lỗi khi xóa media từ database: {}", e.getMessage());
            throw new FileDeleteException("Không thể xóa thông tin media từ database");
        }
    }

    public PageResponse<MediaShortResponse> searchMedia(MediaSearchRequest request) {
        try {
            // TODO: Lấy userId từ SecurityContext
            Long userId = 1L;

            PageRequest pageRequest = PageRequest.of(
                request.getPage() - 1,
                request.getSize()
            );
            // Tìm kiếm và phân trang sử dụng AdminMediaFilter
            Page<Media> pageResult = mediaRepository.findAll(
                adminMediaFilter.searchMedia(request, userId),
                pageRequest
            );
            if (request.getPage() > pageResult.getTotalPages()) {
                throw new IllegalArgumentException("Trang không tồn tại");
            }
            List<MediaShortResponse> mediaShortResponses = pageResult.getContent().stream()
                .map(adminMediaMapper::toMediaShortResponse)
                .toList();
            // Map kết quả và trả về
            return new PageResponse<>(
                mediaShortResponses,
                request.getPage(),
                request.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages()
            );
        } catch (Exception e) {
            log.error("Tìm kiếm danh sách Media thất bại: {}", e.getMessage(), e);
            throw new MediaException("Không thể tìm kiếm media: " + e.getMessage());
        }
    }
}
