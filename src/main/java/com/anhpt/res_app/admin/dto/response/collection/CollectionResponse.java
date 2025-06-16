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
@ToString
@Builder
public class CollectionResponse {
    private final Long id;
    private final String name;
    private final Long mediaId;
    private final Long userId;
    private final Integer displayOrder;
    private final CollectionStatus status;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private final LocalDateTime createdAt;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private final LocalDateTime updatedAt;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private final LocalDateTime publishedAt;
    // Thêm
    private final String userName;
    private final String fileName;
}
