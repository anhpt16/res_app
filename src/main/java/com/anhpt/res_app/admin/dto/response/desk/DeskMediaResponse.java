package com.anhpt.res_app.admin.dto.response.desk;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DeskMediaResponse {
    private Long deskNumber;
    private Long mediaId;
    private Integer displayOrder;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;
    // Thêm
    private String fileName;
}
