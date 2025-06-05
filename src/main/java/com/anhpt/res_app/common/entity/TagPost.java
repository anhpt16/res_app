package com.anhpt.res_app.common.entity;

import com.anhpt.res_app.common.entity.key.TagPostId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tag_post")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TagPost {
    @EmbeddedId
    private TagPostId id = new TagPostId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tagId")
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("postId")
    @JoinColumn(name = "post_id")
    private Post post;
}
