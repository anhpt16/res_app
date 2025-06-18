package com.anhpt.res_app.web.api;

import com.anhpt.res_app.common.dto.response.ApiResponse;
import com.anhpt.res_app.common.dto.response.PageResponse;
import com.anhpt.res_app.web.dto.response.category.CategoryResponse;
import com.anhpt.res_app.web.dto.response.dish.DishShortResponse;
import com.anhpt.res_app.web.service.WebCategoryService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
@Validated
public class WebCategoryApi {
    private final WebCategoryService webCategoryService;

    // Lấy danh sách danh mục đang hoạt động
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getCategories() {
        List<CategoryResponse> categoryResponses = webCategoryService.getCategories();
        ApiResponse<List<CategoryResponse>> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy danh sách danh mục thành công",
            categoryResponses
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    // Lấy danh sách món ăn đang phát hành của một danh mục
    @GetMapping("/{categoryId}/dishes")
    public ResponseEntity<ApiResponse<List<DishShortResponse>>> getDishes(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Long categoryId
    ) {
        List<DishShortResponse> dishShortResponses = webCategoryService.getDishes(categoryId);
        ApiResponse<List<DishShortResponse>> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy danh sách món ăn thành công",
            dishShortResponses
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
