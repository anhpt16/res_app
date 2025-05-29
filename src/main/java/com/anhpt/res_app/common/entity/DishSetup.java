package com.anhpt.res_app.common.entity;

import com.anhpt.res_app.common.enums.status.DishStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_setup")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DishSetup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dish_id")
    private Long dishId;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_status")
    private DishStatus currentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "next_status")
    private DishStatus nextStatus;

    private LocalDateTime milestone;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
