package com.anhpt.res_app.common.config.security;

import com.anhpt.res_app.admin.dto.response.ApiInfo;
import com.anhpt.res_app.common.dto.result.CategoryMatchResult;
import com.anhpt.res_app.common.utils.ApiCategory;
import com.anhpt.res_app.common.utils.ApiMatch;
import com.anhpt.res_app.common.utils.ApiScanner;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

//@Component
@Slf4j
public class ApiAuthenticationRequirementFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Kiểm tra API cần xác thực không
        // 1. Lấy api hiện tại (method, uri)
        String path = request.getRequestURI();
        String method = request.getMethod();
        // 2. Lấy danh sách api của hệ thống
        Map<String, List<ApiInfo>> systemApis = ApiScanner.currentApis;
        if (systemApis.size() == 0) {
            log.error("Không tìm thấy SystemApis");
            ErrorFilter.writeErrorHttpResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "Đã có lỗi sảy ra");
            return;
        }
        // 3. Kiểm tra xem api hiện tại có trùng khớp với api nào của hệ thống hay không -> Nếu không thì ném ra lỗi
        CategoryMatchResult categoryMatchResult = ApiMatch.matchSystemApis(method, path, systemApis);
        // Nếu không trùng với bất kỳ Api nào
        if (!categoryMatchResult.isMatched()) {
            log.error("Không tìm thấy api Method: {}, Path: {}", method, path);
            ErrorFilter.writeErrorHttpResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "Đã có lỗi sảy ra");
            return;
        }

        boolean requiresAuth = true;
        // 4. Nếu tìm thấy api trùng và api public
        if (categoryMatchResult.isMatched() && !categoryMatchResult.isUncategorized()) {
            // 5. Nếu danh mục là public thì requiresAuth = false
            requiresAuth = !categoryMatchResult.getCategory().equals(ApiCategory.CategoryType.PUBLIC);
        }
        request.setAttribute("requiresAuth", requiresAuth);
        filterChain.doFilter(request, response);
    }
}
