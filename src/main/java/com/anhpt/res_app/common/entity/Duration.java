package com.anhpt.res_app.common.entity;

import com.anhpt.res_app.common.enums.status.DurationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_duration")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Duration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer duration;

    @Enumerated(EnumType.STRING)
    private DurationStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "duration")
    private List<DeskDuration> deskDurations = new ArrayList<>();

    @OneToMany(mappedBy = "duration")
    private List<StartTimeDuration> startTimeDurations = new ArrayList<>();
}
