package com.anhpt.res_app.common.entity;

import com.anhpt.res_app.common.entity.key.ComboVersionDishId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "combo_version_dish")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ComboVersionDish {
    @EmbeddedId
    private ComboVersionDishId id = new ComboVersionDishId();

    private Integer count;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("comboVersionId")
    @JoinColumn(name = "combo_version_id")
    private ComboVersion comboVersion;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("dishId")
    @JoinColumn(name = "dish_id")
    private Dish dish;
}
