package com.anhpt.res_app.admin.dto;

import com.anhpt.res_app.admin.dto.response.MediaResponse;
import com.anhpt.res_app.admin.dto.response.MediaShortResponse;
import com.anhpt.res_app.common.entity.Media;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MediaMapper {
    MediaResponse toMediaResponse(Media media);

    MediaShortResponse toMediaShortResponse(Media media);
}
