package com.anhpt.res_app.admin.dto.request.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryUpdateRequest {
    @Size(max = 255, message = "Tên không được vượt quá 255 ký tự")
    private String name;
    private String status;

    public boolean isEmpty() {
        return name == null && status == null;
    }
}
