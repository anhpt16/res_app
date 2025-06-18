package com.anhpt.res_app.web.dto.response.dish;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class DishEndingResponse {
    private final Long id;
    private final String name;
    private final String introduce;
    private final BigDecimal price;
    // Thêm
    private final String thumbnail;
    private final LocalDate dateEnd;
}
