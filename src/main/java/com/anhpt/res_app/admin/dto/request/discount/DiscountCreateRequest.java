package com.anhpt.res_app.admin.dto.request.discount;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class DiscountCreateRequest {
    @NotNull(message = "Thời gian bắt đầu không được để trống")
    private LocalDateTime timeStart;
    @NotNull(message = "Thời gian kết thúc không được để trống")
    private LocalDateTime timeEnd;
    @NotNull(message = "Giá giảm không được để trống")
    private BigDecimal priceDiscount;
}
