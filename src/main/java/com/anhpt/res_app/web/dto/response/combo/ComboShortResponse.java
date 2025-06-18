package com.anhpt.res_app.web.dto.response.combo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class ComboShortResponse {
    private final Long id;
    private final String name;
    private final String introduce;
    // Thêm
    private final BigDecimal price;
    private final BigDecimal priceDiscount;
}
