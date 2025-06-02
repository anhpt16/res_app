package com.anhpt.res_app.common.entity;

import com.anhpt.res_app.common.enums.status.InvoiceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tbl_invoice")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    @Column(precision = 10, scale = 2)
    private BigDecimal prepayment;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "total_price", precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @OneToMany(mappedBy = "invoice")
    List<VoucherInvoice> voucherInvoices;

    @OneToMany(mappedBy = "invoice")
    List<InvoiceStaff> invoiceStaffs;

    // TODO: Payment
    @OneToMany(mappedBy = "invoice")
    List<ComboVersionInvoice> comboVersionInvoices;

    @OneToMany(mappedBy = "invoice")
    List<DishInvoice> dishInvoices;
}
