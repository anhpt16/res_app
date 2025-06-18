package com.anhpt.res_app.web.dto.response.dish;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class DishShortResponse {
    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final BigDecimal priceDiscount;
    private final String introduce;
    // Thêm
    private final String thumbnail;
}
