package com.anhpt.res_app.admin.dto;

import com.anhpt.res_app.admin.dto.response.combo.ComboVersionResponse;
import com.anhpt.res_app.common.entity.ComboVersion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdminComboVersionMapper {

    @Mapping(source = "combo.id", target = "comboId")
    @Mapping(source = "combo.name", target = "comboName")
    @Mapping(source = "comboVersionDishes", target = "dishes")
    ComboVersionResponse toComboVersionResponse (ComboVersion comboVersion);


}
