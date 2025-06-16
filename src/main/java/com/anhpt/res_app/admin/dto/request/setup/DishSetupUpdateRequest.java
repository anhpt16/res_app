package com.anhpt.res_app.admin.dto.request.setup;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DishSetupUpdateRequest {
    private String nextStatus;
    private LocalDate milestone;

    public boolean isEmpty() {
        return nextStatus == null && milestone == null;
    }
}
