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
public class ComboVersionInvoiceId implements Serializable {
    @Column(name = "invoice_id")
    private Long invoiceId;

    @Column(name = "combo_version_id")
    private Long comboVersionId;
}
