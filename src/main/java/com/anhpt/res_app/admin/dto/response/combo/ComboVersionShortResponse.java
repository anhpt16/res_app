package com.anhpt.res_app.admin.dto.response.combo;

import com.anhpt.res_app.common.enums.status.ComboVersionStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ComboVersionShortResponse {
    private final Long id;
    private final String versionCode;
    private final ComboVersionStatus status;
}
