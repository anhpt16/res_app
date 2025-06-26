package com.anhpt.res_app.admin.api;

import com.anhpt.res_app.admin.dto.request.setup.DishSetupRequest;
import com.anhpt.res_app.admin.dto.request.setup.DishSetupSearchRequest;
import com.anhpt.res_app.admin.dto.request.setup.DishSetupUpdateRequest;
import com.anhpt.res_app.admin.dto.response.setup.DishSetupResponse;
import com.anhpt.res_app.admin.service.AdminDishSetupService;
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

@RestController
@RequestMapping("/api/admin/dish-setup")
@RequiredArgsConstructor
@Validated
@ApiCategory(ApiCategory.CategoryType.M_DISH)
public class AdminDishSetupApi {
    private final AdminDishSetupService adminDishSetupService;

    // Thiết lập chuyển trạng thái cho món ăn
    @PostMapping("/{dishId}")
    @ApiDescription("Thêm thiết lập chuyển trạng thái cho món ăn")
    public ResponseEntity<ApiResponse<DishSetupResponse>> create(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Long dishId,
        @RequestBody @Valid DishSetupRequest request
    ) {
        DishSetupResponse dishSetupResponse = adminDishSetupService.create(dishId, request);
        ApiResponse<DishSetupResponse> apiResponse = new ApiResponse<>(
            HttpStatus.CREATED.value(),
            true,
            "Tạo thiết lập trạng thái thành công",
            dishSetupResponse
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    // Cập nhật thông tin chuyển trạng thái của một món ăn
    @PatchMapping("/{dishSetupId}")
    @ApiDescription("Cập nhật thiết lập chuyển trạng thái của một món ăn")
    public ResponseEntity<ApiResponse<DishSetupResponse>> update(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Long dishSetupId,
        @RequestBody @Valid DishSetupUpdateRequest request
    ) {
        DishSetupResponse dishSetupResponse = adminDishSetupService.update(dishSetupId, request);
        ApiResponse<DishSetupResponse> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Cập nhật thiết lập trạng thái thành công",
            dishSetupResponse
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    // Xóa thiết lập trạng thái của một món ăn
    @DeleteMapping("/{dishSetupId}")
    @ApiDescription("Xóa thiết lập chuyển trạng thái của một món ăn")
    public ResponseEntity<ApiResponse<Void>> delete(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Long dishSetupId
    ) {
        adminDishSetupService.delete(dishSetupId);
        ApiResponse<Void> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Xóa thiết lập trạng thái thành công",
            null
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    // Xem danh sách các món ăn đã thiết lập chuyển trạng thái
    @GetMapping
    @ApiDescription("Lấy dánh sách thiết lập chuyển trạng thái của món ăn")
    public ResponseEntity<ApiResponse<PageResponse<DishSetupResponse>>> get(
        @ModelAttribute @Valid DishSetupSearchRequest request
    ) {
        PageResponse<DishSetupResponse> pageResponse = adminDishSetupService.get(request);
        ApiResponse<PageResponse<DishSetupResponse>> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy danh sách thiết lập trạng thái thành công",
            pageResponse
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
