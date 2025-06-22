package com.anhpt.res_app.admin.dto.request.role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleUpdateRequest {
    private String name;
    private String note;

    public boolean isEmpty() {
        return name == null && note == null;
    }
}
