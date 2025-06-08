package com.anhpt.res_app.admin.dto.response.dish;

import com.anhpt.res_app.common.enums.status.DishStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class DishShortResponse {
    private final Long id;
    private final String name;
    private final DishStatus status;
    private final BigDecimal price;
    private final BigDecimal priceDiscount;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private final LocalDateTime createdAt;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private final LocalDateTime updatedAt;
    // Thêm
    private final String categoryName;
    private final String fileName;
}
