package com.anhpt.res_app.admin.dto.response.collection;

import com.anhpt.res_app.common.enums.status.CollectionStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
public class CollectionShortResponse {
    private final Long id;
    private final Long mediaId;
    private final Long userId;
    private final CollectionStatus status;
    private final Integer displayOrder;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private final LocalDateTime createdAt;
    // Thêm
    private final String userName;
    private final String fileName;
}
