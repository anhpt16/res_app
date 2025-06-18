package com.anhpt.res_app.web.dto;

import com.anhpt.res_app.common.entity.Dish;
import com.anhpt.res_app.common.entity.DishMedia;
import com.anhpt.res_app.web.dto.response.dish.DishMediaResponse;
import com.anhpt.res_app.web.dto.response.dish.DishResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = WebDishMediaMapper.class)
public interface WebDishMapper {

    @Mapping(source = "dishMedias", target = "medias")
    DishResponse toDishResponse(Dish dish);
}
