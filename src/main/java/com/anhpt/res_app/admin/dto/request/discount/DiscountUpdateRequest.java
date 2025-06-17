package com.anhpt.res_app.admin.dto.request.discount;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class DiscountUpdateRequest {
    private LocalDateTime timeStart;
    private LocalDateTime timeEnd;
    private BigDecimal priceDiscount;

    public boolean isEmpty() {
        return timeStart == null && timeEnd == null && priceDiscount == null;
    }
}
