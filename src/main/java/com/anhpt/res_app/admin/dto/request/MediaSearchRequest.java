package com.anhpt.res_app.admin.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class MediaSearchRequest {
    @Length(max = 100, message = "Từ khóa tìm kiếm không được vượt quá 100 ký tự")
    private String searchTerm;

    @Pattern(regexp = "(?i)^(ASC|DESC)$", message = "Thứ tự sắp xếp không hợp lệ")
    private String sortByCreatedAt = "DESC"; // Mặc định sắp xếp mới nhất

    @Pattern(regexp = "(?i)^(IMAGE|VIDEO)$", message = "Loại file không hợp lệ")
    private String type;
    
    @Min(value = 1, message = "Số trang phải lớn hơn 0")
    private int page = 1;
    
    @Min(value = 1, message = "Số lượng item không hợp lệ")
    @Max(value = 20, message = "Số lượng item không hợp lệ")
    private int size = 10;
} 