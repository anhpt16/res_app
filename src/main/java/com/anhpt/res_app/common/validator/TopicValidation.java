package com.anhpt.res_app.common.validator;

import com.anhpt.res_app.common.dto.request.reservation.TopicRequest;
import com.anhpt.res_app.common.entity.Duration;
import com.anhpt.res_app.common.entity.StartTime;
import com.anhpt.res_app.common.enums.status.DurationStatus;
import com.anhpt.res_app.common.enums.status.StartTimeStatus;
import com.anhpt.res_app.common.repository.DurationRepository;
import com.anhpt.res_app.common.repository.StartTimeRepository;
import com.anhpt.res_app.common.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class TopicValidation {
    private final DurationRepository durationRepository;
    private final StartTimeRepository startTimeRepository;

    public void validateGet(TopicRequest request) {
        // Kiểm tra date hợp lệ (nằm trong khoảng thời gian cho phép đặt bàn)
        if (!isValidDate(request.getDate())) {
            log.warn("Ngày bắt đầu không hợp lệ {}", request.getDate());
            throw new IllegalArgumentException("Ngày bắt đầu không hợp lệ");
        }
        // Kiểm tra thời gian bắt đầu hợp lệ (thuộc danh sách thời điểm cho phép đặt)
        Optional<StartTime> startTime = startTimeRepository.findByIdAndStatus(request.getStartId(), StartTimeStatus.ACTIVE);
        if (startTime.isEmpty()) {
            log.warn("Thời điểm bắt đầu không hợp lệ {}", request.getStartId());
            throw new IllegalArgumentException("Thời điểm bắt đầu không hợp lệ");
        }
        // Kiểm tra thời lượng hợp lệ (thuộc danh sách thời lượng đang hoạt động)
        Optional<Duration> duration = durationRepository.findByIdAndStatus(request.getDurationId(), DurationStatus.ACTIVE);
        if (duration.isEmpty()) {
            log.warn("Thời lượng không hợp lệ {}", request.getDurationId());
            throw new IllegalArgumentException("Thời lượng không hợp lệ");
        }
    }

    private boolean isValidDate(LocalDate date) {
        LocalDate minDate = LocalDate.now().plusDays(Constants.RESERVATION_CURRENT_START_DATE);
        LocalDate maxDate = minDate.plusDays(Constants.RESERVATION_MAX_DATE);
        return !date.isBefore(minDate) && !date.isAfter(maxDate);
    }
}
