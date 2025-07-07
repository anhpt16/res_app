package com.anhpt.res_app.admin.dto.response;

import com.anhpt.res_app.common.enums.status.StartTimeStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Builder
public class TimeStartResponse {
    private final Long id;
    private final LocalTime timeStart;
    private final StartTimeStatus status;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private final LocalDateTime createdAt;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private final LocalDateTime updatedAt;
}
