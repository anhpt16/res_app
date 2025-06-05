package com.anhpt.res_app.web.dto.shared;

import com.anhpt.res_app.common.enums.status.PostStatus;
import com.anhpt.res_app.web.dto.response.TagResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class PostDetail {
    private final Long id;
    private final String title;
    private final String slug;
    private final String content;
    private final LocalDateTime publishedAt;
    // Thêm
    private final String thumbnail;
    private final List<TagResponse> tags;
}
