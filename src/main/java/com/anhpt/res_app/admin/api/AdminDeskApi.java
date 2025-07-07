package com.anhpt.res_app.admin.api;

import com.anhpt.res_app.admin.dto.request.desk.*;
import com.anhpt.res_app.admin.dto.response.desk.DeskDurationResponse;
import com.anhpt.res_app.admin.dto.response.desk.DeskMediaResponse;
import com.anhpt.res_app.admin.dto.response.desk.DeskResponse;
import com.anhpt.res_app.admin.dto.response.desk.DeskShortResponse;
import com.anhpt.res_app.admin.service.AdminDeskService;
import com.anhpt.res_app.common.dto.response.ApiResponse;
import com.anhpt.res_app.common.dto.response.PageResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/desk")
@RequiredArgsConstructor
@Validated
public class AdminDeskApi {
    private final AdminDeskService adminDeskService;

    // SECTION: Bàn
    // Tạo mới bàn
    @PostMapping
    public ResponseEntity<ApiResponse<DeskResponse>> createDesk(
        @RequestBody @Valid DeskCreateRequest request
    ) {
        DeskResponse response = adminDeskService.createDesk(request);
        ApiResponse<DeskResponse> apiResponse = new ApiResponse<>(
            HttpStatus.CREATED.value(),
            true,
            "Tạo bàn thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    // Cập nhật thông tin bàn
    @PatchMapping("/{deskNumber}")
    public ResponseEntity<ApiResponse<DeskResponse>> updateDesk(
        @PathVariable @Min(value = 1, message = "DeskNumber không hợp lệ") Integer deskNumber,
        @RequestBody @Valid DeskUpdateRequest request
    ) {
        DeskResponse response = adminDeskService.updateDesk(deskNumber, request);
        ApiResponse<DeskResponse> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Cập nhật bàn thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    // Lấy thông tin chi tiết của bàn
    @GetMapping("/{deskNumber}")
    public ResponseEntity<ApiResponse<DeskResponse>> getDeskByNumber(
       @PathVariable @Min(value = 1, message = "DeskNumber không hợp lệ") Integer deskNumber
    ) {
        DeskResponse response = adminDeskService.getDesk(deskNumber);
        ApiResponse<DeskResponse> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy bàn thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    // Cập nhật trạng thái của bàn
    @PatchMapping("/{deskNumber}/status")
    public ResponseEntity<ApiResponse<DeskResponse>> updateDeskStatus(
        @PathVariable @Min(value = 1, message = "DeskNumber không hợp lệ") Integer deskNumber,
        @RequestParam (name = "status") String status
    ) {
        DeskResponse response = adminDeskService.updateDeskStatus(deskNumber, status);
        ApiResponse<DeskResponse> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Cập nhật trạng thái bàn thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    // Lấy danh sách bàn
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<DeskShortResponse>>> getDesks(
        @ModelAttribute @Valid DeskSearchRequest request
    ) {
        PageResponse<DeskShortResponse> response = adminDeskService.getDesks(request);
        ApiResponse<PageResponse<DeskShortResponse>> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy danh sách bàn thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    // SECTION: Ảnh của bàn
    // Thêm ảnh cho một bàn
    @PostMapping("/{deskNumber}/media/{mediaId}")
    public ResponseEntity<ApiResponse<DeskMediaResponse>> addDeskMedia(
        @PathVariable @Min(value = 1, message = "DeskNumber không hợp lệ") Integer deskNumber,
        @PathVariable @Min(value = 1, message = "MediaId không hợp lệ") Long mediaId,
        @RequestBody @Valid DeskMediaRequest request
    ) {
        DeskMediaResponse response = adminDeskService.addDeskMedia(deskNumber, mediaId, request);
        ApiResponse<DeskMediaResponse> apiResponse = new ApiResponse<>(
            HttpStatus.CREATED.value(),
            true,
            "Thêm ảnh cho bàn thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    // Cập nhật ảnh cho bàn
    @PatchMapping("/{deskNumber}/media/{mediaId}")
    public ResponseEntity<ApiResponse<DeskMediaResponse>> updateDeskMedia(
        @PathVariable @Min(value = 1, message = "DeskNumber không hợp lệ") Integer deskNumber,
        @PathVariable @Min(value = 1, message = "MediaId không hợp lệ") Long mediaId,
        @RequestBody @Valid DeskMediaRequest request
    ) {
        DeskMediaResponse response = adminDeskService.updateDeskMedia(deskNumber, mediaId, request);
        ApiResponse<DeskMediaResponse> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Cập nhật ảnh cho bàn thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    // Xóa ảnh khỏi bàn
    @DeleteMapping("/{deskNumber}/media/{mediaId}")
    public ResponseEntity<ApiResponse<Void>> deleteDeskMedia(
        @PathVariable @Min(value = 1, message = "DeskNumber không hợp lệ") Integer deskNumber,
        @PathVariable @Min(value = 1, message = "MediaId không hợp lệ") Long mediaId
    ) {
        adminDeskService.deleteDeskMedia(deskNumber, mediaId);
        ApiResponse<Void> apiResponse = new ApiResponse<>(
            HttpStatus.NO_CONTENT.value(),
            true,
            "Xóa ảnh cho bàn thành công",
            null
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    // Lấy danh sách ảnh của bàn
    @GetMapping("/{deskNumber}/media")
    public ResponseEntity<ApiResponse<List<DeskMediaResponse>>> getDeskMedias(
        @PathVariable @Min(value = 1, message = "DeskNumber không hợp lệ") Integer deskNumber
    ) {
        List<DeskMediaResponse> response = adminDeskService.getDeskMedias(deskNumber);
        ApiResponse<List<DeskMediaResponse>> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy danh sách ảnh cho bàn thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    // SECTION: Thời lượng bàn
    // Thêm thời lượng cho bàn
    @PostMapping("/{deskNumber}/duration/{durationId}")
    public ResponseEntity<ApiResponse<DeskDurationResponse>> createDeskDuration(
        @PathVariable @Min(value = 1, message = "DeskNumber không hợp lệ") Integer deskNumber,
        @PathVariable @Min(value = 1, message = "DurationId không hợp lệ") Long durationId,
        @RequestBody @Valid DeskDurationRequest request
    ) {
        DeskDurationResponse response = adminDeskService.addDeskDuration(deskNumber, durationId, request);
        ApiResponse<DeskDurationResponse> apiResponse = new ApiResponse<>(
            HttpStatus.CREATED.value(),
            true,
            "Thêm thời lượng cho bàn thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    // Cập nhật thông tin thời lượng của bàn
    @PatchMapping("/{deskNumber}/duration/{durationId}")
    public ResponseEntity<ApiResponse<DeskDurationResponse>> updateDeskDuration(
        @PathVariable @Min(value = 1, message = "DeskNumber không hợp lệ") Integer deskNumber,
        @PathVariable @Min(value = 1, message = "DurationId không hợp lệ") Long durationId,
        @RequestBody @Valid DeskDurationRequest request
    ) {
        DeskDurationResponse response = adminDeskService.updateDeskDuration(deskNumber, durationId, request);
        ApiResponse<DeskDurationResponse> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Cập nhật thời lượng cho bàn thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    // TODO: Thêm xử lý để khi thời lượng dừng hoạt động thì thời lượng tương ứng tại các bàn bị vô hiệu hóa
    //  , vẫn có thể thao tác với thời lượng đó -> nhưng sẽ không hiển thị với user
    // Cập nhật trạng thái thời lượng của bàn
    @PatchMapping("/{deskNumber}/duration/{durationId}/status")
    public ResponseEntity<ApiResponse<DeskDurationResponse>> updateDeskDurationStatus(
        @PathVariable @Min(value = 1, message = "DeskNumber không hợp lệ") Integer deskNumber,
        @PathVariable @Min(value = 1, message = "DurationId không hợp lệ") Long durationId,
        @RequestParam (name = "status") String status
    ) {
        DeskDurationResponse response = adminDeskService.updateDeskDurationStatus(deskNumber, durationId, status);
        ApiResponse<DeskDurationResponse> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Cập nhật trạng thái thời lượng cho bàn thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    // Xóa thời lượng của một bàn
    @DeleteMapping("/{deskNumber}/duration/{durationId}")
    public ResponseEntity<ApiResponse<Void>> deleteDeskDuration(
        @PathVariable @Min(value = 1, message = "DeskNumber không hợp lệ") Integer deskNumber,
        @PathVariable @Min(value = 1, message = "DurationId không hợp lệ") Long durationId
    ) {
        adminDeskService.deleteDeskDuration(deskNumber, durationId);
        ApiResponse<Void> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Xóa thời lượng cho bàn thành công",
            null
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    // Lấy danh sách thời lượng của bàn
    @GetMapping("/{deskNumber}/duration")
    public ResponseEntity<ApiResponse<PageResponse<DeskDurationResponse>>> getDeskDuration(
        @PathVariable @Min(value = 1, message = "DeskNumber không hợp lệ") Integer deskNumber,
        @ModelAttribute @Valid DeskDurationSearchRequest request
    ) {
        PageResponse<DeskDurationResponse> response = adminDeskService.getDeskDurations(deskNumber, request);
        ApiResponse<PageResponse<DeskDurationResponse>> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy danh sách thời lượng cho bàn thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
