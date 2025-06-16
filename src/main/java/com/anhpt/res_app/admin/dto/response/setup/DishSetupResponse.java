package com.anhpt.res_app.admin.dto.response.setup;

import com.anhpt.res_app.common.enums.status.DishStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class DishSetupResponse {
    private final Long id;
    private final DishStatus currentStatus;
    private final DishStatus nextStatus;
    private final LocalDate milestone;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private final LocalDateTime createdAt;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private final LocalDateTime updatedAt;
    // Thêm
    private final Long dishId;
    private final String dishName;
}
