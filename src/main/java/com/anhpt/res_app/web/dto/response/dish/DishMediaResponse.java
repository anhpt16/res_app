package com.anhpt.res_app.web.dto.response.dish;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DishMediaResponse {
    private Long dishId;
    private Long mediaId;
    private String fileName;
    private Integer displayOrder;
}
