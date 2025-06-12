package com.anhpt.res_app.admin.service;

import com.anhpt.res_app.admin.dto.AdminComboVersionDishMapper;
import com.anhpt.res_app.admin.dto.request.combo.VersionUpdateRequest;
import com.anhpt.res_app.admin.dto.response.combo.ComboVersionDishResponse;
import com.anhpt.res_app.admin.dto.response.combo.ComboVersionResponse;
import com.anhpt.res_app.admin.dto.AdminComboVersionMapper;
import com.anhpt.res_app.admin.validation.ComboVersionDishValidation;
import com.anhpt.res_app.admin.validation.ComboVersionValidation;
import com.anhpt.res_app.common.entity.Combo;
import com.anhpt.res_app.common.entity.ComboVersion;
import com.anhpt.res_app.common.entity.ComboVersionDish;
import com.anhpt.res_app.common.entity.Dish;
import com.anhpt.res_app.common.entity.key.ComboVersionDishId;
import com.anhpt.res_app.common.enums.status.ComboVersionStatus;
import com.anhpt.res_app.common.exception.ResourceNotFoundException;
import com.anhpt.res_app.common.repository.ComboRepository;
import com.anhpt.res_app.common.repository.ComboVersionDishRepository;
import com.anhpt.res_app.common.repository.ComboVersionRepository;
import com.anhpt.res_app.common.repository.DishRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminComboVersionService {
    // Validation
    private final ComboVersionValidation comboVersionValidation;
    private final ComboVersionDishValidation comboVersionDishValidation;
    // Repository
    private final ComboVersionRepository comboVersionRepository;
    private final ComboVersionDishRepository comboVersionDishRepository;
    private final ComboRepository comboRepository;
    private final DishRepository dishRepository;
    // Mapper
    private final AdminComboVersionMapper adminComboVersionMapper;
    private final AdminComboVersionDishMapper adminComboVersionDishMapper;

    // Tạo mới một phiên bản
    public ComboVersionResponse create(Long comboId) {
        comboVersionValidation.validateCreate(comboId);
        Combo combo = comboRepository.findById(comboId)
            .orElseThrow(() -> new ResourceNotFoundException("Combo không tồn tại"));
        ComboVersion comboVersion = new ComboVersion();
        // Tạo Version Code
        String versionCode = "CV-" + comboId + "-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        comboVersion.setVersionCode(versionCode);
        comboVersion.setStatus(ComboVersionStatus.INACTIVE);
        comboVersion.setCombo(combo);
        comboVersion.setCreatedAt(LocalDateTime.now());
        comboVersion.setUpdatedAt(LocalDateTime.now());
        comboVersion = comboVersionRepository.save(comboVersion);
        return adminComboVersionMapper.toComboVersionResponse(comboVersion);
    }

    // Lấy thông tin chi tiết một phiên bản
    public ComboVersionResponse getComboVersionById(Long comboId, Long versionId) {
        comboVersionValidation.validateGetById(comboId, versionId);
        ComboVersion comboVersion = comboVersionRepository.findByIdAndComboId(versionId, comboId)
            .orElseThrow(() -> new ResourceNotFoundException("Version không tồn tại"));
        return adminComboVersionMapper.toComboVersionResponse(comboVersion);
    }

    // Thêm một món ăn cho một phiên bản
    public ComboVersionDishResponse addDish(Long comboId, Long versionId, Long dishId, Integer count, Integer displayOrder) {
        comboVersionDishValidation.validateCreate(comboId, versionId, dishId);
        ComboVersion comboVersion = comboVersionRepository.findByIdAndComboId(versionId, comboId)
            .orElseThrow(() -> new ResourceNotFoundException("Version không tồn tại"));
        Dish dish = dishRepository.findById(dishId)
            .orElseThrow(() -> new ResourceNotFoundException("Dish không tồn tại"));
        ComboVersionDish comboVersionDish = new ComboVersionDish();
        // Thiết lập ComboVersionDishId
        ComboVersionDishId comboVersionDishId = new ComboVersionDishId();
        comboVersionDishId.setComboVersionId(comboVersion.getId());
        comboVersionDishId.setDishId(dishId);
        comboVersionDish.setId(comboVersionDishId);
        // Các thông tin khác
        comboVersionDish.setComboVersion(comboVersion);
        comboVersionDish.setDish(dish);
        comboVersionDish.setCount(count);
        comboVersionDish.setDisplayOrder(displayOrder);
        comboVersionDish.setCreatedAt(LocalDateTime.now());
        comboVersionDish.setUpdatedAt(LocalDateTime.now());
        comboVersionDish = comboVersionDishRepository.save(comboVersionDish);
        return adminComboVersionDishMapper.toComboVersionDishResponse(comboVersionDish);
    }

    // Cập nhật thông tin một món ăn trong phiên bản
    public ComboVersionDishResponse updateDish(Long comboId, Long versionId, Long dishId, Integer count, Integer displayOrder) {
        comboVersionDishValidation.validateUpdate(comboId, versionId, dishId);
        ComboVersion comboVersion = comboVersionRepository.findByIdAndComboId(versionId, comboId)
            .orElseThrow(() -> new ResourceNotFoundException("Version không tồn tại"));
        // Thiết lập ComboVersionDishId
        ComboVersionDishId comboVersionDishId = new ComboVersionDishId();
        comboVersionDishId.setComboVersionId(comboVersion.getId());
        comboVersionDishId.setDishId(dishId);
        ComboVersionDish comboVersionDish = comboVersionDishRepository.findById(comboVersionDishId)
            .orElseThrow(() -> new ResourceNotFoundException("Dish không tồn tại trong version"));
        comboVersionDish.setCount(count);
        comboVersionDish.setDisplayOrder(displayOrder);
        comboVersionDish.setUpdatedAt(LocalDateTime.now());
        comboVersionDish = comboVersionDishRepository.save(comboVersionDish);
        return adminComboVersionDishMapper.toComboVersionDishResponse(comboVersionDish);
    }

    // Xóa một món ăn trong phiên bản
    public void deleteDish(Long comboId, Long versionId, Long dishId) {
        comboVersionDishValidation.validateDelete(comboId, versionId, dishId);
        ComboVersion comboVersion = comboVersionRepository.findByIdAndComboId(versionId, comboId)
            .orElseThrow(() -> new ResourceNotFoundException("Version không tồn tại"));
        // Thiết lập ComboVersionDishId
        ComboVersionDishId comboVersionDishId = new ComboVersionDishId();
        comboVersionDishId.setComboVersionId(comboVersion.getId());
        comboVersionDishId.setDishId(dishId);
        ComboVersionDish comboVersionDish = comboVersionDishRepository.findById(comboVersionDishId)
            .orElseThrow(() -> new ResourceNotFoundException("Món không tồn tại trong version"));
        comboVersionDishRepository.delete(comboVersionDish);
    }

    // Cập nhật thông tin của phiên bản
    public ComboVersionResponse updateVersion(VersionUpdateRequest request, Long comboId, Long versionId) {
        comboVersionValidation.validateUpdate(request, comboId, versionId);
        ComboVersion comboVersion = comboVersionRepository.findByIdAndComboId(versionId, comboId)
            .orElseThrow(() -> new ResourceNotFoundException("Version không tồn tại"));
        if (request.getPrice() != null) comboVersion.setPrice(request.getPrice());
        if (request.getPriceDiscount() != null) comboVersion.setPriceDiscount(request.getPriceDiscount());
        if (request.getDurationFrom() != null) comboVersion.setDurationFrom(request.getDurationFrom());
        if (request.getDurationTo() != null) comboVersion.setDurationTo(request.getDurationTo());
        if (request.getStartAt() != null) comboVersion.setStartAt(request.getStartAt());
        if (request.getFinishAt() != null) comboVersion.setFinishAt(request.getFinishAt());
        comboVersion.setUpdatedAt(LocalDateTime.now());
        comboVersion = comboVersionRepository.save(comboVersion);
        return adminComboVersionMapper.toComboVersionResponse(comboVersion);
    }
}
