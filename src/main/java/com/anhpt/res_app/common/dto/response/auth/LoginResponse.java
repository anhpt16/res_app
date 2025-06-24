package com.anhpt.res_app.common.dto.response.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class LoginResponse {
    private final String accessToken;
}
