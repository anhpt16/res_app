package com.anhpt.res_app.admin.dto.response.desk;

import com.anhpt.res_app.common.enums.DeskPosition;
import com.anhpt.res_app.common.enums.DeskSeat;
import com.anhpt.res_app.common.enums.DeskType;
import com.anhpt.res_app.common.enums.status.DeskStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class DeskResponse {
    private final Integer number;
    private final DeskType type;
    private final DeskPosition position;
    private final DeskSeat seat;
    private final String description;
    private final DeskStatus status;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private final LocalDateTime createdAt;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private final LocalDateTime updatedAt;
}
