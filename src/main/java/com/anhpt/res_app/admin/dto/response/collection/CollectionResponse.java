package com.anhpt.res_app.admin.dto.response.collection;

import com.anhpt.res_app.common.enums.status.CollectionStatus;
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
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final LocalDateTime publishedAt;
    // Thêm
    private final String userName;
    private final String fileName;
}
