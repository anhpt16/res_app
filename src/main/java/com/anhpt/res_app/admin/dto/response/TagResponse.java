package com.anhpt.res_app.admin.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
public class TagResponse {
    private final Long id;
    private final String name;
    private final String slug;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}
