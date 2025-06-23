package com.anhpt.res_app.admin.validation;

import com.anhpt.res_app.admin.dto.request.user.UserCreateRequest;
import com.anhpt.res_app.admin.dto.request.user.UserSearchRequest;
import com.anhpt.res_app.common.entity.Role;
import com.anhpt.res_app.common.entity.User;
import com.anhpt.res_app.common.enums.status.UserStatus;
import com.anhpt.res_app.common.exception.ForbiddenActionException;
import com.anhpt.res_app.common.exception.MultiDuplicateException;
import com.anhpt.res_app.common.exception.ResourceNotFoundException;
import com.anhpt.res_app.common.repository.RoleRepository;
import com.anhpt.res_app.common.repository.UserRepository;
import com.anhpt.res_app.common.utils.function.FieldNameUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminUserValidation {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public void validateCreate(UserCreateRequest request) {
        Map<String, String> errors = new HashMap<>();
        // Kiểm tra trùng lặp tên đăng nhập
        boolean isUsernameExist = userRepository.existsByUsername(request.getUsername());
        if (isUsernameExist) {
            String field = FieldNameUtil.getFieldName(UserCreateRequest::getUsername);
            errors.put(field, "Đã tồn tại");
        }
        // Kiểm tra tồn tại của Role
        if (request.getRoleIds() != null && request.getRoleIds().size() > 0) {
            for (Integer roleId : request.getRoleIds()) {
                boolean isRoleExist = roleRepository.existsById(roleId);
                if (!isRoleExist) {
                    log.warn("Role không tồn tại: {}", roleId);
                    throw new ResourceNotFoundException("Role không tồn tại");
                }
            }
        }
        if (errors.size() > 0) {
            throw new MultiDuplicateException(errors);
        }
    }

    public void validateUpdateStatus(Long userId, String status) {
        // Kiểm tra user có tồn tại không
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));
        // Kiểm tra trạng thái hợp lệ
        UserStatus updatedStatus = UserStatus.fromCode(status);
        // Kiểm tra trạng thái trùng lặp
        if (user.getStatus().equals(updatedStatus)) {
            throw new IllegalArgumentException("Trạng thái không hợp lệ");
        }
    }

    public void validateDelete(Long userId) {
        // Kiểm tra user có tồn tại không
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));
        // Kiểm tra user phải ở trạng thái inactive
        if(!user.getStatus().equals(UserStatus.INACTIVE)) {
            throw new ForbiddenActionException("Hành động không được phép");
        }
        // TODO: Các kiểm tra khác
    }

    public void validateSearch(UserSearchRequest request) {
        // Kiểm tra trạng thái hợp lệ
        if (request.getStatus() != null) {
            UserStatus status = UserStatus.fromCode(request.getStatus());
        }
        // Kiểm tra Role tồn tại
        if (request.getRoleId() != null) {
            boolean isRoleExist = roleRepository.existsById(request.getRoleId());
            if (!isRoleExist) {
                log.warn("Role không tồn tại: {}", request.getRoleId());
                throw new ResourceNotFoundException("Role không tồn tại");
            }
        }
    }

    // UserRole
    public void validateAddRole(Long userId, Integer roleId) {
        // Kiểm tra user có tồn tại không
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));
        // Kiểm tra role có tồn tại không
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new ResourceNotFoundException("Role không tồn tại"));
        // Kiểm tra user đã có role này chưa
        boolean isRoleExist = user.getUserRoles().stream()
            .anyMatch(userRole -> userRole.getRole().getId().equals(roleId));
        if (isRoleExist) {
            log.warn("User {} đã có Role {}", userId, roleId);
            throw new IllegalArgumentException("User đã có role này");
        }
    }

    public void validateDeleteRole(Long userId, Integer roleId) {
        // Kiểm tra user có tồn tại không
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));
        // Kiểm tra role có tồn tại không
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new ResourceNotFoundException("Role không tồn tại"));
        // Kiểm tra UserRole có tồn tại
        boolean isUserRoleExist = user.getUserRoles().stream()
            .anyMatch(userRole -> userRole.getRole().getId().equals(roleId));
        if (!isUserRoleExist) {
            log.warn("User {} không có Role {}", userId, roleId);
            throw new IllegalArgumentException("User không có role này");
        }
    }
}
