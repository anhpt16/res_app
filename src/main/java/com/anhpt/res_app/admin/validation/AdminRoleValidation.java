package com.anhpt.res_app.admin.validation;

import com.anhpt.res_app.admin.dto.request.role.RoleCreateRequest;
import com.anhpt.res_app.admin.dto.request.role.RoleSearchRequest;
import com.anhpt.res_app.admin.dto.request.role.RoleUpdateRequest;
import com.anhpt.res_app.common.entity.Role;
import com.anhpt.res_app.common.enums.status.RoleStatus;
import com.anhpt.res_app.common.exception.ForbiddenActionException;
import com.anhpt.res_app.common.exception.MultiDuplicateException;
import com.anhpt.res_app.common.exception.ResourceNotFoundException;
import com.anhpt.res_app.common.repository.RoleRepository;
import com.anhpt.res_app.common.utils.function.FieldNameUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminRoleValidation {
    private final RoleRepository roleRepository;

    public void validateCreate(RoleCreateRequest request) {
        Map<String, String> errors = new HashMap<>();
        // Kiểm tra tên tồn tại chưa
        if (roleRepository.existsByName(request.getName())) {
            String field = FieldNameUtil.getFieldName(RoleCreateRequest::getName);
            errors.put(field, "Đã tồn tại");
        }
        if (errors.size() > 0) {
            throw new MultiDuplicateException(errors);
        }
    }

    public void validateUpdate(Integer roleId, RoleUpdateRequest request) {
        if (request.isEmpty()) {
            throw new IllegalArgumentException("Dữ liệu không hợp lệ");
        }
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new ResourceNotFoundException("Role không tồn tại"));
        Map<String, String> errors = new HashMap<>();
        // Kiểm tra tên
        if (request.getName() != null) {
            Optional<Role> roleExist = roleRepository.findByName(request.getName());
            if (roleExist.isPresent() && !roleExist.get().getId().equals(roleId)) {
                String field = FieldNameUtil.getFieldName(RoleUpdateRequest::getName);
                errors.put(field, "Đã tồn tại");
            }
        }
        if (errors.size() > 0) {
            throw new MultiDuplicateException(errors);
        }
    }

    public void validateUpdateStatus(Integer roleId, String status) {
        // Kiểm tra role có tồn tại không
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new ResourceNotFoundException("Role không tồn tại"));
        // Kiểm tra trạng thái hợp lệ
        RoleStatus updatedStatus = RoleStatus.fromCode(status);
        // Kiểm tra trạng thái trùng lặp
        if (role.getStatus().equals(updatedStatus)) {
            throw new IllegalArgumentException("Trạng thái không hợp lệ");
        }
    }

    public void validateDelete(Integer roleId) {
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new ResourceNotFoundException("Role không tồn tại"));
        if (role.getStatus() == RoleStatus.ACTIVE) {
            throw new ForbiddenActionException("Role đang hoạt động");
        }
        // TODO: Kiểm tra role có permission nào không
        // TODO: Kiểm tra role có user nào không
    }

    public void validateSearch(RoleSearchRequest request) {
    }
}
