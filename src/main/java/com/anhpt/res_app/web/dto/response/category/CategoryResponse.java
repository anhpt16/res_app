package com.anhpt.res_app.web.dto.response.category;

import com.anhpt.res_app.common.enums.status.CategoryStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CategoryResponse {
    private final Long id;
    private final String name;
    private final CategoryStatus status;
}
