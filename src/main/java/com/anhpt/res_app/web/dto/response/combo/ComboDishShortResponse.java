package com.anhpt.res_app.web.dto.response.combo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComboDishShortResponse {
    private Long id;
    private String name;
    private Integer displayOrder;
    private Integer count;
    private String thumbnail;
}
