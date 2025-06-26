package com.anhpt.res_app.web.api;

import com.anhpt.res_app.common.dto.response.ApiResponse;
import com.anhpt.res_app.common.dto.response.PageResponse;
import com.anhpt.res_app.common.utils.ApiCategory;
import com.anhpt.res_app.common.utils.ApiDescription;
import com.anhpt.res_app.web.dto.request.PostGetRequest;
import com.anhpt.res_app.web.dto.response.PostResponse;
import com.anhpt.res_app.web.dto.response.PostShortResponse;
import com.anhpt.res_app.web.service.WebPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
@Validated
@ApiCategory(ApiCategory.CategoryType.PUBLIC)
public class WebPostApi {
    private final WebPostService webPostService;

    @GetMapping
    @ApiDescription("Lấy danh sách các bài viết (phát hành)")
    public ResponseEntity<ApiResponse<PageResponse<PostShortResponse>>> get(
        @ModelAttribute @Valid PostGetRequest request
    ) {
        PageResponse<PostShortResponse> pageResponse = webPostService.get(request);
        ApiResponse<PageResponse<PostShortResponse>> response = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy danh sách bài viết thành công",
            pageResponse
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{slug}")
    @ApiDescription("Lấy thông tin chi tiết của một bài viết (phát hành)")
    public ResponseEntity<ApiResponse<PostResponse>> getBySlug(
        @PathVariable String slug
    ) {
        PostResponse postResponse = webPostService.get(slug);
        ApiResponse<PostResponse> response = new ApiResponse<>(
            HttpStatus.OK.value(),
            true,
            "Lấy bài viết thành công",
            postResponse
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
