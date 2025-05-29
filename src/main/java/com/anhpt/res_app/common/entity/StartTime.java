package com.anhpt.res_app.common.entity;

import com.anhpt.res_app.common.enums.status.StartTimeStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "tbl_start_time")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StartTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "time_start")
    private LocalTime timeStart;

    @Enumerated(EnumType.STRING)
    private StartTimeStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
