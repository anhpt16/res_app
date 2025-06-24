package com.anhpt.res_app.admin.validation;

import com.anhpt.res_app.admin.dto.request.pemission.PermissionRequest;
import com.anhpt.res_app.admin.dto.response.ApiInfo;
import com.anhpt.res_app.common.entity.Permission;
import com.anhpt.res_app.common.entity.Role;
import com.anhpt.res_app.common.enums.FeatureMethod;
import com.anhpt.res_app.common.exception.ResourceNotFoundException;
import com.anhpt.res_app.common.repository.PermissionRepository;
import com.anhpt.res_app.common.repository.RoleRepository;
import com.anhpt.res_app.common.utils.ApiScanner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class AdminPermissionValidation {
    // Repository
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public void validateAddPermission(Integer roleId, PermissionRequest request) {
        // Kiểm tra Role tồn tại
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Role"));
        // Kiểm tra Method hợp lệ
        FeatureMethod featureMethod = FeatureMethod.fromCode(request.getMethod());
        // Kiểm tra Method và Uri tồn tại
        if (!apiExists(request.getUri(), request.getMethod())) {
            log.warn("Không tìm thấy Uri: {} và Method: {}", request.getUri(), request.getMethod());
            throw new ResourceNotFoundException("Không tìm thấy Method-Uri");
        }
        // Kiểm tra Role - Method - Uri tồn tại
        boolean isExist = permissionRepository.countByRoleIdAndFeatureUriAndFeatureMethod(roleId, request.getUri(), featureMethod) > 0;
        if (isExist) {
            log.warn("Role: {} đã tồn tại quyền hạn method: {} uri: {}", roleId, request.getMethod(), request.getUri());
            throw new IllegalArgumentException("Quyền hạn đã tồn tại");
        }
    }

    public void validateDeletePermission(Integer roleId, PermissionRequest request) {
        // Kiểm tra Role tồn tại
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Role"));
        // Kiểm tra Method hợp lệ
        FeatureMethod featureMethod = FeatureMethod.fromCode(request.getMethod());
        // Kiểm tra Role - Method - Uri tồn tại
        boolean isExist = permissionRepository.countByRoleIdAndFeatureUriAndFeatureMethod(roleId, request.getUri(), featureMethod) > 0;
        if (!isExist) {
            log.warn("Role: {} không tồn tại quyền hạn method: {} uri: {}", roleId, request.getMethod(), request.getUri());
            throw new IllegalArgumentException("Quyền hạn không tồn tại");
        }
    }

    public void validateGetPermission(Integer roleId) {
        // Kiểm tra Role tồn tại
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Role"));
    }

    private boolean apiExists(String uri, String method) {
        Map<String, List<ApiInfo>> currentApis = ApiScanner.currentApis;
        return currentApis.values().stream()
            .flatMap(Collection::stream)
            .anyMatch(apiInfo -> apiInfo.getMethod().equalsIgnoreCase(method) && apiInfo.getPath().equals(uri));
    }
}
