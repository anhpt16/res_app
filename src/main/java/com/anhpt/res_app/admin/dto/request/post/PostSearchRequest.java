package com.anhpt.res_app.admin.dto.request.post;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class PostSearchRequest {
    
    private String searchTerm;
    private Boolean isMyPost;
    private String status;
    private String tagId;
    private String sortByCreatedAt;

    @Min(value = 1, message = "Số trang phải lớn hơn 0")
    private int page = 1;
    @Min(value = 1, message = "Số lượng item không hợp lệ")
    @Max(value = 20, message = "Số lượng item không hợp lệ")
    private int size = 10;
}
