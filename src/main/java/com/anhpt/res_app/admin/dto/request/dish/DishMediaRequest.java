package com.anhpt.res_app.admin.dto.request.dish;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DishMediaRequest {
    @NotNull(message = "Id không được để trống")
    @Min(value = 1, message = "Id không hợp lệ")
    private Long id;
    @Min(value = 1, message = "Thứ tự hiển thị không hợp lệ")
    private Integer displayOrder = 1;
}
