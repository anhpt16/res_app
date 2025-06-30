package com.anhpt.res_app.common.config.security;

import com.anhpt.res_app.common.dto.UserPrincipal;
import com.anhpt.res_app.common.dto.result.PermissionResult;
import com.anhpt.res_app.common.entity.Permission;
import com.anhpt.res_app.common.service.PermissionService;
import com.anhpt.res_app.common.service.RoleService;
import com.anhpt.res_app.common.utils.ApiMatch;
import com.anhpt.res_app.common.utils.JwtTokenProvider;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Set;

//@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final PermissionService permissionService;
    private final RoleService roleService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Kiểm tra xem api cần xác thực không -> Nếu không, tiếp tục filter chain
        Boolean requiresAuth = (Boolean) request.getAttribute("requiresAuth");
        if (requiresAuth == null || !requiresAuth) {
            filterChain.doFilter(request, response);
            return;
        }
        // Nếu api cần xác thực -> Kiểm tra token
        String token = extractToken(request);
        if (token == null) {
            log.warn("Không tìm thấy Token");
            ErrorFilter.writeErrorResponse(response, HttpStatus.UNAUTHORIZED, "Chức năng cần đăng nhập");
            return;
        }
        if (!validateToken(token)) {
            ErrorFilter.writeErrorResponse(response, HttpStatus.UNAUTHORIZED, "Vui lòng đăng nhâp lại");
            return;
        }
        // Nếu token hợp lệ -> lấy userId và kiểm tra các thông tin liên quan (roleId)
        Long userId = jwtTokenProvider.getUserId(token);
        Set<Integer> roleIds = roleService.getRolesByUserId(userId);

        // TODO: Xử lý nếu roleId hiện tại của trong token khác với roleId trong DB nhưng token vẫn hợp lệ
        // TODO: Mục đích -> Các Role mới được trả về giúp Client cập nhật lại Menu người dùng

        // Kiểm tra danh sách permission của role với api hiện tại
        Set<PermissionResult> rolePermissions = permissionService.getPermissionByRoleIds(roleIds);
        if (!ApiMatch.isApiMatched(request.getMethod(), request.getRequestURI(), rolePermissions)) {
            // Nếu không khớp thì chặn
            log.warn("UserId {} không có quyền thực hiện Api: {} - {}", jwtTokenProvider.getUserId(token), request.getMethod(), request.getRequestURI());
            ErrorFilter.writeErrorResponse(response, HttpStatus.FORBIDDEN, "Không đủ quyền hạn");
            return;
        }
        // Nếu khớp thì lưu userId vào SecurityContext và tiếp tục filter chain
        setAuthenticationInContext(userId);
        filterChain.doFilter(request, response);
    }

    /**
     * Lưu thông tin authentication vào SecurityContext
     * @param userId ID của user hiện tại
     */
    private void setAuthenticationInContext(Long userId) {
        UserPrincipal userPrincipal = new UserPrincipal(userId);
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(userPrincipal, null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String extractToken(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private boolean validateToken(String token) {
        try {
            jwtTokenProvider.parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Token không hợp lệ: {}", token);
            return false;
        }
    }
}
