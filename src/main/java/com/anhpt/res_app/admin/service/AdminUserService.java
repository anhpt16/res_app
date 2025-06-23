package com.anhpt.res_app.admin.service;

import com.anhpt.res_app.admin.dto.AdminUserMapper;
import com.anhpt.res_app.admin.dto.request.user.UserCreateRequest;
import com.anhpt.res_app.admin.dto.request.user.UserSearchRequest;
import com.anhpt.res_app.admin.dto.response.user.UserResponse;
import com.anhpt.res_app.admin.dto.response.user.RoleByUserResponse;
import com.anhpt.res_app.admin.dto.response.user.UserRoleResponse;
import com.anhpt.res_app.admin.filter.AdminUserFilter;
import com.anhpt.res_app.admin.validation.AdminUserValidation;
import com.anhpt.res_app.common.dto.response.PageResponse;
import com.anhpt.res_app.common.entity.Role;
import com.anhpt.res_app.common.entity.User;
import com.anhpt.res_app.common.entity.UserRole;
import com.anhpt.res_app.common.entity.key.UserRoleId;
import com.anhpt.res_app.common.enums.status.UserStatus;
import com.anhpt.res_app.common.enums.type.UserType;
import com.anhpt.res_app.common.exception.ResourceNotFoundException;
import com.anhpt.res_app.common.repository.RoleRepository;
import com.anhpt.res_app.common.repository.UserRepository;
import com.anhpt.res_app.common.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminUserService {
    // Repository
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    // Validation
    private final AdminUserValidation adminUserValidation;
    // Mapper
    private final AdminUserMapper adminUserMapper;
    // Filter
    private final AdminUserFilter adminUserFilter;
    // Other
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse create(UserCreateRequest request) {
        adminUserValidation.validateCreate(request);
        User user = new User();
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus(UserStatus.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setType(UserType.LOCAL);
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getRoleIds() != null && request.getRoleIds().size() > 0) {
            List<UserRole> userRoles = new ArrayList<>();
            List<Role> roles = roleRepository.findAllById(request.getRoleIds());
            for (Role role : roles) {
                UserRole userRole = new UserRole();
                userRole.setUser(user);
                userRole.setRole(role);
                userRole.setCreatedAt(LocalDateTime.now());
                userRoles.add(userRole);
            }
            user.getUserRoles().addAll(userRoles);
        }
        user = userRepository.save(user);
        return adminUserMapper.toUserResponse(user);
    }

    public UserResponse updateStatus(Long userId, String status) {
        adminUserValidation.validateUpdateStatus(userId, status);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));
        UserStatus updatedStatus = UserStatus.fromCode(status);
        user.setStatus(updatedStatus);
        user.setUpdatedAt(LocalDateTime.now());
        user = userRepository.save(user);
        return adminUserMapper.toUserResponse(user);
    }

    public void delete(Long userId) {
        adminUserValidation.validateDelete(userId);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));
        userRepository.delete(user);
    }

    public UserResponse getById(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));
        return adminUserMapper.toUserResponse(user);
    }

    public PageResponse<UserResponse> get(UserSearchRequest request) {
        adminUserValidation.validateSearch(request);
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());
        Page<User> pageResult = userRepository.findAll(adminUserFilter.search(request), pageable);
        List<UserResponse> userResponses = pageResult.getContent().stream()
            .map(adminUserMapper::toUserResponse)
            .collect(Collectors.toList());
        return new PageResponse<>(
            userResponses,
            request.getPage(),
            request.getSize(),
            pageResult.getTotalElements(),
            pageResult.getTotalPages()
        );
    }
    
    // UserRole
    public RoleByUserResponse addRole(Long userId, Integer roleId) {
        adminUserValidation.validateAddRole(userId, roleId);
        // Thêm Role cho User
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new ResourceNotFoundException("Role không tồn tại"));
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);
        userRole.setCreatedAt(LocalDateTime.now());
        userRoleRepository.save(userRole);
        // Lấy tất cả Role của User
        List<UserRole> userRoles = userRoleRepository.findByUser(user);
        List<UserRoleResponse> userRoleResponses = userRoles.stream()
            .map(adminUserMapper::toUserRoleResponse)
            .collect(Collectors.toList());
        return adminUserMapper.toRoleByUserResponse(user, userRoleResponses);
    }

    public void deleteUserRole(Long userId, Integer roleId) {
        adminUserValidation.validateDeleteRole(userId, roleId);
        userRoleRepository.deleteByUserIdAndRoleId(userId, roleId);
    }

    public RoleByUserResponse getRolesByUserId(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));
        List<UserRole> userRoles = userRoleRepository.findByUser(user);
        List<UserRoleResponse> userRoleResponses = userRoles.stream()
            .map(adminUserMapper::toUserRoleResponse)
            .collect(Collectors.toList());
        return adminUserMapper.toRoleByUserResponse(user, userRoleResponses);
    }
}
