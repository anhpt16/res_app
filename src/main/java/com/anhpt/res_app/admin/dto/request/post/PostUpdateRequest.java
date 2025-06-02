package com.anhpt.res_app.admin.dto.request.post;

import com.anhpt.res_app.common.validator.NotBlankButNullable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostUpdateRequest {
    @NotBlankButNullable(message = "Tiêu đề không được để trống")
    private String title;
    @NotBlankButNullable(message = "Nội dung không được để trống")
    private String content;
    public boolean isEmpty() {
        return title == null && content == null;
    }
}
