package com.anhpt.res_app.admin.dto;

import com.anhpt.res_app.admin.dto.response.permission.PermissionResponse;
import com.anhpt.res_app.common.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdminPermissionMapper {

    @Mapping(source = "id.roleId", target = "roleId")
    @Mapping(source = "id.featureMethod", target = "featureMethod")
    @Mapping(source = "id.featureUri", target = "featureUri")
    PermissionResponse toPermissionResponse(Permission permission);
}
