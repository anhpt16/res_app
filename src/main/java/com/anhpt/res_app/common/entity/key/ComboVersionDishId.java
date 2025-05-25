package com.anhpt.res_app.common.entity.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ComboVersionDishId implements Serializable {
    @Column(name = "combo_version_id")
    private Long comboVersionId;

    @Column(name = "dish_id")
    private Long dishId;
}
