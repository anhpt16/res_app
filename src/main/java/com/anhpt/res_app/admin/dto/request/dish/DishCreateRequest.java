package com.anhpt.res_app.admin.dto.request.dish;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class DishCreateRequest {
    @NotBlank(message = "Tên không được để trống")
    @Size(max = 255, message = "Độ dài tối đa 255 ký tự")
    private String name;
    private Long categoryId;

    @Size(max = 50, message = "Độ dài tối đa 50 ký tự")
    private String unit;
    @NotNull(message = "Giá không được để trống")
    private BigDecimal price;
    private Integer durationFrom;
    private Integer durationTo;

    @Size(max = 255, message = "Độ dài tối đa 255 ký tự")
    private String ingradientDisplay;
    private String description;

    @Size(max = 255, message = "Độ dài tối đa 255 ký tự")
    private String introduce;
    // Media
    private List<DishMediaRequest> medias;
}
