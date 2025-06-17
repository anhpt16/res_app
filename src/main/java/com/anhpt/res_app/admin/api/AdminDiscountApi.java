package com.anhpt.res_app.admin.api;

import com.anhpt.res_app.admin.dto.request.discount.DiscountCreateRequest;
import com.anhpt.res_app.admin.dto.request.discount.DiscountSearchRequest;
import com.anhpt.res_app.admin.dto.request.discount.DiscountUpdateRequest;
import com.anhpt.res_app.admin.dto.response.discount.DiscountResponse;
import com.anhpt.res_app.admin.service.AdminDiscountService;
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
@RequestMapping("/api/admin/discount")
@RequiredArgsConstructor
@Validated
public class AdminDiscountApi {
    private final AdminDiscountService adminDiscountService;

    @PostMapping("/{dishId}")
    public ResponseEntity<ApiResponse<DiscountResponse>> create(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Long dishId,
        @RequestBody @Valid DiscountCreateRequest request
    ) {
        DiscountResponse discountResponse = adminDiscountService.create(dishId, request);
        ApiResponse<DiscountResponse> apiResponse = new ApiResponse<>(
            HttpStatus.CREATED.value(),
            true,
            "Tạo Discount thành công",
            discountResponse
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PatchMapping("/{discountId}")
    public ResponseEntity<ApiResponse<DiscountResponse>> update(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Long discountId,
        @RequestBody @Valid DiscountUpdateRequest request
    ) {
        DiscountResponse discountResponse = adminDiscountService.update(discountId, request);
        ApiResponse<DiscountResponse> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Cập nhật Discount thành công",
            discountResponse
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @DeleteMapping("/{discountId}")
    public ResponseEntity<ApiResponse<Void>> delete(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Long discountId
    ) {
        adminDiscountService.delete(discountId);
        ApiResponse<Void> apiResponse = new ApiResponse<>(
            HttpStatus.NO_CONTENT.value(),
            true,
            "Xóa Discount thành công",
            null
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<DiscountResponse>>> get(
        @ModelAttribute @Valid DiscountSearchRequest request
    ) {
        PageResponse<DiscountResponse> pageResponse = adminDiscountService.get(request);
        ApiResponse<PageResponse<DiscountResponse>> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy danh sách Discount thành công",
            pageResponse
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
