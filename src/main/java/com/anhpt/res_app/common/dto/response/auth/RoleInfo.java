package com.anhpt.res_app.common.dto.response.auth;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleInfo {
    private Integer id;
    private String name;
}
