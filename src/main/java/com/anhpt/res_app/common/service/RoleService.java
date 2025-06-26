package com.anhpt.res_app.common.service;

import com.anhpt.res_app.common.entity.Role;
import com.anhpt.res_app.common.entity.User;
import com.anhpt.res_app.common.entity.UserRole;
import com.anhpt.res_app.common.repository.RoleRepository;
import com.anhpt.res_app.common.repository.UserRepository;
import com.anhpt.res_app.common.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final UserRoleRepository userRoleRepository;

    public Set<Integer> getRolesByUserId(Long userId) {
        List<UserRole> userRoles = userRoleRepository.findByUserId(userId);
        return userRoles.stream()
            .map(userRole -> userRole.getRole().getId())
            .collect(Collectors.toSet());
    }
}
