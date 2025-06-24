package com.anhpt.res_app.admin.service;

import com.anhpt.res_app.admin.dto.AdminPermissionMapper;
import com.anhpt.res_app.admin.dto.request.pemission.PermissionRequest;
import com.anhpt.res_app.admin.dto.response.ApiInfo;
import com.anhpt.res_app.admin.dto.response.permission.PermissionResponse;
import com.anhpt.res_app.admin.validation.AdminPermissionValidation;
import com.anhpt.res_app.common.entity.Permission;
import com.anhpt.res_app.common.entity.Role;
import com.anhpt.res_app.common.entity.key.PermissionId;
import com.anhpt.res_app.common.enums.FeatureMethod;
import com.anhpt.res_app.common.exception.ResourceNotFoundException;
import com.anhpt.res_app.common.repository.PermissionRepository;
import com.anhpt.res_app.common.repository.RoleRepository;
import com.anhpt.res_app.common.utils.ApiScanner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminPermissionService {
    // Repository
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    // Validation
    private final AdminPermissionValidation adminPermissionValidation;
    // Mapper
    private final AdminPermissionMapper adminPermissionMapper;

    // Thêm quyền hạn cho một vai trò
    public PermissionResponse addPermission(Integer roleId, PermissionRequest request) {
        adminPermissionValidation.validateAddPermission(roleId, request);
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Role"));
        // Thiết lập permisisonId
        PermissionId permissionId = new PermissionId();
        permissionId.setRoleId(roleId);
        permissionId.setFeatureMethod(FeatureMethod.fromCode(request.getMethod()));
        permissionId.setFeatureUri(request.getUri());
        // Tạo permission
        Permission permission = new Permission();
        permission.setId(permissionId);
        permission.setRole(role);
        permission.setCreatedAt(LocalDateTime.now());
        permission = permissionRepository.save(permission);
        return adminPermissionMapper.toPermissionResponse(permission);
    }

    // Xóa quyền hạn cho một vai trò
    public void deletePermission(Integer roleId, PermissionRequest request) {
        adminPermissionValidation.validateDeletePermission(roleId, request);
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Role"));
        // Tạo permissionId
        PermissionId permissionId = new PermissionId();
        permissionId.setRoleId(roleId);
        permissionId.setFeatureMethod(FeatureMethod.fromCode(request.getMethod()));
        permissionId.setFeatureUri(request.getUri());
        // Xóa
        role.getPermissions().removeIf(permission -> permission.getId().equals(permissionId));
        roleRepository.save(role);
    }

    // Lấy tất cả quyền hạn của một vai trò
    public Map<String, List<PermissionResponse>> get(Integer roleId) {
        adminPermissionValidation.validateGetPermission(roleId);
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Role"));
        // Tìm kiếm permissions
        List<Permission> permissions = permissionRepository.findByRole(role);
        // Lấy danh sách apis hiện tại
        Map<String, List<ApiInfo>> currentApis = ApiScanner.currentApis;
        return mergePermissions(roleId, permissions, currentApis);
    }

    private Map<String, List<PermissionResponse>> mergePermissions(Integer roleId, List<Permission> permissions, Map<String, List<ApiInfo>> currentApis) {
        Map<String, List<PermissionResponse>> result = new LinkedHashMap<>();

        // 1. Duyệt từng nhóm API (category)
        for (Map.Entry<String, List<ApiInfo>> entry : currentApis.entrySet()) {
            String category = entry.getKey();
            List<ApiInfo> apiInfos = entry.getValue();
            List<PermissionResponse> permissionResponses = new ArrayList<>();

            for (ApiInfo apiInfo : apiInfos) {
                String uri = apiInfo.getPath();
                FeatureMethod method;

                try {
                    method = FeatureMethod.valueOf(apiInfo.getMethod());
                } catch (IllegalArgumentException e) {
                    continue; // Bỏ qua method không hợp lệ
                }

                // Tìm permission tương ứng
                Optional<Permission> match = permissions.stream()
                    .filter(p -> Objects.equals(p.getId().getFeatureUri(), uri)
                        && p.getId().getFeatureMethod() == method)
                    .findFirst();

                if (match.isPresent()) {
                    Permission p = match.get();
                    permissionResponses.add(PermissionResponse.builder()
                        .roleId(roleId)
                        .featureMethod(method)
                        .featureUri(uri)
                        .createdAt(p.getCreatedAt())
                        .isActive(true)
                        .build());
                } else {
                    permissionResponses.add(PermissionResponse.builder()
                        .roleId(roleId)
                        .featureMethod(method)
                        .featureUri(uri)
                        .createdAt(null)
                        .isActive(false)
                        .build());
                }
            }

            result.put(category, permissionResponses);
        }

        // 2. Tìm các permission không còn trong hệ thống (orphaned)
        Set<String> allApiKeys = currentApis.values().stream()
            .flatMap(List::stream)
            .map(api -> api.getMethod() + "|" + api.getPath())
            .collect(Collectors.toSet());

        List<Permission> orphaned = permissions.stream()
            .filter(p -> {
                String key = p.getId().getFeatureMethod().name() + "|" + p.getId().getFeatureUri();
                return !allApiKeys.contains(key);
            })
            .toList();

        if (!orphaned.isEmpty()) {
            List<PermissionResponse> orphanedResponses = orphaned.stream()
                .map(p -> PermissionResponse.builder()
                    .roleId(roleId)
                    .featureMethod(p.getId().getFeatureMethod())
                    .featureUri(p.getId().getFeatureUri())
                    .createdAt(p.getCreatedAt())
                    .isActive(null)
                    .build())
                .collect(Collectors.toList());

            result.put("ORPHANED", orphanedResponses);
        }

        return result;
    }
}
