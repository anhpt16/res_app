package com.anhpt.res_app.admin.api;

import com.anhpt.res_app.admin.service.AdminPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/permission")
@RequiredArgsConstructor
@Validated
public class AdminPermissionApi {
    private final AdminPermissionService adminPermissionService;
}
