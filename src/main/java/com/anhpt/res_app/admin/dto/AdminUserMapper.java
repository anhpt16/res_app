package com.anhpt.res_app.admin.dto;

import com.anhpt.res_app.admin.dto.response.user.UserResponse;
import com.anhpt.res_app.admin.dto.response.user.RoleByUserResponse;
import com.anhpt.res_app.admin.dto.response.user.UserRoleResponse;
import com.anhpt.res_app.common.entity.Role;
import com.anhpt.res_app.common.entity.User;
import com.anhpt.res_app.common.entity.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdminUserMapper {

    UserResponse toUserResponse(User user);

    @Mapping(source = "role.id", target = "roleId")
    UserRoleResponse toUserRoleResponse(UserRole userRole);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "userRoleResponses", target = "userRoleResponses")
    RoleByUserResponse toRoleByUserResponse(User user, List<UserRoleResponse> userRoleResponses);
}
