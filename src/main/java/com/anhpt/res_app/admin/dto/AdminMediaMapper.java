package com.anhpt.res_app.admin.dto;

import com.anhpt.res_app.admin.dto.response.dish.DishMediaResponse;
import com.anhpt.res_app.admin.dto.response.media.MediaResponse;
import com.anhpt.res_app.admin.dto.response.media.MediaShortResponse;
import com.anhpt.res_app.common.entity.Media;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdminMediaMapper {
    MediaResponse toMediaResponse(Media media);
    MediaShortResponse toMediaShortResponse(Media media);


}
