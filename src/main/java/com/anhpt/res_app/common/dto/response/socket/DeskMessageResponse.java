package com.anhpt.res_app.common.dto.response.socket;

import com.anhpt.res_app.common.enums.status.DeskMessageStatus;
import com.anhpt.res_app.common.enums.status.DeskStatus;
import com.anhpt.res_app.common.enums.status.ReservationStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DeskMessageResponse {
    private final Long userId;
    private final Integer deskNumber;
    private final DeskMessageStatus status;
}
