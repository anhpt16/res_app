package com.anhpt.res_app.admin.dto;

import com.anhpt.res_app.admin.dto.response.TagResponse;
import com.anhpt.res_app.common.entity.Tag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdminTagMapper {
    TagResponse toTagResponse(Tag tag);
}
