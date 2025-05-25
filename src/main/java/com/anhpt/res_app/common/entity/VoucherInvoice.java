package com.anhpt.res_app.common.entity;

import com.anhpt.res_app.common.entity.key.VoucherInvoiceId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
}
