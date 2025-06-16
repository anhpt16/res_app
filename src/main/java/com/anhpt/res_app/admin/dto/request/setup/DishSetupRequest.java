package com.anhpt.res_app.admin.dto.request.setup;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DishSetupRequest {
    @NotBlank(message = "Trạng thái tiếp theo không được để trống")
    private String nextStatus;

    @NotNull(message = "Mốc thời gian không được để trống")
    private LocalDate milestone;
}
