package com.anhpt.res_app.admin.api;

import com.anhpt.res_app.admin.dto.request.PostCreateRequest;
import com.anhpt.res_app.admin.dto.request.PostUpdateRequest;
import com.anhpt.res_app.admin.dto.response.PostResponse;
import com.anhpt.res_app.admin.dto.response.PostShortResponse;
import com.anhpt.res_app.admin.service.AdminPostService;
import com.anhpt.res_app.common.dto.response.ApiResponse;
import com.anhpt.res_app.common.dto.response.PageResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/post")
@RequiredArgsConstructor
public class AdminPostApi {
    private final AdminPostService adminPostService;

    @PostMapping
    public ResponseEntity<ApiResponse<PostResponse>> create(
        @RequestBody @Valid PostCreateRequest request
    ) {
        return null;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> update(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Long id,
        @RequestBody @Valid PostUpdateRequest request
    ) {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Long id
    ) {
        return null;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<PostShortResponse>>> get() {
        return null;
    }

}
