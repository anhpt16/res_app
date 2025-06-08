package com.anhpt.res_app.admin.dto;

import com.anhpt.res_app.admin.dto.response.dish.DishMediaResponse;
import com.anhpt.res_app.admin.dto.response.dish.DishResponse;
import com.anhpt.res_app.admin.dto.response.dish.DishShortResponse;
import com.anhpt.res_app.common.entity.Dish;
import com.anhpt.res_app.common.entity.DishMedia;
import com.anhpt.res_app.common.entity.Media;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdminDishMapper {

    @Mapping(source = "dish.category.id", target = "categoryId")
    @Mapping(source = "dish.category.name", target = "categoryName")
    DishResponse toDishResponse (Dish dish, List<DishMediaResponse> medias);

    @Mapping(source = "media.id", target = "id")
    @Mapping(source = "media.fileName", target = "fileName")
    DishMediaResponse toDishMediaResponse (DishMedia dishMedia);

    @Mapping(source = "dish.category.name", target = "categoryName")
    @Mapping(source = "thumbnail", target = "fileName")
    DishShortResponse toDishShortResponse(Dish dish, String thumbnail);
}
