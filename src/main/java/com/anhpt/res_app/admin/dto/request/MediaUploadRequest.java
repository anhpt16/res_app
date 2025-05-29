package com.anhpt.res_app.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class MediaUploadRequest {
    @NotNull(message = "File không được để trống")
    private MultipartFile file;

    @NotBlank(message = "Tên file không được để trống")
    private String name;

    private String description;
}
