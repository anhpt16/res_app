package com.anhpt.res_app.admin.dto.response.combo;

import com.anhpt.res_app.common.enums.status.ComboStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ComboListResponse {
    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final BigDecimal priceDiscount;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private final LocalDateTime createdAt;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private final LocalDateTime updatedAt;
    private final ComboStatus status;
}
