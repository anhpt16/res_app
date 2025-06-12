package com.anhpt.res_app.admin.dto.request.combo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComboCreateRequest {
    @NotBlank(message = "Tên không được để trống")
    @Size(max = 255, message = "Tên không được vượt quá 255 ký tự")
    private String name;
    @Size(max = 255, message = "Giới thiệu không được vượt quá 255 ký tự")
    private String introduce;
    private String description;
    @Min(value = 1, message = "Id không hợp lệ")
    private Long mediaId;
}
