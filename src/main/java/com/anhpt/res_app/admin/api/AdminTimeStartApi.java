package com.anhpt.res_app.admin.api;

import com.anhpt.res_app.admin.dto.request.TimeStartSearchRequest;
import com.anhpt.res_app.admin.dto.response.timestart.TimeStartResponse;
import com.anhpt.res_app.admin.dto.response.timestart.TimeStartDurationResponse;
import com.anhpt.res_app.admin.service.AdminTimeStartService;
import com.anhpt.res_app.common.dto.response.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin/time-start")
@RequiredArgsConstructor
@Validated
public class AdminTimeStartApi {
    private final AdminTimeStartService adminTimeStartService;

    // Tạo mới StartTime
    @PostMapping
    public ResponseEntity<ApiResponse<TimeStartResponse>> create(
        @RequestParam LocalTime timeStart
    ) {
        TimeStartResponse response = adminTimeStartService.create(timeStart);
        ApiResponse<TimeStartResponse> apiResponse = new ApiResponse<>(
            HttpStatus.CREATED.value(),
            true,
            "Tạo mới StartTime thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
    // Cập nhật trạng thái StartTime
    @PatchMapping("/{timeStartId}/status")
    public ResponseEntity<ApiResponse<TimeStartResponse>> updateStartTime(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Long timeStartId,
        @RequestParam (name = "status") String status
    ) {
        TimeStartResponse response = adminTimeStartService.updateStatus(timeStartId, status);
        ApiResponse<TimeStartResponse> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Cập nhật trạng thái StartTime thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    // Lấy danh sách StartTime
    @GetMapping
    public ResponseEntity<ApiResponse<List<TimeStartResponse>>> get(
        @ModelAttribute @Valid TimeStartSearchRequest request
    ) {
        List<TimeStartResponse> response = adminTimeStartService.get(request);
        ApiResponse<List<TimeStartResponse>> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy danh sách StartTime thành công",
            response
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    // Thêm thời lượng cho StartTime
    @PostMapping("/{timeStartId}/duration/{durationId}")
    public ResponseEntity<ApiResponse<TimeStartDurationResponse>> addDeskTimeStart(
        @PathVariable @Min(value = 1, message = "TimeStartId không hợp lệ") Long timeStartId,
        @PathVariable @Min(value = 1, message = "DurationId không hợp lệ") Long durationId
    ) {
        TimeStartDurationResponse timeStartDurationResponse = adminTimeStartService.addTimeStartDuration(timeStartId, durationId);
        ApiResponse<TimeStartDurationResponse> response = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Thêm thời lượng thành công",
            timeStartDurationResponse
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Xóa thời lượng của StartTime
    @DeleteMapping("/{timeStartId}/duration/{durationId}")
    public ResponseEntity<ApiResponse<Void>> deleteTimeStartDuration(
        @PathVariable @Min(value = 1, message = "TimeStartId không hợp lệ") Long timeStartId,
        @PathVariable @Min(value = 1, message = "DurationId không hợp lệ") Long durationId
    ) {
        adminTimeStartService.deleteTimeStartDuration(timeStartId, durationId);
        ApiResponse<Void> response = new ApiResponse<>(
            HttpStatus.NO_CONTENT.value(),
            true,
            "Xóa thời lượng thành công",
            null
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Lấy danh sách thời lượng của Start Time
    @GetMapping("/{timeStartId}/duration")
    public ResponseEntity<ApiResponse<List<TimeStartDurationResponse>>> getTimeStartDurations(
        @PathVariable @Min(value = 1, message = "TimeStartId không hợp lệ") Long timeStartId
    ) {
        List<TimeStartDurationResponse> timeStartDurationResponses = adminTimeStartService.getTimeStartDurations(timeStartId);
        ApiResponse<List<TimeStartDurationResponse>> response = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy danh sách thời lượng thành công",
            timeStartDurationResponses
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
