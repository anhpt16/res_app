package com.anhpt.res_app.common.entity;

import com.anhpt.res_app.common.entity.key.DeskMediaId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "desk_media")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DeskMedia {
    @EmbeddedId
    private DeskMediaId id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
