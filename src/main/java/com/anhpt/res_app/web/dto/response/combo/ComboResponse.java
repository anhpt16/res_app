package com.anhpt.res_app.web.dto.response.combo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ComboResponse {
    private Long id;
    private String name;
    private String description;
    // Thêm
    private String thumbnail;
    private Long comboVersionId;
    private BigDecimal price;
    private BigDecimal priceDiscount;
    private Integer durationFrom;
    private Integer durationTo;
}
