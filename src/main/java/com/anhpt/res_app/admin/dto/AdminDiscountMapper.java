package com.anhpt.res_app.admin.dto;

import com.anhpt.res_app.admin.dto.response.discount.DiscountResponse;
import com.anhpt.res_app.common.entity.Discount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "Spring")
public interface AdminDiscountMapper {

    @Mapping(source = "dish.id", target = "dishId")
    @Mapping(source = "dish.name", target = "dishName")
    @Mapping(source = "dish.status", target = "dishStatus")
    DiscountResponse toDiscountResponse(Discount discount);
}
