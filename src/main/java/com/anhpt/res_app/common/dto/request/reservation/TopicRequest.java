package com.anhpt.res_app.common.dto.request.reservation;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TopicRequest {
    @NotNull(message = "Ngày bắt đầu không được để trống")
    private LocalDate date;
    @NotNull(message = "Thời điểm bắt đầu không được để trống")
    private Long startId;
    @NotNull(message = "Thời lượng sử dụng không được để trống")
    private Long durationId;
}
