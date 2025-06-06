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
public class TagPostId implements Serializable {
    @Column(name = "tag_id")
    private Long tagId;

    @Column(name = "post_id")
    private Long postId;
}
