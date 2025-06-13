package com.anhpt.res_app.admin.service;

import com.anhpt.res_app.admin.dto.AdminComboMapper;
import com.anhpt.res_app.admin.dto.AdminDishMapper;
import com.anhpt.res_app.admin.dto.request.combo.ComboCreateRequest;
import com.anhpt.res_app.admin.dto.request.combo.ComboSearchRequest;
import com.anhpt.res_app.admin.dto.request.combo.ComboUpdateRequest;
import com.anhpt.res_app.admin.dto.response.combo.*;
import com.anhpt.res_app.admin.dto.response.dish.DishMediaResponse;
import com.anhpt.res_app.admin.dto.response.dish.DishResponse;
import com.anhpt.res_app.admin.filter.AdminComboFilter;
import com.anhpt.res_app.admin.validation.ComboValidation;
import com.anhpt.res_app.common.dto.response.PageResponse;
import com.anhpt.res_app.common.entity.*;
import com.anhpt.res_app.common.enums.status.ComboStatus;
import com.anhpt.res_app.common.enums.status.ComboVersionStatus;
import com.anhpt.res_app.common.exception.ResourceNotFoundException;
import com.anhpt.res_app.common.repository.ComboRepository;
import com.anhpt.res_app.common.repository.DishRepository;
import com.anhpt.res_app.common.repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminComboService {
    private final ComboRepository comboRepository;
    private final MediaRepository mediaRepository;
    private final ComboValidation comboValidation;
    private final AdminComboMapper adminComboMapper;
    private final AdminComboFilter adminComboFilter;

    public ComboResponse create(ComboCreateRequest request) {
        comboValidation.validateCreate(request);
        Combo combo = new Combo();
        combo.setName(request.getName());
        if (request.getIntroduce() != null) combo.setIntroduce(request.getIntroduce());
        if (request.getDescription() != null) combo.setDescription(request.getDescription());
        if (request.getMediaId() != null) {
            Media media = mediaRepository.findById(request.getMediaId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ảnh"));
            combo.setMedia(media);
        }
        combo.setStatus(ComboStatus.DRAFT);
        combo.setCreatedAt(LocalDateTime.now());
        combo.setUpdatedAt(LocalDateTime.now());
        combo = comboRepository.save(combo);
        return adminComboMapper.toComboResponse(combo);
    }

    public ComboResponse update(ComboUpdateRequest request, Long comboId) {
        comboValidation.validateUpdate(request, comboId);
        Combo combo = comboRepository.findById(comboId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy combo"));
        if (request.getName() != null) combo.setName(request.getName());
        if (request.getIntroduce() != null) combo.setIntroduce(request.getIntroduce());
        if (request.getDescription() != null) combo.setDescription(request.getDescription());
        if (request.getMediaId() != null) {
            Media media = mediaRepository.findById(request.getMediaId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ảnh"));
            combo.setMedia(media);
        }
        combo.setUpdatedAt(LocalDateTime.now());
        combo = comboRepository.save(combo);
        return adminComboMapper.toComboResponse(combo);
    }

    public void delete(Long comboId) {
        comboValidation.validateDelete(comboId);
        Combo combo = comboRepository.findById(comboId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy combo"));
        comboRepository.delete(combo);
    }

    public ComboDetailResponse getById(Long comboId) {
        Combo combo = comboRepository.findById(comboId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy combo"));
        // Lấy ra ComboVersion đang hoạt động
        Optional<ComboVersion> comboVersion = combo.getComboVersions().stream()
            .filter(cbv -> ComboVersionStatus.ACTIVE.equals(cbv.getStatus()))
            .findFirst();
        // Lấy ra ComboVersionDishResponse từ ComboVersion-List<Dish>
        List<ComboVersionDishResponse> comboVersionDishResponses = comboVersion
            .map(cb -> Optional.ofNullable(cb.getComboVersionDishes())
                .orElse(Collections.emptyList())
                .stream()
                .map(comboVersionDish -> adminComboMapper.toComboVersionDishResponse(
                    comboVersionDish,
                    comboVersionDish.getDish(),
                    getMedia(comboVersionDish.getDish())))
                .collect(Collectors.toList()))
            .orElse(Collections.emptyList());
        ComboVersionResponse comboVersionResponse = comboVersion
            .map(cb -> adminComboMapper.toComboVersionResponse(cb, comboVersionDishResponses))
            .orElse(null);
        ComboDetailResponse comboDetailResponse = adminComboMapper.toComboDetailResponse(combo, comboVersionResponse);
        return comboDetailResponse;
    }
    // Lấy ra Thumbnail của Dish
    private String getMedia(Dish dish) {
        return dish.getDishMedias().stream()
            .max(Comparator.comparing(DishMedia::getDisplayOrder))
            .flatMap(dishMedia -> Optional.ofNullable(dishMedia.getMedia())
                                    .map(Media::getFileName))
            .orElse(null);
    }

    public PageResponse<ComboListResponse> get(ComboSearchRequest request) {
        comboValidation.validateSearch(request);
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());
        Page<Combo> comboPage = comboRepository.findAll(adminComboFilter.search(request), pageable);

        if (comboPage.getTotalPages() > 0 && request.getPage() > comboPage.getTotalPages()) {
            throw new IllegalArgumentException("Trang không tồn tại");
        }
        List<ComboListResponse> comboListResponses = comboPage.getContent().stream()
            .map(combo -> {
                ComboVersion comboVersion = combo.getComboVersions().stream()
                    .filter(cbv -> cbv.getStatus().equals(ComboVersionStatus.ACTIVE))
                    .findFirst()
                    .orElse(null);
                return adminComboMapper.toComboListResponse(combo, comboVersion);
            })
            .collect(Collectors.toList());
        return new PageResponse<>(
            comboListResponses,
            request.getPage(),
            request.getSize(),
            comboPage.getTotalElements(),
            comboPage.getTotalPages()
        );
    }
}
