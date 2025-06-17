package com.anhpt.res_app.admin.dto.response.discount;

import com.anhpt.res_app.common.enums.status.DishStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class DiscountResponse {
    private final Long id;
    private final LocalDateTime timeStart;
    private final LocalDateTime timeEnd;
    private final BigDecimal priceDiscount;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    // Thêm
    private final Long dishId;
    private final String dishName;
    private final DishStatus dishStatus;
}
