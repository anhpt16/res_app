package com.anhpt.res_app.admin.validation;

import com.anhpt.res_app.admin.dto.request.dish.DishCreateRequest;
import com.anhpt.res_app.admin.dto.request.dish.DishMediaRequest;
import com.anhpt.res_app.admin.dto.request.dish.DishSearchRequest;
import com.anhpt.res_app.admin.dto.request.dish.DishUpdateRequest;
import com.anhpt.res_app.common.entity.Dish;
import com.anhpt.res_app.common.entity.DishSetup;
import com.anhpt.res_app.common.entity.Media;
import com.anhpt.res_app.common.enums.status.DishStatus;
import com.anhpt.res_app.common.exception.ForbiddenActionException;
import com.anhpt.res_app.common.exception.MultiDuplicateException;
import com.anhpt.res_app.common.exception.ResourceNotFoundException;
import com.anhpt.res_app.common.repository.CategoryRepository;
import com.anhpt.res_app.common.repository.DishRepository;
import com.anhpt.res_app.common.repository.DishSetupRepository;
import com.anhpt.res_app.common.repository.MediaRepository;
import com.anhpt.res_app.common.utils.function.FieldNameUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminDishValidation {
    private final DishRepository dishRepository;
    private final DishSetupRepository dishSetupRepository;
    private final MediaRepository mediaRepository;
    private final CategoryRepository categoryRepository;

    public void validateCreate(DishCreateRequest request) {
        Map<String, String> errors = new HashMap<>();
        boolean isNameExist = dishRepository.existsByName(request.getName());
        // Kiểm tra tên đã tồn tại
        if (isNameExist) {
            String field = FieldNameUtil.getFieldName(DishCreateRequest::getName);
            errors.put(field, "Đã tồn tại");
        }
        // Kiểm tra danh mục tồn tại
        if (request.getCategoryId() != null) {
            boolean isCategoryExist = categoryRepository.existsById(request.getCategoryId());
            if (!isCategoryExist) {
                String field = FieldNameUtil.getFieldName(DishCreateRequest::getCategoryId);
                errors.put(field, "Không tồn tại");
            }
        }
        // Kiểm tra medias
        if (request.getMedias() != null && !request.getMedias().isEmpty()) {
            Set<Long> mediaIds = request.getMedias().stream()
                .map(DishMediaRequest::getId)
                .collect(Collectors.toSet());
            List<Media> medias = mediaRepository.findAllById(mediaIds);
            if (medias.size() != mediaIds.size()) {
                String field = FieldNameUtil.getFieldName(DishUpdateRequest::getMedias);
                errors.put(field, "Không tồn tại");
            }
            // TODO: Thêm phần kiểm tra User-Media
        }
        if (errors.size() > 0) {
            throw new MultiDuplicateException(errors);
        }
    }

    public void validateUpdate(DishUpdateRequest request, Long dishId) {
        Map<String, String> errors = new HashMap<>();
        if (request.isEmpty()) {
            throw new IllegalArgumentException("Không có dữ liệu cập nhật");
        }
        Dish dish = dishRepository.findById(dishId)
            .orElseThrow(() -> {
                log.warn("Không tìm thấy dishId: {}", dishId);
                throw new ResourceNotFoundException("Không tìm thấy Dish");
            });
        // Kiểm tra tên tồn tại
        if (request.getName() != null) {
            Optional<Dish> dishByName = dishRepository.findByName(request.getName());
            if (dishByName.isPresent() && !dishByName.get().getId().equals(dishId)) {
                String field = FieldNameUtil.getFieldName(DishUpdateRequest::getName);
                errors.put(field, "Đã tồn tại");
            }
        }
        // Kiểm tra nếu có giá đã giảm -> không cho chỉnh sửa giá gốc
        if (request.getPrice() != null && dish.getPriceDiscount() != null) {
            String field = FieldNameUtil.getFieldName(DishUpdateRequest::getPrice);
            errors.put(field, "Không thể chỉnh sửa");
        }
        // Kiểm tra medias
        if (request.getMedias() != null & !request.getMedias().isEmpty()) {
            Set<Long> mediaIds = request.getMedias().stream()
                .map(DishMediaRequest::getId)
                .collect(Collectors.toSet());
            List<Media> medias = mediaRepository.findAllById(mediaIds);
            if (medias.size() != mediaIds.size()) {
                String field = FieldNameUtil.getFieldName(DishUpdateRequest::getMedias);
                errors.put(field, "Không tồn tại");
            }
            // TODO: Thêm phần kiểm tra User-Media
        }
        if (errors.size() > 0) {
            throw new MultiDuplicateException(errors);
        }
    }

    public void validateDelete(Long dishId) {
        Dish dish = dishRepository.findById(dishId)
            .orElseThrow(() -> {
                log.warn("Không tìm thấy dishId: {}", dishId);
                throw new ResourceNotFoundException("Không tìm thấy Dish");
            });
        if (dish.getStatus() == DishStatus.ACTIVE) {
            log.warn("Không thể xóa món ăn đang hoạt động dishId: {}", dishId);
            throw new ForbiddenActionException("Không thể xóa món ăn");
        }
        if (dish.getStatus() == DishStatus.PUBLISHED) {
            log.warn("Không thể xóa món ăn đang phát hành dishId: {}", dishId);
            throw new ForbiddenActionException("Không thể xóa món ăn");
        }
        // TODO: Kiểm tra xem món ăn đã được sử dụng
    }

    public void validateSearch(DishSearchRequest request) {
    }

    public void validateUpdateStatus(Long dishId, String status) {
        if (status == null) {
            log.warn("Cập nhật trạng thái dishId {} không hợp lệ", dishId);
            throw new IllegalArgumentException("Trạng thái không hợp lệ");
        }
        // Kiểm tra Dish tồn tại
        Dish dish = dishRepository.findById(dishId)
            .orElseThrow(() -> {
                log.warn("Không tìm thấy dishId: {}", dishId);
                throw new ResourceNotFoundException("Không tìm thấy Dish");
            });
        // Món ăn chưa được thiết lập trạng thái mới có thể cập nhật trạng thái
        Optional<DishSetup> dishSetup = dishSetupRepository.findByDishId(dishId);
        if (dishSetup.isPresent()) {
            log.warn("DishId {} đang được thiết lập trạng thái", dishId);
            throw new IllegalArgumentException("Món ăn đã được thiết lập trạng thái");
        }
        // Kiểm tra status hợp lệ
        DishStatus dishStatus = DishStatus.fromCode(status);
        // Kiểm tra trùng lặp trạng thái
        if (dish.getStatus().equals(dishStatus)) {
            log.warn("Trạng thái dishId {} đã tồn tại {}", dishId, dishStatus.name());
            throw new IllegalArgumentException("Trạng thái đã tồn tại");
        }
    }

    public void validateDeleteAllMedia(Long dishId) {
        // Kiểm tra Dish tồn tại
        Dish dish = dishRepository.findById(dishId)
            .orElseThrow(() -> {
                log.warn("Không tìm thấy dishId: {}", dishId);
                throw new ResourceNotFoundException("Không tìm thấy Dish");
            });
        // Kiểm tra DishMedia không rỗng
        if (dish.getDishMedias().isEmpty()) {
            log.warn("DishId {} không có DishMedia", dishId);
            throw new IllegalArgumentException("Dish không có DishMedia");
        }
    }

    public void validateReissue(Long dishId) {
        // Kiểm tra Dish tồn tại
        Dish dish = dishRepository.findById(dishId)
            .orElseThrow(() -> {
                log.warn("Không tìm thấy dishId: {}", dishId);
                throw new ResourceNotFoundException("Không tìm thấy Dish");
            });
        // Kiểm tra Dish chưa từng được phát hành
        if (dish.getPublishedAt() == null) {
            log.warn("DishId {} chưa từng được phát hành", dishId);
            throw new IllegalArgumentException("Dish chưa từng được phát hành");
        }
        // Trạng thái món ăn phải là đã phát hành
        if (!dish.getStatus().equals(DishStatus.PUBLISHED)) {
            log.warn("DishId {} không phải là món ăn đã phát hành", dishId);
            throw new IllegalArgumentException("Món ăn chưa được phát hành");
        }
    }
}
