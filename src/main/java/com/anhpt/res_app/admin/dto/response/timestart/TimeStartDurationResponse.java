package com.anhpt.res_app.admin.dto.response.timestart;

import com.anhpt.res_app.common.enums.status.DurationStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class TimeStartDurationResponse {
    private final Long startTimeId;
    private final Long durationId;
    private final Integer duration;
    private final DurationStatus status;
    private final LocalDateTime createdAt;
}
