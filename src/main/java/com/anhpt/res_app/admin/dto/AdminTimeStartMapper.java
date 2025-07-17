package com.anhpt.res_app.admin.dto;

import com.anhpt.res_app.admin.dto.response.timestart.TimeStartDurationResponse;
import com.anhpt.res_app.admin.dto.response.timestart.TimeStartResponse;
import com.anhpt.res_app.common.entity.StartTime;
import com.anhpt.res_app.common.entity.StartTimeDuration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdminTimeStartMapper {

    TimeStartResponse toTimeStartResponse(StartTime startTime);

    @Mapping(source = "startTime.id", target = "startTimeId")
    @Mapping(source = "duration.id", target = "durationId")
    @Mapping(source = "duration.duration", target = "duration")
    @Mapping(source = "duration.status", target = "status")
    TimeStartDurationResponse toTimeStartDurationResponse(StartTimeDuration startTimeDuration);
}
