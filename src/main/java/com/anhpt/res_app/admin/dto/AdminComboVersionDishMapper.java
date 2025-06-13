package com.anhpt.res_app.admin.dto;

import com.anhpt.res_app.admin.dto.response.combo.ComboVersionDishResponse;
import com.anhpt.res_app.common.entity.ComboVersionDish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdminComboVersionDishMapper {

    @Mapping(source = "comboVersion.id", target = "comboVersionId")
    @Mapping(source = "dish.id", target = "dishId")
    @Mapping(source = "dish.name", target = "dishName")
    ComboVersionDishResponse toComboVersionDishResponse(ComboVersionDish comboVersionDish);
}
