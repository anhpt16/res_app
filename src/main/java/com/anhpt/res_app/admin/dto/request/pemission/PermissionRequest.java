package com.anhpt.res_app.admin.dto.request.pemission;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionRequest {
    @NotBlank(message = "Phương thức không được để trống")
    private String method;
    @NotBlank(message = "Uri không được để trống")
    private String uri;
}
