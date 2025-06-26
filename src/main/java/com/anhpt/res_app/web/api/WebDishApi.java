package com.anhpt.res_app.web.api;

import com.anhpt.res_app.common.dto.response.ApiResponse;
import com.anhpt.res_app.common.utils.ApiCategory;
import com.anhpt.res_app.common.utils.ApiDescription;
import com.anhpt.res_app.web.dto.response.dish.DishResponse;
import com.anhpt.res_app.web.service.WebDishService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dish")
@RequiredArgsConstructor
@Validated
@ApiCategory(ApiCategory.CategoryType.PUBLIC)
public class WebDishApi {
    private final WebDishService webDishService;

    // Lấy thông tin chi tiết của một món ăn đang phát hành
    @GetMapping("/{id}")
    @ApiDescription("Lấy thông tin chi tiết của một món ăn (phát hành)")
    public ResponseEntity<ApiResponse<DishResponse>> getById(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Long id
    ) {
        DishResponse dishResponse = webDishService.getById(id);
        ApiResponse<DishResponse> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy thông tin chi tiết món ăn thành công",
            dishResponse
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
