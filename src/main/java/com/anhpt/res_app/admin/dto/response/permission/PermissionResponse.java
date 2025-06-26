package com.anhpt.res_app.admin.dto.response.permission;

import com.anhpt.res_app.common.enums.FeatureMethod;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PermissionResponse {
    private final Integer roleId;
    private final FeatureMethod featureMethod;
    private final String featureUri;
    private final String description;
    private final LocalDateTime createdAt;
    private final Boolean isActive;
}
