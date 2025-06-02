package com.anhpt.res_app.admin.api;

import com.anhpt.res_app.admin.dto.request.TagCreateRequest;
import com.anhpt.res_app.admin.dto.response.TagResponse;
import com.anhpt.res_app.admin.service.AdminTagService;
import com.anhpt.res_app.common.dto.response.ApiResponse;
import com.anhpt.res_app.common.dto.response.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/tag")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AdminTagApi {
    private final AdminTagService adminTagService;

    @PostMapping
    public ResponseEntity<ApiResponse<TagResponse>> create(
        @RequestBody @Valid TagCreateRequest request
    ) {
        TagResponse tagResponse = adminTagService.create(request);
        ApiResponse<TagResponse> response = new ApiResponse<>(
            HttpStatus.CREATED.value(),
            true,
            "Tạo tag thành công",
            tagResponse
        );
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<TagResponse>> update(
        @PathVariable Long id,
        @RequestBody @Valid TagCreateRequest request
    ) {
        TagResponse tagResponse = adminTagService.update(id, request);
        ApiResponse<TagResponse> response = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Cập nhật tag thành công",
            tagResponse
        );
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
        @PathVariable Long id
    ) {
        adminTagService.delete(id);
        ApiResponse<Void> response = new ApiResponse<>(
            HttpStatus.NO_CONTENT.value(),
            true,
            "Xóa tag thành công",
            null
        );
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<TagResponse>>> get(
        @RequestParam (name = "page", defaultValue = "1") Integer page,
        @RequestParam (name = "size", defaultValue = "10") Integer size
    ) {
        PageResponse<TagResponse> pageResponse = adminTagService.get(page, size);
        ApiResponse<PageResponse<TagResponse>> response = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy danh sách tag thành công",
            pageResponse
        );
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(response);
    }
}
