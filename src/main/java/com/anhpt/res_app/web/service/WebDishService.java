package com.anhpt.res_app.web.service;

import com.anhpt.res_app.common.entity.Discount;
import com.anhpt.res_app.common.entity.Dish;
import com.anhpt.res_app.common.entity.DishMedia;
import com.anhpt.res_app.common.entity.DishSetup;
import com.anhpt.res_app.common.enums.status.DishStatus;
import com.anhpt.res_app.common.exception.ResourceNotFoundException;
import com.anhpt.res_app.common.repository.DiscountRepository;
import com.anhpt.res_app.common.repository.DishRepository;
import com.anhpt.res_app.common.repository.DishSetupRepository;
import com.anhpt.res_app.common.utils.Constants;
import com.anhpt.res_app.web.dto.WebDishMapper;
import com.anhpt.res_app.web.dto.response.dish.*;
import com.anhpt.res_app.web.validation.WebDishValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WebDishService {
    // Repository
    private final DishRepository dishRepository;
    private final DishSetupRepository dishSetupRepository;
    private final DiscountRepository discountRepository;
    // Mapper
    private final WebDishMapper webDishMapper;
    // Validation
    private final WebDishValidation webDishValidation;

    // Lấy chi tiết món ăn
    public DishResponse getById(Long dishId) {
        webDishValidation.validateGetById(dishId);
        Dish dish = dishRepository.findById(dishId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Dish"));
        DishResponse dishResponse = webDishMapper.toDishResponse(dish);
        return dishResponse;
    }

    // Lấy danh sách món ăn sắp ra mắt
    public List<DishComingResponse> getComingDishes() {
        // Lấy danh sách món ăn trong DishSetup với currentStatus (active) và nextStatus (published) và sắp xếp theo mốc thời gian (milestone) tăng dần
        List<DishSetup> dishSetups = dishSetupRepository.findByCurrentStatusAndNextStatusOrderByMilestoneAsc(DishStatus.ACTIVE, DishStatus.PUBLISHED);
        if (dishSetups.isEmpty()) {
            return Collections.emptyList();
        }
        List<DishComingResponse> dishComingResponses = dishSetups.stream()
            .map(dishSetup -> {
                Dish dish = dishSetup.getDish();
                String thumbnail = dish.getDishMedias().stream()
                    .max(Comparator.comparing(DishMedia::getDisplayOrder))
                    .map(dishMedia -> dishMedia.getMedia().getFileName())
                    .orElse(null);
                return webDishMapper.toDishComingResponse(dishSetup, dish, thumbnail);
            })
            .collect(Collectors.toList());
        return dishComingResponses;
    }

    // Lấy danh sách món ăn sắp kết thúc
    public List<DishEndingResponse> getEndingDishes() {
        // Lấy danh sách món ăn trong DishSetup với currentStatus (published) và nextStatus (inactive) và sắp xếp theo mốc thời gian (milestone) tăng dần
        List<DishSetup> dishSetups = dishSetupRepository.findByCurrentStatusAndNextStatusOrderByMilestoneAsc(DishStatus.PUBLISHED, DishStatus.INACTIVE);
        if (dishSetups.isEmpty()) {
            return Collections.emptyList();
        }
        List<DishEndingResponse> dishEndingResponses = dishSetups.stream()
            .map(dishSetup -> {
                Dish dish = dishSetup.getDish();
                String thumbnail = dish.getDishMedias().stream()
                    .max(Comparator.comparing(DishMedia::getDisplayOrder))
                    .map(dishMedia -> dishMedia.getMedia().getFileName())
                    .orElse(null);
                return webDishMapper.toDishEndingResponse(dishSetup, dish, thumbnail);
            })
            .collect(Collectors.toList());
        return dishEndingResponses;
    }

    // Lấy danh sách món ăn mới cập nhật
    public List<DishShortResponse> getNewlyDishes() {
        // Lấy mốc thời gian phát hành nhỏ nhất
        LocalDateTime numberDate = LocalDateTime.now().minusDays(Constants.NUMBER_DATE_NEW);
        // Lấy danh sách món ăn ở trạng thái phát hành có ngày phát hành >= mốc thời gian phát hành nhỏ nhất
        List<Dish> dishes = dishRepository.findByStatusAndPublishedAtGreaterThanEqualOrderByPublishedAtDesc(DishStatus.PUBLISHED, numberDate);
        if (dishes.isEmpty()) {
            return Collections.emptyList();
        }
        List<DishShortResponse> dishShortResponses = dishes.stream()
            .map(dish -> {
                String thumbnail = dish.getDishMedias().stream()
                    .max(Comparator.comparing(DishMedia::getDisplayOrder))
                    .map(dishMedia -> dishMedia.getMedia().getFileName())
                    .orElse(null);
                return webDishMapper.toDishShortResponse(dish, thumbnail);
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        return dishShortResponses;
    }

    // Lấy danh sách món ăn giảm giá
    public List<DishDiscountResponse> getDiscountDishes() {
        // Lấy danh sách các món ăn trong bảng Discount
        List<Discount> discounts = discountRepository.findAll();
        if (discounts.isEmpty()) {
            return Collections.emptyList();
        }
        List<DishDiscountResponse> dishDiscountResponses = discounts.stream()
            .map(discount -> {
                // Lấy món ăn
                Dish dish = discount.getDish();
                // Lấy thumbnail
                String thumbnail = dish.getDishMedias().stream()
                    .max(Comparator.comparing(DishMedia::getDisplayOrder))
                    .map(dishMedia -> dishMedia.getMedia().getFileName())
                    .orElse(null);
                return webDishMapper.toDishDiscountResponse(discount, dish, thumbnail);
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        return dishDiscountResponses;
    }
}
