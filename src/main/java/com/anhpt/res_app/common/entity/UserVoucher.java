package com.anhpt.res_app.common.entity;

import com.anhpt.res_app.common.entity.key.UserVoucherId;
import com.anhpt.res_app.common.model.enums.status.UserVoucherStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_voucher")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserVoucher {
    @EmbeddedId
    private UserVoucherId id;

    private Integer quantity;

    @Column(name = "quantity_usage")
    private Integer quantityUsage;

    @Enumerated(EnumType.STRING)
    private UserVoucherStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
