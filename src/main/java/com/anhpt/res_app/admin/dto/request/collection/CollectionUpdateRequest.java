package com.anhpt.res_app.admin.dto.request.collection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CollectionUpdateRequest {
    private String name;
    private String status;
    private Integer displayOrder;

    public boolean isEmpty() {
        return name == null && status == null && displayOrder == null;
    }
}
