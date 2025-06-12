package com.anhpt.res_app.admin.api;

import com.anhpt.res_app.admin.dto.request.combo.ComboCreateRequest;
import com.anhpt.res_app.admin.dto.request.combo.ComboSearchRequest;
import com.anhpt.res_app.admin.dto.request.combo.ComboUpdateRequest;
import com.anhpt.res_app.admin.dto.response.combo.ComboDetailResponse;
import com.anhpt.res_app.admin.dto.response.combo.ComboListResponse;
import com.anhpt.res_app.admin.dto.response.combo.ComboResponse;
import com.anhpt.res_app.admin.service.AdminComboService;
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
@RequestMapping("/api/admin/combo")
@RequiredArgsConstructor
@Validated
public class AdminComboApi {
    private final AdminComboService adminComboService;

    @PostMapping
    public ResponseEntity<ApiResponse<ComboResponse>> create(
        @RequestBody @Valid ComboCreateRequest request
    ) {
        ComboResponse response = adminComboService.create(request);
        ApiResponse<ComboResponse> apiResponse = new ApiResponse<>(
            HttpStatus.CREATED.value(),
            true,
            "Tạo combo thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<ComboResponse>> update(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Long id,
        @RequestBody @Valid ComboUpdateRequest request
    ) {
        ComboResponse response = adminComboService.update(request, id);
        ApiResponse<ComboResponse> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Cập nhật combo thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    //TODO: Cập nhật trạng thái của combo
    //TODO: Phát hành lại combo
    //TODO: Xóa ảnh của combo

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Long id
    ) {
        adminComboService.delete(id);
        ApiResponse<Void> apiResponse = new ApiResponse<>(
            HttpStatus.NO_CONTENT.value(),
            true,
            "Xóa combo thành công",
            null
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ComboDetailResponse>> getById(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Long id
    ) {
        ComboDetailResponse response = adminComboService.getById(id);
        ApiResponse<ComboDetailResponse> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy combo thành công",
            response
        ); 
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ComboListResponse>>> get(
        @ModelAttribute @Valid ComboSearchRequest request
    ) {
        PageResponse<ComboListResponse> response = adminComboService.get(request);
        ApiResponse<PageResponse<ComboListResponse>> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy combo thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
