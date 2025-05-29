package com.anhpt.res_app.common.entity;

import com.anhpt.res_app.common.enums.status.DishStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_dish")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "category_id")
    private Long categoryId;

    private String unit;
    private BigDecimal price;

    @Column(name = "price_discount")
    private BigDecimal priceDiscount;

    @Column(name = "duration_from")
    private Integer durationFrom;

    @Column(name = "duration_to")
    private Integer durationTo;

    @Column(name = "ingradient_display")
    private String ingradientDisplay;

    private String description;
    private String introduce;

    @Enumerated(EnumType.STRING)
    private DishStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;
}
