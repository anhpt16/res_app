package com.anhpt.res_app.admin.dto.request.dish;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class DishUpdateRequest {
    @Size(max = 255, message = "Độ dài tối đa 255 ký tự")
    private String name;
    @Size(max = 50, message = "Độ dài tối đa 50 ký tự")
    private String unit;
    private BigDecimal price;
    private Integer durationFrom;
    private Integer durationTo;
    @Size(max = 255, message = "Độ dài tối đa 255 ký tự")
    private String ingradientDisplay;
    private String description;
    private String introduce;
    @Valid
    private List<DishMediaRequest> medias;

    public boolean isEmpty() {
        return name == null && unit == null
            && price == null && durationFrom == null
            && durationTo == null && ingradientDisplay == null
            && description == null && introduce == null
            && medias == null;
    }
}
