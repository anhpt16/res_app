package com.anhpt.res_app.web.dto;

import com.anhpt.res_app.common.entity.Category;
import com.anhpt.res_app.common.entity.Dish;
import com.anhpt.res_app.web.dto.response.category.CategoryResponse;
import com.anhpt.res_app.web.dto.response.dish.DishShortResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WebCategoryMapper {

    CategoryResponse toCategoryResponse(Category category);


}
