package com.anhpt.res_app.web.service;

import com.anhpt.res_app.common.entity.*;
import com.anhpt.res_app.common.enums.status.ComboStatus;
import com.anhpt.res_app.common.enums.status.ComboVersionStatus;
import com.anhpt.res_app.common.exception.ResourceNotFoundException;
import com.anhpt.res_app.common.repository.ComboRepository;
import com.anhpt.res_app.common.repository.ComboVersionRepository;
import com.anhpt.res_app.web.dto.WebComboMapper;
import com.anhpt.res_app.web.dto.response.combo.ComboDishShortResponse;
import com.anhpt.res_app.web.dto.response.combo.ComboResponse;
import com.anhpt.res_app.web.dto.response.combo.ComboShortResponse;
import com.anhpt.res_app.web.validation.WebComboValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WebComboService {
    // Repository
    private final ComboRepository comboRepository;
    private final ComboVersionRepository comboVersionRepository;
    // Mapper
    private final WebComboMapper webComboMapper;
    // Validation
    private final WebComboValidation webComboValidation;

    // Lấy danh sách Combo
    public List<ComboShortResponse> getCombos() {
        // Lấy danh sách Combo đã phát hành và lấy theo thời gian phát hành mới nhất -> cũ nhất
        List<Combo> combos = comboRepository.findByStatus(ComboStatus.PUBLISHED, Sort.by("publishedAt").descending());
        if (combos.isEmpty()) {
            return Collections.emptyList();
        }
        // Lọc và lấy ra các combo có ít nhất 1 combo version đang hoạt động
        List<ComboShortResponse> comboShortResponses = combos.stream()
            .map(combo -> {
                ComboVersion comboVersion = combo.getComboVersions().stream()
                    .filter(cb -> cb.getStatus().equals(ComboVersionStatus.ACTIVE))
                    .findFirst()
                    .orElse(null);
                if (comboVersion == null) {
                    return null;
                }
                return webComboMapper.toComboShortResponse(combo, comboVersion);
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        return comboShortResponses;
    }

    // Lấy chi tiết Combo - ComboVersion
    public ComboResponse getComboById(Long comboId) {
        webComboValidation.validateGetById(comboId);
        Combo combo = comboRepository.findById(comboId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Combo"));
        ComboVersion comboVersion = combo.getComboVersions().stream()
            .filter(cb -> cb.getStatus().equals(ComboVersionStatus.ACTIVE))
            .findFirst()
            .orElse(null);
        return webComboMapper.toComboResponse(combo, comboVersion);
    }

    // Lấy danh sách món ăn của một ComboVersion
    public List<ComboDishShortResponse> getComboVersionDishes(Long comboId, Long comboVersionId) {
        webComboValidation.validateGetComboVersionDishes(comboId, comboVersionId);
        ComboVersion comboVersion = comboVersionRepository.findByIdAndComboId(comboVersionId, comboId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ComboVersion"));
        List<ComboVersionDish> comboVersionDishes = comboVersion.getComboVersionDishes();
        if (comboVersionDishes.isEmpty()) {
            return Collections.emptyList();
        }
        List<ComboDishShortResponse> comboDishShortResponses = comboVersionDishes.stream()
            // Sắp xếp Dishes theo displayOrder giảm dần
            .sorted(Comparator.comparing(ComboVersionDish::getDisplayOrder, Comparator.nullsLast(Comparator.reverseOrder())))
            .map(comboVersionDish -> {
                Dish dish = comboVersionDish.getDish();
                if (dish == null) {
                    return null;
                }
                // Lấy thumbnail của Dish có displayOrder lớn nhất
                String thumbnail = dish.getDishMedias().stream()
                    .max(Comparator.comparing(DishMedia::getDisplayOrder))
                    .map(dishMedia -> dishMedia.getMedia().getFileName())
                    .orElse(null);
                return webComboMapper.toComboDishShortResponse(comboVersionDish, dish, thumbnail);
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        return comboDishShortResponses;
    }
}
