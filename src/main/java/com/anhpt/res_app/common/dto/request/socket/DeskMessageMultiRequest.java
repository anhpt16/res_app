package com.anhpt.res_app.common.dto.request.socket;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeskMessageMultiRequest {
    private Integer oldDesk;
    private Integer newDesk;
}
