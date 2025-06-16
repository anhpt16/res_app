package com.anhpt.res_app.admin.service;

import com.anhpt.res_app.admin.dto.AdminDishSetupMapper;
import com.anhpt.res_app.admin.dto.request.setup.DishSetupRequest;
import com.anhpt.res_app.admin.dto.request.setup.DishSetupSearchRequest;
import com.anhpt.res_app.admin.dto.request.setup.DishSetupUpdateRequest;
import com.anhpt.res_app.admin.dto.response.setup.DishSetupResponse;
import com.anhpt.res_app.admin.filter.AdminDishSetupFilter;
import com.anhpt.res_app.admin.validation.AdminDishSetupValidation;
import com.anhpt.res_app.common.dto.response.PageResponse;
import com.anhpt.res_app.common.entity.Dish;
import com.anhpt.res_app.common.entity.DishSetup;
import com.anhpt.res_app.common.enums.status.DishStatus;
import com.anhpt.res_app.common.exception.ResourceNotFoundException;
import com.anhpt.res_app.common.repository.DishRepository;
import com.anhpt.res_app.common.repository.DishSetupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminDishSetupService {
    // Repository
    private final DishSetupRepository dishSetupRepository;
    private final DishRepository dishRepository;
    // Validation
    private final AdminDishSetupValidation adminDishSetupValidation;
    // Mapper
    private final AdminDishSetupMapper adminDishSetupMapper;
    private final AdminDishSetupFilter adminDishSetupFilter;

    public DishSetupResponse create(Long dishId, DishSetupRequest request) {
        adminDishSetupValidation.validateCreate(dishId, request);
        Dish dish = dishRepository.findById(dishId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Dish"));
        DishStatus nextStatus = DishStatus.fromCode(request.getNextStatus());
        DishSetup dishSetup = new DishSetup();
        dishSetup.setDish(dish);
        dishSetup.setCurrentStatus(dish.getStatus());
        dishSetup.setNextStatus(nextStatus);
        dishSetup.setMilestone(request.getMilestone().atStartOfDay());
        dishSetup.setCreatedAt(LocalDateTime.now());
        dishSetup.setUpdatedAt(LocalDateTime.now());
        dishSetup = dishSetupRepository.save(dishSetup);
        return adminDishSetupMapper.toDishSetupResponse(dishSetup);
    }

    public DishSetupResponse update(Long dishSetupId, DishSetupUpdateRequest request) {
        adminDishSetupValidation.validateUpdate(dishSetupId, request);
        DishSetup dishSetup = dishSetupRepository.findById(dishSetupId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy DishSetup"));
        if (request.getNextStatus() != null) dishSetup.setNextStatus(DishStatus.fromCode(request.getNextStatus()));
        if (request.getMilestone() != null) dishSetup.setMilestone(request.getMilestone().atStartOfDay());
        dishSetup.setUpdatedAt(LocalDateTime.now());
        dishSetup = dishSetupRepository.save(dishSetup);
        return adminDishSetupMapper.toDishSetupResponse(dishSetup);
    }

    public void delete(Long dishSetupId) {
        adminDishSetupValidation.validateDelete(dishSetupId);
        DishSetup dishSetup = dishSetupRepository.findById(dishSetupId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy DishSetup"));
        dishSetupRepository.delete(dishSetup);
    }

    public PageResponse<DishSetupResponse> get(DishSetupSearchRequest request) {
        adminDishSetupValidation.validateSearch(request);
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());
        Page<DishSetup> dishSetups = dishSetupRepository.findAll(adminDishSetupFilter.search(request), pageable);
        if (dishSetups.getTotalPages() > 0 && request.getPage() > dishSetups.getTotalPages()) {
            throw new IllegalArgumentException("Trang không tồn tại");
        }
        List<DishSetupResponse> dishSetupResponses = dishSetups.stream()
            .map(adminDishSetupMapper::toDishSetupResponse)
            .collect(Collectors.toList());
        return new PageResponse<>(
            dishSetupResponses,
            request.getPage(),
            request.getSize(),
            dishSetups.getTotalElements(),
            dishSetups.getTotalPages()
        );
    }
}
