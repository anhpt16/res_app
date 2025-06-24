package com.anhpt.res_app.common.utils;

import com.anhpt.res_app.admin.dto.response.ApiInfo;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ApiScanner {
    private final RequestMappingHandlerMapping handlerMapping;
    public static Map<String, List<ApiInfo>> currentApis;

    @PostConstruct
    public void init() {
        currentApis = getCategorizedApis();
    }

    public Map<String, List<ApiInfo>> getCategorizedApis() {
        Map<String, List<ApiInfo>> categorized = new HashMap<>();
        // Khởi tạo các nhóm theo enum ApiCategory.CategoryType
        for (ApiCategory.CategoryType type : ApiCategory.CategoryType.values()) {
            categorized.put(type.name(), new ArrayList<>());
        }
        categorized.put("UNCATEGORIZED", new ArrayList<>());

        // Lấy tất cả handler method (các controller và endpoint)
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            RequestMappingInfo mappingInfo = entry.getKey();
            HandlerMethod handlerMethod = entry.getValue();
            // Lấy annotation @ApiCategory trên method hoặc class
            ApiCategory annotation = handlerMethod.getMethodAnnotation(ApiCategory.class);
            if (annotation == null) {
                annotation = handlerMethod.getBeanType().getAnnotation(ApiCategory.class);
            }
            // Lấy path từ PathPatternsCondition (Spring Boot 3+)
            Set<String> paths = mappingInfo.getPathPatternsCondition()
                .getPatterns()
                .stream()
                .map(PathPattern::getPatternString)
                .filter(path -> !"/error".equals(path)) // Bỏ qua /error
                .collect(Collectors.toSet());
            // Lấy HTTP methods (GET, POST,...)
            Set<RequestMethod> methods = mappingInfo.getMethodsCondition().getMethods();
            for (String path : paths) {
                if (methods.isEmpty()) {
                    // Không có method cụ thể → ALL
                    ApiInfo apiInfo = new ApiInfo(path, "ALL");
                    addApiToGroup(categorized, annotation, apiInfo);
                } else {
                    for (RequestMethod method : methods) {
                        ApiInfo apiInfo = new ApiInfo(path, method.name());
                        addApiToGroup(categorized, annotation, apiInfo);
                    }
                }
            }
        }

        return categorized;
    }
    // Helper: Thêm API vào đúng nhóm (hoặc UNCATEGORIZED)
    private void addApiToGroup(Map<String, List<ApiInfo>> map, ApiCategory annotation, ApiInfo apiInfo) {
        if (annotation != null) {
            map.get(annotation.value().name()).add(apiInfo);
        } else {
            map.get("UNCATEGORIZED").add(apiInfo);
        }
    }
}
