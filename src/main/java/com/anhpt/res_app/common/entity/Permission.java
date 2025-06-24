package com.anhpt.res_app.common.entity;

import com.anhpt.res_app.common.entity.key.PermissionId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_permission")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Permission {
    @EmbeddedId
    private PermissionId id = new PermissionId();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    private Role role;
}
