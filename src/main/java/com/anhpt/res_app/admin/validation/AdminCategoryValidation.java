package com.anhpt.res_app.admin.validation;

import com.anhpt.res_app.admin.dto.request.category.CategoryCreateRequest;
import com.anhpt.res_app.admin.dto.request.category.CategorySearchRequest;
import com.anhpt.res_app.admin.dto.request.category.CategoryUpdateRequest;
import com.anhpt.res_app.admin.dto.request.collection.CollectionCreateRequest;
import com.anhpt.res_app.common.entity.Category;
import com.anhpt.res_app.common.enums.status.CategoryStatus;
import com.anhpt.res_app.common.exception.ForbiddenActionException;
import com.anhpt.res_app.common.exception.MultiDuplicateException;
import com.anhpt.res_app.common.exception.ResourceNotFoundException;
import com.anhpt.res_app.common.repository.CategoryRepository;
import com.anhpt.res_app.common.utils.function.FieldNameUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
@Slf4j
public class AdminCategoryValidation {
    private final CategoryRepository categoryRepository;

    public void validateCreate(CategoryCreateRequest request) {
        Map<String, String> errors = new HashMap<>();
        // Kiểm tra tên tồn tại
        boolean isExist = categoryRepository.existsByName(request.getName());
        if (isExist) {
            String field = FieldNameUtil.getFieldName(CategoryCreateRequest::getName);
            errors.put(field, "Đã tồn tại");
        }
        if (errors.size() > 0) {
            throw new MultiDuplicateException(errors);
        }
    }

    public void validateUpdate(CategoryUpdateRequest request, Long categoryId) {
        Map<String, String> errors = new HashMap<>();
        if (request.isEmpty()) {
            throw new IllegalArgumentException("Không có dữ liệu cập nhật");
        }
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy CategoryId: " + categoryId));
        if (request.getName() != null) {
            boolean isExist = categoryRepository.existsByName(request.getName());
            if (isExist) {
                String field = FieldNameUtil.getFieldName(CategoryCreateRequest::getName);
                errors.put(field, "Đã tồn tại");
            }
        }
        if (request.getStatus() != null) {
            CategoryStatus status = CategoryStatus.fromCode(request.getStatus());
            if (category.getStatus().equals(status)) {
                throw new IllegalArgumentException("Trạng thái đã tồn tại");
            }
        }
        if (errors.size() > 0) {
            throw new MultiDuplicateException(errors);
        }
    }

    public void validateDelete(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> {
                log.warn("Không tìm thấy bài viết: {}", categoryId);
                throw new ResourceNotFoundException("Không tìm thấy CategoryId: " + categoryId);
            });
        if (category.getStatus().equals(CategoryStatus.ACTIVE)) {
            log.warn("Không thể xóa Category đang hoạt động: {}", categoryId);
            throw new ForbiddenActionException("Không thể xóa danh mục");
        }
    }

    public void validateSearch(CategorySearchRequest request) {
        if (request.getStatus() != null) {
            CategoryStatus status = CategoryStatus.fromCode(request.getStatus());
        }
    }
}
