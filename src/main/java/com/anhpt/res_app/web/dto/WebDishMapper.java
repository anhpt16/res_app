package com.anhpt.res_app.web.dto;

import com.anhpt.res_app.common.entity.Dish;
import com.anhpt.res_app.common.entity.DishMedia;
import com.anhpt.res_app.common.entity.DishSetup;
import com.anhpt.res_app.web.dto.response.dish.*;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = WebDishMediaMapper.class)
public interface WebDishMapper {

    @Mapping(source = "dishMedias", target = "medias")
    DishResponse toDishResponse(Dish dish);

    @Mapping(source = "thumbnail", target = "thumbnail")
    DishShortResponse toDishShortResponse(Dish dish, String thumbnail);

    @Mapping(source = "dish.id", target = "id")
    @Mapping(source = "dish.name", target = "name")
    @Mapping(source = "dish.introduce", target = "introduce")
    @Mapping(source = "dish.price", target = "price")
    @Mapping(source = "thumbnail", target = "thumbnail")
    @Mapping(source = "dishSetup.milestone", target = "dateStart")
    DishComingResponse toDishComingResponse(DishSetup dishSetup, Dish dish, String thumbnail);

    @Mapping(source = "dish.id", target = "id")
    @Mapping(source = "dish.name", target = "name")
    @Mapping(source = "dish.introduce", target = "introduce")
    @Mapping(source = "dish.price", target = "price")
    @Mapping(source = "thumbnail", target = "thumbnail")
    @Mapping(source = "dishSetup.milestone", target = "dateEnd")
    DishEndingResponse toDishEndingResponse(DishSetup dishSetup, Dish dish, String thumbnail);
}
