package com.anhpt.res_app.admin.service;

import com.anhpt.res_app.admin.dto.AdminRoleMapper;
import com.anhpt.res_app.admin.dto.request.role.RoleCreateRequest;
import com.anhpt.res_app.admin.dto.request.role.RoleSearchRequest;
import com.anhpt.res_app.admin.dto.request.role.RoleUpdateRequest;
import com.anhpt.res_app.admin.dto.response.role.RoleResponse;
import com.anhpt.res_app.admin.filter.AdminRoleFilter;
import com.anhpt.res_app.admin.validation.AdminRoleValidation;
import com.anhpt.res_app.common.dto.response.PageResponse;
import com.anhpt.res_app.common.entity.Role;
import com.anhpt.res_app.common.enums.status.RoleStatus;
import com.anhpt.res_app.common.exception.ResourceNotFoundException;
import com.anhpt.res_app.common.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminRoleService {
    // Repository
    private final RoleRepository roleRepository;
    // Validation
    private final AdminRoleValidation adminRoleValidation;
    // Maper
    private final AdminRoleMapper adminRoleMapper;
    // Filter
    private final AdminRoleFilter adminRoleFilter;


    public RoleResponse create(RoleCreateRequest request) {
        adminRoleValidation.validateCreate(request);
        Role role = new Role();
        role.setName(request.getName());
        role.setStatus(RoleStatus.ACTIVE);
        role.setCreatedAt(LocalDateTime.now());
        role.setUpdatedAt(LocalDateTime.now());
        if (request.getNote() != null) role.setNote(request.getNote());
        role = roleRepository.save(role);
        return adminRoleMapper.toRoleResponse(role);
    }

    public RoleResponse update(Integer roleId, RoleUpdateRequest request) {
        adminRoleValidation.validateUpdate(roleId, request);
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role không tồn tại"));
        if (request.getName() != null) role.setName(request.getName());
        if (request.getNote() != null) role.setNote(request.getNote());
        role.setUpdatedAt(LocalDateTime.now());
        role = roleRepository.save(role);
        return adminRoleMapper.toRoleResponse(role);
    }

    public RoleResponse updateStatus(Integer roleId, String status) {
        adminRoleValidation.validateUpdateStatus(roleId, status);
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new ResourceNotFoundException("Role không tồn tại"));
        RoleStatus updatedStatus = RoleStatus.fromCode(status);
        role.setStatus(updatedStatus);
        role.setUpdatedAt(LocalDateTime.now());
        role = roleRepository.save(role);
        return adminRoleMapper.toRoleResponse(role);
    }

    public void delete(Integer roleId) {
        adminRoleValidation.validateDelete(roleId);
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new ResourceNotFoundException("Role không tồn tại"));
        roleRepository.delete(role);
    }

    public RoleResponse getById(Integer roleId) {
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new ResourceNotFoundException("Role không tồn tại"));
        return adminRoleMapper.toRoleResponse(role);
    }

    public PageResponse<RoleResponse> get(RoleSearchRequest request) {
        adminRoleValidation.validateSearch(request);
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());
        Page<Role> pageResult = roleRepository.findAll(adminRoleFilter.search(request), pageable);
        List<RoleResponse> roleResponses = pageResult.getContent().stream()
            .map(adminRoleMapper::toRoleResponse)
            .collect(Collectors.toList());
        return new PageResponse<>(
            roleResponses,
            request.getPage(),
            request.getSize(),
            pageResult.getTotalElements(),
            pageResult.getTotalPages()
        );
    }
}
