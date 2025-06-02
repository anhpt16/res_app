package com.anhpt.res_app.admin.dto.request;

import com.anhpt.res_app.common.validator.NotBlankButNullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class MediaUpdateRequest {
    @NotBlankButNullable(message = "Tên file không được để trống")
    private String name;
    private String description;
}
