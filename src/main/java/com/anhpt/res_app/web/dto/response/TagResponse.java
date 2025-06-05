package com.anhpt.res_app.web.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TagResponse {
    private final Long id;
    private final String slug;
    private final String name;
}
