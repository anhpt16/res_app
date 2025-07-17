package com.anhpt.res_app.admin.validation;

import com.anhpt.res_app.admin.dto.request.TimeStartSearchRequest;
import com.anhpt.res_app.common.entity.StartTime;
import com.anhpt.res_app.common.entity.key.StartTimeDurationId;
import com.anhpt.res_app.common.enums.status.StartTimeStatus;
import com.anhpt.res_app.common.exception.MultiDuplicateException;
import com.anhpt.res_app.common.exception.ResourceNotFoundException;
import com.anhpt.res_app.common.repository.DurationRepository;
import com.anhpt.res_app.common.repository.StartTimeDurationRepository;
import com.anhpt.res_app.common.repository.StartTimeRepository;
import com.anhpt.res_app.common.utils.function.FieldNameUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminTimeStartValidation {
    private final StartTimeRepository startTimeRepository;
    private final DurationRepository durationRepository;
    private final StartTimeDurationRepository startTimeDurationRepository;

    public void validateCreate(LocalTime timeStart) {
        // Kiểm tra thời gian bắt đầu có tồn tại trong database không
        Map<String, String> errors = new HashMap<>();
        Optional<StartTime> startTime = startTimeRepository.findByTimeStart(timeStart);
        if (startTime.isPresent()) {
            String field = FieldNameUtil.getFieldName(StartTime::getTimeStart);
            errors.put(field, "Đã tồn tại");
        }
        if (errors.size() > 0) {
            throw new MultiDuplicateException(errors);
        }
    }

    public void validateUpdateStatus(Long timeStartId, String status) {
        // Kiểm tra thời gian bắt đầu có tồn tại trong database không
        Optional<StartTime> startTime = startTimeRepository.findById(timeStartId);
        if (startTime.isEmpty()) {
            log.warn("Thời gian bắt đầu không tồn tại: {}", timeStartId);
            throw new ResourceNotFoundException("Thời gian bắt đầu không tồn tại");
        }
        // Kiểm tra trạng thái có hợp lệ không
        StartTimeStatus startTimeStatus = StartTimeStatus.fromCode(status);
        // Kiểm tra trùng lặp trạng thái
        if (startTimeStatus.equals(startTime.get().getStatus())) {
            log.warn("Trạng thái đã tồn tại: {}", status);
            throw new ResourceNotFoundException("Trạng thái đã tồn tại");
        }
    }

    public void validateSearch(TimeStartSearchRequest request) {
        // Kiểm tra trạng thái có hợp lệ không
        if (request.getStatus() != null) {
            StartTimeStatus startTimeStatus = StartTimeStatus.fromCode(request.getStatus());
        }
    }

    public void validateAddDuration(Long timeStartId, Long durationId) {
        // Kiểm tra timeStart tồn tại
        if (!startTimeRepository.existsById(timeStartId)) {
            log.warn("Thời gian bắt đầu không tồn tại {}", timeStartId);
            throw new IllegalArgumentException("Thời gian bắt đầu không tồn tại");
        }
        // Kiểm tra duration tồn tại
        if (!durationRepository.existsById(durationId)) {
            log.warn("Thời lượng không tồn tại {}", durationId);
            throw new IllegalArgumentException("Thời lượng không tồn tại");
        }
        // Kiểm tra timeStart - Duration tồn tại
        StartTimeDurationId startTimeDurationId = new StartTimeDurationId(timeStartId, durationId);
        if (startTimeDurationRepository.existsById(startTimeDurationId)) {
            log.warn("Thời gian bắt đầu đã được thiết lập thời lượng TimeStartId - {}, DurationId - {}", timeStartId, durationId);
            throw new IllegalArgumentException("Thời gian bắt đầu đã được thiết lập thời lượng");
        }
    }

    public void validateDeleteDuration(Long timeStartId, Long durationId) {
        // Kiểm tra timeStartId - durationId tồn tại
        StartTimeDurationId startTimeDurationId = new StartTimeDurationId(timeStartId, durationId);
        if (!startTimeDurationRepository.existsById(startTimeDurationId)) {
            log.warn("Không tồn tại TimeStartId - {}, DurationId - {}", timeStartId, durationId);
            throw new ResourceNotFoundException("Không tìm thấy TimeStartId - DurationId");
        }
    }

}
