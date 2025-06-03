package com.anhpt.res_app.admin.dto.request.collection;

import com.anhpt.res_app.common.validator.NotBlankButNullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CollectionCreateRequest {
    @NotNull(message = "Tệp không được để trống")
    private Long mediaId;
    @NotBlankButNullable(message = "Tên không được để trống")
    @Size(max = 255, message = "Tên không được vượt quá 255 ký tự")
    private String name;
    private Integer displayOrder;
    private String status;
}
