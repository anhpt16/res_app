package com.anhpt.res_app.web.validation;

import com.anhpt.res_app.common.entity.Dish;
import com.anhpt.res_app.common.enums.status.DishStatus;
import com.anhpt.res_app.common.exception.ResourceNotFoundException;
import com.anhpt.res_app.common.repository.DishRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebDishValidation {
    private final DishRepository dishRepository;

    public void validateGetById(Long dishId) {
        // Kiểm tra Dish tồn tại
        Dish dish = dishRepository.findById(dishId)
            .orElseThrow(() -> {
                log.warn("Không tìm thấy dishId: {}", dishId);
                throw new ResourceNotFoundException("Không tìm thấy Dish");
            });
        // Kiểm tra Dish ở trạng thái đã phát hành
        if (!dish.getStatus().equals(DishStatus.PUBLISHED)) {
            log.warn("DishId {} không phải là món ăn đã phát hành", dishId);
            throw new IllegalArgumentException("Món ăn chưa được phát hành");
        }
    }
}
