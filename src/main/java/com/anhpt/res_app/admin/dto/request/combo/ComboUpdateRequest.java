package com.anhpt.res_app.admin.dto.request.combo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComboUpdateRequest {
    @Size(max = 255, message = "Tên không được vượt quá 255 ký tự")
    private String name;
    @Min(value = 1, message = "Id không hợp lệ")
    private Long mediaId;
    @Size(max = 255, message = "Giới thiệu không được vượt quá 255 ký tự")
    private String introduce;
    private String description;

    public boolean isEmpty() {
        return name == null && mediaId == null && introduce == null && description == null;
    }
}
