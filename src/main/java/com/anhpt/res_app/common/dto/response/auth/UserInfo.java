package com.anhpt.res_app.common.dto.response.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserInfo {
    private Long id;
    private String name;
}
