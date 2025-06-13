package com.anhpt.res_app.admin.service;

import com.anhpt.res_app.admin.dto.AdminCategoryMapper;
import com.anhpt.res_app.admin.dto.request.category.CategoryCreateRequest;
import com.anhpt.res_app.admin.dto.request.category.CategorySearchRequest;
import com.anhpt.res_app.admin.dto.request.category.CategoryUpdateRequest;
import com.anhpt.res_app.admin.dto.response.category.CategoryResponse;
import com.anhpt.res_app.admin.validation.CategoryValidation;
import com.anhpt.res_app.common.dto.response.PageResponse;
import com.anhpt.res_app.common.entity.Category;
import com.anhpt.res_app.common.enums.status.CategoryStatus;
import com.anhpt.res_app.common.exception.ResourceNotFoundException;
import com.anhpt.res_app.common.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminCategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryValidation categoryValidation;
    private final AdminCategoryMapper adminCategoryMapper;

    public CategoryResponse create(CategoryCreateRequest request) {
        categoryValidation.validateCreate(request);
        Category category = new Category();
        category.setName(request.getName());
        category.setStatus(CategoryStatus.INACTIVE);
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        category = categoryRepository.save(category);
        return adminCategoryMapper.toCategoryResponse(category);
    }

    public CategoryResponse update(CategoryUpdateRequest request, Long categoryId) {
        categoryValidation.validateUpdate(request, categoryId);
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy CategoryId: " + categoryId));
        if (request.getName() != null) {
            category.setName(request.getName());
        }
        if (request.getStatus() != null) {
            category.setStatus(CategoryStatus.fromCode(request.getStatus()));
        }
        category.setUpdatedAt(LocalDateTime.now());
        category = categoryRepository.save(category);
        return adminCategoryMapper.toCategoryResponse(category);
    }

    public void delete(Long categoryId) {
        categoryValidation.validateDelete(categoryId);
        categoryRepository.deleteById(categoryId);
    }

    public PageResponse<CategoryResponse> get(CategorySearchRequest request) {
        categoryValidation.validateSearch(request);

        Pageable pageable = PageRequest.of(
            request.getPage() - 1,
            request.getSize()
        );

        Page<Category> categories = request.getStatus() != null
            ? categoryRepository.findByStatusOrderByCreatedAt(CategoryStatus.fromCode(request.getStatus()), pageable)
            : categoryRepository.findByOrderByCreatedAt(pageable);

        if (categories.getTotalPages() > 0 && categories.getTotalPages() < request.getPage()) {
            throw new IllegalArgumentException("Trang không tồn tại");
        }

        List<CategoryResponse> categoryResponses = categories.stream()
            .map(adminCategoryMapper::toCategoryResponse)
            .toList();

        return new PageResponse<>(
            categoryResponses,
            request.getPage(),
            request.getSize(),
            categories.getTotalElements(),
            categories.getTotalPages()
        );
    }
}
