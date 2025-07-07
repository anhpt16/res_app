package com.anhpt.res_app.admin.validation;

import com.anhpt.res_app.admin.dto.request.desk.DurationCreateRequest;
import com.anhpt.res_app.admin.dto.request.desk.DurationSearchRequest;
import com.anhpt.res_app.admin.dto.request.dish.DishCreateRequest;
import com.anhpt.res_app.common.entity.Duration;
import com.anhpt.res_app.common.enums.status.DurationStatus;
import com.anhpt.res_app.common.exception.MultiDuplicateException;
import com.anhpt.res_app.common.exception.ResourceNotFoundException;
import com.anhpt.res_app.common.repository.DurationRepository;
import com.anhpt.res_app.common.utils.function.FieldNameUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AdminDurationValidation {
    private final DurationRepository durationRepository;

    public void validateCreate(DurationCreateRequest request) {
        Map<String, String> errors = new HashMap<>();
        if (request.getDuration() == null) {
            String field = FieldNameUtil.getFieldName(DurationCreateRequest::getDuration);
            errors.put(field, "Không được để trống");
        } else {
            Optional<Duration> duration = durationRepository.findByDuration(request.getDuration());
            if (duration.isPresent()) {
                String field =FieldNameUtil.getFieldName(DurationCreateRequest::getDuration);
                errors.put(field, "Đã tồn tại");
            }
        }
        if (errors.size() > 0) {
            throw new MultiDuplicateException(errors);
        }
    }

    public void validateUpdateStatus(Long durationId, String status) {
        // Kiểm tra status tồn tại
        if (status == null) {
            throw new IllegalArgumentException("Duration Status không hợp lệ");
        }
        DurationStatus durationStatus = DurationStatus.fromCode(status);
        // Kiểm tra Duration tồn tại
        Duration duration = durationRepository.findById(durationId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Duration"));
        // Kiểm tra trùng lặp trạng thái
        if (durationStatus.equals(duration.getStatus())) {
            throw new IllegalArgumentException("Trạng thái đã tồn tại");
        }
    }

    public void validateSearch(DurationSearchRequest request) {
        // Kiểm tra trạng thái hợp lệ
        if (request.getStatus() != null) {
            DurationStatus.fromCode(request.getStatus());
        }
    }
}
