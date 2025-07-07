package com.anhpt.res_app.admin.service;

import com.anhpt.res_app.admin.dto.AdminDurationMapper;
import com.anhpt.res_app.admin.dto.request.desk.DurationCreateRequest;
import com.anhpt.res_app.admin.dto.request.desk.DurationSearchRequest;
import com.anhpt.res_app.admin.dto.response.desk.DurationResponse;
import com.anhpt.res_app.admin.filter.AdminDurationFilter;
import com.anhpt.res_app.admin.validation.AdminDurationValidation;
import com.anhpt.res_app.common.dto.response.PageResponse;
import com.anhpt.res_app.common.entity.Duration;
import com.anhpt.res_app.common.enums.status.DurationStatus;
import com.anhpt.res_app.common.exception.ResourceNotFoundException;
import com.anhpt.res_app.common.repository.DurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminDurationService {
    // Repository
    private final DurationRepository durationRepository;
    // Validation
    private final AdminDurationValidation adminDurationValidation;
    // Mapper
    private final AdminDurationMapper adminDurationMapper;
    // Other
    private final AdminDurationFilter adminDurationFilter;

    // Tạo Duration
    public DurationResponse create(DurationCreateRequest request) {
        adminDurationValidation.validateCreate(request);
        Duration duration = new Duration();
        duration.setDuration(request.getDuration());
        duration.setStatus(DurationStatus.ACTIVE);
        duration.setCreatedAt(LocalDateTime.now());
        duration.setUpdatedAt(LocalDateTime.now());
        durationRepository.save(duration);
        duration = durationRepository.save(duration);
        return adminDurationMapper.toDurationResponse(duration);
    }

    // Cập nhật trạng thái Duration
    public DurationResponse updateStatus(Long durationId, String status) {
        adminDurationValidation.validateUpdateStatus(durationId, status);
        DurationStatus durationStatus = DurationStatus.fromCode(status);
        Duration duration = durationRepository.findById(durationId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Duration"));
        duration.setStatus(durationStatus);
        duration.setUpdatedAt(LocalDateTime.now());
        duration = durationRepository.save(duration);
        return adminDurationMapper.toDurationResponse(duration);
    }

    // Lấy danh sách Duration
    public PageResponse<DurationResponse> search(DurationSearchRequest request) {
        adminDurationValidation.validateSearch(request);
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());
        Specification<Duration> specification = adminDurationFilter.search(request);
        Page<Duration> durations = durationRepository.findAll(specification, pageable);
        List<DurationResponse> durationResponses = durations.stream()
            .map(adminDurationMapper::toDurationResponse)
            .collect(Collectors.toList());
        return new PageResponse<>(
            durationResponses,
            request.getPage(),
            request.getSize(),
            durations.getTotalElements(),
            durations.getTotalPages()
        );
    }
}
