package com.anhpt.res_app.common.entity;

import com.anhpt.res_app.common.entity.key.DeskMediaId;
import jakarta.persistence.*;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("deskNumber")
    @JoinColumn(name = "desk_number")
    private Desk desk;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("mediaId")
    @JoinColumn(name = "media_id")
    private Media media;
}
