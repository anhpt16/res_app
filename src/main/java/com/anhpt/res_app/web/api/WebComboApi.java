package com.anhpt.res_app.web.api;

import com.anhpt.res_app.common.dto.response.ApiResponse;
import com.anhpt.res_app.web.dto.response.combo.ComboDishShortResponse;
import com.anhpt.res_app.web.dto.response.combo.ComboResponse;
import com.anhpt.res_app.web.dto.response.combo.ComboShortResponse;
import com.anhpt.res_app.web.service.WebComboService;
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
@RequestMapping("/api/combo")
@RequiredArgsConstructor
@Validated
public class WebComboApi {
    private final WebComboService webComboService;

    // Lấy danh sách combo đang phát hành
    @GetMapping
    public ResponseEntity<ApiResponse<List<ComboShortResponse>>> get() {
        List<ComboShortResponse> comboShortResponses = webComboService.getCombos();
        ApiResponse<List<ComboShortResponse>> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy danh sách combo đang phát hành thành công",
            comboShortResponses
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    // Lấy chi tiết một combo đang phát hành
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ComboResponse>> getById(
        @PathVariable @Min(value = 1, message = "Id không hợp lệ") Long id
    ) {
        ComboResponse comboResponse = webComboService.getComboById(id);
        ApiResponse<ComboResponse> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy chi tiết combo đang phát hành thành công",
            comboResponse
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    // Lấy danh sách các món ăn đang phát hành của một comboVersion
    @GetMapping("/{comboId}/combo-version/{comboVersionId}/dishes")
    public ResponseEntity<ApiResponse<List<ComboDishShortResponse>>> getComboVersionDishes(
        @PathVariable @Min(value = 1, message = "CId không hợp lệ") Long comboId,
        @PathVariable @Min(value = 1, message = "VId không hợp lệ") Long comboVersionId
    ) {
        List<ComboDishShortResponse> comboDishShortResponses = webComboService.getComboVersionDishes(comboId, comboVersionId);
        ApiResponse<List<ComboDishShortResponse>> apiResponse = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy danh sách món ăn của combo version đang phát hành thành công",
            comboDishShortResponses
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

}
