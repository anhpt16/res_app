package com.anhpt.res_app.web.service;

import com.anhpt.res_app.common.entity.Dish;
import com.anhpt.res_app.common.exception.ResourceNotFoundException;
import com.anhpt.res_app.common.repository.DishRepository;
import com.anhpt.res_app.web.dto.WebDishMapper;
import com.anhpt.res_app.web.dto.response.dish.DishResponse;
import com.anhpt.res_app.web.validation.WebDishValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebDishService {
    // Repository
    private final DishRepository dishRepository;
    // Mapper
    private final WebDishMapper webDishMapper;
    // Validation
    private final WebDishValidation webDishValidation;

    public DishResponse getById(Long dishId) {
        webDishValidation.validateGetById(dishId);
        Dish dish = dishRepository.findById(dishId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Dish"));
        DishResponse dishResponse = webDishMapper.toDishResponse(dish);
        return dishResponse;
    }
}
