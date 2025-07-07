package com.anhpt.res_app.common.entity;

import com.anhpt.res_app.common.enums.DeskPosition;
import com.anhpt.res_app.common.enums.DeskSeat;
import com.anhpt.res_app.common.enums.DeskType;
import com.anhpt.res_app.common.enums.status.DeskStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_desk")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Desk {
    @Id
    private Integer number;

    @Enumerated(EnumType.STRING)
    private DeskType type;
    @Enumerated(EnumType.STRING)
    private DeskPosition position;

    @Enumerated(EnumType.STRING)
    private DeskSeat seat;

    private String description;

    @Enumerated(EnumType.STRING)
    private DeskStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "desk", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeskMedia> deskMedias = new ArrayList<>();

    @OneToMany(mappedBy = "desk", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeskDuration> deskDurations = new ArrayList<>();

    @OneToMany(mappedBy = "desk")
    private List<Reservation> reservations = new ArrayList<>();
}
