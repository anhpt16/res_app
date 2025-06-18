package com.anhpt.res_app.web.validation;

import com.anhpt.res_app.common.entity.Category;
import com.anhpt.res_app.common.enums.status.CategoryStatus;
import com.anhpt.res_app.common.exception.ResourceNotFoundException;
import com.anhpt.res_app.common.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebCategoryValidation {
    private final CategoryRepository categoryRepository;

    public void validateGetDishes(Long categoryId) {
        // Kiểm tra Category tồn tại
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> {
                log.warn("Không tìm thấy categoryId: {}", categoryId);
                throw new ResourceNotFoundException("Không tìm thấy Category");
            });
        // Kiểm tra Category đang hoạt động
        if (!category.getStatus().equals(CategoryStatus.ACTIVE)) {
            log.warn("CategoryId {} không hoạt động", categoryId);
            throw new IllegalArgumentException("Category không hoạt động");
        }
    }
}
