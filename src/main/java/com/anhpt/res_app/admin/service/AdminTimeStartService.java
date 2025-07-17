package com.anhpt.res_app.admin.service;

import com.anhpt.res_app.admin.dto.AdminTimeStartMapper;
import com.anhpt.res_app.admin.dto.request.TimeStartSearchRequest;
import com.anhpt.res_app.admin.dto.response.timestart.TimeStartDurationResponse;
import com.anhpt.res_app.admin.dto.response.timestart.TimeStartResponse;
import com.anhpt.res_app.admin.validation.AdminTimeStartValidation;
import com.anhpt.res_app.common.entity.Duration;
import com.anhpt.res_app.common.entity.StartTime;
import com.anhpt.res_app.common.entity.StartTimeDuration;
import com.anhpt.res_app.common.entity.key.StartTimeDurationId;
import com.anhpt.res_app.common.enums.status.StartTimeStatus;
import com.anhpt.res_app.common.exception.ResourceNotFoundException;
import com.anhpt.res_app.common.repository.DurationRepository;
import com.anhpt.res_app.common.repository.StartTimeDurationRepository;
import com.anhpt.res_app.common.repository.StartTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminTimeStartService {
    // Repository
    private final StartTimeRepository startTimeRepository;
    private final DurationRepository durationRepository;
    private final StartTimeDurationRepository startTimeDurationRepository;
    // Mappper
    private final AdminTimeStartMapper adminTimeStartMapper;
    // Validation
    private final AdminTimeStartValidation adminTimeStartValidation;

    // Tạo mới Start Time
    public TimeStartResponse create(LocalTime timeStart) {
        adminTimeStartValidation.validateCreate(timeStart);
        StartTime startTime = new StartTime();
        startTime.setTimeStart(timeStart);
        startTime.setStatus(StartTimeStatus.INACTIVE);
        startTime.setCreatedAt(LocalDateTime.now());
        startTime.setUpdatedAt(LocalDateTime.now());
        startTime = startTimeRepository.save(startTime);
        return adminTimeStartMapper.toTimeStartResponse(startTime);
    }

    // Cập nhật trạng thái Start Time
    public TimeStartResponse updateStatus(Long timeStartId, String status) {
        adminTimeStartValidation.validateUpdateStatus(timeStartId, status);
        StartTime startTime = startTimeRepository.findById(timeStartId)
            .orElseThrow(() -> new ResourceNotFoundException("Thời gian bắt đầu không tồn tại"));
        startTime.setStatus(StartTimeStatus.fromCode(status));
        startTime.setUpdatedAt(LocalDateTime.now());
        startTime = startTimeRepository.save(startTime);
        return adminTimeStartMapper.toTimeStartResponse(startTime);
    }

    // Lấy danh sách Start Time
    public List<TimeStartResponse> get(TimeStartSearchRequest request) {
        adminTimeStartValidation.validateSearch(request);
        List<StartTime> startTimes = new ArrayList<>();
        if (request.getStatus() != null) {
            StartTimeStatus startTimeStatus = StartTimeStatus.fromCode(request.getStatus());
            startTimes = startTimeRepository.findByStatus(startTimeStatus);
        } else {
            startTimes = startTimeRepository.findAll();
        }
        if (request.getSortByTime() != null && request.getSortByTime().equals("DESC")) {
            startTimes.sort(Comparator.comparing(StartTime::getTimeStart).reversed());
        } else {
            startTimes.sort(Comparator.comparing(StartTime::getTimeStart));
        }
        return startTimes.stream()
            .map(adminTimeStartMapper::toTimeStartResponse)
            .collect(Collectors.toList());
    }

    // Thêm thời lượng cho Start Time
    public TimeStartDurationResponse addTimeStartDuration(Long timeStartId, Long durationId) {
        adminTimeStartValidation.validateAddDuration(timeStartId, durationId);
        StartTime startTime = startTimeRepository.findById(timeStartId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy StartTime"));
        Duration duration = durationRepository.findById(durationId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Duration"));
        StartTimeDuration startTimeDuration = new StartTimeDuration();
        StartTimeDurationId startTimeDurationId = new StartTimeDurationId(timeStartId, durationId);
        startTimeDuration.setId(startTimeDurationId);
        startTimeDuration.setStartTime(startTime);
        startTimeDuration.setDuration(duration);
        startTimeDuration.setCreatedAt(LocalDateTime.now());
        startTimeDuration = startTimeDurationRepository.save(startTimeDuration);
        return adminTimeStartMapper.toTimeStartDurationResponse(startTimeDuration);
    }

    // Xóa thời lượng cho Start Time
    public void deleteTimeStartDuration(Long timeStartId, Long durationId) {
        adminTimeStartValidation.validateDeleteDuration(timeStartId, durationId);
        StartTime startTime = startTimeRepository.findById(timeStartId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy StartTime"));
        startTime.getStartTimeDurations().removeIf(startTimeDuration -> startTimeDuration.getDuration().getId().equals(durationId));
        startTimeRepository.save(startTime);
    }

    // Lấy danh sách thời lượng của Start Time
    public List<TimeStartDurationResponse> getTimeStartDurations(Long timeStartId) {
        StartTime startTime = startTimeRepository.findById(timeStartId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy StartTime"));
        List<StartTimeDuration> startTimeDurations = startTime.getStartTimeDurations();
        if (startTimeDurations.isEmpty()) {
            return Collections.emptyList();
        }
        return startTimeDurations.stream()
            .map(adminTimeStartMapper::toTimeStartDurationResponse)
            .collect(Collectors.toList());
    }
}
