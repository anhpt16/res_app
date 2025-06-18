package com.anhpt.res_app.web.dto.response.dish;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
public class DishResponse {
    private final Long id;
    private final String name;
    private final String unit;
    private final BigDecimal price;
    private final BigDecimal priceDiscount;
    private final Integer durationFrom;
    private final Integer durationTo;
    private final String ingradientDisplay;
    private final String description;
    // Thêm
    private final String categoryName;
    private final List<DishMediaResponse> medias;
}
