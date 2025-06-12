package com.anhpt.res_app.admin.validation;

import com.anhpt.res_app.admin.dto.request.combo.ComboCreateRequest;
import com.anhpt.res_app.admin.dto.request.combo.ComboSearchRequest;
import com.anhpt.res_app.admin.dto.request.combo.ComboUpdateRequest;
import com.anhpt.res_app.common.entity.Combo;
import com.anhpt.res_app.common.enums.status.ComboStatus;
import com.anhpt.res_app.common.exception.ForbiddenActionException;
import com.anhpt.res_app.common.exception.MultiDuplicateException;
import com.anhpt.res_app.common.exception.ResourceNotFoundException;
import com.anhpt.res_app.common.repository.ComboRepository;
import com.anhpt.res_app.common.repository.MediaRepository;
import com.anhpt.res_app.common.utils.function.FieldNameUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class ComboValidation {
    private final ComboRepository comboRepository;
    private final MediaRepository mediaRepository;

    public void validateCreate(ComboCreateRequest request) {
        Map<String, String> errors = new HashMap<>();
        // Kiểm tra tên tồn tại
        boolean comboExistByName = comboRepository.existsByName(request.getName());
        if (comboExistByName) {
            String field = FieldNameUtil.getFieldName(ComboCreateRequest::getName);
            errors.put(field, "Đã tồn tại");
        }
        // Kiểm tra ảnh
        if (request.getMediaId() != null) {
            //TODO: Kiểm tra User-Media
            boolean mediaExist = mediaRepository.existsById(request.getMediaId());
            if (!mediaExist) {
                String field = FieldNameUtil.getFieldName(ComboCreateRequest::getMediaId);
                errors.put(field, "Không tìm thấy ảnh");
            }
        }
        if (errors.size() > 0) {
            throw new MultiDuplicateException(errors);
        }
    }

    public void validateUpdate(ComboUpdateRequest request, Long comboId) {
        Map<String, String> errors = new HashMap<>();
        if (request.isEmpty()) {
            throw new IllegalArgumentException("Không có dữ liệu cập nhật");
        }
        Combo combo = comboRepository.findById(comboId)
            .orElseThrow(() -> {
                log.warn("Không tìm thấy comboId: {}", comboId);
                throw new ResourceNotFoundException("Không tìm thấy Combo");
            });
        // Kiểm tra tên tồn tại
        if (request.getName() != null) {
            Optional<Combo> comboExistByName = comboRepository.findByName(request.getName());
            if (comboExistByName.isPresent() && !comboExistByName.get().getId().equals(comboId)) {
                String field = FieldNameUtil.getFieldName(ComboUpdateRequest::getName);
                errors.put(field, "Đã tồn tại");
            }
        }
        // Kiểm tra ảnh
        if (request.getMediaId() != null) {
            //TODO: Kiểm tra User-Media
            boolean mediaExist = mediaRepository.existsById(request.getMediaId());
            if (!mediaExist) {
                String field = FieldNameUtil.getFieldName(ComboUpdateRequest::getMediaId);
                errors.put(field, "Không tìm thấy ảnh");
            }
        }
        if (errors.size() > 0) {
            throw new MultiDuplicateException(errors);
        }
    }

    public void validateDelete(Long comboId) {
        Combo combo = comboRepository.findById(comboId)
            .orElseThrow(() -> {
                log.warn("Không tìm thấy comboId: {}", comboId);
                throw new ResourceNotFoundException("Không tìm thấy Combo");
            });
        if (combo.getStatus().equals(ComboStatus.PUBLISHED)) {
            log.warn("Không thể xóa combo đã phát hành comboId: {}", comboId);
            throw new ForbiddenActionException("Không thể xóa combo");
        }
        //TODO: Kiểm tra Combo đã được sử dụng
    }

    public void validateSearch(ComboSearchRequest request) {
        //TODO: Kiểm tra ComboStatus
        if (request.getStatus() != null) {
            ComboStatus status = ComboStatus.fromCode(request.getStatus());
        }
    }
}
