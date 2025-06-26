package com.anhpt.res_app.web.api;

import com.anhpt.res_app.common.dto.response.ApiResponse;
import com.anhpt.res_app.common.dto.response.PageResponse;
import com.anhpt.res_app.common.utils.ApiCategory;
import com.anhpt.res_app.common.utils.ApiDescription;
import com.anhpt.res_app.web.dto.request.CollectionGetRequest;
import com.anhpt.res_app.web.dto.response.CollectionResponse;
import com.anhpt.res_app.web.service.WebCollectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/collection")
@RequiredArgsConstructor
@Validated
@ApiCategory(ApiCategory.CategoryType.PUBLIC)
public class WebCollectionApi {
    private final WebCollectionService webCollectionService;

    @GetMapping
    @ApiDescription("Lấy bộ sưu tập (phát hành)")
    public ResponseEntity<ApiResponse<PageResponse<CollectionResponse>>> get(
        @ModelAttribute @Valid CollectionGetRequest request
    ) {
        PageResponse<CollectionResponse> pageResponse = webCollectionService.get(request);
        ApiResponse<PageResponse<CollectionResponse>> response = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy danh sách bộ sưu tập thành công",
            pageResponse
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
