package com.anhpt.res_app.common.entity.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class VoucherInvoiceId implements Serializable {
    @Column(name = "voucher_id")
    private Long voucherId;

    @Column(name = "invoice_id")
    private Long invoiceId;
}
