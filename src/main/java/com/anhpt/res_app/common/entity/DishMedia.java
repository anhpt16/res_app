package com.anhpt.res_app.common.entity;

import com.anhpt.res_app.common.entity.key.DishMediaId;
import com.anhpt.res_app.common.entity.key.TagPostId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "dish_media")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DishMedia {
    @EmbeddedId
    private DishMediaId id = new DishMediaId();;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("dishId")
    @JoinColumn(name = "dish_id")
    private Dish dish;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("mediaId")
    @JoinColumn(name = "media_id")
    private Media media;

    @Column(name = "display_order")
    private Integer displayOrder;
}
