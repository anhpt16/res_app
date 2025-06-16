package com.anhpt.res_app.admin.validation;

import com.anhpt.res_app.admin.dto.request.setup.DishSetupRequest;
import com.anhpt.res_app.admin.dto.request.setup.DishSetupSearchRequest;
import com.anhpt.res_app.admin.dto.request.setup.DishSetupUpdateRequest;
import com.anhpt.res_app.common.entity.Dish;
import com.anhpt.res_app.common.entity.DishSetup;
import com.anhpt.res_app.common.enums.status.DishStatus;
import com.anhpt.res_app.common.exception.MultiDuplicateException;
import com.anhpt.res_app.common.exception.ResourceNotFoundException;
import com.anhpt.res_app.common.repository.DishRepository;
import com.anhpt.res_app.common.repository.DishSetupRepository;
import com.anhpt.res_app.common.utils.function.FieldNameUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class AdminDishSetupValidation {
    private final DishSetupRepository dishSetupRepository;
    private final DishRepository dishRepository;

    public void validateCreate(Long dishId, DishSetupRequest request) {
        Map<String, String> errors = new HashMap<>();
        // Kiểm tra Dish tồn tại
        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Dish"));
        // Kiểm tra DishSetup tồn tại Dish
        if (dishSetupRepository.existsByDishId(dishId)) {
            throw new IllegalArgumentException("DishSetup đã tồn tại");
        }
        // Kiểm tra trạng thái hợp lệ
        DishStatus nextStatus = DishStatus.fromCode(request.getNextStatus());
        // Kiểm tra trùng lặp trạng thái
        if (dish.getStatus().equals(nextStatus)) {
            String field = FieldNameUtil.getFieldName(DishSetupRequest::getNextStatus);
            errors.put(field, "Trạng thái không hợp lệ");
        }
        // Kiểm tra mốc thời gian hợp lệ
        LocalDate milestone = request.getMilestone();
        if (milestone.isBefore(LocalDate.now()) || milestone.isEqual(LocalDate.now())) {
            String field = FieldNameUtil.getFieldName(DishSetupRequest::getMilestone);
            errors.put(field, "Mốc thời gian không hợp lệ");
        }
        if (errors.size() > 0) {
            throw new MultiDuplicateException(errors);
        }
    }

    public void validateUpdate(Long dishSetupId, DishSetupUpdateRequest request) {
        Map<String, String> errors = new HashMap<>();
        // Kiểm tra request không rỗng
        if (request.isEmpty()) {
            throw new IllegalArgumentException("Yêu cầu không hợp lệ");
        }
        // Kiểm tra DishSetup tồn tại
        DishSetup dishSetup = dishSetupRepository.findById(dishSetupId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy DishSetup"));
        // Kiểm tra trạng thái hợp lệ
        if (request.getNextStatus() != null) {
            DishStatus nextStatus = DishStatus.fromCode(request.getNextStatus());
            if (dishSetup.getCurrentStatus().equals(nextStatus)) {
                String field = FieldNameUtil.getFieldName(DishSetupRequest::getNextStatus);
                errors.put(field, "Trạng thái không hợp lệ");
            }
        }
        // Kiểm tra mốc thời gian hợp lệ
        if (request.getMilestone() != null) {
            LocalDate milestone = request.getMilestone();
            if (milestone.isBefore(LocalDate.now()) || milestone.isEqual(LocalDate.now())) {
                String field = FieldNameUtil.getFieldName(DishSetupRequest::getMilestone);
                errors.put(field, "Mốc thời gian không hợp lệ");
            }
        }
        if (errors.size() > 0) {
            throw new MultiDuplicateException(errors);
        }
    }

    public void validateDelete(Long dishSetupId) {
        // Kiểm tra DishSetup tồn tại
        DishSetup dishSetup = dishSetupRepository.findById(dishSetupId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy DishSetup"));
    }

    public void validateSearch(DishSetupSearchRequest request) {
        // Kiểm tra trạng thái hiện tại hợp lệ
        if (request.getCurrentStatus() != null) {
            DishStatus currentStatus = DishStatus.fromCode(request.getCurrentStatus());
        }
        // Kiểm tra trạng thái tiếp theo hợp lệ
        if (request.getNextStatus() != null) {
            DishStatus nextStatus = DishStatus.fromCode(request.getNextStatus());
        }
    }
}
