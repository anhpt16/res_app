package com.anhpt.res_app.common.entity;

import com.anhpt.res_app.common.model.enums.status.DeskStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_desk")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Desk {
    @Id
    private Integer number;

    private String type;
    private String position;

    @Column(name = "min_seat")
    private Integer minSeat;

    @Column(name = "max_seat")
    private Integer maxSeat;

    private String description;

    @Enumerated(EnumType.STRING)
    private DeskStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
