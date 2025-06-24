package com.anhpt.res_app.common.dto.response.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RoleInfo {
    private Integer id;
    private String name;
}
