package com.anhpt.res_app.web.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PostShortResponse {
    private final Long id;
    private final String title;
    private final String slug;
    private final LocalDateTime publishedAt;
    // Thêm
    private String thumbnail;
}
