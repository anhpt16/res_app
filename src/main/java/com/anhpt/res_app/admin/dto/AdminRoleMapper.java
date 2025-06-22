package com.anhpt.res_app.admin.dto;

import com.anhpt.res_app.admin.dto.response.role.RoleResponse;
import com.anhpt.res_app.common.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdminRoleMapper {

    RoleResponse toRoleResponse(Role role);
}
