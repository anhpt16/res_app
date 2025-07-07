package com.anhpt.res_app.admin.api;

import com.anhpt.res_app.admin.dto.request.desk.DurationCreateRequest;
import com.anhpt.res_app.admin.dto.request.desk.DurationSearchRequest;
import com.anhpt.res_app.admin.dto.response.desk.DurationResponse;
import com.anhpt.res_app.admin.service.AdminDurationService;
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
@RequestMapping("/api/admin/duration")
@RequiredArgsConstructor
@Validated
public class AdminDurationApi {
    private final AdminDurationService adminDurationService;

    // Tạo mới Duration
    @PostMapping
    public ResponseEntity<ApiResponse<DurationResponse>> create(
        @RequestBody @Valid DurationCreateRequest request
    ) {
        DurationResponse response = adminDurationService.create(request);
        ApiResponse<DurationResponse> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Tạo Duration thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    // Cập nhật trạng thái Duration
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<DurationResponse>> updateStatus(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Long id,
        @RequestParam (name = "status") String status
    ) {
        DurationResponse response = adminDurationService.updateStatus(id, status);
        ApiResponse<DurationResponse> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Cập nhật trạng thái Duration thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    // Lấy danh sách Duration
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<DurationResponse>>> get(
        @ModelAttribute @Valid DurationSearchRequest request
    ) {
        PageResponse<DurationResponse> pageResponse = adminDurationService.search(request);
        ApiResponse<PageResponse<DurationResponse>> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy danh sách Duration thành công",
            pageResponse
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
