package com.anhpt.res_app.common.utils;

import com.anhpt.res_app.admin.dto.response.ApiInfo;
import com.anhpt.res_app.common.dto.result.CategoryMatchResult;
import com.anhpt.res_app.common.dto.result.PermissionResult;
import com.anhpt.res_app.common.entity.Permission;
import com.anhpt.res_app.common.entity.key.PermissionId;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ApiMatch {
    public static CategoryMatchResult matchSystemApis(String requestMethod, String requestUri, Map<String, List<ApiInfo>> systemApis) {
        PathPatternParser parser = new PathPatternParser();

        for (Map.Entry<String, List<ApiInfo>> entry : systemApis.entrySet()) {
            String categoryName = entry.getKey();
            List<ApiInfo> apiInfoList = entry.getValue();

            for (ApiInfo apiInfo : apiInfoList) {
                if (!apiInfo.getMethod().equalsIgnoreCase(requestMethod)) continue;

                PathPattern pattern = parser.parse(apiInfo.getPath());
                PathContainer pathContainer = PathContainer.parsePath(requestUri);

                if (pattern.matches(pathContainer)) {
                    if ("UNCATEGORIZED".equalsIgnoreCase(categoryName)) {
                        return CategoryMatchResult.uncategorized();
                    }
                    try {
                        ApiCategory.CategoryType categoryType = ApiCategory.CategoryType.valueOf(categoryName);
                        return CategoryMatchResult.categorized(categoryType);
                    } catch (IllegalArgumentException e) {
                        return CategoryMatchResult.uncategorized();
                    }
                }
            }
        }
        return CategoryMatchResult.notMatched();
    }

    public static boolean isApiMatched(String requestMethod, String requestUri, Set<PermissionResult> permissions) {
        PathPatternParser parser = new PathPatternParser();

        for (PermissionResult permission : permissions) {
            if (!permission.method().name().equalsIgnoreCase(requestMethod)) continue;

            PathPattern pattern = parser.parse(permission.uri());
            PathContainer pathContainer = PathContainer.parsePath(requestUri);
            // Khớp api
            if (pattern.matches(pathContainer)) return true;
        }

        return false;
    }
}
