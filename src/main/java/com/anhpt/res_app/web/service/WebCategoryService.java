package com.anhpt.res_app.web.service;

import com.anhpt.res_app.common.dto.response.PageResponse;
import com.anhpt.res_app.common.entity.Category;
import com.anhpt.res_app.common.entity.Dish;
import com.anhpt.res_app.common.entity.DishMedia;
import com.anhpt.res_app.common.enums.status.CategoryStatus;
import com.anhpt.res_app.common.enums.status.DishStatus;
import com.anhpt.res_app.common.exception.ResourceNotFoundException;
import com.anhpt.res_app.common.repository.CategoryRepository;
import com.anhpt.res_app.common.repository.DishRepository;
import com.anhpt.res_app.web.dto.WebCategoryMapper;
import com.anhpt.res_app.web.dto.response.category.CategoryResponse;
import com.anhpt.res_app.web.dto.response.dish.DishShortResponse;
import com.anhpt.res_app.web.validation.WebCategoryValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WebCategoryService {
    // Repository
    private final CategoryRepository categoryRepository;
    private final DishRepository dishRepository;
    // Mapper
    private final WebCategoryMapper webCategoryMapper;
    // Validation
    private final WebCategoryValidation webCategoryValidation;

    public List<CategoryResponse> getCategories() {
        List<Category> categories = categoryRepository.findByStatus(CategoryStatus.ACTIVE);
        if (categories.isEmpty()) {
            return Collections.emptyList();
        }
        List<CategoryResponse> categoryResponses = categories.stream()
            .map(webCategoryMapper::toCategoryResponse)
            .collect(Collectors.toList());
        return categoryResponses;
    }

    public List<DishShortResponse> getDishes(Long categoryId) {
        webCategoryValidation.validateGetDishes(categoryId);
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Category"));
        // Lấy danh sách món ăn phát hành của một danh mục và lấy theo ngày phát hành giảm dần
        List<Dish> dishes = dishRepository.findByCategoryAndStatus(category, DishStatus.PUBLISHED, Sort.by("publishedAt").descending());
        if (dishes.isEmpty()) {
            return Collections.emptyList();
        }
        List<DishShortResponse> dishShortResponses = dishes.stream()
            .map(dish -> {
                String thumbnail = dish.getDishMedias().stream()
                    .max(Comparator.comparing(DishMedia::getDisplayOrder))
                    .map(dishMedia -> dishMedia.getMedia().getFileName())
                    .orElse(null);
                return webCategoryMapper.toDishShortResponse(dish, thumbnail);
            })
            .collect(Collectors.toList());
        return dishShortResponses;
    }
}
