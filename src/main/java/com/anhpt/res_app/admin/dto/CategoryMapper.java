package com.anhpt.res_app.admin.dto;

import com.anhpt.res_app.admin.dto.response.category.CategoryResponse;
import com.anhpt.res_app.common.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponse toCategoryResponse (Category category);
}
