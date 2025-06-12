package com.anhpt.res_app.admin.dto.response.combo;

import com.anhpt.res_app.common.enums.status.ComboVersionStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class ComboVersionResponse {
    private final Long id;
    private final String versionCode;
    private final Long comboId;
    private final BigDecimal price;
    private final BigDecimal priceDiscount;
    private final Integer durationFrom;
    private final Integer durationTo;
    private final ComboVersionStatus status;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private final LocalDateTime createdAt;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private final LocalDateTime updatedAt;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private final LocalDateTime startAt;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private final LocalDateTime finishAt;

    private final List<ComboVersionDishResponse> dishes;
}
