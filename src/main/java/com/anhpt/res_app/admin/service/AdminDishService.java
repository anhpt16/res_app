package com.anhpt.res_app.admin.service;

import com.anhpt.res_app.admin.dto.AdminDishMapper;
import com.anhpt.res_app.admin.dto.request.dish.DishCreateRequest;
import com.anhpt.res_app.admin.dto.request.dish.DishMediaRequest;
import com.anhpt.res_app.admin.dto.request.dish.DishSearchRequest;
import com.anhpt.res_app.admin.dto.request.dish.DishUpdateRequest;
import com.anhpt.res_app.admin.dto.response.dish.DishMediaResponse;
import com.anhpt.res_app.admin.dto.response.dish.DishResponse;
import com.anhpt.res_app.admin.dto.response.dish.DishShortResponse;
import com.anhpt.res_app.admin.filter.AdminDishFilter;
import com.anhpt.res_app.admin.validation.AdminDishValidation;
import com.anhpt.res_app.common.dto.response.PageResponse;
import com.anhpt.res_app.common.entity.Dish;
import com.anhpt.res_app.common.entity.DishMedia;
import com.anhpt.res_app.common.entity.Media;
import com.anhpt.res_app.common.enums.status.DishStatus;
import com.anhpt.res_app.common.exception.ResourceNotFoundException;
import com.anhpt.res_app.common.repository.CategoryRepository;
import com.anhpt.res_app.common.repository.DishMediaRepository;
import com.anhpt.res_app.common.repository.DishRepository;
import com.anhpt.res_app.common.repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminDishService {
    private final DishRepository dishRepository;
    private final CategoryRepository categoryRepository;
    private final MediaRepository mediaRepository;
    private final DishMediaRepository dishMediaRepository;
    private final AdminDishValidation adminDishValidation;
    private final AdminDishMapper adminDishMapper;
    private final AdminDishFilter adminDishFilter;

    @Transactional
    public DishResponse create(DishCreateRequest request) {
        adminDishValidation.validateCreate(request);
        Dish dish = new Dish();
        dish.setName(request.getName());
        dish.setCategory(categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục: " + request.getCategoryId())));
        if (request.getUnit() != null) {
            dish.setUnit(request.getUnit());
        }
        dish.setPrice(request.getPrice());
        if (request.getDurationFrom() != null) dish.setDurationFrom(request.getDurationFrom());
        if (request.getDurationTo() != null) dish.setDurationTo(request.getDurationTo());
        if (request.getIngradientDisplay() != null) dish.setIngradientDisplay(request.getIngradientDisplay());
        if (request.getDescription() != null) dish.setDescription(request.getDescription());
        if (request.getIntroduce() != null) dish.setIntroduce(request.getIntroduce());
        if (request.getMedias() != null && !request.getMedias().isEmpty()) {
            // Lấy Media từ MediaId
            Set<Long> mediaIds = request.getMedias().stream()
                .map(DishMediaRequest::getId)
                .collect(Collectors.toSet());
            List<Media> medias = mediaRepository.findAllById(mediaIds);
            // Map
            Map<Long, DishMediaRequest> dishMediaRequestMapById = request.getMedias().stream()
                .collect(Collectors.toMap(DishMediaRequest::getId, dishMediaRequest -> dishMediaRequest));
            // Lưu DishMedia
            List<DishMedia> dishMediaList = new ArrayList<>();
            for (Media media : medias) {
                DishMedia dishMedia = new DishMedia();
                dishMedia.setDish(dish);
                dishMedia.setMedia(media);
                DishMediaRequest mediaRequest = dishMediaRequestMapById.get(media.getId());
                dishMedia.setDisplayOrder(mediaRequest.getDisplayOrder());
                dishMediaList.add(dishMedia);
            }
            dish.getDishMedias().addAll(dishMediaList);
        }
        dish.setStatus(DishStatus.INACTIVE);
        dish.setCreatedAt(LocalDateTime.now());
        dish.setUpdatedAt(LocalDateTime.now());
        dish = dishRepository.save(dish);
        // Trả về các Media của Dish
        List<DishMediaResponse> mediaResponses = dish.getDishMedias().stream()
            .map(adminDishMapper::toDishMediaResponse)
            .collect(Collectors.toList());
        return adminDishMapper.toDishResponse(dish, mediaResponses);
    }

    @Transactional
    public DishResponse update(DishUpdateRequest request, Long dishId) {
        adminDishValidation.validateUpdate(request, dishId);
        Dish dish = dishRepository.findById(dishId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy món ăn"));
        // Cập nhật thông tin cơ bản
        updateDishBasicInfo(dish, request);
        // Cập nhật hình ảnh
        updateDishMedias(dish, request);
        dish.setUpdatedAt(LocalDateTime.now());
        dish = dishRepository.save(dish);
        // Trả về các Media của Dish
        List<DishMediaResponse> mediaResponses = dish.getDishMedias().stream()
            .map(adminDishMapper::toDishMediaResponse)
            .collect(Collectors.toList());
        return adminDishMapper.toDishResponse(dish, mediaResponses);
    }

    private void updateDishBasicInfo(Dish dish, DishUpdateRequest request) {
        if (request.getName() != null) dish.setName(request.getName());
        if (request.getUnit() != null) dish.setUnit(request.getUnit());
        if (request.getPrice() != null) dish.setPrice(request.getPrice());
        if (request.getDurationFrom() != null) dish.setDurationFrom(request.getDurationFrom());
        if (request.getDurationTo() != null) dish.setDurationTo(request.getDurationTo());
        if (request.getIngradientDisplay() != null) dish.setIngradientDisplay(request.getIngradientDisplay());
        if (request.getDescription() != null) dish.setDescription(request.getDescription());
        if (request.getIntroduce() != null) dish.setIntroduce(request.getIntroduce());
    }

    private void updateDishMedias(Dish dish, DishUpdateRequest request) {
        if (request.getMedias() == null) {
            return;
        }
        // Lấy danh sách mediaId trong DB
        Set<Long> mediaIdsExist = dish.getDishMedias().stream()
            .map(dishMedia -> dishMedia.getMedia().getId())
            .collect(Collectors.toSet());
        // Lấy danh sách mediaId trong Request
        Set<Long> mediaIdsUpdate = request.getMedias().stream()
            .map(DishMediaRequest::getId)
            .collect(Collectors.toSet());
        // Tìm danh sách media cần xóa
        Set<Long> mediaIdsToDelete = mediaIdsExist.stream()
            .filter(id -> !mediaIdsUpdate.contains(id))
            .collect(Collectors.toSet());
        if (!mediaIdsToDelete.isEmpty()) {
            List<DishMedia> dishMediaToDelete = dish.getDishMedias().stream()
                .filter(dishMedia -> !mediaIdsToDelete.contains(dishMedia.getMedia().getId()))
                .collect(Collectors.toList());
            dishMediaRepository.deleteAll(dishMediaToDelete);
            dish.getDishMedias().removeIf(dishMedia -> mediaIdsToDelete.contains(dishMedia.getMedia().getId()));
        }
        // Tìm danh sách mediaId cần thêm
        Set<Long> mediaIdsToAdd = mediaIdsUpdate.stream()
            .filter(id -> !mediaIdsExist.contains(id))
            .collect(Collectors.toSet());
        if (!mediaIdsToAdd.isEmpty()) {
            List<Media> newMedias = mediaRepository.findAllById(mediaIdsToAdd);
            // Tạo map lấy displayOrder từ request
            Map<Long, DishMediaRequest> dishMediaRequestMapById = request.getMedias().stream()
                .collect(Collectors.toMap(DishMediaRequest::getId, dishMediaRequest -> dishMediaRequest));
            List<DishMedia> dishMediaList = new ArrayList<>();
            for (Media media : newMedias) {
                DishMedia dishMedia = new DishMedia();
                dishMedia.setDish(dish);
                dishMedia.setMedia(media);
                DishMediaRequest mediaRequest = dishMediaRequestMapById.get(media.getId());
                dishMedia.setDisplayOrder(mediaRequest.getDisplayOrder());
                dishMediaList.add(dishMedia);
            }
            dishMediaRepository.saveAll(dishMediaList);
        }
        // Duyệt danh sách mediaId còn lại, cập nhật nếu có thay đổi
        List<DishMedia> dishMediaInDb = dishMediaRepository.findByDish(dish);
        List<DishMedia> dishMediaRemaining = dishMediaInDb.stream()
            .filter(dishMedia -> !mediaIdsToAdd.contains(dishMedia.getMedia().getId()))
            .collect(Collectors.toList());
        if (!dishMediaRemaining.isEmpty()) {
            // Tạo map lấy displayOrder từ request
            Map<Long, DishMediaRequest> dishMediaRequestMapById = request.getMedias().stream()
                .collect(Collectors.toMap(DishMediaRequest::getId, dishMediaRequest -> dishMediaRequest));
            // Lọc các media cần cập nhật displayOrder
            List<DishMedia> mediaToUpdate = dishMediaRemaining.stream()
                .filter(dishMedia -> {
                    DishMediaRequest mediaRequest = dishMediaRequestMapById.get(dishMedia.getMedia().getId());
                    return mediaRequest != null &&
                        !Objects.equals(dishMedia.getDisplayOrder(), mediaRequest.getDisplayOrder());
                })
                .peek(dishMedia -> {
                    DishMediaRequest mediaRequest = dishMediaRequestMapById.get(dishMedia.getMedia().getId());
                    dishMedia.setDisplayOrder(mediaRequest.getDisplayOrder());
                })
                .collect(Collectors.toList());
            if (!mediaIdsUpdate.isEmpty()) {
                dishMediaRepository.saveAll(mediaToUpdate);
            }
        }
    }

    public DishResponse updateStatus(Long dishId, String status) {
        adminDishValidation.validateUpdateStatus(dishId, status);
        Dish dish = dishRepository.findById(dishId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Dish"));
        DishStatus dishStatus = DishStatus.fromCode(status);
        dish.setStatus(dishStatus);
        dish.setUpdatedAt(LocalDateTime.now());
        dish = dishRepository.save(dish);
        return adminDishMapper.toDishResponse(dish);
    }

    public DishResponse reissue(Long dishId) {
        adminDishValidation.validateReissue(dishId);
        Dish dish = dishRepository.findById(dishId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Dish"));
        dish.setPublishedAt(LocalDateTime.now());
        dish.setUpdatedAt(LocalDateTime.now());
        dish = dishRepository.save(dish);
        return adminDishMapper.toDishResponse(dish);
    }

    public void deleteAllMedia(Long dishId) {
        adminDishValidation.validateDeleteAllMedia(dishId);
        Dish dish = dishRepository.findById(dishId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Dish"));
        dishMediaRepository.deleteAll(dish.getDishMedias());
        dish.getDishMedias().clear();
        dish.setUpdatedAt(LocalDateTime.now());
        dishRepository.save(dish);
    }

    public void delete(Long dishId) {
        adminDishValidation.validateDelete(dishId);
        dishRepository.deleteById(dishId);
    }

    public DishResponse getById(Long dishId) {
        Dish dish = dishRepository.findById(dishId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Dish"));
        List<DishMediaResponse> mediaResponses = dish.getDishMedias().stream()
            .map(adminDishMapper::toDishMediaResponse)
            .collect(Collectors.toList());
        return adminDishMapper.toDishResponse(dish, mediaResponses);
    }

    public PageResponse<DishShortResponse> search(DishSearchRequest request) {
        adminDishValidation.validateSearch(request);
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());
        Page<Dish> dishes = dishRepository.findAll(adminDishFilter.search(request), pageable);
        if (dishes.getTotalPages() > 0 && request.getPage() > dishes.getTotalPages()) {
            throw new IllegalArgumentException("Trang không tồn tại");
        }
        List<DishShortResponse> dishShortResponses = dishes.stream()
            .map(dish -> {
                String thumbnail = dish.getDishMedias().stream()
                    .max(Comparator.comparing(DishMedia::getDisplayOrder))
                    .map(dishMedia -> dishMedia.getMedia().getFileName())
                    .orElse(null);
                return adminDishMapper.toDishShortResponse(dish, thumbnail);
            })
            .toList();
        return new PageResponse<>(
            dishShortResponses,
            request.getPage(),
            request.getSize(),
            dishes.getTotalElements(),
            dishes.getTotalPages()
        );
    }
}
