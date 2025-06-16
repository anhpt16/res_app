package com.anhpt.res_app.admin.dto;

import com.anhpt.res_app.admin.dto.response.setup.DishSetupResponse;
import com.anhpt.res_app.common.entity.DishSetup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdminDishSetupMapper {

    @Mapping(source = "dish.id", target = "dishId")
    @Mapping(source = "dish.name", target = "dishName")
    DishSetupResponse toDishSetupResponse(DishSetup dishSetup);
}
