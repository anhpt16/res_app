package com.anhpt.res_app.common.entity;

import com.anhpt.res_app.common.enums.status.ReservationStatus;
import com.anhpt.res_app.common.enums.type.ReservationType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "tbl_reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private Integer duration;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    private String name;
    private String phone;

    @Column(name = "staff_id")
    private Long staffId;

    @Enumerated(EnumType.STRING)
    private ReservationType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "desk_number")
    private Desk desk;

    @OneToOne(mappedBy = "reservation", fetch = FetchType.LAZY)
    private Invoice invoice;
}
