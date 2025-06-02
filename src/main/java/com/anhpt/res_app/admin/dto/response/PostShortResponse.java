package com.anhpt.res_app.admin.dto.response;

import com.anhpt.res_app.common.enums.status.PostStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class PostShortResponse {
    private final Long id;
    private final Long authorId;
    private final String authorName;
    private final String title;
    private PostStatus status;
}
