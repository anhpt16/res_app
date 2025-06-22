package com.anhpt.res_app.admin.api;

import com.anhpt.res_app.admin.dto.request.category.CategoryCreateRequest;
import com.anhpt.res_app.admin.dto.request.category.CategorySearchRequest;
import com.anhpt.res_app.admin.dto.request.category.CategoryUpdateRequest;
import com.anhpt.res_app.admin.dto.response.category.CategoryResponse;
import com.anhpt.res_app.admin.service.AdminCategoryService;
import com.anhpt.res_app.common.dto.response.ApiResponse;
import com.anhpt.res_app.common.dto.response.PageResponse;
import com.anhpt.res_app.common.utils.ApiCategory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/category")
@RequiredArgsConstructor
@Validated
public class AdminCategoryApi {
    private final AdminCategoryService adminCategoryService;

    @PostMapping
    @ApiCategory(ApiCategory.CategoryType.ADMIN)
    public ResponseEntity<ApiResponse<CategoryResponse>> create(
        @RequestBody @Valid CategoryCreateRequest request
    ) {
        CategoryResponse categoryResponse = adminCategoryService.create(request);
        ApiResponse<CategoryResponse> response = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Tạo danh mục thành công",
            categoryResponse
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{id}")
    @ApiCategory(ApiCategory.CategoryType.ADMIN)
    public ResponseEntity<ApiResponse<CategoryResponse>> update(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Long id,
        @RequestBody CategoryUpdateRequest request
    ) {
        CategoryResponse categoryResponse = adminCategoryService.update(request, id);
        ApiResponse<CategoryResponse> response = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Cập nhật danh mục thành công",
            categoryResponse
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    @ApiCategory(ApiCategory.CategoryType.ADMIN)
    public ResponseEntity<ApiResponse<Void>> delete(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Long id
    ) {
        adminCategoryService.delete(id);
        ApiResponse<Void> response = new ApiResponse<>(
            HttpStatus.NO_CONTENT.value(),
            true,
            "Xóa danh mục thành công",
            null
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    @ApiCategory(ApiCategory.CategoryType.ADMIN)
    public ResponseEntity<ApiResponse<PageResponse<CategoryResponse>>> get(
        @ModelAttribute @Valid CategorySearchRequest request
    ) {
        PageResponse<CategoryResponse> pageResponse = adminCategoryService.get(request);
        ApiResponse<PageResponse<CategoryResponse>> response = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy danh sách danh mục thành công",
            pageResponse
        ); 
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
