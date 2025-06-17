package com.anhpt.res_app.admin.api;

import com.anhpt.res_app.admin.dto.request.dish.DishCreateRequest;
import com.anhpt.res_app.admin.dto.request.dish.DishSearchRequest;
import com.anhpt.res_app.admin.dto.request.dish.DishUpdateRequest;
import com.anhpt.res_app.admin.dto.response.dish.DishResponse;
import com.anhpt.res_app.admin.dto.response.dish.DishShortResponse;
import com.anhpt.res_app.admin.service.AdminDishService;
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
@RequestMapping("/api/admin/dish")
@RequiredArgsConstructor
@Validated
public class AdminDishApi {
    private final AdminDishService adminDishService;

    @PostMapping
    public ResponseEntity<ApiResponse<DishResponse>> create(
        @RequestBody @Valid DishCreateRequest request
    ) {
        DishResponse response = adminDishService.create(request);
        ApiResponse<DishResponse> apiResponse = new ApiResponse<>(
            HttpStatus.CREATED.value(),
            true,
            "Tạo món ăn thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<DishResponse>> update(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Long id,
        @RequestBody @Valid DishUpdateRequest request
    ) {
        DishResponse response = adminDishService.update(request, id);
        ApiResponse<DishResponse> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Cập nhật món ăn thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    // Cập nhật trạng thái món ăn
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<DishResponse>> updateStatus(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Long id,
        @RequestParam (name = "status") String status
    ) {
        DishResponse response = adminDishService.updateStatus(id, status);
        ApiResponse<DishResponse> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Cập nhật trạng thái món ăn thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    // Phát hành lại món ăn
    @PatchMapping("/{id}/reissue")
    public ResponseEntity<ApiResponse<DishResponse>> reissue(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Long id
    ) {
        DishResponse response = adminDishService.reissue(id);
        ApiResponse<DishResponse> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Phát hành lại món ăn thành công",
            response
        ); 
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    // Xóa toàn bộ ảnh của món ăn
    @DeleteMapping("/{id}/media")
    public ResponseEntity<ApiResponse<Void>> deleteAllMedia(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Long id
    ) {
        adminDishService.deleteAllMedia(id);
        ApiResponse<Void> apiResponse = new ApiResponse<>(
            HttpStatus.NO_CONTENT.value(),
            true,
            "Xóa toàn bộ ảnh của món ăn thành công",
            null
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    // Thêm phần xử lý thứ tự ảnh của Dish khi lấy ra danh sách ảnh

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Long id
    ) {
        adminDishService.delete(id);
        ApiResponse<Void> apiResponse = new ApiResponse<>(
            HttpStatus.NO_CONTENT.value(),
            true,
            "Xóa món ăn thành công",
            null
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DishResponse>> getById(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Long id
    ) {
        DishResponse response = adminDishService.getById(id);
        ApiResponse<DishResponse> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy món ăn thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<DishShortResponse>>> get(
        @ModelAttribute @Valid DishSearchRequest request
    ) {
        PageResponse<DishShortResponse> response = adminDishService.search(request);
        ApiResponse<PageResponse<DishShortResponse>> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy danh sách món ăn thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
