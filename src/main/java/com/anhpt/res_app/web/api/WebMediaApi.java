package com.anhpt.res_app.web.api;

import com.anhpt.res_app.common.utils.ApiCategory;
import com.anhpt.res_app.common.utils.ApiDescription;
import com.anhpt.res_app.web.service.WebMediaService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
@Validated
@ApiCategory(ApiCategory.CategoryType.PUBLIC)
public class WebMediaApi {

    private final WebMediaService webMediaService;

    @GetMapping("/{fileName:.+}")
    @ApiDescription("Lấy tệp")
    public ResponseEntity<Resource> loadMedia(
        @PathVariable @NotBlank String fileName
    ) {
        return webMediaService.loadMedia(fileName);
    }

}
