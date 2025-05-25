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
public class DishMediaId implements Serializable {
    @Column(name = "media_id")
    private Long mediaId;

    @Column(name = "dish_id")
    private Long dishId;
}
