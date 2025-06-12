package com.anhpt.res_app.admin.validation;

import com.anhpt.res_app.admin.dto.request.combo.VersionUpdateRequest;
import com.anhpt.res_app.common.entity.ComboVersion;
import com.anhpt.res_app.common.exception.MultiDuplicateException;
import com.anhpt.res_app.common.exception.ResourceNotFoundException;
import com.anhpt.res_app.common.repository.ComboRepository;
import com.anhpt.res_app.common.repository.ComboVersionRepository;
import com.anhpt.res_app.common.utils.function.FieldNameUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class ComboVersionValidation {
    private final ComboRepository comboRepository;
    private final ComboVersionRepository comboVersionRepository;

    public void validateCreate(Long comboId) {
        boolean isExist = comboRepository.existsById(comboId);
        if (!isExist) {
            log.warn("Combo không tồn tại: {}", comboId);
            throw new ResourceNotFoundException("Combo không tồn tại");
        }
    }

    public void validateUpdate(VersionUpdateRequest request, Long comboId, Long versionId) {
        Map<String, String> errors = new HashMap<>();
        if (request.isEmpty()) {
            log.warn("Không có dữ liệu cập nhật");
            throw new IllegalArgumentException("Không có dữ liệu cập nhật");
        }
        // Kiểm tra combo có tồn tại không
        boolean isExist = comboRepository.existsById(comboId);
        if (!isExist) {
            log.warn("Combo không tồn tại: {}", comboId);
            throw new ResourceNotFoundException("Combo không tồn tại");
        }
        // Kiểm tra version có tồn tại không
        Optional<ComboVersion> comboVersion = comboVersionRepository.findByIdAndComboId(versionId, comboId);
        if (comboVersion.isEmpty()) {
            log.warn("Version không tồn tại: {}", versionId);
            throw new ResourceNotFoundException("Version không tồn tại");
        }
        // Kiểm tra nếu có priceDiscount
        if (request.getPriceDiscount() != null) {
            // Kiểm tra nếu request có price (priceDiscount phải nhỏ hơn price)
            if (request.getPrice() != null && request.getPrice().compareTo(request.getPriceDiscount()) <= 0) {
                log.warn("PriceDiscount phải nhỏ hơn Price: {}", request.getPriceDiscount());
                String field = FieldNameUtil.getFieldName(VersionUpdateRequest::getPriceDiscount);
                errors.put(field, "Không hợp lệ");
            }
            // Kiểm tra nếu DB có price (priceDiscount phải nhỏ hơn price)
            if (comboVersion.get().getPrice() != null && comboVersion.get().getPrice().compareTo(request.getPriceDiscount()) <= 0) {
                log.warn("PriceDiscount phải nhỏ hơn Price: {}", request.getPriceDiscount());
                String field = FieldNameUtil.getFieldName(VersionUpdateRequest::getPriceDiscount);
                errors.put(field, "Không hợp lệ");
            }
            // Nếu không có price thì không được có priceDiscount
            if (request.getPrice() == null && comboVersion.get().getPrice() == null) {
                log.warn("PriceDiscount không được có khi không có Price");
                String field = FieldNameUtil.getFieldName(VersionUpdateRequest::getPriceDiscount);
                errors.put(field, "Không hợp lệ");
            }
        }
        // Kiểm tra StartDate và EndDate
        // Nếu có StartDate thì phải có EndDate
        if (request.getStartAt() != null && request.getFinishAt() == null) {
            log.warn("EndDate không được null khi StartDate có giá trị");
            String field = FieldNameUtil.getFieldName(VersionUpdateRequest::getFinishAt);
            errors.put(field, "Không hợp lệ");
        }
        // Nếu có EndDate thì phải có StartDate
        if (request.getFinishAt() != null && request.getStartAt() == null) {
            log.warn("StartDate không được null khi EndDate có giá trị");
            String field = FieldNameUtil.getFieldName(VersionUpdateRequest::getStartAt);
            errors.put(field, "Không hợp lệ");
        }
        // Nếu có StartDate và EndDate thì StartDate phải nhỏ hơn EndDate
        if (request.getStartAt() != null && request.getFinishAt() != null && request.getStartAt().compareTo(request.getFinishAt()) >= 0) {
            log.warn("StartDate phải nhỏ hơn EndDate: {}", request.getStartAt());
            String field = FieldNameUtil.getFieldName(VersionUpdateRequest::getStartAt);
            errors.put(field, "Không hợp lệ");
        }
        if (errors.size() > 0) {
            throw new MultiDuplicateException(errors);
        }
    }

    public void validateGetById(Long comboId, Long versionId) {
        boolean isExist = comboVersionRepository.existsByIdAndComboId(versionId, comboId);
        if (!isExist) {
            log.warn("Version không tồn tại: {}", versionId);
            throw new ResourceNotFoundException("Version không tồn tại");
        }   
    }
}
