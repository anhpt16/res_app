package com.anhpt.res_app.web.dto;

import com.anhpt.res_app.common.entity.Combo;
import com.anhpt.res_app.common.entity.ComboVersion;
import com.anhpt.res_app.common.entity.ComboVersionDish;
import com.anhpt.res_app.common.entity.Dish;
import com.anhpt.res_app.web.dto.response.combo.ComboDishShortResponse;
import com.anhpt.res_app.web.dto.response.combo.ComboResponse;
import com.anhpt.res_app.web.dto.response.combo.ComboShortResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WebComboMapper {

    @Mapping(source = "combo.id", target = "id")
    @Mapping(source = "combo.name", target = "name")
    @Mapping(source = "combo.introduce", target = "introduce")
    @Mapping(source = "comboVersion.price", target = "price")
    @Mapping(source = "comboVersion.priceDiscount", target = "priceDiscount")
    ComboShortResponse toComboShortResponse(Combo combo, ComboVersion comboVersion);

    @Mapping(source = "combo.id", target = "id")
    @Mapping(source = "combo.name", target = "name")
    @Mapping(source = "combo.description", target = "description")
    @Mapping(source = "combo.media.fileName", target = "thumbnail")
    @Mapping(source = "comboVersion.id", target = "comboVersionId")
    @Mapping(source = "comboVersion.price", target = "price")
    @Mapping(source = "comboVersion.priceDiscount", target = "priceDiscount")
    @Mapping(source = "comboVersion.durationFrom", target = "durationFrom")
    @Mapping(source = "comboVersion.durationTo", target = "durationTo")
    ComboResponse toComboResponse(Combo combo, ComboVersion comboVersion);


    @Mapping(source = "dish.id", target = "id")
    @Mapping(source = "dish.name", target = "name")
    @Mapping(source = "comboVersionDish.displayOrder", target = "displayOrder")
    @Mapping(source = "comboVersionDish.count", target = "count")
    @Mapping(source = "thumbnail", target = "thumbnail")
    ComboDishShortResponse toComboDishShortResponse(ComboVersionDish comboVersionDish, Dish dish, String thumbnail);
}
