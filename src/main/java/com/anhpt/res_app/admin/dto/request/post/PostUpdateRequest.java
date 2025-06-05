package com.anhpt.res_app.admin.dto.request.post;

import com.anhpt.res_app.common.validator.NotBlankButNullable;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostUpdateRequest {
    @NotBlankButNullable(message = "Tiêu đề không được để trống")
    private String title;
    @NotBlankButNullable(message = "Nội dung không được để trống")
    private String content;

    @Min(value = 1, message = "Ảnh không hợp lệ")
    private Long thumbnail;
    private List<Long> tagIds;
    public boolean isEmpty() {
        return title == null && content == null && thumbnail == null && tagIds == null;
    }
}
