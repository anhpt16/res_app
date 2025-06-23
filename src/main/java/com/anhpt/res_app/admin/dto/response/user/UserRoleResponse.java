package com.anhpt.res_app.admin.dto.response.user;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserRoleResponse {
    private Integer roleId;
    private LocalDateTime createdAt;
}
