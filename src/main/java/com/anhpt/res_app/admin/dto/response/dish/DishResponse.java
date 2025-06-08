package com.anhpt.res_app.admin.dto.response.dish;

import com.anhpt.res_app.common.enums.status.DishStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class DishResponse {
    private final Long id;
    private final String name;
    private final Long categoryId;
    private final String unit;
    private final BigDecimal price;
    private final BigDecimal priceDiscount;
    private final Integer durationFrom;
    private final Integer durationTo;
    private final String ingradientDisplay;
    private final String description;
    private final String introduce;
    private final DishStatus status;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private final LocalDateTime createdAt;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private final LocalDateTime updatedAt;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private final LocalDateTime publishedAt;
    // Thêm
    private final String categoryName;
    private final List<DishMediaResponse> medias;
}
