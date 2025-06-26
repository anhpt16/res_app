package com.anhpt.res_app.admin.api;

import com.anhpt.res_app.admin.dto.response.ApiInfo;
import com.anhpt.res_app.common.utils.ApiCategory;
import com.anhpt.res_app.common.utils.ApiDescription;
import com.anhpt.res_app.common.utils.ApiScanner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/admin/api-docs")
@RequiredArgsConstructor
@Validated
@ApiCategory(ApiCategory.CategoryType.PUBLIC)
public class AdminDocApi {
    private final ApiScanner apiScanner;

    @GetMapping("/list")
    @ApiDescription("Lấy danh sách quyền hạn của hệ thống")
    public Map<String, List<ApiInfo>> getApis() {
        return apiScanner.getCategorizedApis();
    }
}
