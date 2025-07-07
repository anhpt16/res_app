package com.anhpt.res_app.admin.dto;

import com.anhpt.res_app.admin.dto.response.desk.DurationResponse;
import com.anhpt.res_app.common.entity.Duration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdminDurationMapper {

    DurationResponse toDurationResponse(Duration duration);
}
