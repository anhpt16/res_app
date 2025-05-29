package com.anhpt.res_app.common.entity;

import com.anhpt.res_app.common.entity.key.ComboVersionInvoiceId;
import com.anhpt.res_app.common.enums.status.ComboVersionInvoiceStatus;
import com.anhpt.res_app.common.enums.type.ComboVersionInvoiceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "combo_version_invoice")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ComboVersionInvoice {
    @EmbeddedId
    private ComboVersionInvoiceId id;

    private Integer count;
    private BigDecimal price;

    @Column(name = "price_discount")
    private BigDecimal priceDiscount;

    @Enumerated(EnumType.STRING)
    private ComboVersionInvoiceType type;

    @Enumerated(EnumType.STRING)
    private ComboVersionInvoiceStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
