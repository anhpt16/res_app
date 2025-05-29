package com.anhpt.res_app.admin.api;

import com.anhpt.res_app.admin.dto.request.MediaUpdateRequest;
import com.anhpt.res_app.admin.dto.request.MediaUploadRequest;
import com.anhpt.res_app.admin.dto.response.MediaResponse;
import com.anhpt.res_app.admin.service.AdminMediaService;
import com.anhpt.res_app.common.dto.response.ApiResponse;
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

    @PutMapping("/{id}")
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
        @PathVariable Long id
    ) {
        adminMediaService.deleteMediaById(id);
        ApiResponse<Void> response = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Xóa media thành công",
            null
        );
        return ResponseEntity.ok(response);
    }

}
