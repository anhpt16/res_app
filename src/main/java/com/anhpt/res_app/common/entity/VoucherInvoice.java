package com.anhpt.res_app.common.entity;

import com.anhpt.res_app.common.entity.key.VoucherInvoiceId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "voucher_invoice")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VoucherInvoice {
    @EmbeddedId
    private VoucherInvoiceId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("voucherId")
    @JoinColumn(name = "voucher_id")
    private Voucher voucher;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("invoiceId")
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;
}
