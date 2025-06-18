package com.anhpt.res_app.web.validation;

import com.anhpt.res_app.common.entity.Combo;
import com.anhpt.res_app.common.entity.ComboVersion;
import com.anhpt.res_app.common.enums.status.ComboStatus;
import com.anhpt.res_app.common.enums.status.ComboVersionStatus;
import com.anhpt.res_app.common.exception.ResourceNotFoundException;
import com.anhpt.res_app.common.repository.ComboRepository;
import com.anhpt.res_app.common.repository.ComboVersionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebComboValidation {
    // Repository
    private final ComboRepository comboRepository;
    private final ComboVersionRepository comboVersionRepository;

    public void validateGetById(Long comboId) {
        // Kiểm tra Combo tồn tại
        Combo combo = comboRepository.findById(comboId)
            .orElseThrow(() -> {
                log.warn("Không tìm thấy comboId: {}", comboId);
                throw new ResourceNotFoundException("Không tìm thấy Combo");
            });
        // Kiểm tra Combo đã phát hành
        if (!combo.getStatus().equals(ComboStatus.PUBLISHED)) {
            log.warn("ComboId {} không phải là combo đã phát hành", comboId);
            throw new IllegalArgumentException("Combo chưa được phát hành");
        }
        // Kiểm tra Combo có ít nhất 1 combo version đang hoạt động
        List<ComboVersion> comboVersions = combo.getComboVersions();
        if (comboVersions.isEmpty()) {
            log.warn("ComboId {} không có combo version đang hoạt động", comboId);
            throw new IllegalArgumentException("Combo không có combo version đang hoạt động");
        }
    }

    public void validateGetComboVersionDishes(Long comboId, Long comboVersionId) {
        // Kiểm tra Combo tồn tại
        Combo combo = comboRepository.findById(comboId)
            .orElseThrow(() -> {
                log.warn("Không tìm thấy comboId: {}", comboId);
                throw new ResourceNotFoundException("Không tìm thấy Combo");
            });
        // Kiểm tra ComboVersion tồn tại
        ComboVersion comboVersion = comboVersionRepository.findByIdAndComboId(comboVersionId, comboId)
            .orElseThrow(() -> {
                log.warn("Không tìm thấy comboVersionId: {}", comboVersionId);
                throw new ResourceNotFoundException("Không tìm thấy ComboVersion");
            });
        // Kiểm tra ComboVersion đang hoạt động
        if (!comboVersion.getStatus().equals(ComboVersionStatus.ACTIVE)) {
            log.warn("ComboVersionId {} không phải là combo version đang hoạt động", comboVersionId);
            throw new IllegalArgumentException("Combo version chưa được phát hành");
        }
    }
}
