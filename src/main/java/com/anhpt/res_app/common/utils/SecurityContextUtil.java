package com.anhpt.res_app.common.utils;

import com.anhpt.res_app.common.dto.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SecurityContextUtil {
    /**
     * Lấy userId từ SecurityContext
     * @return userId hoặc null nếu không có authentication
     */
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return userPrincipal.getUserId();
        }
        return null;
    }
    /**
     * Lấy userId từ WebSocket STOMP message
     * @param message STOMP message từ WebSocket
     * @return userId hoặc null nếu không tìm thấy
     */
    public static Long getCurrentUserIdFromMessage(Message<?> message) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor != null && accessor.getUser() instanceof Authentication auth) {
            Object principal = auth.getPrincipal();
            if (principal instanceof UserPrincipal user) {
                return user.getUserId();
            }
        }
        return null;
    }
    /**
     * Kiểm tra xem có authentication hiện tại không
     * @return true nếu có authentication, false nếu không
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    /**
     * Lấy UserPrincipal từ SecurityContext
     * @return UserPrincipal hoặc null nếu không có
     */
    public static UserPrincipal getCurrentUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            return (UserPrincipal) authentication.getPrincipal();
        }
        return null;
    }
}
