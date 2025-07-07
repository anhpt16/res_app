package com.anhpt.res_app.admin.dto;

import com.anhpt.res_app.admin.dto.response.desk.DeskDurationResponse;
import com.anhpt.res_app.admin.dto.response.desk.DeskMediaResponse;
import com.anhpt.res_app.admin.dto.response.desk.DeskResponse;
import com.anhpt.res_app.admin.dto.response.desk.DeskShortResponse;
import com.anhpt.res_app.common.entity.Desk;
import com.anhpt.res_app.common.entity.DeskDuration;
import com.anhpt.res_app.common.entity.DeskMedia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdminDeskMapper {

    DeskResponse toDeskResponse(Desk desk);
    DeskShortResponse toDeskShortResponse(Desk desk);

    @Mapping(source = "desk.number", target = "deskNumber")
    @Mapping(source = "media.id", target = "mediaId")
    @Mapping(source = "media.fileName", target = "fileName")
    DeskMediaResponse toDeskMediaResponse(DeskMedia deskMedia);

    @Mapping(source = "id", target = "deskDurationId")
    @Mapping(source = "desk.number", target = "deskNumber")
    @Mapping(source = "duration.id", target = "durationId")
    @Mapping(source = "duration.duration", target = "duration")
    DeskDurationResponse toDeskDurationResponse(DeskDuration deskDuration);
}
