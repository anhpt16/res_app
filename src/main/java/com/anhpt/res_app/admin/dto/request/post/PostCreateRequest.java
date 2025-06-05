package com.anhpt.res_app.admin.dto.request.post;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.List;

@Getter
public class PostCreateRequest {
    @NotBlank(message = "Tiêu đề không được để trống")
    private String title;

    @NotBlank(message = "Nội dung không được để trống")
    private String content;

    @Min(value = 1, message = "Ảnh không hợp lệ")
    private Long thumbnail;

    private List<Long> tagIds;
}
