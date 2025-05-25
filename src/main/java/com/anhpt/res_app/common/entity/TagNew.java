package com.anhpt.res_app.common.entity;

import com.anhpt.res_app.common.entity.key.TagNewId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tag_new")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TagNew {
    @EmbeddedId
    private TagNewId id;
}
