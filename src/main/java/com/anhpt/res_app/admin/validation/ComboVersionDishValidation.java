package com.anhpt.res_app.admin.validation;

import com.anhpt.res_app.common.entity.ComboVersion;
import com.anhpt.res_app.common.entity.key.ComboVersionDishId;
import com.anhpt.res_app.common.exception.ResourceNotFoundException;
import com.anhpt.res_app.common.repository.ComboRepository;
import com.anhpt.res_app.common.repository.ComboVersionDishRepository;
import com.anhpt.res_app.common.repository.ComboVersionRepository;
import com.anhpt.res_app.common.repository.DishRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ComboVersionDishValidation {
    private final DishRepository dishRepository;
    private final ComboRepository comboRepository;
    private final ComboVersionRepository comboVersionRepository;
    private final ComboVersionDishRepository comboVersionDishRepository;

    public void validateCreate(Long comboId, Long versionId, Long dishId) {
        validateBasicEntities(comboId, versionId, dishId);
        ComboVersion comboVersion = comboVersionRepository.findByIdAndComboId(versionId, comboId)
            .orElseThrow(() -> new ResourceNotFoundException("Version không tồn tại"));
        // Thiết lập ComboVersionDishId
        ComboVersionDishId comboVersionDishId = new ComboVersionDishId();
        comboVersionDishId.setComboVersionId(comboVersion.getId());
        comboVersionDishId.setDishId(dishId);
        if (isComboVersionDishExist(comboVersionDishId)) {
            log.warn("Dish {} đã tồn tại trong version: {}", dishId, versionId);
            throw new IllegalArgumentException("Món đã tồn tại trong version");
        }
    }

    public void validateUpdate(Long comboId, Long versionId, Long dishId) {
        validateBasicEntities(comboId, versionId, dishId);
        ComboVersion comboVersion = comboVersionRepository.findByIdAndComboId(versionId, comboId)
            .orElseThrow(() -> new ResourceNotFoundException("Version không tồn tại"));
        // Thiết lập ComboVersionDishId
        ComboVersionDishId comboVersionDishId = new ComboVersionDishId();
        comboVersionDishId.setComboVersionId(comboVersion.getId());
        comboVersionDishId.setDishId(dishId);
        if (!isComboVersionDishExist(comboVersionDishId)) {
            log.warn("Dish {} không tồn tại trong version: {}", dishId, versionId);
            throw new ResourceNotFoundException("Dish không tồn tại trong version");
        }
    }

    public void validateDelete(Long comboId, Long versionId, Long dishId) {
        validateBasicEntities(comboId, versionId, dishId);
        ComboVersion comboVersion = comboVersionRepository.findByIdAndComboId(versionId, comboId)
            .orElseThrow(() -> new ResourceNotFoundException("Version không tồn tại"));
        // Thiết lập ComboVersionDishId
        ComboVersionDishId comboVersionDishId = new ComboVersionDishId();
        comboVersionDishId.setComboVersionId(comboVersion.getId());
        comboVersionDishId.setDishId(dishId);
        if (!isComboVersionDishExist(comboVersionDishId)) {
            log.warn("Dish {} không tồn tại trong version: {}", dishId, versionId);
            throw new ResourceNotFoundException("Dish không tồn tại trong version");
        }
    }

    private void validateBasicEntities(Long comboId, Long versionId, Long dishId) {
        if (!isComboExist(comboId)) {
            log.warn("Combo không tồn tại: {}", comboId);
            throw new ResourceNotFoundException("Combo không tồn tại");
        }
        
        if (!isComboVersionExist(comboId, versionId)) {
            log.warn("Version không tồn tại: {}", versionId);
            throw new ResourceNotFoundException("Version không tồn tại");
        }
        
        if (!isDishExist(dishId)) {
            log.warn("Món không tồn tại: {}", dishId);
            throw new ResourceNotFoundException("Món không tồn tại");
        }
    }

    private boolean isComboExist(Long comboId) {
        return comboRepository.existsById(comboId);
    }

    private boolean isComboVersionExist(Long comboId, Long versionId) {
        return comboVersionRepository.existsByIdAndComboId(versionId, comboId);
    }

    private boolean isDishExist(Long dishId) {
        return dishRepository.existsById(dishId);
    }

    private boolean isComboVersionDishExist(ComboVersionDishId id) {
        return comboVersionDishRepository.existsById(id);
    }
}
