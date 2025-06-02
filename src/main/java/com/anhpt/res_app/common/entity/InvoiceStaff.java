package com.anhpt.res_app.common.entity;

import com.anhpt.res_app.common.entity.key.InvoiceStaffId;
import com.anhpt.res_app.common.enums.type.InvoiceStaffType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "invoice_staff")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InvoiceStaff {
    @EmbeddedId
    private InvoiceStaffId id;

    @Enumerated(EnumType.STRING)
    private InvoiceStaffType type;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("invoiceId")
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("staffId")
    @JoinColumn(name = "staff_id")
    private User user;
}
