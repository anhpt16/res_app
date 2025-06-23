package com.anhpt.res_app.admin.dto.response.user;

import com.anhpt.res_app.common.enums.status.UserStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class UserResponse {
    private final Long id;
    private final String name;
    private final String username;
    private final String email;
    private final String phone;
    private final String type;
    private final String typeId;
    private final Boolean emailVerified;
    private final Boolean phoneVerified;
    private final UserStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}
