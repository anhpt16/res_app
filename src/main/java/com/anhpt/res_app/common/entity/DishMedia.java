package com.anhpt.res_app.common.entity;

import com.anhpt.res_app.common.entity.key.DishMediaId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
    private DishMediaId id;
}
