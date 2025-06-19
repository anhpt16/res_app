package com.anhpt.res_app.web.dto.response.dish;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class DishDiscountResponse {
    private final Long id;
    private final String name;
    private final String introduce;
    private final BigDecimal price;
    private final BigDecimal priceDiscount;
    // Thêm
    private final String thumbnail;
    private final LocalDateTime timeStart;
    private final LocalDateTime timeEnd;
}
