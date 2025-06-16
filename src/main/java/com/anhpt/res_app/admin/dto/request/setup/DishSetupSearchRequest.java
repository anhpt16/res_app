package com.anhpt.res_app.admin.dto.request.setup;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DishSetupSearchRequest {
    private String currentStatus;
    private String nextStatus;
    @Pattern(regexp = "(?i)^(ASC|DESC)$", message = "Thứ tự sắp xếp không hợp lệ")
    private String sortByCreatedAt;
    @Min(value = 1, message = "Số trang phải lớn hơn 0")
    private int page = 1;
    @Min(value = 1, message = "Số lượng item không hợp lệ")
    @Max(value = 20, message = "Số lượng item không hợp lệ")
    private int size = 10;
}
