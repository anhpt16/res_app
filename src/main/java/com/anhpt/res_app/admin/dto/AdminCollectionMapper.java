package com.anhpt.res_app.admin.dto;

import com.anhpt.res_app.admin.dto.response.collection.CollectionResponse;
import com.anhpt.res_app.admin.dto.response.collection.CollectionShortResponse;
import com.anhpt.res_app.common.entity.Collection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdminCollectionMapper {
    @Mapping(source = "user.id", target = "userId")
    //    @Mapping(source = "user.name", target = "userName")
    @Mapping(source = "media.id", target = "mediaId")
    @Mapping(source = "media.fileName", target = "fileName")
    CollectionResponse toCollectionResponse(Collection collection);

    @Mapping(source = "user.id", target = "userId")
    //    @Mapping(source = "user.name", target = "userName")
    @Mapping(source = "media.id", target = "mediaId")
    @Mapping(source = "media.fileName", target = "fileName")
    CollectionShortResponse toCollectionShortResponse(Collection collection);
}
