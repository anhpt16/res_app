package com.anhpt.res_app.admin.dto.request.desk;

import com.anhpt.res_app.common.enums.DeskSeat;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeskUpdateRequest {
    private String type;
    private String position;
    private String seat;
    private String description;

    public boolean isEmpty() {
        return type == null && position == null && seat == null && description == null;
    }
}
