package com.anhpt.res_app.admin.dto.response.media;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
public class MediaResponse {
    private final Long id;
    private final Long userId;
    private final String fileName;
    private final String originName;
    private final String mimeType;
    private final Long fileSize;
    private final Integer width;
    private final Integer height;
    private final Long duration;
    private final String description;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private final LocalDateTime createdAt;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private final LocalDateTime updatedAt;
}
