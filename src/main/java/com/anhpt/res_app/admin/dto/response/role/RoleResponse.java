package com.anhpt.res_app.admin.dto.response.role;

import com.anhpt.res_app.common.enums.status.RoleStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class RoleResponse {
    private final Integer id;
    private final String name;
    private final String note;
    private final RoleStatus status;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private final LocalDateTime createdAt;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private final LocalDateTime updatedAt;
}
