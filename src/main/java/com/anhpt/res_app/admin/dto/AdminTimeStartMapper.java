package com.anhpt.res_app.admin.dto;

import com.anhpt.res_app.admin.dto.response.TimeStartResponse;
import com.anhpt.res_app.common.entity.StartTime;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdminTimeStartMapper {

    TimeStartResponse toTimeStartResponse(StartTime startTime);
}
