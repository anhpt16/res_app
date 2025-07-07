package com.anhpt.res_app.admin.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeStartSearchRequest {
    private String status;
    @Pattern(regexp = "(?i)^(ASC|DESC)$", message = "Thứ tự sắp xếp không hợp lệ")
    private String sortByTime;
}
