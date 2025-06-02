package com.anhpt.res_app.common.entity;

import com.anhpt.res_app.common.entity.key.PermissionId;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_permission")
public class Permission {
    @EmbeddedId
    private PermissionId id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    private Role role;
}
