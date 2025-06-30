package com.anhpt.res_app.admin.api;

import com.anhpt.res_app.admin.dto.request.user.UserCreateRequest;
import com.anhpt.res_app.admin.dto.request.user.UserSearchRequest;
import com.anhpt.res_app.admin.dto.response.user.UserResponse;
import com.anhpt.res_app.admin.dto.response.user.RoleByUserResponse;
import com.anhpt.res_app.admin.service.AdminUserService;
import com.anhpt.res_app.common.dto.response.ApiResponse;
import com.anhpt.res_app.common.dto.response.PageResponse;
import com.anhpt.res_app.common.utils.ApiCategory;
import com.anhpt.res_app.common.utils.ApiDescription;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/user")
@RequiredArgsConstructor
@Validated
@ApiCategory(ApiCategory.CategoryType.PUBLIC)
public class AdminUserApi {
    private final AdminUserService adminUserService;

    @PostMapping
    @ApiDescription("Tạo mới một tài khoản")
    public ResponseEntity<ApiResponse<UserResponse>> create(
        @RequestBody @Valid UserCreateRequest request
    ) {
        UserResponse response = adminUserService.create(request);
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>(
            HttpStatus.CREATED.value(),
            true,
            "Tạo user thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PatchMapping("/{id}/status")
    @ApiDescription("Cập nhật trạng thái của một tài khoản")
    public ResponseEntity<ApiResponse<UserResponse>> updateStatus(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Long id,
        @RequestParam (name = "status") String status
    ) {
        UserResponse response = adminUserService.updateStatus(id, status);
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Cập nhật trạng thái user thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    @ApiDescription("Xóa một tài khoản")
    public ResponseEntity<ApiResponse<Void>> delete(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Long id
    ) {
        adminUserService.delete(id);
        ApiResponse<Void> apiResponse = new ApiResponse<>(
            HttpStatus.NO_CONTENT.value(),
            true,
            "Xóa user thành công",
            null
        );
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(apiResponse);
    }

    @GetMapping("/{id}")
    @ApiDescription("Lấy thông tin chi tiết của một tài khoản")
    public ResponseEntity<ApiResponse<UserResponse>> getById(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Long id
    ) {
        UserResponse response = adminUserService.getById(id);
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy user thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping
    @ApiDescription("Lấy danh sách tài khoản")
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> get(
        @ModelAttribute @Valid UserSearchRequest request
    ) {
        PageResponse<UserResponse> response = adminUserService.get(request);
        ApiResponse<PageResponse<UserResponse>> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy danh sách user thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    // UserRole
    @PostMapping("/{userId}/role/{roleId}")
    @ApiDescription("Thêm mới vai trò cho một tài khoản")
    public ResponseEntity<ApiResponse<RoleByUserResponse>> addRole(
        @PathVariable @Min(value = 1, message = "UId không hợp lệ") Long userId,
        @PathVariable @Min(value = 1, message = "RId không hợp lệ") Integer roleId
    ) {
        RoleByUserResponse response = adminUserService.addRole(userId, roleId);
        ApiResponse<RoleByUserResponse> apiResponse = new ApiResponse<>(
            HttpStatus.CREATED.value(),
            true,
            "Thêm role cho user thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @DeleteMapping("/{userId}/role/{roleId}")
    @ApiDescription("Xóa một vai trò của một tài khoản")
    public ResponseEntity<ApiResponse<Void>> deleteRole(
        @PathVariable @Min(value = 1, message = "UId không hợp lệ") Long userId,
        @PathVariable @Min(value = 1, message = "RId không hợp lệ") Integer roleId
    ) {
        adminUserService.deleteUserRole(userId, roleId);
        ApiResponse<Void> apiResponse = new ApiResponse<>(
            HttpStatus.NO_CONTENT.value(),
            true,
            "Xóa role cho user thành công",
            null
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/{userId}/role")
    @ApiDescription("Lấy danh sách vai trò của một tài khoản")
    public ResponseEntity<ApiResponse<RoleByUserResponse>> getRolesByUserId(
        @PathVariable @Min(value = 1, message = "UId không hợp lệ") Long userId
    ) {
        RoleByUserResponse response = adminUserService.getRolesByUserId(userId);
        ApiResponse<RoleByUserResponse> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy danh sách role của user thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
