package com.anhpt.res_app.common.entity;

import com.anhpt.res_app.common.entity.key.PermissionId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_permission")
public class Permission {
    @EmbeddedId
    private PermissionId id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
