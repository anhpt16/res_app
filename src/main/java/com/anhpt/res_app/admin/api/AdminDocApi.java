package com.anhpt.res_app.admin.api;

import com.anhpt.res_app.admin.dto.response.ApiInfo;
import com.anhpt.res_app.common.utils.ApiScanner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/admin/api-docs")
@RequiredArgsConstructor
@Slf4j
public class AdminDocApi {
    private final ApiScanner apiScanner;

    @GetMapping("/list")
    public Map<String, List<ApiInfo>> getApis() {
        return apiScanner.getCategorizedApis();
    }
}
