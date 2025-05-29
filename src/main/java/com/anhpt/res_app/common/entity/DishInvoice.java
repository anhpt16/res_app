package com.anhpt.res_app.common.entity;

import com.anhpt.res_app.common.entity.key.DishInvoiceId;
import com.anhpt.res_app.common.enums.status.DishInvoiceStatus;
import com.anhpt.res_app.common.enums.type.DishInvoiceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "dish_invoice")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DishInvoice {
    @EmbeddedId
    private DishInvoiceId id;

    private Integer count;
    private BigDecimal price;

    @Column(name = "price_discount")
    private BigDecimal priceDiscount;

    @Enumerated(EnumType.STRING)
    private DishInvoiceType type;

    @Enumerated(EnumType.STRING)
    private DishInvoiceStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
