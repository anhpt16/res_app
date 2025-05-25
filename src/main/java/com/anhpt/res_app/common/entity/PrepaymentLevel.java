package com.anhpt.res_app.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_prepayment_level")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PrepaymentLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_bill_from")
    private BigDecimal totalBillFrom;

    @Column(name = "total_bill_to")
    private BigDecimal totalBillTo;

    private BigDecimal percent;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
