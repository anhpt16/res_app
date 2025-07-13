package com.anhpt.res_app.common.config.security;

import com.anhpt.res_app.common.dto.UserPrincipal;
import com.anhpt.res_app.common.utils.JwtTokenProvider;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class SocketAuthHandshake implements HandshakeInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String token = extractToken(request);
        // Kiểm tra Token
        if (token == null) {
            log.warn("Không tìm thấy Token");
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }
        if (!validateToken(token)) {
            log.warn("Token không hợp lệ");
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }
        // TODO: Thêm tính năng phân quyền với endpoint WebSocket
        // TODO: Kiểm tra user có quyền thao tác với WebSocket
        // Thiết lập User hiện tại
        Long userId = jwtTokenProvider.getUserId(token);
        UserPrincipal userPrincipal = new UserPrincipal(userId);
        attributes.put("user", userPrincipal);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }

    private String extractToken(ServerHttpRequest request) {
        if (request instanceof ServletServerHttpRequest servletRequest) {
            Cookie[] cookies = servletRequest.getServletRequest().getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("accessToken".equals(cookie.getName())) {
                        return cookie.getValue();
                    }
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
