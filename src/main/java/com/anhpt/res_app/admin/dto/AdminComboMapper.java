package com.anhpt.res_app.admin.dto;

import com.anhpt.res_app.admin.dto.response.combo.*;
import com.anhpt.res_app.admin.dto.response.dish.DishResponse;
import com.anhpt.res_app.common.entity.Combo;
import com.anhpt.res_app.common.entity.ComboVersion;
import com.anhpt.res_app.common.entity.ComboVersionDish;
import com.anhpt.res_app.common.entity.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "Spring")
public interface AdminComboMapper {

    @Mapping(source = "media.id", target = "mediaId")
    @Mapping(source = "media.fileName", target = "fileName")
    ComboResponse toComboResponse(Combo combo);

    @Mapping(source = "combo.id", target = "id")
    @Mapping(source = "combo.createdAt", target = "createdAt")
    @Mapping(source = "combo.updatedAt", target = "updatedAt")
    @Mapping(source = "combo.status", target = "status")
    @Mapping(source = "comboVersion.price", target = "price")
    @Mapping(source = "comboVersion.priceDiscount", target = "priceDiscount")
    ComboListResponse toComboListResponse(Combo combo, ComboVersion comboVersion);


    @Mapping(source = "combo.id", target = "id")
    @Mapping(source = "combo.createdAt", target = "createdAt")
    @Mapping(source = "combo.updatedAt", target = "updatedAt")
    @Mapping(source = "combo.status", target = "status")
    @Mapping(source = "combo.media.id", target = "mediaId")
    @Mapping(source = "combo.media.fileName", target = "thumbnail")
    ComboDetailResponse toComboDetailResponse(Combo combo, ComboVersionResponse comboVersion);

    ComboVersionResponse toComboVersionResponse(ComboVersion comboVersion, List<ComboVersionDishResponse> dishes);

    @Mapping(source = "dish.id", target = "dishId")
    @Mapping(source = "dish.name", target = "dishName")
    @Mapping(source = "thumbnail", target = "dishFileName")
    @Mapping(source = "comboVersionDish.createdAt", target = "createdAt")
    @Mapping(source = "comboVersionDish.updatedAt", target = "updatedAt")
    ComboVersionDishResponse toComboVersionDishResponse(ComboVersionDish comboVersionDish, Dish dish, String thumbnail);
}
