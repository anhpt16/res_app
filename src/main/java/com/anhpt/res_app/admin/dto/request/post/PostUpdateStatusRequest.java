package com.anhpt.res_app.admin.dto.request.post;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostUpdateStatusRequest {
    @NotBlank(message = "Trạng thái không được để trống")
    private String status;
}
