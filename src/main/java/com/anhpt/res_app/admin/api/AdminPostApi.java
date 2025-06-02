package com.anhpt.res_app.admin.api;

import com.anhpt.res_app.admin.dto.request.post.PostCreateRequest;
import com.anhpt.res_app.admin.dto.request.post.PostSearchRequest;
import com.anhpt.res_app.admin.dto.request.post.PostUpdateRequest;
import com.anhpt.res_app.admin.dto.request.post.PostUpdateStatusRequest;
import com.anhpt.res_app.admin.dto.response.PostResponse;
import com.anhpt.res_app.admin.dto.response.PostShortResponse;
import com.anhpt.res_app.admin.service.AdminPostService;
import com.anhpt.res_app.common.dto.response.ApiResponse;
import com.anhpt.res_app.common.dto.response.PageResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/post")
@RequiredArgsConstructor
@Validated
public class AdminPostApi {
    private final AdminPostService adminPostService;

    @PostMapping
    public ResponseEntity<ApiResponse<PostResponse>> create(
        @RequestBody @Valid PostCreateRequest request
    ) {
        PostResponse postResponse = adminPostService.create(request);
        ApiResponse<PostResponse> response = new ApiResponse<>(
            HttpStatus.CREATED.value(),
            true,
            "Tạo bài viết thành công",
            postResponse
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> update(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Long id,
        @RequestBody @Valid PostUpdateRequest request
    ) {
        PostResponse postResponse = adminPostService.update(request, id);
        ApiResponse<PostResponse> response = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Cập nhật bài viết thành công",
            postResponse
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Long id
    ) {
        adminPostService.delete(id);
        ApiResponse<Void> response = new ApiResponse<>(
            HttpStatus.NO_CONTENT.value(),
            true,
            "Xóa bài viết thành công",
            null
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<PostShortResponse>>> get(
        @ModelAttribute @Valid PostSearchRequest request
    ) {
        PageResponse<PostShortResponse> pageResponse = adminPostService.get(request);
        ApiResponse<PageResponse<PostShortResponse>> response = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy danh sách bài viết thành công",
            pageResponse
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<PostResponse>> updateStatus(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Long id,
        @RequestBody PostUpdateStatusRequest request
    ) {
        PostResponse postResponse = adminPostService.updateStatus(id, request);
        ApiResponse<PostResponse> response = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Cập nhật trạng thái bài viết thành công",
            postResponse
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
