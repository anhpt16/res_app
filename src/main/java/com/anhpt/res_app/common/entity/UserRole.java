package com.anhpt.res_app.common.entity;

import com.anhpt.res_app.common.entity.key.UserRoleId;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_role")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserRole {
    @EmbeddedId
    private UserRoleId id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
