package com.anhpt.res_app.common.service;

import com.anhpt.res_app.common.dto.result.PermissionResult;
import com.anhpt.res_app.common.entity.Permission;
import com.anhpt.res_app.common.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public Set<PermissionResult> getPermissionByRoleIds(Set<Integer> roleIds) {
        return permissionRepository.findByRoleIds(roleIds);
    }
}
