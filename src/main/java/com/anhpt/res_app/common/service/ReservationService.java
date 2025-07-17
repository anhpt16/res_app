package com.anhpt.res_app.common.service;

import com.anhpt.res_app.common.dto.request.reservation.TopicRequest;
import com.anhpt.res_app.common.dto.response.reservation.*;
import com.anhpt.res_app.common.entity.Duration;
import com.anhpt.res_app.common.entity.StartTime;
import com.anhpt.res_app.common.enums.DeskSeat;
import com.anhpt.res_app.common.enums.status.DurationStatus;
import com.anhpt.res_app.common.enums.status.StartTimeStatus;
import com.anhpt.res_app.common.repository.DurationRepository;
import com.anhpt.res_app.common.repository.StartTimeRepository;
import com.anhpt.res_app.common.utils.Constants;
import com.anhpt.res_app.common.validator.TopicValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {
    // Repository
    private final StartTimeRepository startTimeRepository;
    private final DurationRepository durationRepository;
    // Validation
    private final TopicValidation topicValidation;

    public ReservationOptionResponse initReservationOptions() {
        List<SeatResponse> seats = getAllSeats();
        DateResponse date = getDate();
        List<TimeStartResponse> timeStarts = getAllTimeslots();
        List<DurationResponse> durations = getAllDurations();
        return ReservationOptionResponse.builder()
            .seats(seats)
            .date(date)
            .timeStarts(timeStarts)
            .durations(durations)
            .build();
    }

    // Lấy danh sách topic
    public List<String> getTopics(TopicRequest request) {
        topicValidation.validateGet(request);
        // 9:30 | 80p -> (9:30, 10:00, 10:30), 11:00, 11:30, 12:00
        return null;
    }



    // Lấy danh sách chỗ ngồi
    private List<SeatResponse> getAllSeats() {
        return Arrays.stream(DeskSeat.values())
            .map(seat -> new SeatResponse(seat.name(), seat.getLabel()))
            .collect(Collectors.toList());
    }

    // Lấy danh sách timeslot (hoạt động)
    private List<TimeStartResponse> getAllTimeslots() {
        List<StartTime> startTimes = startTimeRepository.findByStatus(StartTimeStatus.ACTIVE);
        if (startTimes.isEmpty()) {
            return Collections.emptyList();
        }
        return startTimes.stream()
            .map(startTime -> new TimeStartResponse(startTime.getId(), startTime.getTimeStart()))
            .collect(Collectors.toList());
    }

    // Lấy danh sách thời lượng (hoạt động)
    private List<DurationResponse> getAllDurations() {
        List<Duration> durations = durationRepository.findByStatus(DurationStatus.ACTIVE);
        if (durations.isEmpty()) {
            return Collections.emptyList();
        }
        return durations.stream()
            .map(duration -> new DurationResponse(duration.getId(), duration.getDuration()))
            .collect(Collectors.toList());
    }

    // Lấy ngày đặt bàn (nhỏ nhất và lớn nhất)
    private DateResponse getDate() {
        // Lấy thời điểm bắt đầu
        LocalDate dateMin = LocalDate.now().plusDays(Constants.RESERVATION_CURRENT_START_DATE);
        // Lấy thời điểm kết thúc
        LocalDate dateMax = dateMin.plusDays(Constants.RESERVATION_MAX_DATE);
        return new DateResponse(dateMin, dateMax);
    }
}
