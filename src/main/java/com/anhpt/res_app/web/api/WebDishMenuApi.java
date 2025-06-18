package com.anhpt.res_app.web.api;

import com.anhpt.res_app.common.dto.response.ApiResponse;
import com.anhpt.res_app.web.dto.response.dish.DishComingResponse;
import com.anhpt.res_app.web.dto.response.dish.DishEndingResponse;
import com.anhpt.res_app.web.dto.response.dish.DishShortResponse;
import com.anhpt.res_app.web.service.WebDishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
@Slf4j
public class WebDishMenuApi {
    private final WebDishService webDishService;

    // Lấy danh sách món ăn sắp ra mắt
    @GetMapping("/coming")
    public ResponseEntity<ApiResponse<List<DishComingResponse>>> getComingDishes() {
        List<DishComingResponse> dishComingResponses = webDishService.getComingDishes();
        ApiResponse<List<DishComingResponse>> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy danh sách món ăn sắp ra mắt thành công",
            dishComingResponses
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    // Lấy danh sách món ăn sắp kết thúc
    @GetMapping("/ending")
    public ResponseEntity<ApiResponse<List<DishEndingResponse>>> getEndingDishes() {
        List<DishEndingResponse> dishEndingResponses = webDishService.getEndingDishes();
        ApiResponse<List<DishEndingResponse>> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy danh sách món ăn sắp kết thúc thành công",
            dishEndingResponses
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    // Lấy danh sách món ăn mới cập nhật
    @GetMapping("/new")
    public ResponseEntity<ApiResponse<List<DishShortResponse>>> getNewlyDishes() {
        List<DishShortResponse> dishShortResponses = webDishService.getNewlyDishes();
        ApiResponse<List<DishShortResponse>> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy danh sách món ăn mới cập nhật thành công",
            dishShortResponses
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
