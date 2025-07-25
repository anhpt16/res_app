package com.anhpt.res_app.admin.api;

import com.anhpt.res_app.admin.dto.request.pemission.PermissionRequest;
import com.anhpt.res_app.admin.dto.request.role.RoleCreateRequest;
import com.anhpt.res_app.admin.dto.request.role.RoleSearchRequest;
import com.anhpt.res_app.admin.dto.request.role.RoleUpdateRequest;
import com.anhpt.res_app.admin.dto.response.permission.PermissionResponse;
import com.anhpt.res_app.admin.dto.response.role.RoleResponse;
import com.anhpt.res_app.admin.service.AdminPermissionService;
import com.anhpt.res_app.admin.service.AdminRoleService;
import com.anhpt.res_app.common.dto.response.ApiResponse;
import com.anhpt.res_app.common.dto.response.PageResponse;
import com.anhpt.res_app.common.utils.ApiCategory;
import com.anhpt.res_app.common.utils.ApiDescription;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/role")
@RequiredArgsConstructor
@Validated
@ApiCategory(ApiCategory.CategoryType.PUBLIC)
public class AdminRoleApi {
    private final AdminRoleService adminRoleService;
    private final AdminPermissionService adminPermissionService;

    @PostMapping
    @ApiDescription("Tạo mới một vai trò")
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
    @ApiDescription("Cập nhật thông tin một vai trò")
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
    @ApiDescription("Cập nhật trạng thái của một vai trò")
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
    @ApiDescription("Xóa một vai trò")
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
    @ApiDescription("Lấy thông tin chi tiết của một vai trò")
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
    @ApiDescription("Lấy danh sách vai trò")
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

    // Role - Permission
    // Thêm quyền hạn cho một vai trò
    @PostMapping("/{roleId}/permission")
    @ApiDescription("Thêm quyền hạn cho một vai trò")
    public ResponseEntity<ApiResponse<PermissionResponse>> addPermission(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Integer roleId,
        @RequestBody @Valid PermissionRequest request
    ) {
        PermissionResponse response = adminPermissionService.addPermission(roleId, request);
        ApiResponse<PermissionResponse> apiResponse = new ApiResponse<>(
            HttpStatus.CREATED.value(),
            true,
            "Thêm quyền hạn cho role thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
    // Xóa quyền hạn của một vai trò
    @DeleteMapping("/{roleId}/permission")
    @ApiDescription("Xóa quyền hạn của một vai trò")
    public ResponseEntity<ApiResponse<Void>> deletePermission(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Integer roleId,
        @RequestBody @Valid PermissionRequest request
    ) {
        adminPermissionService.deletePermission(roleId, request);
        ApiResponse<Void> apiResponse = new ApiResponse<>(
            HttpStatus.NO_CONTENT.value(),
            true,
            "Xóa quyền hạn cho role thành công",
            null
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    // Lấy ra các quyền hạn của một vai trò
    @GetMapping("/{roleId}/permission")
    @ApiDescription("Lấy danh sách quyền hạn của một vai trò")
    public ResponseEntity<ApiResponse<Map<String, List<PermissionResponse>>>> getByRoleId(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Integer roleId
    ) {
        Map<String, List<PermissionResponse>> response = adminPermissionService.get(roleId);
        ApiResponse<Map<String, List<PermissionResponse>>> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy quyền hạn cho role thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
