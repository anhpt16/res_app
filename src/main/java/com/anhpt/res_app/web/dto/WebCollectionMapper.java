package com.anhpt.res_app.web.dto;

import com.anhpt.res_app.common.entity.Collection;
import com.anhpt.res_app.web.dto.response.CollectionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "Spring")
public interface WebCollectionMapper {
    @Mapping(source = "media.fileName", target = "fileName")
    CollectionResponse toCollectionResponse(Collection collection);
}
