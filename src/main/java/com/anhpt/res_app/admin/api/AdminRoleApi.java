package com.anhpt.res_app.admin.api;

import com.anhpt.res_app.admin.dto.request.role.RoleCreateRequest;
import com.anhpt.res_app.admin.dto.request.role.RoleSearchRequest;
import com.anhpt.res_app.admin.dto.request.role.RoleUpdateRequest;
import com.anhpt.res_app.admin.dto.response.role.RoleResponse;
import com.anhpt.res_app.admin.service.AdminRoleService;
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
@RequestMapping("/api/admin/role")
@RequiredArgsConstructor
@Validated
public class AdminRoleApi {
    private final AdminRoleService adminRoleService;

    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponse>> create(
        @RequestBody @Valid RoleCreateRequest request
    ) {
        RoleResponse response = adminRoleService.create(request);
        ApiResponse<RoleResponse> apiResponse = new ApiResponse<>(
            HttpStatus.CREATED.value(),
            true,
            "Tạo role thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponse>> update(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Integer id,
        @RequestBody @Valid RoleUpdateRequest request
    ) {
        RoleResponse response = adminRoleService.update(id, request);
        ApiResponse<RoleResponse> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Cập nhật role thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<RoleResponse>> updateStatus(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Integer id,
        @RequestParam (name = "status") String status
    ) {
        RoleResponse response = adminRoleService.updateStatus(id, status);
        ApiResponse<RoleResponse> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Cập nhật trạng thái role thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Integer id
    ) {
        adminRoleService.delete(id);
        ApiResponse<Void> apiResponse = new ApiResponse<>(
            HttpStatus.NO_CONTENT.value(),
            true,
            "Xóa role thành công",
            null
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponse>> getById(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Integer id
    ) {
        RoleResponse response = adminRoleService.getById(id);
        ApiResponse<RoleResponse> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy role thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<RoleResponse>>> get(
        @ModelAttribute @Valid RoleSearchRequest request
    ) {
        PageResponse<RoleResponse> response = adminRoleService.get(request);
        ApiResponse<PageResponse<RoleResponse>> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy danh sách role thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
