package com.anhpt.res_app.web.dto;

import com.anhpt.res_app.common.entity.Post;
import com.anhpt.res_app.web.dto.response.PostResponse;
import com.anhpt.res_app.web.dto.response.PostShortResponse;
import com.anhpt.res_app.web.dto.response.TagResponse;
import com.anhpt.res_app.web.dto.shared.PostDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WebPostMapper {

    @Mapping(source = "media.fileName", target = "thumbnail")
    PostShortResponse toPostShortResponse(Post post);

    PostResponse toPostResponse(PostDetail postDetail, List<PostShortResponse> postsRelated);

    @Mapping(source = "post.media.fileName", target = "thumbnail")
    PostDetail toPostDetail(Post post, List<TagResponse> tags);
}
