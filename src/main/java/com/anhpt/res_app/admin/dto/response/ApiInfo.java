package com.anhpt.res_app.admin.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiInfo {
    private String path;
    private String method;
    private String description;
}
