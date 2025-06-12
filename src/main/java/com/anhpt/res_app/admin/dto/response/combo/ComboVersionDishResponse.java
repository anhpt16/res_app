package com.anhpt.res_app.admin.dto.response.combo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ComboVersionDishResponse {
    private final Long comboVersionDishId;
    private final Long dishId;
    private final Integer count;
    private final Integer displayOrder;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private final LocalDateTime createdAt;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private final LocalDateTime updatedAt;
    // Thêm
    private final String dishName;
    private final String dishFileName;
}
