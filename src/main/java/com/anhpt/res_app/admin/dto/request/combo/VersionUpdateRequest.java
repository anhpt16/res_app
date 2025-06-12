package com.anhpt.res_app.admin.dto.request.combo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class VersionUpdateRequest {
    private BigDecimal price;
    private BigDecimal priceDiscount;
    private Integer durationFrom;
    private Integer durationTo;
    private LocalDateTime startAt;
    private LocalDateTime finishAt;

    public boolean isEmpty() {
        return price == null && priceDiscount == null && durationFrom == null && durationTo == null && startAt == null && finishAt == null;
    }
}
