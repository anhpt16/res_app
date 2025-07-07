package com.anhpt.res_app.admin.dto.request.desk;

import com.anhpt.res_app.common.enums.DeskSeat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeskCreateRequest {
    @NotNull(message = "Số bàn không được để trống")
    private Integer number;
    private String type;
    private String position;
    @NotNull(message = "Số chỗ ngồi không được để trống")
    private String seat;
    private String description;
}
