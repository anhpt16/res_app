package com.anhpt.res_app.admin.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class MediaShortResponse {
    private final Long id;
    private final String originName;
    private final String fileName;
}
