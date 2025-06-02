package com.anhpt.res_app.common.entity;

import com.anhpt.res_app.common.enums.status.ComboVersionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tbl_combo_version")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ComboVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "version_code")
    private String versionCode;

    private BigDecimal price;

    @Column(name = "price_discount")
    private BigDecimal priceDiscount;

    @Column(name = "duration_from")
    private Integer duarationFrom;

    @Column(name = "duration_to")
    private Integer durationTo;

    @Enumerated(EnumType.STRING)
    private ComboVersionStatus status;

    @Column(name = "start_at")
    private LocalDateTime startAt;

    @Column(name = "finish_at")
    private LocalDateTime finishAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "combo_id")
    private Combo combo;

    @OneToMany(mappedBy = "comboVersion")
    private List<ComboVersionDish> comboVersionDishes;

    @OneToMany(mappedBy = "comboVersion")
    private List<ComboVersionInvoice> comboVersionInvoices;
}
