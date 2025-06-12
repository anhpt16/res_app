package com.anhpt.res_app.admin.dto.response.combo;

import com.anhpt.res_app.common.enums.status.ComboStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ComboResponse {
    private final Long id;
    private final String name;
    private final Long mediaId;
    private final String introduce;
    private final String description;
    private final ComboStatus status;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private final LocalDateTime createdAt;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private final LocalDateTime updatedAt;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private final LocalDateTime publishedAt;
    // Thêm
    private final String fileName;
}
