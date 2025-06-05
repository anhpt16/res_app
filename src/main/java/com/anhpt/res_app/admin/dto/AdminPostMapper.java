package com.anhpt.res_app.admin.dto;

import com.anhpt.res_app.admin.dto.response.post.PostResponse;
import com.anhpt.res_app.admin.dto.response.post.PostShortResponse;
import com.anhpt.res_app.common.entity.Post;
import com.anhpt.res_app.common.entity.TagPost;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AdminPostMapper {
    @Mapping(source = "user.id", target = "authorId")
//    @Mapping(source = "user.name", target = "authorName")
    @Mapping(source = "media.id", target = "mediaId")
    @Mapping(source = "media.fileName", target = "fileName")
    @Mapping(source = "tagPosts", target = "tagIds")
    PostResponse toPostResponse(Post post);

    @Mapping(source = "user.id", target = "authorId")
//    @Mapping(source = "user.name", target = "authorName")
    @Mapping(source = "media.id", target = "mediaId")
    @Mapping(source = "media.fileName", target = "fileName")
    PostShortResponse toPostShortResponse(Post post);

    default List<Long> mapTagPostsToTagIds(List<TagPost> tagPosts) {
        if (tagPosts == null) {
            return Collections.emptyList();
        }
        return tagPosts.stream()
            .map(tp -> tp.getTag().getId())
            .collect(Collectors.toList());
    }
}
