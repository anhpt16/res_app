package com.anhpt.res_app.admin.dto;

import com.anhpt.res_app.admin.dto.response.PostResponse;
import com.anhpt.res_app.admin.dto.response.PostShortResponse;
import com.anhpt.res_app.common.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {
    @Mapping(source = "user.id", target = "authorId")
//    @Mapping(source = "user.name", target = "authorName")
    PostResponse toPostResponse(Post post);

    @Mapping(source = "user.id", target = "authorId")
//    @Mapping(source = "user.name", target = "authorName")
    PostShortResponse toPostShortResponse(Post post);
}
