package com.anhpt.res_app.common.dto.response.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class TokenExtractor {
    private final Long id;
    private final String name;
    private final List<RoleInfo> roles;
}
