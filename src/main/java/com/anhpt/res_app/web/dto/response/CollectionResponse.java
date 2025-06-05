package com.anhpt.res_app.web.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CollectionResponse {
    private final Long id;
    private final String fileName;
}
