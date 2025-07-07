package com.anhpt.res_app.admin.dto.response.desk;

import com.anhpt.res_app.common.enums.status.DeskDurationStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class DeskDurationResponse {
    private Long deskDurationId;
    private Integer deskNumber;
    private Long durationId;
    private BigDecimal price;
    private DeskDurationStatus status;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime updatedAt;
    // Thêm
    private Integer duration;
}
