package com.anhpt.res_app.admin.api;

import com.anhpt.res_app.admin.dto.request.collection.CollectionCreateRequest;
import com.anhpt.res_app.admin.dto.request.collection.CollectionSearchRequest;
import com.anhpt.res_app.admin.dto.request.collection.CollectionUpdateRequest;
import com.anhpt.res_app.admin.dto.response.collection.CollectionResponse;
import com.anhpt.res_app.admin.dto.response.collection.CollectionShortResponse;
import com.anhpt.res_app.admin.service.AdminCollectionService;
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
@RequestMapping("/api/admin/collection")
@RequiredArgsConstructor
@Validated
@ApiCategory(ApiCategory.CategoryType.M_POST)
public class AdminCollectionApi {
    private final AdminCollectionService adminCollectionService;

    @PostMapping
    @ApiDescription("Thêm mới phương tiện vào bộ sưu tập")
    public ResponseEntity<ApiResponse<CollectionResponse>> create(
        @RequestBody @Valid CollectionCreateRequest request
    ) {
        CollectionResponse response = adminCollectionService.create(request);
        ApiResponse<CollectionResponse> apiResponse = new ApiResponse<>(
            HttpStatus.CREATED.value(),
            true,
            "Thêm mới vào bộ sưu tập thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PatchMapping("/{id}")
    @ApiDescription("Cập nhật một phương tiện trong bộ sưu tập")
    public ResponseEntity<ApiResponse<CollectionResponse>> update(
        @PathVariable @Min(value = 1,message = "Id không hợp lệ") Long id,
        @RequestBody @Valid CollectionUpdateRequest request
    ) {
        CollectionResponse response = adminCollectionService.update(id, request);
        ApiResponse<CollectionResponse> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Cập nhật bộ sưu tập thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    @ApiDescription("Xóa một phương tiện khỏi bộ sưu tập")
    public ResponseEntity<ApiResponse<Void>> delete(
        @PathVariable @Min(value = 1,message = "Id không hợp lệ") Long id
    ) {
        adminCollectionService.delete(id);
        ApiResponse<Void> apiResponse = new ApiResponse<>(
            HttpStatus.NO_CONTENT.value(),
            true,
            "Xóa bộ sưu tập thành công",
            null
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/{id}")
    @ApiDescription("Lấy thông tin chi tiết một phương tiện trong bộ sưu tập")
    public ResponseEntity<ApiResponse<CollectionResponse>> getById(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Long id
    ) {
        CollectionResponse response = adminCollectionService.getById(id);
        ApiResponse<CollectionResponse> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy bộ sưu tập thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping
    @ApiDescription("Lấy danh sách các phương tiện trong bộ sưu tập")
    public ResponseEntity<ApiResponse<PageResponse<CollectionShortResponse>>> get(
        @ModelAttribute @Valid CollectionSearchRequest request
    ) {
        PageResponse<CollectionShortResponse> response = adminCollectionService.get(request);
        ApiResponse<PageResponse<CollectionShortResponse>> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy danh sách bộ sưu tập thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    // TODO: Api làm mới thời điểm phát hành
}
