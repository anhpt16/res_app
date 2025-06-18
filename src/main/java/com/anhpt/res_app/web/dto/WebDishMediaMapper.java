package com.anhpt.res_app.web.dto;

import com.anhpt.res_app.common.entity.DishMedia;
import com.anhpt.res_app.web.dto.response.dish.DishMediaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WebDishMediaMapper {

    @Mapping(source = "dish.id", target = "dishId")
    @Mapping(source = "media.id", target = "mediaId")
    @Mapping(source = "media.fileName", target = "fileName")
    DishMediaResponse toDishMediaResponse(DishMedia dishMedia);
}
