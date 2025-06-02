package com.anhpt.res_app.admin.dto.request;

import lombok.Getter;

@Getter
public class PostUpdateRequest {
    private String title;
    private String content;
    private String status;
}
