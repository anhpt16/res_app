package com.anhpt.res_app.common.entity;

import com.anhpt.res_app.common.entity.key.StartTimeDurationId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "start_time_duration")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StartTimeDuration {
    @EmbeddedId
    private StartTimeDurationId id = new StartTimeDurationId();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("startTimeId")
    @JoinColumn(name = "start_time_id")
    private StartTime startTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("durationId")
    @JoinColumn(name = "duration_id")
    private Duration duration;
}
