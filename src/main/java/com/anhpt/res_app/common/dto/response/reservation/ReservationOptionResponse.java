package com.anhpt.res_app.common.dto.response.reservation;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ReservationOptionResponse {
    private final List<SeatResponse> seats;
    private final DateResponse date;
    private final List<TimeStartResponse> timeStarts;
    private final List<DurationResponse> durations;
}
