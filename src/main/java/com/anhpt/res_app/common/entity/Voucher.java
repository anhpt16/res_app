package com.anhpt.res_app.common.entity;


import com.anhpt.res_app.common.enums.VoucherScope;
import com.anhpt.res_app.common.enums.status.VoucherStatus;
import com.anhpt.res_app.common.enums.type.VoucherType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_voucher")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String name;
    private String description;

    @Column(name = "discount_type")
    @Enumerated(EnumType.STRING)
    private VoucherType voucherType;

    @Column(name = "discount_value", precision = 10, scale = 2)
    private BigDecimal discountValue;

    @Column(name = "max_discount", precision = 10, scale = 2)
    private BigDecimal maxDiscount;

    @Column(name = "min_order_value", precision = 10, scale = 2)
    private BigDecimal minOrderValue;

    @Column(name = "applicable_scope")
    @Enumerated(EnumType.STRING)
    private VoucherScope voucherScope;

    private Integer quantity;

    @Column(name = "start_at")
    private LocalDateTime startAt;

    @Column(name = "end_at")
    private LocalDateTime endAt;

    @Column(name = "max_usage")
    private Integer maxUsage;

    @Enumerated(EnumType.STRING)
    private VoucherStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "voucher")
    private List<UserVoucher> userVouchers = new ArrayList<>();

    @OneToMany(mappedBy = "voucher")
    private List<VoucherInvoice> voucherInvoices = new ArrayList<>();
}
