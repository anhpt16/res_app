package com.anhpt.res_app.admin.dto.response;

import com.anhpt.res_app.common.enums.status.PostStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
public class PostResponse {
    private final Long id;
    private final Long authorId;
    private final String author;
    private final String title;
    private final String slug;
    private final String content;
    private final PostStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final LocalDateTime publishedAt;
}
