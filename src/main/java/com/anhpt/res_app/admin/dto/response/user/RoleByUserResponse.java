package com.anhpt.res_app.admin.dto.response.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class RoleByUserResponse {
    private final Long userId;
    private final List<UserRoleResponse> userRoleResponses;
}
