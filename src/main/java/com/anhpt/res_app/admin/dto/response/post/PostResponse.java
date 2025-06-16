package com.anhpt.res_app.admin.dto.response.post;

import com.anhpt.res_app.common.enums.status.PostStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
public class PostResponse {
    private final Long id;
    private final Long authorId;
    private final String authorName;
    private final String title;
    private final String slug;
    private final String content;
    private final PostStatus status;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private final LocalDateTime createdAt;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private final LocalDateTime updatedAt;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private final LocalDateTime publishedAt;
    // Thêm
    private final Long mediaId;
    private final String fileName;
    private final List<Long> tagIds;
}
