package com.anhpt.res_app.admin.api;

import com.anhpt.res_app.admin.dto.request.combo.*;
import com.anhpt.res_app.admin.dto.response.combo.*;
import com.anhpt.res_app.admin.service.AdminComboService;
import com.anhpt.res_app.admin.service.AdminComboVersionService;
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

@RestController
@RequestMapping("/api/admin/combo")
@RequiredArgsConstructor
@Validated
@ApiCategory(ApiCategory.CategoryType.M_DISH)
public class AdminComboApi {
    private final AdminComboService adminComboService;
    private final AdminComboVersionService adminComboVersionService;

    @PostMapping
    @ApiDescription("Tạo mới một combo")
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
    @ApiDescription("Cập nhật thông tin của một combo")
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

    // Cập nhật trạng thái của combo
    @PatchMapping("/{id}/status")
    @ApiDescription("Cập nhật trạng thái của một combo")
    public ResponseEntity<ApiResponse<ComboResponse>> updateStatus(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Long id,
        @RequestParam (name = "status") String status
    ) {
        ComboResponse response = adminComboService.updateStatus(id, status);
        ApiResponse<ComboResponse> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Cập nhật trạng thái combo thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    // Phát hành lại combo
    @PatchMapping("/{id}/reissue")
    @ApiDescription("Phát hành lại combo")
    public ResponseEntity<ApiResponse<ComboResponse>> reissueCombo(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Long id
    ) {
        ComboResponse response = adminComboService.reissue(id);
        ApiResponse<ComboResponse> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Phát hành lại combo thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    // Xóa ảnh của combo
    @DeleteMapping("/{id}/media")
    @ApiDescription("Xóa ảnh của một combo")
    public ResponseEntity<ApiResponse<Void>> deleteComboMedia(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Long id
    ) {
        adminComboService.deleteComboMedia(id);
        ApiResponse<Void> apiResponse = new ApiResponse<>(
            HttpStatus.NO_CONTENT.value(),
            true,
            "Xóa ảnh combo thành công",
            null
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    @ApiDescription("Xóa một combo")
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
    @ApiDescription("Lấy thông tin chi tiết của một combo")
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
    @ApiDescription("Lấy danh sách các combo")
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


    // SECTION: Combo Version - Combo Version Dish

    // Thêm mới một phiên bản cho một combo
    @PostMapping("/{id}/version")
    @ApiDescription("Thêm mới một phiên bản cho một combo")
    public ResponseEntity<ApiResponse<ComboVersionResponse>> create(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Long id
    ) {
        ComboVersionResponse response = adminComboVersionService.create(id);
        ApiResponse<ComboVersionResponse> apiResponse = new ApiResponse<>(
            HttpStatus.CREATED.value(),
            true,
            "Tạo phiên bản thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    // Lấy ra thông tin chi tiết về môt phiên bản
    @GetMapping("/{id}/version/{versionId}")
    @ApiDescription("Lấy thông tin chi tiết về một phiên bản của một combo")
    public ResponseEntity<ApiResponse<ComboVersionResponse>> getVersionById(
        @PathVariable @Min(value = 1, message = "CId không hợp lệ") Long id,
        @PathVariable @Min(value = 1, message = "VId không hợp lệ") Long versionId
    ) {
        ComboVersionResponse response = adminComboVersionService.getComboVersionById(id, versionId);
        ApiResponse<ComboVersionResponse> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy phiên bản thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    // Lấy danh sách các phiên bản của một combo
    @GetMapping("/{id}/version")
    @ApiDescription("Lấy danh sách các phiên bản của một combo")
    public ResponseEntity<ApiResponse<List<ComboVersionShortResponse>>> getVersionsById(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Long id
    ) {
        List<ComboVersionShortResponse> response = adminComboVersionService.getComboVersions(id);
        ApiResponse<List<ComboVersionShortResponse>> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy danh sách phiên bản thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    // Cập nhật trạng thái cho một phiên bản
    @PatchMapping("/{id}/version/{versionId}/status")
    @ApiDescription("Cập nhật trạng thái cho một phiên bản của một combo")
    public ResponseEntity<ApiResponse<ComboVersionResponse>> updateVersionStatus(
        @PathVariable @Min(value = 1, message = "CId không hợp lệ") Long id,
        @PathVariable @Min(value = 1, message = "VId không hợp lệ") Long versionId,
        @RequestParam (name = "status") String status
    ) {
        ComboVersionResponse response = adminComboVersionService.updateVersionStatus(id, versionId, status);
        ApiResponse<ComboVersionResponse> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Cập nhật trạng thái phiên bản thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    // Xóa một phiên bản
    @DeleteMapping("/{id}/version/{versionId}")
    @ApiDescription("Xóa một phiên bản của một combo")
    public ResponseEntity<ApiResponse<Void>> deleteVersion(
        @PathVariable @Min(value = 1, message = "CId không hợp lệ") Long id,
        @PathVariable @Min(value = 1, message = "VId không hợp lệ") Long versionId
    ) {
        adminComboVersionService.deleteComboVersion(id, versionId);
        ApiResponse<Void> apiResponse = new ApiResponse<>(
            HttpStatus.NO_CONTENT.value(),
            true,
            "Xóa phiên bản thành công",
            null
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    // Thêm một món ăn cho phiên bản
    @PostMapping("/{id}/version/{versionId}/dish/{dishId}")
    @ApiDescription("Thêm món ăn cho một phiên bản")
    public ResponseEntity<ApiResponse<ComboVersionDishResponse>> addDish(
        @PathVariable @Min(value = 1, message = "CId không hợp lệ") Long id,
        @PathVariable @Min(value = 1, message = "VId không hợp lệ") Long versionId,
        @PathVariable @Min(value = 1, message = "DId không hợp lệ") Long dishId,
        @RequestParam (name = "count", defaultValue = "1") Integer count,
        @RequestParam (name = "displayOrder", defaultValue = "0") Integer displayOrder
    ) {
        ComboVersionDishResponse response = adminComboVersionService.addDish(id, versionId, dishId, count, displayOrder);
        ApiResponse<ComboVersionDishResponse> apiResponse = new ApiResponse<>(
            HttpStatus.CREATED.value(),
            true,
            "Thêm món ăn thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    // Cập nhật số lượng món ăn của một phiên bản
    @PatchMapping("/{id}/version/{versionId}/dish/{dishId}")
    @ApiDescription("Cập nhật số lượng của một món ăn trong một phiên bản")
    public ResponseEntity<ApiResponse<ComboVersionDishResponse>> updateDish(
        @PathVariable @Min(value = 1, message = "CId không hợp lệ") Long id,
        @PathVariable @Min(value = 1, message = "VId không hợp lệ") Long versionId,
        @PathVariable @Min(value = 1, message = "DId không hợp lệ") Long dishId,
        @RequestParam (name = "count", defaultValue = "1") Integer count,
        @RequestParam (name = "displayOrder", defaultValue = "0") Integer displayOrder
    ) {
        ComboVersionDishResponse response = adminComboVersionService.updateDish(id, versionId, dishId, count, displayOrder);
        ApiResponse<ComboVersionDishResponse> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Cập nhật món ăn thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    // Xóa một món ăn trong một phiên bản
    @DeleteMapping("/{id}/version/{versionId}/dish/{dishId}")
    @ApiDescription("Xóa một món ăn khỏi một phiên bản")
    public ResponseEntity<ApiResponse<Void>> deleteDish(
        @PathVariable @Min(value = 1, message = "CId không hợp lệ") Long id,
        @PathVariable @Min(value = 1, message = "VId không hợp lệ") Long versionId,
        @PathVariable @Min(value = 1, message = "DId không hợp lệ") Long dishId
    ) {
        adminComboVersionService.deleteDish(id, versionId, dishId);
        ApiResponse<Void> apiResponse = new ApiResponse<>(
            HttpStatus.NO_CONTENT.value(),
            true,
            "Xóa món ăn thành công",
            null
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    // Cập nhật thông tin cho một phiên bản
    @PatchMapping("/{id}/version/{versionId}")
    @ApiDescription("Cập nhật thông tin cho một phiên bản")
    public ResponseEntity<ApiResponse<ComboVersionResponse>> updateVersionById(
        @PathVariable @Min(value = 1, message = "CId không hợp lệ") Long id,
        @PathVariable @Min(value = 1, message = "VId không hợp lệ") Long versionId,
        @RequestBody @Valid VersionUpdateRequest request
    ) {
        ComboVersionResponse response = adminComboVersionService.updateVersion(request, id, versionId);
        ApiResponse<ComboVersionResponse> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Cập nhật phiên bản thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


}
