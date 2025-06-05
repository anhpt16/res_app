package com.anhpt.res_app.web.dto;

import com.anhpt.res_app.common.entity.Tag;
import com.anhpt.res_app.web.dto.response.TagResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WebTagMapper {
    TagResponse toTagResponse (Tag tag);
}
