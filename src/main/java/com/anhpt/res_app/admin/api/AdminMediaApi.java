package com.anhpt.res_app.admin.api;

import com.anhpt.res_app.admin.dto.request.media.MediaSearchRequest;
import com.anhpt.res_app.admin.dto.request.media.MediaUpdateRequest;
import com.anhpt.res_app.admin.dto.request.media.MediaUploadRequest;
import com.anhpt.res_app.admin.dto.response.media.MediaResponse;
import com.anhpt.res_app.admin.dto.response.media.MediaShortResponse;
import com.anhpt.res_app.admin.service.AdminMediaService;
import com.anhpt.res_app.common.dto.response.ApiResponse;
import com.anhpt.res_app.common.dto.response.PageResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/media")
@RequiredArgsConstructor
public class AdminMediaApi {
    private final AdminMediaService adminMediaService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<MediaResponse>> upload(
        @ModelAttribute @Valid MediaUploadRequest request
    ) {
        MediaResponse mediaResponse = adminMediaService.uploadFile(request);
        ApiResponse<MediaResponse> response = new ApiResponse<>(
            HttpStatus.CREATED.value(),
            true,
            "Upload file thành công",
            mediaResponse
        );
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MediaResponse>> getDetailMedia(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Long id
    ) {
        MediaResponse mediaResponse = adminMediaService.getMediaById(id);
        ApiResponse<MediaResponse> response = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy thông tin media thành công",
            mediaResponse
        );
        return ResponseEntity.ok(response);
    }

    // TODO: Sửa lại mã để có thể cập nhật từng phần đối với Media
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<MediaResponse>> update(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Long id,
        @RequestBody @Valid MediaUpdateRequest request
    ) {
        MediaResponse mediaResponse = adminMediaService.updateMediaById(id, request);
        ApiResponse<MediaResponse> response = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Cập nhật media thành công",
            mediaResponse
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Long id
    ) {
        adminMediaService.deleteMediaById(id);
        ApiResponse<Void> response = new ApiResponse<>(
            HttpStatus.NO_CONTENT.value(),
            true,
            "Xóa media thành công",
            null
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<MediaShortResponse>>> search(
        @ModelAttribute @Valid MediaSearchRequest request
    ) {
        PageResponse<MediaShortResponse> pageResponse = adminMediaService.searchMedia(request);
        ApiResponse<PageResponse<MediaShortResponse>> response = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy danh sách media thành công",
            pageResponse
        );
        return ResponseEntity.ok(response);
    }
}
