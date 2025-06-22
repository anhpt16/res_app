package com.anhpt.res_app.admin.dto.request.role;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleCreateRequest {
    @NotBlank(message = "Tên không được để trống")
    private String name;
    private String note;
}
